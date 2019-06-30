package dubboTest.iDubboQueryApi;

import dataEntity.T_SERVICE_KEY_INFO;
import dataEntity.T_SERVICE_KEY_RELA;
import testTools.SelectKeyRela;
import com.alibaba.fastjson.JSONObject;
import com.xx.bigdata.actual.api.IDubboQueryApi;
import com.xx.bigdata.actual.api.model.RequestStringDO;
import com.xx.bigdata.actual.api.model.ResponseStringDO;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by andy on 2019/3/14.
 */
public class QueryString {

    private String type = "hbase-hongbao-000002";
    private String key;
    private String key_id;
    private String user_name;

    @BeforeClass
    public void testBefore() throws Exception{
        List<T_SERVICE_KEY_RELA> listRela = selectKeyRela.selectRela(type);
        if (listRela != null){
            for (T_SERVICE_KEY_RELA rela : listRela){
                System.out.println("**************");
                System.out.println("key:" + rela.getKey());
                key = rela.getKey();
                System.out.println("inst_interface_type:" + rela.getInst_interface_type());
            }
        }

        List<T_SERVICE_KEY_INFO> listInfo = selectKeyRela.selectInfo(key);
        if (listInfo != null){
            for (T_SERVICE_KEY_INFO info : listInfo){
                System.out.println("**************");
                key_id = String.valueOf(info.getKey_id());
                user_name = info.getUser_name();
                System.out.println("user_name:" + info.getUser_name());
                System.out.println("key_id:" + info.getKey_id());
                System.out.println("key:" + info.getKey());
                System.out.println("key_decr:" + info.getKey_decr());
            }
        }
    }

    @Test
    public void queryStringTest(){
        try{
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:consumer.xml");
            IDubboQueryApi iDubboQueryApi = (IDubboQueryApi) applicationContext.getBean("iDubboQueryApi");

            RequestStringDO requestStringDO = new RequestStringDO();

            JSONObject request = new JSONObject();
            request.put("pageCount","20");
            request.put("pageNum","1");
            request.put("keyId",key_id);
            request.put("userName",user_name);
            request.put("type",type);
            request.put("param","phone:13197396828");//user:2015,merchant:2015
            String md5Value= DigestUtils.md5Hex("userName=" + user_name + "&keyId=" + key_id + "&type=" + type +"&key=" + key);
            System.out.println(md5Value);
            request.put("md5Value",md5Value);

            //String str = request.toJSONString();

            requestStringDO.setRequestString(request.toJSONString());

            ResponseStringDO responseStringDO = iDubboQueryApi.queryString(requestStringDO);
            System.out.println("返回结果:" + responseStringDO.getResponseString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
