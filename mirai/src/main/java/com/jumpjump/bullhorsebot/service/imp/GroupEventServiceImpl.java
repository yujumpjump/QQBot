package com.jumpjump.bullhorsebot.service.imp;

import com.jumpjump.bullhorsebot.bean.Server;
import com.jumpjump.bullhorsebot.bean.User;
import com.jumpjump.bullhorsebot.constants.ApiURLConstant;
import com.jumpjump.bullhorsebot.constants.StaticConstants;
import com.jumpjump.bullhorsebot.bean.Bmd;
import com.jumpjump.bullhorsebot.mapper.BmdMapper;
import com.jumpjump.bullhorsebot.service.GroupAPIService;
import com.jumpjump.bullhorsebot.service.GroupEventService;
import com.jumpjump.bullhorsebot.utils.CheckServer;
import com.jumpjump.bullhorsebot.utils.CreateImgUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.ID;
import love.forte.simbot.LongID;
import love.forte.simbot.component.mirai.MiraiGroup;
import love.forte.simbot.component.mirai.MiraiMember;
import love.forte.simbot.component.mirai.announcement.MiraiAnnouncements;
import love.forte.simbot.component.mirai.bot.MiraiBot;
import love.forte.simbot.component.mirai.event.MiraiGroupMessageEvent;
import love.forte.simbot.component.mirai.event.MiraiGroupMessageRecallEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberJoinEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberLeaveEvent;
import love.forte.simbot.message.*;
import love.forte.simbot.resources.Resource;
import love.forte.simbot.resources.StandardResource;
import love.forte.simbot.utils.item.Items;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.contact.announcement.Announcements;
import net.mamoe.mirai.contact.announcement.OnlineAnnouncement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GroupEventServiceImpl implements GroupEventService {

    // 加群保存用户的key
    private static final Map<Long, Long> joinUserKey = new HashMap<>();

    private final GroupAPIService groupAPIService;

    private final CreateImgUtil createImgUtil;


    private final BmdMapper bmdMapper;


    public GroupEventServiceImpl(GroupAPIService groupAPIService, CreateImgUtil createImgUtil, BmdMapper bmdMapper) {
        this.groupAPIService = groupAPIService;
        this.createImgUtil = createImgUtil;
        this.bmdMapper = bmdMapper;
    }

    /**
     * 处理群里面关键字并且回复
     */
    @Override
    public void messageGroup(MiraiGroupMessageEvent miraiGroupMessageEvent) {
        // 信息发送的群
        MiraiGroup group = miraiGroupMessageEvent.getGroup();
        // 信息发送的人
        MiraiMember groupUser = miraiGroupMessageEvent.getAuthor();
        // 信息内容
        String message = miraiGroupMessageEvent.getMessageContent().getPlainText();
        // @用户 并且创建消息链
        At atUser = new At(groupUser.getId());
        Messages toMessages = Messages.toMessages(atUser);

        // 处理群主任的设置
        if(message.contains("add admin=") || message.contains("remover admin=")){
            ownerMessage(miraiGroupMessageEvent);
            return;
        }

        // 处理管理员的操作
        if(message.equals(".") || message.equals("..") || message.equals("。。")){
            adminMessage(miraiGroupMessageEvent);
            return;
        }

        /*---------------------------判断普通用户的需求,排除白名单 管理员-------------------------*/
        if(StaticConstants.admin.get(groupUser.getId().toString())==null){
            if (message.contains("被踢")) {
                group.sendAsync(Messages.toMessages(Text.of("pb=" + groupUser.getNickname())));
                return;
            }
            if (message.contains("注册")) {
                ClassPathResource classPathResourceRegImg = new ClassPathResource("img/reg.jpg");
                try {
                    StandardResource ofImg = Resource.of(classPathResourceRegImg.getInputStream());
                    ResourceImage resourceImage = Image.of(ofImg);
                    group.sendAsync(toMessages.plus(Messages.toMessages(Text.of("注册如下图\n"))).plus(Messages.toMessages(resourceImage)));
                    return;
                } catch (IOException e) {
                    log.info("注册图片读取失败!");
                    throw new RuntimeException(e);
                }
            }
        }

        if (message.contains("!cx") || message.contains("!fwq")) {
            String[] subMessage = message.split(" ");
            String keyMessage = subMessage[0];
            String name = subMessage[1];
            String checkBan;
            User userData;
            InputStream dataImg;
            StandardResource ofImg;
            ResourceImage userDataOf;
            Server server;
            if (keyMessage.equals("!cx")) {
                try {
                    checkBan = groupAPIService.quireCheck(ApiURLConstant.checkUserBan, name);
                    userData = groupAPIService.quireUser(ApiURLConstant.getQueryUserAll, name);
                    if (checkBan == null || userData == null) {
                        group.sendAsync(toMessages.plus(Messages.toMessages(Text.of("请输入正确的游戏id"))));
                        return;
                    }
                    dataImg = createImgUtil.createUserDataImg(userData, checkBan);
                    ofImg = Resource.of(dataImg);
                    userDataOf = Image.of(ofImg);
                    group.sendAsync(toMessages.plus(Text.of("\n")).plus(Messages.toMessages(userDataOf)));
                } catch (IOException e) {
                    log.info("本地图片读取错误!");
                    throw new RuntimeException(e);
                }
                return;
            }
            if (keyMessage.equals("!fwq")) {
                try {
                    server = groupAPIService.quireServer(ApiURLConstant.getServer, name);
                    if (server == null) {
                        group.sendAsync(toMessages.plus(Messages.toMessages(Text.of("8653服务器正在运行的数量为" + StaticConstants.servers.size()))));
                        return;
                    }
                    dataImg = createImgUtil.createServerImg(server);
                    ofImg = Resource.of(dataImg);
                    userDataOf = Image.of(ofImg);
                    group.sendAsync(toMessages.plus(Text.of("\n")).plus(Messages.toMessages(userDataOf)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 解决新人进群公告解除禁言
     *
     * @param miraiBotJoinGroupEvent
     */
    @SneakyThrows
    @Override
    public void joinGroup(MiraiMemberJoinEvent miraiBotJoinGroupEvent) {
        // 获取进入群成员
        MiraiMember member = miraiBotJoinGroupEvent.getMember();
        // 获取群对象
        MiraiGroup group = miraiBotJoinGroupEvent.getGroup();
        // 以成员的qq号为key qq为value;
        joinUserKey.put(member.getId().toLong(), member.getId().toLong());
        member.muteBlocking(30, TimeUnit.DAYS);
        // 创建@ 并且添加消息链
        At at = new At(member.getId());
        Messages messages = Messages.toMessages(at);

        group.sendAsync(messages.plus(Messages.toMessages(Text.of(" 看群置顶公告自动解除禁言!"))));
        /*-------------------------获取公告-----------------------------*/
        MiraiAnnouncements announcements = group.getAnnouncements();
        Announcements originalAnnouncements = announcements.getOriginalAnnouncements();
        List<OnlineAnnouncement> onlineAnnouncements = originalAnnouncements.toList();
        OnlineAnnouncement onlineAnnouncement = onlineAnnouncements.get(0);


        /*------------------------检查map是否还有人---------------------*/
        while (true) {
            Thread.sleep(10000);
            List<NormalMember> members = onlineAnnouncement.members(true);
            for (NormalMember user : members) {
                if (joinUserKey.get(user.getId()) == null) {
                    continue;
                }
                user.unmute();
                joinUserKey.remove(user.getId());
                At unAt = new At(new LongID(user.getId()));
                Messages unMessages = Messages.toMessages(unAt, Text.of("检测到你已经阅读置顶公告自动解除禁言!"));
                group.sendAsync(unMessages);
                return;
            }
        }
    }

    /**
     * 解决成员退群的操作
     *
     * @param miraiMemberLeaveEvent
     */
    @Override
    public void laveGroup(MiraiMemberLeaveEvent miraiMemberLeaveEvent) {
        MiraiGroup group = miraiMemberLeaveEvent.getGroup();
        MiraiMember member = miraiMemberLeaveEvent.getMember();
        group.sendAsync(member.getId() + "已退群!");
    }


    /**
     * 处理群消息撤回操作
     * @param miraiGroupMessageRecallEvent
     */
    @Override
    public void groupMessageRecallEvent(MiraiGroupMessageRecallEvent miraiGroupMessageRecallEvent) {
        Messages messages = miraiGroupMessageRecallEvent.getMessages();
        MiraiGroup group = miraiGroupMessageRecallEvent.getGroup();
        MiraiMember author = miraiGroupMessageRecallEvent.getAuthor();
        MiraiBot bot = miraiGroupMessageRecallEvent.getBot();
        MiraiGroup adminGroup = bot.getGroup(ID.$(600723467));
        int sum = new Random().nextInt(9) + 1;
        messages.forEach(data->{
            if(data instanceof Text){
                group.sendAsync("群昵称: "+group.getName()+" \n昵称为: "+author.getNickname()+" \nqq号码为: "+author.getId()+"\n撤回了一条消息\n"+((Text) data).getText());
                try {
                    Thread.sleep(sum * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                adminGroup.sendAsync("群昵称: "+group.getName()+" \n昵称为: "+author.getNickname()+" \nqq号码为: "+author.getId()+"\n撤回了一条消息\n"+((Text) data).getText());
            }else if(data instanceof Image){
                Image image = (Image) data;
                Resource resource = image.getResource();
                ResourceImage of = Image.of(resource);
                Messages img = Messages.toMessages(of);
                group.sendAsync(Messages.toMessages(Text.of("群昵称: "+group.getName()+" \n昵称为: "+author.getNickname()+" \nqq号码为: "+author.getId()+"\n撤回了一张图片")).plus(img));
                try {
                    Thread.sleep(sum * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                adminGroup.sendAsync(Messages.toMessages(Text.of("群昵称: "+group.getName()+" \n昵称为: "+author.getNickname()+" \nqq号码为: "+author.getId()+"\n撤回了一张图片")).plus(img));
            }
        });
    }


    /**
     * 处理管理员的命令
     *
     * @param miraiGroupMessageEvent
     */
    private void adminMessage(MiraiGroupMessageEvent miraiGroupMessageEvent) {
        MiraiGroup group = miraiGroupMessageEvent.getGroup();
        MiraiMember author = miraiGroupMessageEvent.getAuthor();
        String message = miraiGroupMessageEvent.getMessageContent().getPlainText();
        if(author.isAdmin() || author.isOwner()){
            switch (message){
                case "." :{
                    Items<MiraiMember> members = group.getMembers();
                    List<MiraiMember> miraiMembers = members.collectToList();
                    miraiMembers.forEach(miraiMember -> {
                        if(miraiMember.isMuted()){
                            miraiMember.unmuteAsync();
                        }
                    });
                    for(Long key:joinUserKey.keySet()){
                        joinUserKey.remove(key);
                    }
                    group.sendAsync("已清除群内所有禁言!");
                    break;
                }
                 case ".." :{
                    group.muteBlocking();
                    group.sendAsync("已开启全员禁言!");
                    break;
                }
                 case "。。" :{
                    group.unmuteAsync();
                    group.sendAsync("已解除全员禁言!");
                    break;
                }
            }
        }
    }


    /**
     * 处理群主的命令 设置白名单
     */
    private void ownerMessage(MiraiGroupMessageEvent miraiGroupMessageEvent){
        String message = miraiGroupMessageEvent.getMessageContent().getPlainText();
        MiraiGroup group = miraiGroupMessageEvent.getGroup();
        MiraiMember author = miraiGroupMessageEvent.getAuthor();
        if(author.getId().toString().equals("184595172") || author.getId().toString().equals("2564839024")){
            if(message.contains("add admin=") || message.contains("remover admin=")){
                String[] qq = message.split("=");
                // 判断是否为qq为数字
                if(!qq[1].matches("\\d+")){
                    group.sendAsync(qq[1]+" qq为数字!");
                    return;
                }
                MiraiMember member = group.getMember(ID.$(Long.parseLong(qq[1])));
                if(member==null){
                    group.sendAsync("群内不存在"+qq[1]+"用户");
                    return;
                }
                try {
                    if(message.contains("add")){
                        Bmd bmd = new Bmd();
                        bmd.setQq(qq[1]);
                        int insert = bmdMapper.insert(bmd);
                        if(insert>0){
                            group.sendAsync("已添加"+qq[1]+"为白名单");
                            StaticConstants.admin.put(member.getId().toString(),member.getId().toString());
                            return;
                        }
                        group.sendAsync("添加"+qq[1]+"为白名单失败!");
                        return;
                    }
                }catch (Exception e){
                    group.sendAsync("白名单中已存在此用户!");
                }
                try {
                    if(message.contains("remove")){
                        int i = bmdMapper.deleteById(qq[1]);
                        if(i>0){
                            group.sendAsync("已取消"+qq[1]+"为白名单");
                            StaticConstants.admin.remove(member.getId().toString());
                            return;
                        }
                        group.sendAsync("取消"+qq[1]+"为白名单失败!");
                    }
                }catch (Exception e){
                    group.sendAsync("白名单中无此用户!");
                }
            }
        }
    }


}
