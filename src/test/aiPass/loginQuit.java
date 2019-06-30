package aiPass;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by andy on 2019/4/28.
 *
 * 登录、登出
 */
public class loginQuit {



    public static void login(WebDriver webDriver){
        //接住List,取值
//        List<String> list = ExcelOperation.excelLogin(title);
//        String post = list.get(0);
//        String name = list.get(1);
//        String pwd = list.get(2);

        String verifyCode = null;

        try {
            if(webDriver.findElement(By.id("username")).isDisplayed()){
                webDriver.findElement(By.id("username")).sendKeys("Zihuatanejo@xx.com.cn");
            }
            if(webDriver.findElement(By.id("password")).isDisplayed()){
                webDriver.findElement(By.id("password")).sendKeys("Zihuatanejo@12345");
            }
            if (webDriver.findElement(By.id("verifyImg")).isDisplayed()){
                verifyCode = webDriver.findElement(By.id("verifyImg")).getAttribute("data-info");
            }

            String key = "verifyCode:" + verifyCode;

            if (webDriver.findElement(By.id("verifyImg")).isDisplayed()){
                webDriver.findElement(By.id("captcha")).sendKeys(loginQuit.redisData(key));
            }
            if(webDriver.findElement(By.id("loginButton")).isDisplayed()){
                webDriver.findElement(By.id("loginButton")).click();
            }
        }catch (Exception e){
            System.out.println("登录异常!");
        }
    }

    //退出按钮
    public static void logout(WebDriver webDriver){
        try{
            if (webDriver.findElement(By.id("layoutButton")).isDisplayed()){
                webDriver.findElement(By.id("layoutButton")).click();
            }
            if (webDriver.findElement(By.xpath("//button[contains(@class,'el-button el-button--default el-button--small el-button--primary ')]")).isDisplayed()){
                webDriver.findElement(By.xpath("//button[contains(@class,'el-button el-button--default el-button--small el-button--primary ')]")).sendKeys(Keys.ENTER);
                System.out.println("===================" + "用户退出" + "===================");
            }else {
                System.out.println("就是这里有问题");
            }
        }catch (Exception e){
            System.out.println("退出异常!");
        }
    }

    //获取redis值方法,集群
    public static String redisData(String key){
        try{
            Set<HostAndPort> nodes = new HashSet<HostAndPort>();
            nodes.add(new HostAndPort("172.17.xx.11",36379));
            nodes.add(new HostAndPort("172.17.xx.11",36381));
            nodes.add(new HostAndPort("172.17.xx.12",36379));
            nodes.add(new HostAndPort("172.17.xx.12",36381));
            nodes.add(new HostAndPort("172.17.xx.13",36379));
            nodes.add(new HostAndPort("172.17.xx.13",36381));

            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(10);//最大连接数
            jedisPoolConfig.setMaxWaitMillis(2000);//最大等待时间
            jedisPoolConfig.setTestOnBorrow(true);//获得链接有效性检查

            JedisCluster jedisCluster = new JedisCluster(nodes,jedisPoolConfig);
            String value = jedisCluster.get(key);
            System.out.println("验证码:" + jedisCluster.get(key) + "\n剩余时间:" + jedisCluster.ttl(key));
            return value;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
