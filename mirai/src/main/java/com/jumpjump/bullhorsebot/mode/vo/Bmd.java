package com.jumpjump.bullhorsebot.mode.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "bmd")
public class Bmd {


    @TableId
    private String qq;
}
