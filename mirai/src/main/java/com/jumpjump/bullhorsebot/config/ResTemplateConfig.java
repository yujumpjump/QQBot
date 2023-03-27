package com.jumpjump.bullhorsebot.config;

import com.jumpjump.bullhorsebot.utils.HttpsClientRequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
@Slf4j
public class ResTemplateConfig {

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        // 配置代理
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 10808));
        SimpleClientHttpRequestFactory factory = new HttpsClientRequestFactory();
        factory.setProxy(proxy);
        factory.setReadTimeout(6000);  // 单位为ms
        factory.setConnectTimeout(6000);  // 单位为ms
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory){

        return new RestTemplate(clientHttpRequestFactory);
    }




}


