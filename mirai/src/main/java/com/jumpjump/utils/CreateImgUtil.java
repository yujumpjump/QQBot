package com.jumpjump.utils;


import com.freewayso.image.combiner.ImageCombiner;
import com.freewayso.image.combiner.enums.OutputFormat;
import com.freewayso.image.combiner.enums.ZoomMode;
import com.jumpjump.bean.Vehicles;
import com.jumpjump.bean.User;
import com.jumpjump.client.WebHttpClient;
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
//            imageCombiner= new ImageCombiner(read, OutputFormat.JPG);
            imageCombiner= new ImageCombiner(1800,1000,new Color(21, 23, 33), OutputFormat.JPG);
            // 设置 渐变色
//            imageCombiner.addRectangleElement(0,0, imageCombiner.getCanvasWidth(), imageCombiner.getCanvasHeight()).setGradient(
//                    new Color(28, 45, 44, 208),
//                    new Color(52, 21, 38),
//                    0,
//                    imageCombiner.getCanvasHeight(),
//                    GradientDirection.TopBottom
//            ).setAlpha(.4f);
            //针对背景和整图的设置
//            imageCombiner.setBackgroundBlur(1);     //设置背景高斯模糊（毛玻璃效果）
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
    public InputStream createServerImg(String serverUlr){
      return null;

    }


}
