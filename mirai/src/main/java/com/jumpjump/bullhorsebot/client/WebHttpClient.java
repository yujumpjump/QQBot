package com.jumpjump.bullhorsebot.client;


import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.internal.deps.io.ktor.client.HttpClient;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class WebHttpClient {
    private final RestTemplate restTemplate;

    public WebHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * get获取网络资源 并返回输入流
     *
     * @param urlParam
     * @return inputStream
     */
    public InputStream setGetImg(String urlParam) {
        Resource resource = restTemplate.getForObject(urlParam, Resource.class);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            log.info("获取网络图片资源失败,可能存在代理出现问题！");
            return null;
        }
        return inputStream;
    }

    /**
     * 请求使用的服务器
     *
     * @param serverUrl
     * @return
     */

    public String senGet(String serverUrl) {
        // 配置网络头
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        log.info("请求的url,{}", serverUrl);
        ResponseEntity<String> forEntity;
        String body = null;
        try {
            forEntity = restTemplate.exchange(serverUrl, HttpMethod.GET, entity, String.class);
            if (forEntity.getStatusCode().is2xxSuccessful()) {
                body = forEntity.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取网络数据有问题！");
            return null;
        }
        return body;
    }



}
