package login;

import cn.hutool.json.JSONObject;
import data.InitLogin;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.CSVData;
import tools.HuToolHttpUtil;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by andy on 2019/6/11.
 *
 * 登陆模块
 *
 */
public class LoginTest {

    private static String urlHead = "http://101.qq.118.202:9999/";

    @DataProvider(name = "loginTest")
    public static Iterator<Object[]> testData() throws Exception{
        return (Iterator<Object[]>) new CSVData("loginTest.csv");
    }

    @Test(dataProvider = "loginTest")
    public void loginTest(Map<String,String> map) throws Exception{
        String url = urlHead + "auth/login";
        JSONObject request = new JSONObject();
        //String encryption = InitLogin.encryption(password);
        request.put("username",map.get("username"));
        if (map.get("password").equals("")){
            request.put("password",null);
        }else {
            String encryption = InitLogin.encryption(map.get("password"));
            request.put("password",encryption);
        }
        //发送验证码
        String verifyCodeKey = InitLogin.verifyCodeData();
        request.put("verifyCodeKey",verifyCodeKey);//秘钥key
        String k = "verifyCode:" + verifyCodeKey;//验证码key
        //从redis获取验证码
        //String v = RedisTest.redisData(k);
        String v = InitLogin.redisValue(k);
        request.put("verifyCodeInfo",v);//验证码

        JSONObject respone = HuToolHttpUtil.post(url, "", "", request);
        String actualMessage = (String) respone.get("message");
        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));

    }

}
