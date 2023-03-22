package com.jumpjump.bullhorsebot.bean;

import lombok.Data;


/**
 * 下一个地图的信息
 */
@Data
public class Rotation {
    private String mapname; // 地图名字;
    private String image;// 地图img；
    private String mode; // 模式
    private int index; //位置

}
