package com.jumpjump.service;

import love.forte.simbot.component.mirai.event.MiraiGroupMessageEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberJoinEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberLeaveEvent;
import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.event.EventProcessingContext;

import java.util.Set;

public interface GroupEventService {


    /**
     * 处理群信息相关的方法
     */
    public void messageGroup(MiraiGroupMessageEvent miraiGroupMessageEvent);


    /**
     * 处理已经加入群的方法
     */

    public void joinGroup(MiraiMemberJoinEvent miraiBotJoinGroupEvent);


    /**
     * 处理 退群的信息
     */

    public void laveGroup(MiraiMemberLeaveEvent miraiMemberLeaveEvent);


    void persistentSessions(MiraiGroupMessageEvent miraiGroupMessageEvent, ContinuousSessionContext sessionContext);
}
