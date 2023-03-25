package com.jumpjump.bullhorsebot.constants;

import com.jumpjump.bullhorsebot.bean.Servers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticConstants {

    /**
     * 8653 所有的服务器
     */
    public static List<Servers>  servers = new ArrayList<>();

    /**
     * 8653 所有假的服务器
     */

    public static  List<Servers> falseServer = new ArrayList<>();
    /**
     * 8653 所有的开服号uid 1~12
     */
    public static String[] ownerId = new String[]{
            "1003202205466","1038206483","1840286783","1006129945426","1617567943","864996152",
            "634518016","1809269828", "1005165079340","381321089","1005623515836","1005539220986"};
    /**
     *保存白名单的
     */
    public static  Map<String,String> admin = new HashMap<>();
}
