package com.jumpjump.bullhorsebot.start;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.jumpjump.bullhorsebot.constants.StaticConstants;
import com.jumpjump.bullhorsebot.bean.Bmd;
import com.jumpjump.bullhorsebot.mapper.BmdMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdminStart implements ApplicationRunner {


    private final BmdMapper bmdMapper;

    public AdminStart(BmdMapper bmdMapper) {
        this.bmdMapper = bmdMapper;
    }

    /**
     * 实现admin加载
     * @param args incoming application arguments
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Bmd> bmds = bmdMapper.selectList(null);
        if(bmds==null){
            return;
        }
        log.info("初始化bmd,{}",bmds);
            bmds.forEach(bmd -> {
                StaticConstants.admin.put(bmd.getQq(),bmd.getQq());
            });
        }
}

