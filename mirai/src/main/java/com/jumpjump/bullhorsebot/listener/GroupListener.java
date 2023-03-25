package com.jumpjump.bullhorsebot.listener;

import com.jumpjump.bullhorsebot.service.GroupEventService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.mirai.MiraiMember;
import love.forte.simbot.component.mirai.event.MiraiGroupMessageEvent;
import love.forte.simbot.component.mirai.event.MiraiGroupMessageRecallEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberJoinEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberLeaveEvent;
import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.event.GroupEvent;
import love.forte.simbot.message.At;
import love.forte.simbot.message.Text;
import net.mamoe.mirai.contact.announcement.OnlineAnnouncement;
import net.mamoe.mirai.event.SimpleListenerHost;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class GroupListener extends SimpleListenerHost {
    @Resource
    private GroupEventService eventService;

    /**
     * @param event
     */
    @SneakyThrows
    @Listener
//    @Filter(targets = @Filter.Targets(groups = {"600723467"}))
    public  void onMessage(GroupEvent event) {

        Thread.sleep(2000);
        switch (event.getKey().getId().toString()) {
            case "mirai.member_leave" -> {
                eventService.laveGroup((MiraiMemberLeaveEvent) event);
            }
            case "mirai.group_message" -> {
                MiraiGroupMessageEvent miraiGroupMessageEvent = (MiraiGroupMessageEvent) event;
                miraiGroupMessageEvent.getMessageContent().getMessages().forEach(message->{
                    if(message instanceof Text){
                        eventService.messageGroup(miraiGroupMessageEvent);
                    }else if(message instanceof At){
                        miraiGroupMessageEvent.getGroup().sendAsync("我不处理任何事情，有事请找其他管理员!");
                    }
                });

            }
            case "mirai.member_join" ->{
                eventService.joinGroup((MiraiMemberJoinEvent) event);
            }
            case "mirai.group_message_recall" ->{
                eventService.groupMessageRecallEvent((MiraiGroupMessageRecallEvent) event);
            }
        }
    }

}
