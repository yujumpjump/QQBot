package com.jumpjump.bullhorsebot.start;

import com.alibaba.fastjson.JSONObject;
import com.jumpjump.bullhorsebot.bean.Servers;
import com.jumpjump.bullhorsebot.client.WebHttpClient;
import com.jumpjump.bullhorsebot.constants.ApiURLConstant;
import com.jumpjump.bullhorsebot.constants.StaticConstants;
import com.jumpjump.bullhorsebot.utils.CheckServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiStart implements ApplicationRunner {

    private final WebHttpClient webHttpClient;

    public ApiStart(WebHttpClient webHttpClient) {
        this.webHttpClient = webHttpClient;
    }

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
        // 加载服务器之前 进行真服务器的验证
        StaticConstants.servers = CheckServer.checkServer(servers);
    }
}
