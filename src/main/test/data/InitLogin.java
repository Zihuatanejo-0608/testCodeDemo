package data;

import cn.hutool.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import tools.HuToolHttpUtil;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by andy on 2019/6/12.
 */
public class InitLogin {

    private static String urlHead = "http://101.qq.118.202:9999/";

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
        request.put("password",password);
        //发送验证码
        String verifyCodeKey = InitLogin.verifyCodeData();
        request.put("verifyCodeKey",verifyCodeKey);
        String k = "verifyCode:" + verifyCodeKey;
        //获取验证码
        //String v = RedisTest.redisData(k);
        String v = InitLogin.redisValue(k);
        request.put("verifyCodeInfo",v);
        JSONObject respone = HuToolHttpUtil.post(url, "", "", request);
        JSONObject data = (JSONObject) respone.get("data");
        String token = data.get("token").toString();
        System.out.println("登录成功!" + "\n" + "生成token:" + data.get("token") + "\n" + "~~~~~~~~~~~~~~~~~~~~~~~~~");

        return token;
    }

    //api版本
    public static String loginApi(String username,String password) throws Exception{
        String url = urlHead + "auth/login";
        JSONObject request = new JSONObject();
        request.put("username",username);
        String encryption = InitLogin.encryption(password);
        request.put("password",encryption);
        //发送验证码
        String verifyCodeKey = InitLogin.verifyCodeData();
        request.put("verifyCodeKey",verifyCodeKey);
        String k = "verifyCode:" + verifyCodeKey;
        //获取验证码
        String v = InitLogin.redisValue(k);
        request.put("verifyCodeInfo",v);

        JSONObject respone = HuToolHttpUtil.post(url, "", "", request);
        JSONObject data = (JSONObject) respone.get("data");
        String token = data.get("token").toString();
        System.out.println(data.get("token"));
        System.out.println("================================");
        System.out.println("================================");
        return token;
    }

    //秘钥
    public static String verifyCodeData() throws Exception{
        String url = urlHead + "auth/verifyCode";
        JSONObject respone = HuToolHttpUtil.get(url, null, null);
        JSONObject data = (JSONObject) respone.get("data");
        String verifyCodeKey = data.get("verifyCodeKey").toString();
        System.out.println(verifyCodeKey);

        return verifyCodeKey;
    }

    //通过api获取验证码
    public static String redisValue(String key){
        String url = urlHead + "auth/redisValue?key=" + key;
        try{
            JSONObject respone = HuToolHttpUtil.get(url, null, null);
            String data = (String)respone.get("data");
            System.out.println("验证码:" + data);
            return data;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("获取key接口失败!");
        }
        return null;
    }

}
