package com.jumpjump.bullhorsebot.service;

import com.jumpjump.bullhorsebot.bean.Server;
import com.jumpjump.bullhorsebot.bean.User;

public interface GroupAPIService {

    /**
     *
     * @param url
     * @param name
     * @return
     */

    String quireCheck(String url, String name);


    User quireUser(String url, String name);


    public Server quireServer(String url,String name);



}
