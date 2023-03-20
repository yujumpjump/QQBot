package com.jumpjump.constants;

import com.alibaba.fastjson.JSONObject;
import com.jumpjump.bean.Servers;
import com.jumpjump.client.WebHttpClient;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiStart implements ApplicationRunner {

    @Resource
   private WebHttpClient webHttpClient;
    /**
     * 系统启动时加载8653的服务器
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String serverss = webHttpClient.senGet(ApiURLConstant.getServers);
        JSONObject jsonObject = JSONObject.parseObject(serverss);
        List<Servers> servers = JSONObject.parseArray(jsonObject.getString("servers"), Servers.class);
        ServersConstants.servers = servers;
    }
}
