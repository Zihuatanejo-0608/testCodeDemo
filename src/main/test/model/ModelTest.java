package model;

import cn.hutool.json.JSONObject;
import data.InitLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.CSVData;
import tools.HuToolHttpUtil;
import tools.ToolsUnit;
import data.InitServingModelData;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by andy on 2019/7/5.
 *
 * 模型管理
 *
 */
public class ModelTest {
    private String token;
    private String headerName = "Authorization";
    private String mName = "m" + ToolsUnit.randomData();
    private static String urlHead = "http://101.qq.118.202:9999/";
    private Map<String, String> allServingDataA;
    private Map<String, String> allServingDataB;
    private Map<String, String> allServingDataC;

    @BeforeClass(alwaysRun = true)
    public void tokenOut() throws Exception{
        token = InitLogin.loginApi("it@qq.com.cn","qq@12345");
        allServingDataA = InitServingModelData.allServingData(6, headerName, token);//>=1只创建模型,>=2创建版本,>=3加载文件,>=4创建模型任务,>=5停止任务,>=6删除任务
        allServingDataB = InitServingModelData.allServingData(6, headerName, token);
        allServingDataC = InitServingModelData.allServingData(4, headerName, token);
    }

    @DataProvider(name = "addModelTest")
    public static Iterator<Object[]> testData() throws Exception{
        return (Iterator<Object[]>) new CSVData("addModelTest.csv");
    }

    @Test(dataProvider = "addModelTest")
    public void addModelTest(Map<String,String> map) throws Exception{
        String url = urlHead + "servingModel/addModel";

        JSONObject request = new JSONObject();
        //模型名字,类型,描述
        if (map.get("name").equals("str")){
            request.put("name",mName);
        }
        request.put("type",map.get("type"));//dict_image_info表image_name字段,test_name
        request.put("description",map.get("description"));
        JSONObject response = HuToolHttpUtil.post(url,headerName,token,request);
        String actualMessage = (String) response.get("message");
        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }

    @DataProvider(name = "updateModelTest")
    public static Iterator<Object[]> testDataB() throws Exception{
        return (Iterator<Object[]>) new CSVData("updateModelTest.csv");
    }
    @Test(dataProvider = "updateModelTest")
    public void updateModelTest(Map<String,String> map) throws Exception{
        String url = urlHead + "servingModel/updateModel";

        JSONObject request = new JSONObject();
        if (map.get("id") != "str"){
            request.put("id",map.get("id"));
        }
        if (map.get("id").equals("stop")){
            request.put("id",allServingDataA.get("mid"));
        }
        if (map.get("name").equals("str")){
            request.put("name","u" + ToolsUnit.randomData());
        }
        request.put("description",map.get("description"));

        JSONObject response = HuToolHttpUtil.put(url,headerName,token,request);
        String actualMessage = (String) response.get("message");
        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }


    @DataProvider(name = "deleteModelTest")
    public static Iterator<Object[]> testDataC() throws Exception{
        return (Iterator<Object[]>) new CSVData("deleteModelTest.csv");
    }
    @Test(dataProvider = "deleteModelTest")
    public void deleteModelTest(Map<String,String> map) throws Exception{
        String modelId = map.get("mid");
        if (map.get("mid").equals("running")){
            modelId = allServingDataC.get("mid");
        }
        if (map.get("mid").equals("stop")){
            modelId = allServingDataB.get("mid");
        }
        String url = urlHead + "servingModel/deleteModel?id=" + modelId;
        JSONObject response = HuToolHttpUtil.delete(url, headerName, token);
        String actualMessage = (String) response.get("message");
        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }








}
