package com.jumpjump;

import love.forte.simboot.spring.autoconfigure.EnableSimbot;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableSimbot
@MapperScan(value = "com.jumpjump.mapper")
public class MiraiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiraiApplication.class, args);
    }
}
