package com.jumpjump.bullhorsebot.listener;

import com.jumpjump.bullhorsebot.service.GroupEventService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.component.mirai.event.MiraiGroupMessageEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberLeaveEvent;
import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.event.GroupEvent;
import net.mamoe.mirai.event.SimpleListenerHost;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class GroupListener extends SimpleListenerHost {
    @Resource
    private GroupEventService eventService;


    /**
     * @param event
     * @param context
     */
//    @Listener
//    @Filter(targets = @Filter.Targets(groups = {"566673777"}))
//    @SneakyThrows
    public  void onMessage(GroupEvent event, ContinuousSessionContext context) {

        String eventKey = event.getKey().getId().toString();
        log.info("事件信息" + eventKey);
        switch (eventKey) {
            case "mirai.member_leave" -> {
                eventService.laveGroup((MiraiMemberLeaveEvent) event);
            }
            case "mirai.group_message" -> {
                MiraiGroupMessageEvent miraiGroupMessageEvent = (MiraiGroupMessageEvent) event;
                // 方法1 直接遍历
                    // 只有管理员才可以进行hmd的录入
                    if (miraiGroupMessageEvent.getMessageContent().getPlainText().equals("!lr") && miraiGroupMessageEvent.getAuthor().isAdmin()) {
                        eventService.persistentSessions(miraiGroupMessageEvent, context);
                    } else {
                        eventService.messageGroup(miraiGroupMessageEvent);
                    }
                }
        }
    }



    public void test(){

    }


}
