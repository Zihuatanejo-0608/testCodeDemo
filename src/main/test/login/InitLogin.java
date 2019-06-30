package login;

import com.alibaba.fastjson.JSONObject;
import tools.HttpMethod;
import tools.RedisTest;

/**
 * Created by andy on 2019/6/12.
 */
public class InitLogin {

    public static String login(String username,String password) throws Exception{
        //打印结果
        boolean flag = false;
        //定义url
        String url = "http://172.17.xx.85:30000/auth/login";

        JSONObject request = new JSONObject();

        request.put("username",username);
        request.put("password",password);
        //发送验证码
        String verifyCodeKey = VerifyCodeData.verifyCodeData();
        request.put("verifyCodeKey",verifyCodeKey);
        String k = "verifyCode:" + verifyCodeKey;
        //获取验证码
        String v = RedisTest.redisData(k);
        request.put("verifyCodeInfo",v);

        System.out.println("请求:" + request.toString());

        String respone = HttpMethod.httpPost(url,request.toString(),"","",flag);

        JSONObject obj  = JSONObject.parseObject(respone);
        JSONObject data = (JSONObject) obj.get("data");
        String token = data.get("token").toString();
        System.out.println("登录成功!" + "\n" + "生成token:" + data.get("token") + "\n" + "~~~~~~~~~~~~~~~~~~~~~~~~~");

        return token;
    }
}
