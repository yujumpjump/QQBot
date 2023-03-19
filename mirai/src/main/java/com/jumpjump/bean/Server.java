package com.jumpjump.bean;

import lombok.Data;

import java.util.List;


/**
 * 当前服务器对象
 */
@Data
public class Server {
    private int playerAmount;  // 当前人数

    private int maxPlayerAmount; // 房间可以容的人数

    private int inQueue; // 正在排队的人数

    private String prefix; // 服务器名

    private String description; // 游戏描述
    private String currentMap; //当前地图
    private String currentMapImage; //当前地图img
    private String region;// 地区
    private String country; //国家
    private String mode; // 模式
    private List<Rotation> rotation; // 下一个地图

    private Owner owner; // 开发信息
}
