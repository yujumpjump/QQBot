package com.jumpjump.bullhorsebot.bean;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "bmd")
public class Bmd {
    @TableId
    private String qq;
    public Bmd(){
    }
    public Bmd(String qq){
        this.qq = qq;
    }
}
