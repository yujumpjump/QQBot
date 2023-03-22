package com.jumpjump.bullhorsebot.constants;

public interface ApiURLConstant {


    String key = "key";

    /**
     * c查询用户是否开挂
     */
    String checkUserBan = "https://api.gametools.network/bfban/checkban?names="+key;

    /**
     * 查询用户的所有的信息
     */
    String getQueryUserAll =  "https://api.gametools.network/bfv/all/?format_values=true&name="+key+"&platform=pc&skip_battlelog=false&lang=zh-CN";

    /**
     * 查询当前服务器信息
     */

    String getServer = "https://api.gametools.network/bfv/detailedserver/?name="+key+"&platform=pc&lang=zh-CN";

    /**
     * 查询8653全部服务器
     */
    String getServers = "https://api.gametools.network/bfv/servers/?name=8653&region=all&platform=pc&limit=20&lang=zh-CN";


}
