package httpTest.loginService;

import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.Test;
import testTools.HttpMethod;
import testTools.RedisTest;

/**
 * Created by andy on 2019/5/27.
 *
 * 登陆,生成token
 */
public class LoginTest {
    private String headerName = "Authorization";

    public static String login(String username,String password) throws Exception{
        //打印结果
        boolean flag = false;
        //定义url
        String url = "http://172.17.xx.85:30000/auth/login";

        JSONObject request = new JSONObject();

        request.put("username",username);
        request.put("password",password);
        //发送验证码
        String verifyCodeKey = LoginTest.verifyCodeData();
        request.put("verifyCodeKey",verifyCodeKey);//验证码key
        String k = "verifyCode:" + verifyCodeKey;
        //获取验证码
        String v = RedisTest.redisData(k);//验证码value,在redis
        request.put("verifyCodeInfo",v);

        System.out.println("请求:" + request.toString());

        String respone = HttpMethod.httpPost(url,request.toString(),"","",flag);

        JSONObject obj  = JSONObject.parseObject(respone);
        JSONObject data = (JSONObject) obj.get("data");
        String token = data.get("token").toString();
        System.out.println(data.get("token"));
        System.out.println("================================");
        System.out.println("================================");

        return token;
    }

    @Test
    public void loginTest() throws Exception{
        //打印结果
        boolean flag = false;
        //定义url
        String url = "http://172.17.xx.xx:30000/auth/login";

        String userName = "Zihuatanejo@qq.com.cn";
        String password = "Zihuatanejo@12345";
        JSONObject request = new JSONObject();

        request.put("username",userName);
        request.put("password",password);
        //发送验证码
        String verifyCodeKey = LoginTest.verifyCodeData();
        request.put("verifyCodeKey",verifyCodeKey);//验证码key
        String k = "verifyCode:" + verifyCodeKey;
        //获取验证码
        String v = RedisTest.redisData(k);//验证码value,在redis
        request.put("verifyCodeInfo",v);

        System.out.println("请求:" + request.toString());

        String respone = HttpMethod.httpPost(url,request.toString(),"","",flag);

        String errorNum = "userNameKey:" + userName;
        RedisTest.redisData(errorNum);

        JSONObject obj  = JSONObject.parseObject(respone);
        JSONObject data = (JSONObject) obj.get("data");
        if (data != null){
            System.out.println("token:" + data.get("token"));
        }
    }

    @Test
    public void logout() throws Exception{

        String token = login("Zihuatanejo@qq.com.cn","Zihuatanejo@12345");

        boolean flag = false;
        String url = "http://172.17.xx.85:30000/auth/logout";

        HttpMethod.httpGet(url,"",headerName,token,flag);
    }

    @Test
    //下发验证码接口
    public void verifyCode() throws Exception{
        boolean flag = false;

        String url = "http://172.17.xx.85:30000/auth/verifyCode";
        String respone = HttpMethod.httpGet(url,"","","",flag);

        JSONObject obj  = JSONObject.parseObject(respone);
        JSONObject data = (JSONObject) obj.get("data");
        System.out.println(data.get("verifyCodeKey"));
    }

    public static String verifyCodeData() throws Exception{
        boolean flag = false;

        String url = "http://172.17.xx.85:30000/auth/verifyCode";
        String respone = HttpMethod.httpGet(url,"","","",flag);

        JSONObject obj  = JSONObject.parseObject(respone);
        JSONObject data = (JSONObject) obj.get("data");
        String verifyCodeKey = data.get("verifyCodeKey").toString();
        System.out.println(verifyCodeKey);

        return verifyCodeKey;
    }

}
