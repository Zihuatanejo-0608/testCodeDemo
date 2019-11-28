package httpTest.aiApplicationService;

import aiPaasTest.loginService.Configure;
import aiPaasTest.loginService.LoginTest;
import cn.hutool.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import toolsUnit.HuToolHttpUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by andy on 2019/10/15.
 *
 *
 */
public class AiTaskTest {
    static Map<String,String> map = Configure.txt();
    String token;
    String headerName = map.get("headerName");
    static String urlHead = map.get("urlHead");
    private String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    @BeforeClass(alwaysRun = true)
    public void tokenOut() throws Exception{
        token = LoginTest.loginApiTest(map.get("ckm"),map.get("ckmPwd"));
    }

    @Test
    public void create() throws Exception{
        String url = urlHead + "ai/task/create";
        JSONObject request = new JSONObject();
        request.put("name","ai" + str);
        request.put("type","idCard_OCR");// idCard_OCR 、common_OCR，bankCard_OCR**
        request.put("resourceId",99);
        request.put("instance",0);
        HuToolHttpUtil.post(url,headerName,token,request);
    }

    @Test
    //换部门
    public void update() throws Exception{
        String url = urlHead + "ai/task/update";
        JSONObject request = new JSONObject();
        request.put("id",71);
        request.put("resourceId",null);//没变传0
        request.put("instance",2);
        HuToolHttpUtil.post(url,headerName,token,request);
    }

    @Test
    public void stop() throws Exception{
        String id = "72";
        String url = urlHead + "ai/task/stop?id=" + id;
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    public void start() throws Exception{
        String id = "61";
        String url = urlHead + "ai/task/start?id=" + id;
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    public void delete() throws Exception{
        String id = "23";
        String url = urlHead + "ai/task/delete?id=" + id;
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    //获取服务token和url
    public void getTokenAndUrl() throws Exception{
        String id = "13";
        String url = urlHead + "ai/task/getTokenAndUrl?id=" + id;
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    //获取所有AI模型与服务关联的模型服务的token和url
    public void getAiTaskInfoList() throws Exception{
        String url = urlHead + "ai/task/getAiTaskInfoList";
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    //分页查询
    public void findPage() throws Exception{
        String current = "1";
        String size = "100";
        String url = urlHead + "ai/task/findPage?current=" + current + "&size=" + size;
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    //根据用户查询部门资源列表
    public void getResourceList() throws Exception{
        String url = urlHead + "serving/task/getResourceList";
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    //专用于更新时，查询部门资源列表
    public void findResourceByUpdate() throws Exception{
        String id = "61";
        String url = urlHead + "ai/task/findResourceByUpdate?id=" + id;
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    //根据type查询镜像信息是否支持Cpu/Gpu
    public void getImagesInfo() throws Exception{
        String type = "idCard_OCR";//idCard_OCR  、bankCard_OCR、 idCard_OCR
        String url = urlHead + "ai/model/getImagesInfo?type=" + type;
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    //查询部署实例信息
    public void findReplicas() throws Exception{
        String id = "21";
        String url = urlHead + "ai/findReplicas?id=" + id;
        HuToolHttpUtil.get(url,headerName,token);
    }

    @Test
    //资源监控
    public void resourceMonitor() throws Exception{
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis / 1000);
        Long start = (timeMillis / 1000) - 5000;
        Long end = (timeMillis / 1000) + 5000;
        System.out.println("开始时间:" + start);
        System.out.println("结束时间:" + end);
        String url = urlHead + "ai/resource/monitor";
        JSONObject request = new JSONObject();
        request.put("taskId",21);
        request.put("startTime",start);
        request.put("endTime",end);
        request.put("podName","");
        request.put("step",20);
        HuToolHttpUtil.post(url,headerName,token,request);
    }

    @Test
    //日志查询
    public void log() throws Exception{
        long timeMillis = System.currentTimeMillis();
        String url = urlHead + "ai/task/log";
        JSONObject request = new JSONObject();
        request.put("taskId",21);
        request.put("date",timeMillis);
        request.put("podName","chukaimin-it-idcard-ck6tfpz3-ai-6c4b7c59b5-kv9gf");
        request.put("nowPage",1);
        request.put("size",100);
        HuToolHttpUtil.post(url,headerName,token,request);
    }

    @Test
    public void requestMonitor() throws Exception{
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis / 1000);
        Long start = (timeMillis / 1000) - 5000;
        Long end = (timeMillis / 1000) + 5000;
        System.out.println("开始时间:" + start);
        System.out.println("结束时间:" + end);
        String url = urlHead + "ai/request/monitor";
        JSONObject request = new JSONObject();
        request.put("taskId",21);
        request.put("startTime",start);
        request.put("endTime",end);
        request.put("step",100);
        HuToolHttpUtil.post(url,headerName,token,request);
    }


}
