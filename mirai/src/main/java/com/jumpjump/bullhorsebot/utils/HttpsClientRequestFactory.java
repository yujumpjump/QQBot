package com.jumpjump.bullhorsebot.utils;

import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyStore;


/**
 *
 */
public class HttpsClientRequestFactory extends SimpleClientHttpRequestFactory {
    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        try {
            if (!(connection instanceof HttpsURLConnection)) {
                //http协议
                super.prepareConnection(connection, httpMethod);
            }
            if ((connection instanceof HttpsURLConnection)) {
                HttpsURLConnection httpsURLConnection= (HttpsURLConnection) connection;
                //https协议
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                //信任任何链接(也就是忽略认证)
                TrustStrategy anyTrustStrategy = (chain, authType) -> true;
                SSLContext ctx = SSLContexts.custom().loadTrustMaterial(trustStore, anyTrustStrategy).build();
                httpsURLConnection.setSSLSocketFactory(ctx.getSocketFactory());
                super.prepareConnection(httpsURLConnection, httpMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
