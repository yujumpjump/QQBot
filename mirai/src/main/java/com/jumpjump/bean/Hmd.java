package com.jumpjump.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 数据库hmd对象
 */
@Data
@TableName("jump")
public class Hmd {
    @TableId
    private String id;
    private String data;
    private String server;
    private String admin;
    private String loggindate;
    private  String url;

    private String yuanYin;

}
