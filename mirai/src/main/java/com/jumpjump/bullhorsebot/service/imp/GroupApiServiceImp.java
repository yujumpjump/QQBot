package com.jumpjump.bullhorsebot.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jumpjump.bullhorsebot.bean.Server;
import com.jumpjump.bullhorsebot.bean.Servers;
import com.jumpjump.bullhorsebot.bean.User;
import com.jumpjump.bullhorsebot.client.WebHttpClient;
import com.jumpjump.bullhorsebot.constants.ApiURLConstant;
import com.jumpjump.bullhorsebot.constants.ServersConstants;
import com.jumpjump.bullhorsebot.service.GroupAPIService;
import com.jumpjump.bullhorsebot.utils.CheckServer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class GroupApiServiceImp implements GroupAPIService {


    @Resource
    private WebHttpClient httpClient;


    /**
     * 请求用户是否在ban有记录
     * @param url
     * @param name
     * @return
     */
    @Override
    public String quireCheck(String url, String name){
        String uri = subName(url, name);
        String body = httpClient.senGet(uri);;
        String string = JSONObject.parseObject(body).getString("names");
        String data = JSONObject.parseObject(string).getString(name.toLowerCase());
        JSONObject jsonObject = JSON.parseObject(data);
        int status = jsonObject.getIntValue("status");
        switch (status){
            case 1:
                return "石锤";
            case 2:
                return "待自证";
            case 3:
                return "Moss自证";
            case 4:
                return "无效举报";
            case 5:
                return "讨论中";
            case 6:
                return "需要更多管理投票";
            case 8:
                return "刷枪";
        }
        return "无记录";
    }

    /**
     * 实现查询用户所有的信息查询
     * @param url
     * @param name
     * @return
     */
    @Override
    public User quireUser(String url, String name) {
        String uri = subName(url, name);
        String data = httpClient.senGet(uri);
        User user = JSONObject.parseObject(data, User.class);
        return user;
    }
    /**
     * 实现当前服务器的查询 并且返回服务器详细内容
     * @param url
     * @param serverName
     * @return
     */
    @Override
    public Server quireServer(String url, String serverName) {
        Server server;
        for(Servers server1: ServersConstants.servers){
            if(server1.getPrefix().contains(serverName.toUpperCase())){
                String uri = subName(url, server1.getPrefix());
                String data = httpClient.senGet(uri);
                if(data!=null){
                    server = JSONObject.parseObject(data,Server.class);
                    return server;
                }else {
                    // 如果查询不到, 就重新获取到所有服务器列表
                    String servers = httpClient.senGet(ApiURLConstant.getServers);
                    JSONObject jsonObject = JSONObject.parseObject(servers);
                    List<Servers> date = JSONObject.parseArray(jsonObject.getString("servers"), Servers.class);
                    ServersConstants.servers = CheckServer.checkServer(date);
                    return null;
                }
            }
        }
        return null;
    }
    /**
     * 封装字符串的操作
     * @param url
     * @param name
     * @return
     */
    private String subName(String url, String name){
        return  url.replaceFirst("key", name);
    }
}
