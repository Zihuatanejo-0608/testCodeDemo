package login;

import com.alibaba.fastjson.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.CSVData;
import tools.HttpMethod;
import tools.RedisTest;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by andy on 2019/6/11.
 */
public class LoginTest {

    @DataProvider(name = "loginTest")
    public static Iterator<Object[]> testData() throws Exception{
        return (Iterator<Object[]>) new CSVData("loginTest.csv");
    }

    @Test(dataProvider = "loginTest")
    public void loginTest(Map<String,String> map) throws Exception{
        boolean flag = false;
        String url = "http://172.17.xx.85:30000/auth/login";

        JSONObject request = new JSONObject();

        request.put("username",map.get("username"));
        if (map.get("password").equals("")){
            request.put("password",null);
        }else {
            request.put("password",map.get("password"));
        }

        //发送验证码
        String verifyCodeKey = VerifyCodeData.verifyCodeData();
        request.put("verifyCodeKey",verifyCodeKey);//秘钥key
        String k = "verifyCode:" + verifyCodeKey;//验证码key
        //从redis获取验证码
        String v = RedisTest.redisData(k);
        request.put("verifyCodeInfo",v);//验证码

        System.out.println("请求对象:" + request.toString());
        String respone = HttpMethod.httpPost(url,request.toString(),"","",flag);

        JSONObject obj  = JSONObject.parseObject(respone);
        String actualMessage = (String) obj.get("message");

        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);

        //断言
        Assert.assertEquals(actualMessage,map.get("message"));

    }

}
