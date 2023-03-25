package com.jumpjump.bullhorsebot.start;

import com.jumpjump.bullhorsebot.constants.StaticConstants;
import com.jumpjump.bullhorsebot.mapper.BmdMapper;
import com.jumpjump.bullhorsebot.mode.vo.Bmd;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
        bmds.forEach(bmd -> {
            StaticConstants.admin.put(bmd.getQq(),bmd.getQq());
        });
    }
}
