package com.jumpjump.bean;

import lombok.Data;


/**
 * 服务器的属性对象
 */
@Data
public class Servers {
    private String prefix;
    private String description;
    private int playerAmount;
    private int maxPlayers;
    private int inQue;
    private String serverInfo;
    private String url;
    private String mode;
    private String currentMap;
    private String ownerId;
    private String region;
    private String platform;
    private String serverId;
    private String smallMode;
    private String teams;
    private boolean official;
    private String gameId;
}
