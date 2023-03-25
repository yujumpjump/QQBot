package com.jumpjump.bullhorsebot.service;

import com.jumpjump.bullhorsebot.bean.Server;
import com.jumpjump.bullhorsebot.bean.User;

public interface GroupAPIService {


    /**
     * 检查用户是否在联办有记录
     * @param url
     * @param name
     * @return
     */
    String quireCheck(String url, String name);

    /**
     * 查询一个用户数据
     * @param url
     * @param name
     * @return
     */
    User quireUser(String url, String name);


    /**
     * 查询一个服务器的信息
     * @param url
     * @param name
     * @return
     */
    public Server quireServer(String url,String name);
}
