package com.jumpjump.bullhorsebot.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;


@Component
@Slf4j
public class WebHttpClient {
    private final RestTemplate restTemplate;

    public WebHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * get获取网络资源 并返回输入流
     * @param urlParam
     * @return inputStream
     */
    public InputStream setGetImg(String urlParam){
        Resource resource = restTemplate.getForObject(urlParam, Resource.class);
        InputStream inputStream = null;
        try {
             inputStream = resource.getInputStream();
        } catch (IOException e) {
            log.info("获取网络图片资源失败,可能存在代理出现问题！");
            return  null;
        }
        return inputStream;
    }


    /**
     * 请求使用的服务器
     * @param serverUrl
     * @return
     */

    public String senGet(String serverUrl) {
        log.info("请求的url,{}", serverUrl);
        ResponseEntity<String> forEntity;
        String body = null;
        try {
            forEntity= restTemplate.getForEntity(serverUrl, String.class);
            if (forEntity.getStatusCode().is2xxSuccessful()) {
                body =  forEntity.getBody();
            }
        } catch (Exception e) {
            log.info("获取为了数据有问题！");
            return null;
        }
        return body;
    }
}
