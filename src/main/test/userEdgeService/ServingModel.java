package userEdgeService;

import com.alibaba.fastjson.JSONObject;
import login.InitLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.CSVData;
import tools.HttpMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by andy on 2019/6/17.
 */
public class ServingModel {
    private String token;
    private String headerName = "Authorization";
    private String taskId;
    private String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    @BeforeClass(alwaysRun = true)
    public String tokenOut() throws Exception{
        token = InitLogin.login("Zihuatanejo@xx.com.cn","Zihuatanejo@12345");
        return token;
    }

    @DataProvider(name = "addModelTest")
    public static Iterator<Object[]> testData() throws Exception{
        return (Iterator<Object[]>) new CSVData("addModelTest.csv");
    }

    @DataProvider(name = "updateModelTest")
    public static Iterator<Object[]> testDataB() throws Exception{
        return (Iterator<Object[]>) new CSVData("updateModelTest.csv");
    }

    @Test(dataProvider = "addModelTest")
    public void addModelTest(Map<String,String> map) throws Exception{
        boolean flag = false;
        String url = "http://172.17.xx.85:30000/servingModel/addModel";

        JSONObject request = new JSONObject();
        //模型名字,类型,描述
        if (map.get("name").equals("str")){
            request.put("name","M" + str);
        }
        request.put("type",map.get("type"));//dict_image_info表image_name字段,test_name
        request.put("description",map.get("description"));
        System.out.println("请求对象:" + request.toString());

        String respone = HttpMethod.httpPost(url,request.toString(),headerName,token,flag);

        JSONObject obj  = JSONObject.parseObject(respone);
        String actualMessage = (String) obj.get("message");

        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));

    }

    //@Test(dataProvider = "updateModelTest")
    public void updateModelTest(Map<String,String> map) throws Exception{
        boolean flag = false;
        String url = "http://172.17.xx.85:30000/servingModel/updateModel";

        JSONObject request = new JSONObject();
        if (map.get("id") != null){
            request.put("id",map.get("id"));
        }
        if (map.get("name") != null){
            request.put("name","U" + str);
        }
        request.put("description",map.get("description"));
        System.out.println("请求对象:" + request.toString());

        String respone = HttpMethod.httpPut(url,request.toString(),headerName,token,flag);

        JSONObject obj  = JSONObject.parseObject(respone);
        String actualMessage = (String) obj.get("message");

        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }





}
