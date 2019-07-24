package data;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import tools.HuToolHttpUtil;
import tools.ToolsUnit;

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

    private static String headerName = "Authorization";
    private static String urlHead = "http://101.qq.118.202:9999/";

    //场景建模创建任务,train_fast_list
    public static String createTrainFastModeling(String token) throws Exception{
        String url = urlHead + "k8s/createTrainFastModeling";
        JSONObject request = new JSONObject();
        request.put("taskName","f" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        request.put("resourceId",99);//处理器资源
        request.put("storageId",99);//磁盘资源
        request.put("type","template");//template=模板,id=dict_fasttrain_model_info表主键&&&&&&user-defined=自定义时,id=dict_image_info表的主键
        request.put("id",1);//dict_fasttrain_model_info表id
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        String data = (String)respone.get("data");
        return data;
    }

    //读取文件路径
    public static String listFilesData(String taskId,String token) throws Exception{
        System.out.println("**************************");
        String url = urlHead + "user/listFiles?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.get(url, headerName, token);
        List dataList = (List) respone.get("data");
        JSONObject data = (JSONObject) dataList.get(0);
        String path = data.get("id").toString();
        System.out.println("文件路径:" + path);
        System.out.println("**************************");
        return path;
    }

    //上传文件
    public static boolean upload(String taskId,String path,String token) throws Exception{
        String url = urlHead + "user/upload";
        String file = ToolsUnit.filePath("testData.txt");
        //String file = ToolsUnit.filePath("mnist.tar.gz");
        HttpResponse respone = HttpUtil.createPost(url).header(headerName, token).form("file", FileUtil.file(file)).form("path", path).form("taskId", taskId).execute();
        System.out.println(respone.body());
        if (respone.getStatus() == 200){
            return true;
        }
        return false;
    }

    //快捷建模启动任务,fasttrain_record（启动成功后 更新state=Running）
    public static boolean startTrainFast(String taskId,String token) throws Exception{
        String url = urlHead + "k8s/startTrainFast?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, null);
        return respone.getBool("success");
    }

    //
    public static boolean stopTrainFast(String taskId,String token) throws Exception{
        String url = urlHead + "k8s/stopTrainFast?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, null);
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

    //fastTrain创建=1,查询路径=2,上传文件=2,启动=3
    public static Map<String,String> allFastTrainData(int i,String token) throws Exception{
        String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String taskId = null;
        String path = null;
        boolean uploadSuccess = false;
        boolean startSuccess = false;
        boolean stopSuccess = false;

        if (i >= 1){
            taskId = InitFastTrain.createTrainFastModeling(token);
            Thread.sleep(10000);
        }
        if (i >= 2 && taskId != null){
            path = InitFastTrain.listFilesData(taskId, token);
        }
        if (i >= 2 && taskId != null){
            uploadSuccess = InitFastTrain.upload(taskId, path, token);
        }
        if (i >= 3 && uploadSuccess){
            startSuccess = InitFastTrain.startTrainFast(taskId, token);
        }
        if (i >= 4 && startSuccess){
            stopSuccess = InitFastTrain.stopTrainFast(taskId, token);
        }
        String taskStatus = InitFastTrain.findTaskStatus(taskId, "fasttraining");
        HashMap<String, String> map = new LinkedHashMap<>(16);
        map.put("===============","===============");
        map.put("No",nowTime);
        map.put("taskId",taskId);
        map.put("taskStatus",taskStatus);
        map.put("===============","===============");
        List<String> list = map.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList());
        String file = "allFastTrainData.txt";
        ToolsUnit.writeTxt(file,list);
        System.out.println("======================="+"noteBook测试数据创建完毕"+"=======================");

        return map;
    }



}
