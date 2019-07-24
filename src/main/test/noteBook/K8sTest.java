package noteBook;

import cn.hutool.json.JSONObject;
import data.InitNoteBookData;
import data.InitLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.CSVData;
import tools.HuToolHttpUtil;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by andy on 2019/6/12.
 */
public class K8sTest {
    private String token;
    private String headerName = "Authorization";
    private Map<String,String> allNoteBookDataA;
    private Map<String,String> allNoteBookDataB;
    private static String urlHead = "http://101.qq.118.202:9999/";

    @BeforeClass(alwaysRun = true)
    public void tokenOut() throws Exception{
        token = InitLogin.loginApi("it@qq.com.cn","qq@12345");
        allNoteBookDataA = InitNoteBookData.allNoteBookData(1, headerName, token);//1创建,2停止,3删除
        allNoteBookDataB = InitNoteBookData.allNoteBookData(2, headerName, token);
    }

    @DataProvider(name = "createNoteBookModelingTest")
    public static Iterator<Object[]> testData() throws Exception{
        return (Iterator<Object[]>) new CSVData("createNoteBookModelingTest.csv");
    }
    @Test(dataProvider = "createNoteBookModelingTest")
    public void createNoteBookModelingTest(Map<String,String> map) throws Exception{
        String url = urlHead + "k8s/createNodeBookModeling";

        JSONObject request = new JSONObject();
        if (map.get("taskName").equals("str")){
            request.put("taskName",map.get("taskName"));
        }
        request.put("resourceId",map.get("resourceId"));//处理器资源,int或string都可以
        request.put("storageId",map.get("storageId"));//磁盘资源
        request.put("imageName",map.get("imageName"));
        JSONObject response = HuToolHttpUtil.post(url, headerName, token, request);
        String actualMessage = (String) response.get("message");
        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }

    @DataProvider(name = "startDeploymentTest")
    public static Iterator<Object[]> testDataB() throws Exception{
        return (Iterator<Object[]>) new CSVData("startDeploymentTest.csv");
    }
    @Test(groups = "noteBookModel",dataProvider = "startDeploymentTest")
    public void startDeploymentTest(Map<String,String> map)throws Exception{
        String taskId;
        if (map.get("taskId").equals("id")){
            taskId = allNoteBookDataA.get("taskId");
        }else {
            taskId = map.get("taskId");
        }
        String url = urlHead + "k8s/startDeployment?taskId=" + taskId;

        JSONObject response = HuToolHttpUtil.post(url, headerName, token, null);
        String actualMessage = (String) response.get("message");
        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }

    @DataProvider(name = "updateDeploymentTest")
    public static Iterator<Object[]> testDataC() throws Exception{
        return (Iterator<Object[]>) new CSVData("updateDeploymentTest.csv");
    }
    @Test(dataProvider = "updateDeploymentTest")
    public void updateDeploymentTest(Map<String,String> map) throws Exception{
        String url = urlHead + "k8s/updateDeployment";
        JSONObject request = new JSONObject();
        request.put("taskId",map.get("taskId"));
        if (map.get("taskId").equals("normal")){
            request.put("taskId",allNoteBookDataA.get("taskId"));
        }
        if (map.get("resourceId").equals("null")){
            request.put("resourceId",null);
        }else {
            request.put("resourceId",Integer.valueOf(map.get("resourceId")));//资源id
        }
        JSONObject response = HuToolHttpUtil.post(url, headerName, token, request);
        String actualMessage = (String) response.get("message");
        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }

    @DataProvider(name = "stopDeploymentTest")
    public static Iterator<Object[]> testDataD() throws Exception{
        return (Iterator<Object[]>) new CSVData("stopDeploymentTest.csv");
    }
    @Test(dataProvider = "stopDeploymentTest")
    public void stopDeploymentTest(Map<String,String> map) throws Exception{
        String taskId;
        taskId = map.get("taskId");
        if (map.get("taskId").equals("running")){
            taskId = allNoteBookDataA.get("taskId");
        }
        String url = urlHead + "k8s/stopDeployment?taskId=" + taskId;
        JSONObject response = HuToolHttpUtil.post(url, headerName, token, null);
        String actualMessage = (String) response.get("message");
        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }

    @DataProvider(name = "deleteDeploymentTest")
    public static Iterator<Object[]> testDataE() throws Exception{
        return (Iterator<Object[]>) new CSVData("deleteDeploymentTest.csv");
    }
    @Test(dataProvider = "deleteDeploymentTest")
    public void deleteDeploymentTest(Map<String,String> map) throws Exception{
        String taskId;
        switch (map.get("taskId")){
            case "running":
                taskId = allNoteBookDataA.get("taskId");
                break;
            case "stop":
                taskId = allNoteBookDataB.get("taskId");
                break;
            default:
                taskId = map.get("taskId");
                break;
        }
        String url = urlHead + "k8s/deleteDeployment?taskId=" + taskId;
        JSONObject response = HuToolHttpUtil.post(url, headerName, token, null);
        String actualMessage = (String) response.get("message");
        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }

}


