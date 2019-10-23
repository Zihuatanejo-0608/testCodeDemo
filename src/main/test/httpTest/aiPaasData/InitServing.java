package aiPaasTest.aiPaasData;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.testng.annotations.Test;
import toolsUnit.HuToolHttpUtil;
import toolsUnit.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/7/2.
 */
public class InitServing {
    private static String urlHead = "http://101.91.118.202:9999/";
    //1.创建一个模型
    public static String addModelData(String mName,String type,String headerName,String token) throws Exception{
        String url = urlHead + "serving/model/add";
        JSONObject request = new JSONObject();
        //模型名字,类型,描述
        request.put("name", mName);//长度不超过10,小写开头,必须含有小写字母，下划线，数字,不含大写字母
        request.put("type",type);//dict_image_info表image_name字段
        request.put("description", "dataInit");
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        String data = (String)respone.get("data");
        return data;
    }

    //2.根据模型id创建一个版本
    public static String addVersionsData(String vName,String modelId,String headerName,String token) throws Exception{
        String url = urlHead + "serving/version/add";
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
        String url = urlHead + "serving/version/find?modelId=" + modelId;
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

    //4.上传文件,5M+
    public static boolean uploaderVersionData(String fileName,String path,String headerName,String token) throws Exception{
        String url = urlHead + "serving/version/upload";
        String file = Tools.filePath(fileName);
        HttpResponse httpResponse = HttpUtil.createPost(url).header(headerName,token).form("file", FileUtil.file(file)).form("path", path).execute();
        String body = httpResponse.body();
        System.out.println(body);
        return new cn.hutool.json.JSONObject(body).getInt("code") == 200;
    }

    //5.创建一个模型服务
    public static String create(String versionId,String headerName,String token) throws Exception{
        String url = urlHead + "serving/task/create";
//        JSONObject request = new JSONObject();
//        request.put("resourceId",99);
//        request.put("versionId",Long.valueOf(versionId));

        //serving_online_image_info表id字段,1=op,2=ONIX,3=pmml
        int imageId = 3;

        JSONObject request = new JSONObject();
        request.put("resourceId",99);//serving_online_resource_info
        request.put("versionId",Long.valueOf(versionId));//modelData.get("vid")
        request.put("imageId",imageId);
        request.put("instance",1);//实例数,K8S上检查N个pod
        request.put("name","s" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        request.put("description","ckmTest");

        JSONObject ONIX = new JSONObject();//imageId=2
        ONIX.put("saved_type","checkpoint");
        ONIX.put("input_tensor_name","input:0");
        ONIX.put("output_tensor_name","result:0");

        JSONObject op = new JSONObject();//imageId=1
        op.put("handle_file","minist_inference.py");
        op.put("handle_class","modelService");

        switch (imageId){
            case (1):
                request.put("params",op.toString());//镜像参数,如果imageId的type=tensorflow,则根据process_type传值
                break;
            case (2):
                request.put("params",ONIX.toString());
                break;
            default:
                request.put("params",null);//如果imageId的type=pmml,那么不填
                break;
        }

        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        String data = String.valueOf(respone.get("data"));
        JSONObject obj = (JSONObject) respone.get("data");
        return  String.valueOf(obj.get("detailId"));
    }

    @Test
    //一条龙服务
    public static Map<String,String> createModelData(int i,String headerName,String token,String fileName,String type) throws Exception{
        String modelId = null;
        String versionId = null;
        String path = null;
        boolean upload = false;
        String detailId = null;
        String random = Tools.randomData();
        String mName = "m" + random;
        String vName = "v" + random;

        if (i >= 1){
            modelId = InitServing.addModelData(mName,type,headerName,token);
        }
        if (i >= 2 && modelId != null){
            versionId = InitServing.addVersionsData(vName,modelId,headerName,token);
        }
        if (i >= 3 && versionId != null){
            path = InitServing.findVersionsData(modelId,headerName,token);
        }
        if (i >= 3 && path != null){
            upload = InitServing.uploaderVersionData(fileName,path,headerName,token);
        }
        if (i >= 4 && upload){
            detailId = InitServing.create(versionId, headerName, token);
        }
        HashMap<String, String> map = new HashMap<>(16);
        map.put("mid", modelId);
        map.put("mName", mName);
        map.put("vid", versionId);
        map.put("vName", vName);
        map.put("detailId", detailId);
        map.put("filePath",path);
        List<String> list = map.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList());
        //写入txt
        String file = "testData.txt";
        Tools.writeTxt(file,list);
        System.out.println("======================="+"一条龙服务结束"+"=======================");
        return map;

    }




}
