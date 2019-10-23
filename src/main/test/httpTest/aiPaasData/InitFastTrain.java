package aiPaasTest.aiPaasData;

import aiPaasTest.loginService.Configure;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import toolsUnit.HuToolHttpUtil;
import toolsUnit.Tools;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/7/17.
 *
 * fastTrain 创建流程
 *
 */
public class InitFastTrain {
    static Map<String,String> map = Configure.txt();
    static String headerName = map.get("headerName");
    static String urlHead = map.get("urlHead");

    //场景建模创建任务,train_fast_list
    public static String createTrainFastModeling(String type,String token) throws Exception{
        String url = urlHead + "training/fasttrain/create";
        JSONObject request = new JSONObject();
        request.put("taskName","f" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        request.put("resourceId",99);
        request.put("storageId",99);
        //模板,template,dict_fasttrain_model_info
        //自定义,user-defined,dict_image_info
        request.put("type",type);
        switch (type){
            case "template":
                request.put("typeInfoId",1);
                break;
            case "user-defined":
                request.put("typeInfoId",2);
                break;
        }
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        String data = (String)respone.get("data");
        return data;
    }

    //读取文件路径
    public static String listFilesData(String taskId,String token) throws Exception{
        System.out.println("**************************");
        String url = urlHead + "training/data/listFiles?taskId=" + taskId;
        JSONObject response = HuToolHttpUtil.get(url, headerName, token);
        List dataList = (List) response.get("data");
        JSONObject data = (JSONObject) dataList.get(0);
        String path = data.get("id").toString();
        System.out.println("文件路径:" + path);
        System.out.println("**************************");
        return path;
    }

    //上传文件
    public static boolean upload(String taskId,String path,String type,String token) throws Exception{
        String url = urlHead + "training/data/upload";
        String file = null;
        switch (type){
            case "template":
                file = Tools.filePath("mnist.tar.gz");
                break;
            case "user-defined":
                file = Tools.filePath("mnist.zip");
                break;
        }
        HttpResponse respone = HttpUtil.createPost(url).header(headerName, token).form("file", FileUtil.file(file)).form("path", path).form("taskId", taskId).execute();
        System.out.println(respone.body());
        if (respone.getStatus() == 200){
            return true;
        }
        return false;
    }

    //快捷建模启动任务,fasttrain_record（启动成功后 更新state=Running）
    public static boolean startTrainFast(String taskId,String token) throws Exception{
        String url = urlHead + "training/fasttrain/start?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.get(url, headerName, token);
        return respone.getBool("success");

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

    public static List<String> listFiles(String taskId,String token) throws Exception{
        String url = urlHead + "training/data/listFiles?taskId=" + taskId;
        JSONObject response = HuToolHttpUtil.get(url, headerName, token);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONArray jsonArray = jsonObject.getJSONArray("data")
                .getJSONObject(0).getJSONArray("children")
                .getJSONObject(0).getJSONArray("children")
                .getJSONObject(0).getJSONArray("children");
        List<String> idList = jsonArray.stream().map(JSONObject::new).map(o -> o.getStr("id")).collect(Collectors.toList());
        System.out.println(idList);
        return idList;
    }

    //添加任务启动命令配置
    public static String addTaskStartCmdConfList(String type,String taskId,List<String> idList,String token) throws Exception{
        String url = urlHead + "training/fasttrain/addTaskStartCmdConfList";
        JSONObject request = new JSONObject();

        List list = new ArrayList<>();
        HashMap<String, String> mapA = new HashMap<>();
        mapA.put("parameter","model");
        mapA.put("parameterValue",idList.get(0));//选择数据路径
        HashMap<String, String> mapB = new HashMap<>();
        mapB.put("parameter","output");
        mapB.put("parameterValue",idList.get(1));//模型输出路径
        HashMap<String, String> mapC = new HashMap<>();
        mapC.put("parameter","height");
        mapC.put("parameterValue","180");
        list.add(mapA);
        list.add(mapB);
        list.add(mapC);

        switch (type){
            case "user-defined":
                request.put("cmd","python");
                request.put("cmdArg",idList.get(2));
                request.put("fasttrainTaskId",taskId);
                request.put("parameters",list);
                break;
            case "template":
                request.put("cmd",null);
                request.put("cmdArg",null);
                request.put("fasttrainTaskId",taskId);
                request.put("parameters",null);
                break;
            default:
                System.out.println("没有这个类型");
                break;
        }
        JSONObject response = HuToolHttpUtil.post(url, headerName, token, request);
        return response.getStr("data");
    }

    //给用户 FastTrain 任务配置启动时的命令
    public static boolean configureStartupCmd(String taskId,String taskListId,String token) throws Exception{
        String url = urlHead + "training/fasttrain/configureStartupCmd?taskId=" + taskId +"&&confId=" + taskListId;
        JSONObject response = HuToolHttpUtil.get(url,headerName,token);
        if (response.getBool("success")){
            return true;
        }
        return false;
    }

    //fastTrain创建=1,查询路径=2,上传文件=2,启动=3
    public static Map<String,String> allFastTrainData(int i,String type,String token) throws Exception{
        String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String taskId = null;
        String path = null;
        String taskListId = null;
        List<String> idList = null;
        boolean uploadSuccess = false;
        boolean startSuccess = false;
        boolean cmdSuccess = false;

        if (i >= 1){
            taskId = InitFastTrain.createTrainFastModeling(type,token);
            Thread.sleep(20000);
        }
        if (i >= 2 && taskId != null){
            path = InitFastTrain.listFilesData(taskId, token);
        }
        if (i >= 2 && taskId != null){
            uploadSuccess = InitFastTrain.upload(taskId, path, type,token);
        }
        if (i >= 2 && uploadSuccess){
            idList = InitFastTrain.listFiles(taskId, token);
        }
        if (i >= 2 && idList != null){
            taskListId = InitFastTrain.addTaskStartCmdConfList(type, taskId, idList, token);//添加任务启动命令配置
        }
        if (i >= 2 && taskListId !=null){
            cmdSuccess = InitFastTrain.configureStartupCmd(taskId, taskListId, token);//给用户 FastTrain 任务配置启动时的命令
        }
        if (i >= 3 && cmdSuccess){
            startSuccess = InitFastTrain.startTrainFast(taskId, token);
        }
        //String taskStatus = InitFastTrain.findTaskStatus(taskId, "fasttraining");
        HashMap<String, String> map = new LinkedHashMap<>(16);
        map.put("===============","===============");
        map.put("No",nowTime);
        map.put("taskId",taskId);
        //map.put("taskStatus",taskStatus);
        map.put("===============","===============");
        List<String> list = map.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList());
        String file = "allFastTrainData.txt";
        Tools.writeTxt(file,list);
        System.out.println("======================="+"noteBook测试数据创建完毕"+"=======================");

        return map;
    }



}
