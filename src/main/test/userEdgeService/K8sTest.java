package userEdgeService;

import com.alibaba.fastjson.JSONObject;
import login.InitLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.CSVData;
import tools.HttpMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by andy on 2019/6/12.
 */
public class K8sTest {
    private String token;
    private String headerName = "Authorization";
    private String taskId;

    @BeforeClass(alwaysRun = true)
    public String tokenOut() throws Exception{
        token = InitLogin.login("Zihuatanejo@xx.com.cn","Zihuatanejo@12345");
        return token;
    }

//    @BeforeMethod(groups = "noteBookModel")
//    public String taskIdOut() throws Exception{
//        taskId = K8sTest.createNoteBookModelingData(headerName,token);
//        return null;
//    }

    @DataProvider(name = "createNoteBookModelingTest")
    public static Iterator<Object[]> testData() throws Exception{
        return (Iterator<Object[]>) new CSVData("createNoteBookModelingTest.csv");
    }

    @DataProvider(name = "startDeploymentTest")
    public static Iterator<Object[]> testDataB() throws Exception{
        return (Iterator<Object[]>) new CSVData("startDeploymentTest.csv");
    }

    @Test(dataProvider = "createNoteBookModelingTest")
    public void createNoteBookModelingTest(Map<String,String> map) throws Exception{
        boolean flag = false;
        String url = "http://172.17.XX.85:30000/k8s/createNodeBookModeling";

        JSONObject request = new JSONObject();
        if (map.get("taskName").equals("str")){
            request.put("taskName",map.get("taskName"));
        }
        request.put("resourceId",map.get("resourceId"));//处理器资源,int或string都可以
        request.put("storageId",map.get("storageId"));//磁盘资源
        request.put("imageName",map.get("imageName"));
        System.out.println("请求对象:" + request.toString());

        String respone = HttpMethod.httpPost(url,request.toString(),headerName,token,flag);

        JSONObject obj  = JSONObject.parseObject(respone);
        String actualMessage = (String) obj.get("message");

        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));
    }

    public static String createNoteBookModelingData(String headerName,String token) throws Exception{
        boolean flag = false;
        String url = "http://172.17.xx.85:30000/k8s/createNodeBookModeling";
        JSONObject request = new JSONObject();
        request.put("taskName","N" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        request.put("resourceId",99);//处理器资源
        request.put("storageId",99);//磁盘资源
        request.put("imageName","172.17.xx.86:8090/library/deeplearning-nd:auto");

        String respone = HttpMethod.httpPost(url,request.toString(),headerName,token,flag);
        JSONObject obj  = JSONObject.parseObject(respone);
        String taskId = (String) obj.get("data");
        System.out.println("任务id:" + taskId);
        Thread.sleep(9000);//等待10S,容器启动
        return taskId;
    }

    @Test(groups = "noteBookModel",dataProvider = "startDeploymentTest")
    public void startDeploymentTest(Map<String,String> map)throws Exception{
        boolean flag = false;

        if (map.get("taskId").equals("id")){
            taskId = K8sTest.createNoteBookModelingData(headerName,token);
        }else {
            taskId = map.get("taskId");
        }
        String url = "http://172.17.xx.85:30000/k8s/startDeployment?taskId="+ taskId;

        String respone = HttpMethod.httpPost(url,"",headerName,token,flag);

        JSONObject obj  = JSONObject.parseObject(respone);
        String actualMessage = (String) obj.get("message");

        System.out.println("预期结果:" + map.get("message") + "\n" + "实际结果:" + actualMessage);
        //断言
        Assert.assertEquals(actualMessage,map.get("message"));

    }








}


