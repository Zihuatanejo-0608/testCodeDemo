package login;

import com.alibaba.fastjson.JSONObject;
import tools.HttpMethod;

/**
 * Created by andy on 2019/6/12.
 */
public class VerifyCodeData {
    //获取秘钥key
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
