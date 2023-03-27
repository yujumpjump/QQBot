package com.jumpjump.bullhorsebot.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.jumpjump.bullhorsebot.bean.Servers;
import com.jumpjump.bullhorsebot.constants.StaticConstants;
import com.jumpjump.bullhorsebot.bean.Bmd;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class CheckServer {

    /**
     * 判断真服和假服
     */
    public static List<Servers>  checkServer(List<Servers> servers){
        List<Servers> list = new ArrayList<>();
        for(Servers server:servers){
            for (String uid: StaticConstants.ownerId) {
                if(server.getOwnerId()==null){
                    continue;
                }
                if (server.getOwnerId().equals(uid)) {
                    list.add(server);
                }
            }
        }
        return list;
    }

    /**
     * 操作bmd
     */

    @SneakyThrows
    public static void upDate(Map<String,String> admin){
        List<Bmd> bmds = new ArrayList<>();
        admin.forEach((s, s2) -> {
            bmds.add(new Bmd(s));
        });
        FileUtil.writeBytes(new JSONArray(Collections.singletonList(bmds)).toString().getBytes(),new ClassPathResource("/config/admin.json").getFile().getAbsoluteFile());
    }
}
