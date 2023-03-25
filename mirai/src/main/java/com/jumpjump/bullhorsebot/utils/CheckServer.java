package com.jumpjump.bullhorsebot.utils;

import com.jumpjump.bullhorsebot.bean.Servers;
import com.jumpjump.bullhorsebot.constants.StaticConstants;

import java.util.ArrayList;
import java.util.List;


public class CheckServer {

    /**
     * 判断真服和假服
     */
    public static List<Servers>  checkServer(List<Servers> servers){
        List<Servers> list = new ArrayList<>();
        for(Servers server:servers){
            for (String uid: StaticConstants.ownerId) {
                if (server.getOwnerId().equals(uid)) {
                    list.add(server);
                }else {

                }
            }
        }
        return list;
    }
}
