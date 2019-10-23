package aiPaasTest.loginService;

import cn.hutool.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.testng.annotations.Test;
import toolsUnit.HuToolHttpUtil;
import toolsUnit.RedisUtil;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by andy on 2019/5/27.
 *
 * 登陆,生成token
 */
public class LoginTest {
    private String headerName = "Authorization";
    private static String urlHead = "http://101.91.118.xx:9999/";

    public static String encryption(String str) throws Exception{
        String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA2OExk3OYNmgWlkVxAOu0z8MM1i1YLvagfgDzEHBjAmQRX8dNVNbETW36Mpfwaw5jKtvcB4Rnm5UZHhrAXx/bCBeQDj5Ow3llyQYVVow8yxQB6h+VO6ukaUW9j7/MwwVgvQt9pnn53UHbNdKUGCIL1Z69/VNn9cZzcKqsc6gwuKO+I2IJl68hA3ya5ZvXLtHs3oyHQb59pzYntZ0BBxLd37StsIUGP3V2RAqpzHE0oLDTJDqAVHi+hIo/4XowVQmnZLGq+INtpllREChP0YVqlh0j/IiTEpEVxNVnaJHHlN5xekSCWR8/h7QYMjJBxOhGacZYaDTl1Yo5ANBGxldWCOU1p8a52oMzWuulDKciQUUXdZ7UmWxeceoKirU0CwArKOn1X+av/dJ0iyL6on5SuLziljhl+2cKAuk7IIiYFYrNkDFmRmkTLn67LwkBEFomGCu1aSW8xOVJ7PL0KVya9xiqy5Ukf4kQfp6b7/GfRJjJKKAv7Ucv9sPuO0mBdUf4ZUf2eLwcINakHlyzqSARCXdExh+IRLyeldw6aEkn/XEjeT0ZDzrFQVD29BdoIlCRXSmvsYffDAgVjCjxZ1CEfcRAWav1uUSmJRD+C/etTu4LZNmyYkIE9VelWniozHbEh9Za6wMkt94acj7hDNkpQNN1EIjZXg4m3xr+lDDQCXUCAwEAAQ==";
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    public static String login(String username,String password) throws Exception{
        String url = urlHead + "auth/login";
        JSONObject request = new JSONObject();

        request.put("username",username);
        String encryption = LoginTest.encryption(password);
        request.put("password",encryption);
        //发送验证码
        String verifyCodeKey = LoginTest.verifyCodeData();

        request.put("verifyCodeKey",verifyCodeKey);//验证码key
        String k = "verifyCode:" + verifyCodeKey;
        //获取验证码
        String v = RedisUtil.redisData(k);//验证码value,在redis
        request.put("verifyCodeInfo",v);

        JSONObject respone = HuToolHttpUtil.post(url, "", "", request);
        JSONObject data = (JSONObject) respone.get("data");
        String token = data.get("token").toString();
        System.out.println(data.get("token"));
        System.out.println("================================");
        System.out.println("================================");

        return token;
    }

    //api版本
    public static String loginApiTest(String username,String password) throws Exception{
        //打印结果
        boolean flag = false;
        //定义url
        String url = urlHead + "auth/login";

        JSONObject request = new JSONObject();

        request.put("username",username);
        String encryption = LoginTest.encryption(password);
        request.put("password",encryption);
        //发送验证码
        String verifyCodeKey = LoginTest.verifyCodeData();
        request.put("verifyCodeKey",verifyCodeKey);
        String k = "verifyCode:" + verifyCodeKey;
        //获取验证码
        String v = LoginTest.redisValue(k);
        request.put("verifyCodeInfo",v);

        JSONObject respone = HuToolHttpUtil.post(url, "", "", request);
        JSONObject data = (JSONObject) respone.get("data");
        String token = data.get("token").toString();
        System.out.println(data.get("token"));
        System.out.println("================================");
        System.out.println("================================");
        return token;

    }

    //redis版本
    public void loginTest() throws Exception{
        //打印结果
        boolean flag = false;
        //定义url
        String url = urlHead + "auth/login";

        String userName = "chukaimin-it@qq.com.cn";
        String password = "chukaimin@12345";
        JSONObject request = new JSONObject();

        request.put("username",userName);
        String encryption = LoginTest.encryption(password);
        request.put("password",encryption);
        //发送验证码
        String verifyCodeKey = LoginTest.verifyCodeData();
        request.put("verifyCodeKey",verifyCodeKey);//验证码key
        String k = "verifyCode:" + verifyCodeKey;
        //获取验证码
        String v = RedisUtil.redisData(k);//验证码value,在redis
        request.put("verifyCodeInfo",v);

        JSONObject respone = HuToolHttpUtil.post(url, "", "", request);

//        String errorNum = "userNameKey:" + userName;
//        RedisUtil.redisData(errorNum);

        JSONObject data = (JSONObject) respone.get("data");
        if (data != null){
            System.out.println("token:" + data.get("token"));
        }
    }

    @Test
    public void logout() throws Exception{
        String token = login("chukaimin-it@qq.com.cn","chukaimin@12345");
        String url = urlHead + "auth/logout";
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    //下发验证码接口
    public void verifyCode() throws Exception{
        String url = urlHead + "auth/verifyCode";
        JSONObject respone = HuToolHttpUtil.get(url, "", "");
        JSONObject data = (JSONObject) respone.get("data");
        System.out.println(data.get("verifyCodeKey"));
    }

    public static String verifyCodeData() throws Exception{
        String url = urlHead + "auth/verifyCode";
        JSONObject respone = HuToolHttpUtil.get(url, "", "");
        JSONObject data = (JSONObject) respone.get("data");
        String verifyCodeKey = data.get("verifyCodeKey").toString();
        System.out.println(verifyCodeKey);
        return verifyCodeKey;
    }

    public static String redisValue(String key){
        String url = urlHead + "auth/redisValue?key=" + key;
        try{
            JSONObject response = HuToolHttpUtil.get(url, "", "");
            String data = (String)response.get("data");
            System.out.println("验证码:" + data);
            return data;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("获取key接口失败!");
        }
        return null;
    }

    @Test
    public static void redisValueA() throws Exception{
        String key = "verifyCode:" + LoginTest.verifyCodeData();
        String url = urlHead + "auth/redisValue?key=" + key;
        JSONObject respone = HuToolHttpUtil.get(url, "", "");
        System.out.println((String)respone.get("data"));
    }

}
