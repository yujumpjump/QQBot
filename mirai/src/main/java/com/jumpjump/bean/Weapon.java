package com.jumpjump.bean;

import lombok.Data;


/**
 * 武器对象
 */
@Data
public class Weapon {
    private String weaponName;  // 武器名称
    private String type;  // 武器类型
    private  String image; // 武器图片


    private long kills;  // 杀人数量

    private double killsPerMinute; // kpm

    private long headshotKills; // 爆头数

    private String headshots; //  爆头率


    private long shotsFired; //射击发射数

    private long shotsHit;  // 名中数

    private String accuracy; // 命中率

    private long hitVKills;



}
