package aiPaasTest.aiPaasData;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import toolsUnit.HuToolHttpUtil;
import toolsUnit.Tools;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/7/18.
 */
public class InitNoteBook {
    private static String urlHead = "http://101.91.118.202:9999/";

    //创建任务,返回任务id
    public static String createNoteBookModelingData(String headerName,String token) throws Exception{
        String url = urlHead + "training/notebook/create";
        JSONObject request = new JSONObject();
        request.put("taskName","n" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        request.put("resourceId",99);//处理器资源
        request.put("storageId",99);//磁盘资源
        request.put("imagePath","172.16.1.222:10004/notebook/deeplearning_jupyter_limit_token:v1.3");
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        String taskId = (String) respone.get("data");
        System.out.println("任务id:" + taskId);
        return taskId;
    }

    //停止任务,返回布尔值
    public static boolean stopDeploymentData(String taskId,String headerName,String token) throws Exception{
        String url = urlHead + "training/notebook/stop?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, null);
        boolean data = respone.getBool("success");
        return data;
    }

    //删除任务,返回布尔值
    public static boolean deleteDeployment(String taskId,String headerName,String token) throws Exception{
        String url = urlHead + "training/notebook/delete?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, null);
        boolean data = respone.getBool("success");
        return data;
    }

    //读取文件路径
    public static String listFilesData(String taskId,String headerName,String token) throws Exception{
        System.out.println("**************************");
        String url = urlHead + "training/data/listFiles?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.get(url, headerName, token);
        List dataList = (List) respone.get("data");
        JSONObject data = (JSONObject) dataList.get(0);
        String path = data.get("id").toString();
        System.out.println("文件路径:" + path);
        System.out.println("**************************");
        return path;
    }

    public static boolean upload(String taskId,String path,String headerName,String token) throws Exception{
        String url = urlHead + "training/data/upload";
        String file = Tools.filePath("testData.txt");
        HttpResponse respone = HttpUtil.createPost(url).header(headerName, token).form("file", FileUtil.file(file)).form("path", path).form("taskId", taskId).execute();
        String body = respone.body();
        System.out.println(body);
        return new cn.hutool.json.JSONObject(body).getInt("code") == 200;
    }

    //8.查询任务状态
    public static String findTaskStatus(String id,String type) throws Exception{
        String url = urlHead + "servingModel/findTaskStatus";

        Map<String,String> map = new HashMap<>();
        map.put("id",id);//taskID
        map.put("type",type);//notebook,fasttraining,modelserving
        HttpResponse response = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(map)).execute();
        if (response.isOk()) {
            System.out.println(response.body());
            return new JSONObject(response.body()).getStr("data");
        }
        return null;
    }

    //noteBook创建=1,停止=2,删除=3
    //training/data/listFiles,training/data/upload,path=id
    public static Map<String,String> allNoteBookData(int i, String headerName, String token) throws Exception{
        String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String taskId = null;
        String path = null;
        boolean stop = false;
        if (i >= 1){
            taskId = InitNoteBook.createNoteBookModelingData(headerName, token);
            Thread.sleep(15000);
        }
        if (taskId != null && i >= 2){
            path = InitNoteBook.listFilesData(taskId, headerName, token);
        }
        if (path != null && i >= 2){
            InitNoteBook.upload(taskId,path,headerName,token);
        }
        if (i >= 3){
            stop = InitNoteBook.stopDeploymentData(taskId, headerName, token);
            Thread.sleep(10000);
        }
        if (i >= 4 && stop){
            InitNoteBook.deleteDeployment(taskId,headerName,token);
            Thread.sleep(10000);
        }
        //String taskStatus = InitNoteBook.findTaskStatus(taskId, "notebook");
        HashMap<String, String> map = new LinkedHashMap<>(16);
        map.put("===============","===============");
        map.put("No",nowTime);
        map.put("taskId",taskId);
        //map.put("taskStatus",taskStatus);
        map.put("===============","===============");
        List<String> list = map.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList());
        String file = "allNoteBookData.txt";
        Tools.writeTxt(file,list);
        System.out.println("======================="+"noteBook测试数据创建完毕"+"=======================");

        return map;
    }
}
