package com.jumpjump.service;

import com.jumpjump.bean.Server;
import com.jumpjump.bean.User;

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
