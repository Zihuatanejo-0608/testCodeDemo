package data;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import tools.HuToolHttpUtil;
import tools.ToolsUnit;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/7/4.
 *
 * 这个类将模型服务功能拆解,组件化
 * createServingData创建一个模型服务,输出到txt
 * deleteServingData删除一个模型服务,输出到txt
 *
 *
 */
public class InitServingModelData {
    private static String urlHead = "http://101.qq.118.202:9999/";
    //1.创建一个模型
    public static String addModelData(String mName,String headerName,String token) throws Exception{
        String url = urlHead + "servingModel/addModel";
        JSONObject request = new JSONObject();
        //模型名字,类型,描述
        request.put("name", mName);//长度不超过10,小写开头,必须含有小写字母，下划线，数字,不含大写字母
        request.put("type","pmml");//dict_image_info表image_name字段
        request.put("description", "dataInit");
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        String data = (String)respone.get("data");
        return data;
    }

    //2.根据模型id创建一个版本
    public static String addVersionsData(String vName,String modelId,String headerName,String token) throws Exception{
        String url = urlHead + "servingModel/addVersions";
        //String modelId = .addModelData(headerName,token);
        JSONObject request = new JSONObject();
        //版本名称,模型ID,描述
        request.put("name",vName);
        request.put("modelId",modelId);
        request.put("description","ckmTest");
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        String data = (String)respone.get("data");
        return data;
    }

    //3.根据模型id查询出版本路径
    public static String findVersionsData(String modelId,String headerName,String token) throws Exception{
        String url = urlHead + "servingModel/findVersions?modelId=" + modelId;
        JSONObject respone = HuToolHttpUtil.get(url, headerName, token);
        JSONArray list = respone.getJSONArray("data");
        List<Object> fileObject = list.stream().filter(l -> new JSONObject(l.toString()).get("fileObject") != null).collect(Collectors.toList());
        if (fileObject.isEmpty()){
            return null;
        }
        Object data = fileObject.get(0);
        System.out.println(data.toString());
        String string = new JSONObject(data.toString()).getJSONObject("fileObject").getStr("id");
        System.out.println(string);

        return string;
    }

    //4.上传文件
    public static boolean uploaderVersionData(String path,String headerName,String token) throws Exception{
        String url = urlHead + "servingModel/uploaderVersion";
        String file = ToolsUnit.filePath("adult.pmml");
        HttpResponse httpResponse = HttpUtil.createPost(url).header(headerName,token).form("file", FileUtil.file(file)).form("path", path).execute();
        String body = httpResponse.body();
        System.out.println(body);
        return new cn.hutool.json.JSONObject(body).getInt("code") == 200;
    }

    //5.创建一个模型服务
    public static String createData(String versionId,String headerName,String token) throws Exception{
        String url = urlHead + "serving/create";
        JSONObject request = new JSONObject();
        request.put("resourceId",99);
        request.put("versionId",Long.valueOf(versionId));
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        String data = (String)respone.get("data");
        return data;
    }

    //6.停止模型服务
    public static boolean stopData(String taskId,String headerName,String token) throws Exception{
        String url = urlHead + "serving/stop?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.get(url, headerName, token);
        boolean data = respone.getBool("success");
        return data;
    }

    //7.删除模型服务
    public static boolean deleteData(String taskId,String headerName,String token) throws Exception{
        String url = urlHead + "serving/delete?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.delete(url, headerName, token);
        boolean data = respone.getBool("success");
        return data;
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

    //按照数字,走流程,>=1只创建模型,>=2创建版本,>=3加载文件,>=4创建模型任务,>=5停止任务,>=6删除任务
    public static Map<String,String> allServingData(int i,String headerName, String token) throws Exception{
        String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mName = "m" + ToolsUnit.randomData();
        String vName = "v" + ToolsUnit.randomData();
        String modelId = null;
        String versionId = null;
        String path = null;
        String taskId = null;
        boolean upload = false;
        boolean stop = false;

        if (i >= 1){
            modelId = InitServingModelData.addModelData(mName,headerName,token);
        }
        if (modelId != null && i >= 2){
            versionId = InitServingModelData.addVersionsData(vName,modelId,headerName,token);
        }
        if (versionId != null && i >= 3){
            path = InitServingModelData.findVersionsData(modelId,headerName,token);
        }
        if (path != null && i >= 3){
            upload = InitServingModelData.uploaderVersionData(path,headerName,token);
        }
        if (upload && i >= 4){
            taskId = InitServingModelData.createData(versionId, headerName, token);
            Thread.sleep(15000);
        }
        if (taskId != null && i >= 5){
            Thread.sleep(10000);
            stop = InitServingModelData.stopData(taskId, headerName, token);
        }
        if (stop && i >= 6){
            Thread.sleep(10000);
            InitServingModelData.deleteData(taskId,headerName,token);
        }
        String taskStatus = findTaskStatus(taskId, "modelserving");
        HashMap<String, String> map = new LinkedHashMap<>(16);
        map.put("===============", "===============");
        map.put("No",nowTime);
        map.put("mid", modelId);
        map.put("mName", mName);
        map.put("vid", versionId);
        map.put("vName", vName);
        map.put("path", path);
        map.put("taskId", taskId);
        map.put("status", taskStatus);
        map.put("===============", "===============");
        List<String> list = map.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList());
        //写入txt
        String file = "allServingData.txt";
        ToolsUnit.writeTxt(file,list);
        System.out.println("======================="+"一条龙服务结束"+"=======================");
        return map;
    }




}
