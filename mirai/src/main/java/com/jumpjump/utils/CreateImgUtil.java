package com.jumpjump.utils;


import com.freewayso.image.combiner.ImageCombiner;
import com.freewayso.image.combiner.enums.GradientDirection;
import com.freewayso.image.combiner.enums.OutputFormat;
import com.freewayso.image.combiner.enums.ZoomMode;
import com.jumpjump.bean.Rotation;
import com.jumpjump.bean.Server;
import com.jumpjump.bean.Vehicles;
import com.jumpjump.bean.User;
import com.jumpjump.client.WebHttpClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

/**
 * 生成图片
 */
@Component
@Slf4j
public class CreateImgUtil {


    private final WebHttpClient webHttpClient;

    public CreateImgUtil(WebHttpClient webHttpClient) {
        this.webHttpClient = webHttpClient;
    }

    /**
     *查询个人数据
     * @param user
     */
    public  InputStream createImg(User user, String check, List<Vehicles> aircrafts) {
        BufferedImage avatar = null;
        ClassPathResource resource = new ClassPathResource("img/bag.png");
        InputStream inputStream = null;
        BufferedImage read = null;
        ImageCombiner imageCombiner = null;
        InputStream combinedImageStream = null;
        // 配置字体颜色
        Color fontColor = new Color(253,253,253);
        try {
            // 头像
            avatar = ImageIO.read(webHttpClient.setGetImg(user.getAvatar()));
            inputStream = resource.getInputStream();
            read = ImageIO.read(inputStream);
            imageCombiner= new ImageCombiner(read,OutputFormat.JPG);
            // 设置 渐变色
            imageCombiner.addRectangleElement(500,500,1000, 1000).setAlpha(.1f);
            //针对背景和整图的设置
            imageCombiner.setBackgroundBlur(1);     //设置背景高斯模糊（毛玻璃效果）
            imageCombiner.setQuality(.1f);           //设置图片保存质量（0.0~1.0，Java9以下仅jpg格式有效）

            // 设置用户头像
            imageCombiner.addImageElement(avatar,100,162,200,200,ZoomMode.WidthHeight).setRoundCorner(500).setX(50);
            // 设置用户姓名
            imageCombiner.addTextElement(user.getUserName(),130,320,120).setColor(new Color(215, 246, 250, 250)).setX(270);
            // 设置用户等级
            imageCombiner.addTextElement("等级 "+user.getRank(),60,320,270).setColor(Color.WHITE).setX(280);
            // 设置用户游戏时长
            imageCombiner.addTextElement("游玩时长 "+user.getSecondsPlayed()/3600,60,630,270).setColor(fontColor).setX(590);


            // 设置生涯
            imageCombiner.addTextElement(
                           "K/D: "+user.getInfantryKillDeath()+" " +
                                "KPM: "+user.getKillsPerMinute()+" " +
                                "SPM: "+user.getScorePerMinute(),
                            "微软雅黑", 55, 80, 400).setColor(fontColor);
            imageCombiner.addTextElement(
                       "命中率: "+user.getAccuracy()+"  " +
                            "爆头率: "+user.getHeadshots()+"  " +
                            "爆头数: "+user.getDeaths(),
                    "微软雅黑", 55, 80, 490).setColor(fontColor);

            imageCombiner.addTextElement(
                       "击杀: "+user.getKills()+"  " +
                            "死亡: "+user.getDeaths()+"  " +
                            "救援: "+user.getRevives(),
                    "微软雅黑", 55, 80, 580).setColor(fontColor);

            imageCombiner.addTextElement(
                       "胜率: "+user.getWinPercent()+"  " +
                            "胜场: "+user.getWins()+"  " +
                            "败场: "+user.getLoses(),
                    "微软雅黑", 55, 80, 670).setColor(fontColor);

            imageCombiner.addTextElement(
                       "救援: "+user.getRevives()+"  " +
                            "最远爆头 : "+user.getLongestHeadShot()+"  " +
                            "最高连杀: "+user.getHighestKillStreak(),
                    "微软雅黑", 55, 80, 760).setColor(fontColor);


            imageCombiner.addTextElement("*联ban状态:"+check,"微软雅黑", 45, imageCombiner.getCanvasWidth()-500, 100).setColor(Color.RED);
            imageCombiner.combine();
            combinedImageStream= imageCombiner.getCombinedImageStream();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("图片渲染错误.... 可能问题存在类BotUtils中");
        }
        return combinedImageStream;

    }


    /**
     * 查询服务器 并且返回图片
     */
    @SneakyThrows
    public InputStream createServerImg(Server server){

        ImageCombiner imageCombiner;
        BufferedImage currentMapImage;
        int imgX=50;
        int imgY=280;
        int wzX=80;
        int wzY=350;
        imageCombiner = new ImageCombiner(1000, 670, new Color(14, 15, 20), OutputFormat.JPG);
        currentMapImage = ImageIO.read(webHttpClient.setGetImg(server.getCurrentMapImage()));
        imageCombiner.addImageElement(currentMapImage,50,70,170,100,ZoomMode.WidthHeight).setRoundCorner(20);
        imageCombiner.addTextElement(server.getPrefix(),"宋体",21,230,75).setColor(255, 255, 255);
        imageCombiner.addTextElement(
                server.getPlayerAmount()+"/"+server.getMaxPlayerAmount()+"-"+server.getCurrentMap(),
                "宋体",18,230,110).setColor(255, 255, 255);
        imageCombiner.addTextElement(
                server.getRegion()+"/"+server.getCountry()+"-"+server.getMode(),
                "宋体",18,230,140).setColor(255, 255, 255);

        imageCombiner.addTextElement(
                "服务器地图循环",
                "宋体",24,50,250).setColor(255, 255, 255);

        for(Rotation rotation:server.getRotation()){
            if(imgX>950){
                imgX=50;
                imgY+=120;
                wzX=80;
                wzY+=120;
                continue;
            }
            imageCombiner.addImageElement(ImageIO.read(webHttpClient.setGetImg(rotation.getImage())),imgX,imgY,110,70,ZoomMode.WidthHeight).setRoundCorner(10);
            imageCombiner.addTextElement(rotation.getMapname(),"宋体",14,wzX,wzY).setColor(255, 255, 255);
            imageCombiner.addTextElement(rotation.getMode(),"宋体",14,wzX,wzY+15).setColor(255, 255, 255);
            imageCombiner.combine();
            imgX+=130;
            wzX+=130;

        }
        imageCombiner.addTextElement("服主信息","宋体",25,780,20).setColor(255, 255, 255);
        imageCombiner.addImageElement(ImageIO.read(webHttpClient.setGetImg(server.getOwner().getAvatar())),870,50,80,80,ZoomMode.WidthHeight);
        imageCombiner.addTextElement(server.getOwner().getName(),"宋体",18,850,120).setColor(255, 255, 255);
        imageCombiner.combine();
        return imageCombiner.getCombinedImageStream();
    }


}
