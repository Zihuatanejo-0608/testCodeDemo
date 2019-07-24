package data;

import cn.hutool.json.JSONObject;
import tools.HuToolHttpUtil;
import tools.ToolsUnit;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/7/15.
 */
public class InitNoteBookData {
    private static String urlHead = "http://101.qq.118.202:9999/";

    //创建任务,返回任务id
    public static String createNoteBookModelingData(String headerName,String token) throws Exception{
        String url = urlHead + "k8s/createNodeBookModeling";
        JSONObject request = new JSONObject();
        request.put("taskName","n" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        request.put("resourceId",99);//处理器资源
        request.put("storageId",99);//磁盘资源
        request.put("imageName","172.16.1.222:10004/library/anaconda3:tf-cpu_v1");
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        String taskId = (String) respone.get("data");
        System.out.println("任务id:" + taskId);
        return taskId;
    }

    //停止任务,返回布尔值
    public static boolean stopDeploymentData(String taskId,String headerName,String token) throws Exception{
        String url = urlHead + "k8s/stopDeployment?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, null);
        boolean data = respone.getBool("success");
        return data;
    }

    //删除任务,返回布尔值
    public static boolean deleteDeployment(String taskId,String headerName,String token) throws Exception{
        String url = urlHead + "k8s/deleteDeployment?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, null);
        boolean data = respone.getBool("success");
        return data;
    }

    //noteBook创建=1,停止=2,删除=3
    public static Map<String,String> allNoteBookData(int i,String headerName,String token) throws Exception{
        String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String taskId = null;
        boolean stop = false;
        if (i >= 1){
            taskId = InitNoteBookData.createNoteBookModelingData(headerName, token);
            Thread.sleep(10000);
        }
        if (i >= 2){
            stop = InitNoteBookData.stopDeploymentData(taskId, headerName, token);
            Thread.sleep(10000);
        }
        if (i >= 3 && stop){
            InitNoteBookData.deleteDeployment(taskId,headerName,token);
            Thread.sleep(10000);
        }
        String taskStatus = InitServingModelData.findTaskStatus(taskId, "notebook");
        HashMap<String, String> map = new LinkedHashMap<>(16);
        map.put("===============","===============");
        map.put("No",nowTime);
        map.put("taskId",taskId);
        map.put("taskStatus",taskStatus);
        map.put("===============","===============");
        List<String> list = map.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList());
        String file = "allNoteBookData.txt";
        ToolsUnit.writeTxt(file,list);
        System.out.println("======================="+"noteBook测试数据创建完毕"+"=======================");

        return map;
    }
}
