package com.jumpjump.bullhorsebot.utils;


import com.freewayso.image.combiner.ImageCombiner;
import com.freewayso.image.combiner.enums.OutputFormat;
import com.freewayso.image.combiner.enums.ZoomMode;
import com.jumpjump.bullhorsebot.bean.*;
import com.jumpjump.bullhorsebot.client.WebHttpClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Comparator;
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
            }
            imageCombiner.addImageElement(ImageIO.read(webHttpClient.setGetImg(rotation.getImage())),imgX,imgY,110,70,ZoomMode.WidthHeight).setRoundCorner(10);
            imageCombiner.addTextElement(rotation.getMapname(),"宋体",16,wzX,wzY).setColor(255, 255, 255);
            imageCombiner.addTextElement(rotation.getMode(),"宋体",16,wzX,wzY+15).setColor(255, 255, 255);
            imageCombiner.combine();
            imgX+=130;
            wzX+=130;

        }
        imageCombiner.addTextElement("服主信息","宋体",25,780,20).setColor(255, 255, 255);
        imageCombiner.addImageElement(ImageIO.read(webHttpClient.setGetImg(server.getOwner().getAvatar())),870,50,80,80,ZoomMode.WidthHeight);
        imageCombiner.addTextElement(server.getOwner().getName(),"宋体",18,850,130).setColor(255, 255, 255);
        imageCombiner.combine();
        return imageCombiner.getCombinedImageStream();
    }


    /**
     * 查询用户信息，并返回图片
     * @return
     */

    @SneakyThrows
    public InputStream createUserDataImg(User user, String banCheck){
        ImageCombiner imageCombiner = new ImageCombiner(1500,850,new Color(14, 15, 20),OutputFormat.JPG);
        imageCombiner.addTextElement("个人生涯","宋体",32,50,270).setColor(255,255,255);
        InputStream inputStream = new ClassPathResource("img/bfv.png").getInputStream();
        imageCombiner.addRectangleElement(50,300,580,250).setColor(22, 24, 31).setRoundCorner(20);
        imageCombiner.setQuality(.1f);
        imageCombiner.addImageElement(ImageIO.read(inputStream),1250,5,200,100,ZoomMode.WidthHeight);
        imageCombiner.addTextElement("联办查询:"+banCheck,30,50,680).setColor(255, 255, 255);
        imageCombiner.addImageElement(ImageIO.read(webHttpClient.setGetImg(user.getAvatar())),50,105,100,100,ZoomMode.WidthHeight).setRoundCorner(100);
        imageCombiner.addTextElement(user.getUserName(),30,160,110).setColor(255, 255, 255);
        imageCombiner.addTextElement("等级:"+user.getRank(),24,160,145).setColor(255, 255, 255);
        imageCombiner.addTextElement("游玩时间:"+(user.getSecondsPlayed()/3600),24,160,170).setColor(255, 255, 255);
        //写个人生涯
        imageCombiner.addTextElement(
                "K/D: "+user.getInfantryKillDeath()+" " +
                        "KPM: "+user.getKillsPerMinute()+" " +
                        "SPM: "+user.getScorePerMinute(),
                "宋体", 24, 120, 310).setColor(255, 255, 255);

        imageCombiner.addTextElement(
                "命中率: "+user.getAccuracy()+"  " +
                        "爆头率: "+user.getHeadshots()+"  " +
                        "爆头数: "+user.getDeaths(),
                "微软雅黑", 20, 120, 360).setColor(255, 255, 255);
        imageCombiner.addTextElement(
                "击杀: "+user.getKills()+"  " +
                        "死亡: "+user.getDeaths()+"  " +
                        "救援: "+user.getRevives(),
                "微软雅黑", 20, 120, 410).setColor(255, 255, 255);
        imageCombiner.addTextElement(
                "胜率: "+user.getWinPercent()+"  " +
                        "胜场: "+user.getWins()+"  " +
                        "败场: "+user.getLoses(),
                "微软雅黑", 20, 120, 460).setColor(255, 255, 255);

        imageCombiner.addTextElement(
                "救援: "+user.getRevives()+"  " +
                        "最远爆头 : "+user.getLongestHeadShot()+"  " +
                        "最高连杀: "+user.getHighestKillStreak(),
                "微软雅黑", 20, 120, 510).setColor(255, 255, 255);


        //武器渲染
        imageCombiner.addRectangleElement(750,150,700,300).setColor(22, 24, 31).setRoundCorner(20);
        imageCombiner.addTextElement("武器/载具(以击杀数前5显示)","宋体",28,750,120).setColor(255,255,255);
        imageCombiner.addTextElement("枪械名称                      KPM     KILLS      准度      爆头率",24,800,170).setColor(255,255,255);
        List<Weapon> weapons = user.getWeapons();
        weapons.sort(Comparator.comparing(Weapon::getKills).reversed());
        List<Weapon> weapons5 = weapons.subList(0, 5);
        int wzY = 210;
        for(Weapon weapon:weapons5){
//            imageCombiner.addImageElement(ImageIO.read(webHttpClient.setGetImg(weapon.getImage())),760,imgY,120,50,ZoomMode.WidthHeight);
            imageCombiner.addTextElement(weapon.getWeaponName(),24,800,wzY).setColor(255,255,255);
            imageCombiner.addTextElement(weapon.getKillsPerMinute()+"",20,1050,wzY).setColor(255,255,255);
            imageCombiner.addTextElement(weapon.getKills()+"",24,1140,wzY).setColor(255,255,255);
            imageCombiner.addTextElement(weapon.getAccuracy()+"",24,1240,wzY).setColor(255,255,255);
            imageCombiner.addTextElement(weapon.getHeadshots()+"",24,1330,wzY).setColor(255,255,255);
            wzY+=50;
        }

        // 载具渲染
        imageCombiner.addRectangleElement(750,480,700,300).setColor(22, 24, 31).setRoundCorner(20);
        imageCombiner.addTextElement("载具名称                      KPM     KILLS      摧毁      种类",24,800,500).setColor(255,255,255);
        List<Vehicles> vehicles = user.getVehicles();
        vehicles.sort(Comparator.comparing(Vehicles::getKills).reversed());
        List<Vehicles> vehicles5 = vehicles.subList(0, 5);
        int Y = 540;
        for(Vehicles vehicle:vehicles5){
            imageCombiner.addTextElement(vehicle.getVehicleName(),24,800,Y).setColor(255,255,255);
            imageCombiner.addTextElement(vehicle.getKillsPerMinute()+"",24,1050,Y).setColor(255,255,255);
            imageCombiner.addTextElement(vehicle.getKills()+"",24,1140,Y).setColor(255,255,255);
            imageCombiner.addTextElement(vehicle.getDestroyed()+"",24,1240,Y).setColor(255,255,255);
            imageCombiner.addTextElement(vehicle.getType()+"",24,1330,Y).setColor(255,255,255);
            Y+=50;
        }
        imageCombiner.addTextElement("爱来自:8653全体管理！",20,1280,820).setColor(255, 255, 255);
        imageCombiner.combine();
        InputStream combinedImageStream = imageCombiner.getCombinedImageStream();
        return combinedImageStream;
    }


}
