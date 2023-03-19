package com.jumpjump;


import com.freewayso.image.combiner.ImageCombiner;
import com.freewayso.image.combiner.enums.OutputFormat;
import com.jumpjump.bean.Server;
import com.jumpjump.bean.User;
import com.jumpjump.client.WebHttpClient;
import com.jumpjump.constants.ApiURLConstant;
import com.jumpjump.service.GroupAPIService;
import com.jumpjump.utils.CreateImgUtil;
import gui.ava.html.image.generator.HtmlImageGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUrl {


    @jakarta.annotation.Resource
    private CreateImgUtil botUtils;

    @Autowired
    private GroupAPIService groupAPIService;

    @Autowired
    private WebHttpClient webHttpClient;

    public static String s = "https://secure.gravatar.com/avatar/None?s=204&d=https://eaassets-a.akamaihd.net/battlelog/defaultavatars/default-avatar-36.png";

    @Test
    public  void getImage()throws Exception{
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 10808));
        factory.setProxy(proxy);
        factory.setReadTimeout(5000);  // 单位为ms
        factory.setConnectTimeout(5000);  // 单位为ms
        RestTemplate restTemplate = new RestTemplate(factory);


        Resource resource = restTemplate.getForObject(s, Resource.class);
        //获取entity中的数据
        InputStream inputStream = resource.getInputStream();
        BufferedImage read = ImageIO.read(inputStream);
        ImageCombiner imageCombiner = new ImageCombiner(read, OutputFormat.JPG);
        imageCombiner.combine();
        imageCombiner.save("C:\\Users\\admin\\Desktop\\miraicore\\mirai\\src\\main\\resources\\a.jpg");
        //创建输出流  输出到本地

    }

    /**
     * 设置图片的大小
     */

    @Test
    public void test02()throws Exception{
        User user = groupAPIService.quireUser(ApiURLConstant.getQueryUserAll,"yu_jumpjump");
        String suchdogs = groupAPIService.quireCheck(ApiURLConstant.checkUserBan, "yu_jumpjump");
//        List<Aircraft> yuJumpjump = groupAPIService.quireAircraft(ApiURLConstant.getQueryUserAll, "yu_jumpjump");
        InputStream img = botUtils.createImg(user, suchdogs,null);
        BufferedImage read = ImageIO.read(img);
        ImageIO.write(read,"jpg",new File("C:\\Users\\admin\\Desktop\\miraicore\\mirai\\src\\test\\resources\\img\\img.png"));
    }



    @Test
    public void test3() throws IOException {
        String s = "余显龙";
        String html = "<html>\n" +
                "  <head>" +
                "    <title>渐变色示例</title>" +
                "    <style>" +
                "      body {" +
                "        background: linear-gradient(to right, #ff416c, #ff4b2b);" +
                "      }" +
                "    </style>" +
                "  </head>" +
                "  <body style='background:red'>" +
                "    <h1>这是一个渐变色示例</h1>" +
                "    <p>这是一个使用 CSS 渐变色的 HTML 示例。</p>" +
                "  </body>" +
                "</html>";

        HtmlImageGenerator htmlImageGenerator = new HtmlImageGenerator();
        htmlImageGenerator.loadHtml(html);

        ImageOutputStream imOut = null;
        FileOutputStream fileOut = null;
        ByteArrayOutputStream byteOut = null;
            // 拿图片流
            BufferedImage bufferedImage = htmlImageGenerator.getBufferedImage();
            bufferedImage.flush();
            // 写图片流
            byteOut = new ByteArrayOutputStream();
            imOut = ImageIO.createImageOutputStream(byteOut);
            ImageIO.write(bufferedImage, "png", imOut);
            // 拿到图片流
            byte[] b = byteOut.toByteArray();
            String path = "C:\\Users\\admin\\Desktop\\miraicore\\mirai\\src\\test\\resources\\img\\test.png";
            fileOut = new FileOutputStream(path);
            fileOut.write(b);
            fileOut.flush();
            imOut.close();
            fileOut.close();
            byteOut.close();
    }


    @Test
    public  void test4(){
//        Server server = groupAPIService.quireWeapon(ApiURLConstant.getServer,"S10");
        Server server = groupAPIService.quireServer(ApiURLConstant.getServer, "s1");
        System.out.println(server);
    }


    @Test
    public void test5(){
        groupAPIService.quireUser(ApiURLConstant.getQueryUserAll,"yu_jumpjump");
    }
}
