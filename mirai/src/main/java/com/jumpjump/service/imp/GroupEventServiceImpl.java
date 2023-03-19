package com.jumpjump.service.imp;

import com.jumpjump.bean.Vehicles;
import com.jumpjump.bean.Hmd;
import com.jumpjump.bean.User;
import com.jumpjump.constants.ApiURLConstant;
import com.jumpjump.mapper.HmdDataMapper;
import com.jumpjump.service.GroupAPIService;
import com.jumpjump.service.GroupEventService;
import com.jumpjump.utils.CreateImgUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.LongID;
import love.forte.simbot.component.mirai.MiraiGroup;
import love.forte.simbot.component.mirai.MiraiMember;
import love.forte.simbot.component.mirai.announcement.MiraiAnnouncements;
import love.forte.simbot.component.mirai.event.MiraiGroupMessageEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberJoinEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberLeaveEvent;
import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.message.*;
import love.forte.simbot.resources.Resource;
import love.forte.simbot.resources.StandardResource;
import love.forte.simbot.resources.URLResource;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.contact.announcement.Announcements;
import net.mamoe.mirai.contact.announcement.OnlineAnnouncement;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GroupEventServiceImpl implements GroupEventService {


     private static final Map<String,LongID>  joinKye = new HashMap<>();

     private final GroupAPIService groupAPIService;


     private final CreateImgUtil botUtils;


    private final HmdDataMapper hmdDataMapper;

    public GroupEventServiceImpl(GroupAPIService groupAPIService, CreateImgUtil botUtils, HmdDataMapper hmdDataMapper) {
        this.groupAPIService = groupAPIService;
        this.botUtils = botUtils;
        this.hmdDataMapper = hmdDataMapper;
    }

    /**
     *处理群里面关键字并且回复
     */
    @Override
    public void messageGroup(MiraiGroupMessageEvent miraiGroupMessageEvent) {
        String message = miraiGroupMessageEvent.getMessageContent().getPlainText();
        MiraiMember member = miraiGroupMessageEvent.getAuthor();
        At at = new At(member.getId());
        if(message.contains("被踢")){
            Messages messages = Messages.toMessages(Text.of("pb="+member.getNickname()));
            miraiGroupMessageEvent.sendAsync(messages);
        }else if(message.contains("注册")){
            URLResource resource;
            try {
                resource = Resource.of(new ClassPathResource("/img/reg.jpg").getURL());
                ResourceImage resourceImage = Image.of(resource);
                Messages messages = Messages.toMessages(at, Text.of(" 注册如下图\n")).plus(resourceImage);
                miraiGroupMessageEvent.sendBlocking(messages);
            } catch (IOException e) {
                log.error("图片读取错误....");
                throw new RuntimeException(e);
            }
        }else if(message.contains("查询")){
            String name;
            String check;
            User user;
            List<Vehicles> aircraft=null;
            try {
                 name = message.substring(2);
                 user = groupAPIService.quireUser(ApiURLConstant.getQueryUserAll,name);
                 check = groupAPIService.quireCheck(ApiURLConstant.checkUserBan,name);
            }catch (Exception e){
                log.info("数据请求超时");
                e.printStackTrace();
                Messages messages = Messages.toMessages(at, Text.of(" 网络超时，请稍后在查询!"));
                miraiGroupMessageEvent.sendBlocking(messages);
                return;
            }
            if(user==null || check==null || user.getUserName()==null){
                Messages messages = Messages.toMessages(at, Text.of(" 请输入正确id!"));
                miraiGroupMessageEvent.sendBlocking(messages);
                return;
            }
            try {
                /**
                 * 创建图片并且发生 ,部署
                 */
                InputStream img = botUtils.createImg(user, check,aircraft);
                StandardResource cx = Resource.of(img);
                ResourceImage of = Image.of(cx);
                Messages messages = Messages.toMessages(at, Text.of("\n")).plus(of);
                miraiGroupMessageEvent.sendBlocking(messages);
                /**
                 * 测试
                 */
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(message.contains("!hmd=")){
            String substring = message.split("=")[1];
            Hmd hmd = hmdDataMapper.selectById(substring);
            Messages messages;
            if(hmd==null){
                messages = Messages.toMessages(at, Text.of("本地黑名单没有此用户"));
            }else {
                messages = Messages.toMessages(at, Text.of(" 以下被本地用服务器拉黑的用户信息，如果是本人请联系管理员处理！\n"+
                        "id: " + hmd.getId() + "\n被ban服务器: " + hmd.getServer()+"\n原因: "+hmd.getYuanYin()+
                                "\n被举报时间: "+ hmd.getData()+"\n处事件的管理员id: "+hmd.getAdmin()+"\n录入时间: "+hmd.getLoggindate()+"\n证据如下:\n"+hmd.getUrl()
                ));
            }
            miraiGroupMessageEvent.sendBlocking(messages);
        }
    }


    /**
     * 解决新人进群公告解除禁言
     * @param miraiBotJoinGroupEvent
     */
    @Override
    public void joinGroup(MiraiMemberJoinEvent miraiBotJoinGroupEvent) {
        MiraiMember member = miraiBotJoinGroupEvent.getMember();
        MiraiGroup group = miraiBotJoinGroupEvent.getGroup();
        LongID id = member.getId();
        joinKye.put(id.toString(), id);
        member.muteBlocking(30, TimeUnit.DAYS);
        At at = new At(member.getId());
        Messages messages = Messages.toMessages(at, Text.of( "看群置顶自动解除禁言!"));
        group.sendAsync(messages);
        /*-------------------------获取公告-----------------------------*/
        MiraiAnnouncements announcements = group.getAnnouncements();
        Announcements originalAnnouncements = announcements.getOriginalAnnouncements();
        List<OnlineAnnouncement> onlineAnnouncements = originalAnnouncements.toList();
        log.info("公告数量{}",onlineAnnouncements.size());
        OnlineAnnouncement onlineAnnouncement = onlineAnnouncements.get(0);
        log.info("公告数量",onlineAnnouncement);
        /*------------------------检查map是否还有人---------------------*/
        while (joinKye.size()>0) {
            List<NormalMember> members = onlineAnnouncement.members(true);
            try {
                Thread.sleep(5000);
                for(NormalMember member1:members){
                    MiraiMember unMember = (MiraiMember) member1;
                    if(joinKye.get(unMember.getId().toString())==null){
                        continue;
                    }
                    unMember.unmuteBlocking();
                    joinKye.remove(joinKye.get(unMember.getId().toString()));
                    At at1 = new At(unMember.getId());
                    Messages unMessages = Messages.toMessages(at1, Text.of("检测到你已经看置顶公告自动解除禁言!"));
                    group.sendAsync(unMessages);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 解决成员退群的操作
     * @param miraiMemberLeaveEvent
     */
    @Override
    public void laveGroup(MiraiMemberLeaveEvent miraiMemberLeaveEvent) {
        MiraiMember member = miraiMemberLeaveEvent.getMember();
        LongID id = member.getId();
        member.sendAsync(id +"已退群!");
    }



    /**
     * hmd的录入功能
     * @param miraiGroupMessageEvent
     * @param sessionContext
     */
    @Override
    @Async()
    public void persistentSessions(MiraiGroupMessageEvent miraiGroupMessageEvent, ContinuousSessionContext  sessionContext) {
        miraiGroupMessageEvent.sendAsync("请输入举报人的id\n可随时输入 取消命令 取消录入，");
        Map<Integer,String> data = new HashMap<>();
        LongID id = miraiGroupMessageEvent.getAuthor().getId();
        final int[] a = {4};
        String[]  messages = new String[]{"请输入视频证据链接，如果没有请输入证据图片！","请输入处理的管理员id","请输入举报人被举报的时间","请输入被举报的服务器：例:S1","请输入举报原因"};
        final MessageContent message = sessionContext.waitingForNextMessage(miraiGroupMessageEvent.Key, (context, event) -> {
            int sum = a[0];
            if(event.getAuthor().getId().equals(id)){
                // 判断是否是同一个人
                if(event.getMessageContent().getPlainText().contains("取消")){
                    miraiGroupMessageEvent.sendAsync("已取消录入..");
                    return true;
                }
                data.put(sum+1,event.getMessageContent().getPlainText());
                if(sum <= -1){
                    Hmd hmd = getHmd(data);
                    try {
                        int insert = hmdDataMapper.insert(hmd);
                        if(insert==1){
                            miraiGroupMessageEvent.sendAsync("已成功录入数据库..");
                            return true;
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                        miraiGroupMessageEvent.sendAsync("数据库已存在此用户，请删除此用户在重新录入,或重新修改用户信息，或查询此用户的信息!");
                        return true;
                    }
                }

                miraiGroupMessageEvent.sendAsync(messages[sum]);
                a[0] = a[0] - 1;
                if(sum==0){
                    sum = sum - 1;
                    String imgUrl = getImgUrl(event, data);
                    data.put(sum,imgUrl);
                }
            }
            return false;
        });
        System.out.println(data);
    }
    /**
     * set信息转为对象
     * @param strings
     * @return
     */
    private Hmd getHmd(Map<Integer,String> strings){
        Hmd hmd = new Hmd();
        hmd.setId(strings.get(5));
        hmd.setYuanYin(strings.get(4));
        hmd.setServer(strings.get(3));
        hmd.setData(strings.get(2));
        hmd.setAdmin(strings.get(-1));
        hmd.setUrl(strings.get(0));
        return  hmd;
    }


    /**
     * 获取图片 并且保存本地
     * @param miraiGroupMessageEvent
     * @param map
     * @return
     */
    @SneakyThrows
    private String getImgUrl(MiraiGroupMessageEvent miraiGroupMessageEvent,Map<Integer,String> map){
        for (Message.Element<?> message : miraiGroupMessageEvent.getMessageContent().getMessages()) {
            if (message instanceof Image) {
                Image image =(Image) message;
                InputStream inputStream = image.getResource().openStream();
                // 先创建用户文件夹
                File file = new File("C:\\Users\\admin\\Desktop\\img" + map.get(5));
                file.mkdir();
                // 在把图片放在文件夹里面
                writeToLocal(file.getAbsolutePath(),inputStream);
                return "图片";
            }else {
                return miraiGroupMessageEvent.getMessageContent().getPlainText();
            }
        }
        return miraiGroupMessageEvent.getMessageContent().getPlainText();
    }

    /**
     * 文件流读取
     * @param destination
     * @param input
     * @throws IOException
     */
    private void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();

    }


}
