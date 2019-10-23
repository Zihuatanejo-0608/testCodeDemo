package aiPaasTest.aiPaasData;

import aiPaasTest.loginService.Configure;
import aiPaasTest.loginService.LoginTest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import toolsUnit.HuToolHttpUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/6/24.
 *
 * 批量处理任务
 */
public class BatchProcessingServingModel {
    static Map<String,String> map = Configure.txt();
    String token;
    static String headerName = map.get("headerName");
    static String urlHead = map.get("urlHead");

    @BeforeClass(alwaysRun = true)
    public void tokenOut() throws Exception{
        token = LoginTest.loginApiTest(map.get("ckm"),map.get("ckmPwd"));
    }

    @Test
    public static void findStatusTest() throws Exception{
        String id = "chukaimin-itfc5b2611-9048-42-notebook";
        String type = "notebook";
        findTaskStatus(id,type);
    }

    @Test
    //批量删除模型
    public void deleteModel() throws Exception{
        List list = selectPage();
        for (Object id :list){
            String url = urlHead + "serving/model/delete?id=" + id;
            JSONObject respone = HuToolHttpUtil.delete(url, headerName, token);
            boolean success = respone.getBool("success");
            if (success == false){

            }
        }
    }

    @Test
    //批量删除serving数据
    public void batchProcessServingModelData() throws Exception{
        //查询任务列表,解析出ID,传给list
        List<Map<String, String>> maps = BatchProcessingServingModel.SelectTaskPage(token);
        //遍历list,赋值给id
        for (Map<String,String> map : maps){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String detailId = entry.getKey();
                String status = entry.getValue();
                System.out.println("detail_id:" + detailId);

                if (status.equals("Running")){
                    //对于running 的任务,停止后删除
                    if (BatchProcessingServingModel.stop(detailId,token)){
                        BatchProcessingServingModel.delete(detailId,token);
                    }
                }else {
                    BatchProcessingServingModel.delete(detailId,token);
                }
            }
        }
    }

    //新分页查询
    public static List<Map<String,String>> SelectTaskPage(String token) throws Exception{
        String current = "1";
        String size = "100";
        String url = urlHead + "serving/task/selectPage?current=" + current + "&size=" + size;
        JSONObject respone = HuToolHttpUtil.get(url, headerName, token);
        JSONObject data = respone.getJSONObject("data");
        JSONArray list = data.getJSONArray("records");

        List<Map<String,String>> idList = new ArrayList<>();
        list.stream().map(JSONObject::new).map(j -> j.getJSONArray("servingOnlineDetailRecords")).map(d -> d.stream().map(o -> new JSONObject(o)).map(j -> Collections.singletonMap(j.getStr("id"),j.getStr("status"))).collect(Collectors.toList())).forEach(l -> idList.addAll(l));
//        ArrayList<String> statusList = new ArrayList<>();
//        list.stream().map(JSONObject::new).map(j -> j.getJSONArray("servingOnlineDetailRecords")).map(d -> d.stream().map(o -> new JSONObject(o).getStr("status")).collect(Collectors.toList())).forEach(l -> statusList.addAll(l));

        return idList;
    }

    //新启动任务,serving_task_record
    public static boolean start(String id,String token) throws Exception{
        //serving_online_detail_record 表ID
        String url = urlHead + "serving/task/start?id=" + id;
        JSONObject respone = HuToolHttpUtil.get(url, headerName, token);
        boolean success = respone.getBool("success");

        return success;
    }

    //新停止任务,serving_online_detail_record
    public static boolean stop(String detailId,String token) throws Exception{
        String url = urlHead + "serving/task/stop?detailId=" + detailId;
        JSONObject respone = HuToolHttpUtil.get(url, headerName, token);
        boolean success = respone.getBool("success");

        return success;
    }

    //新删除任务,serving_task_record
    public static void delete(String detailId,String token) throws Exception{
        String url = urlHead + "serving/task/delete?detailId=" + detailId;
        HuToolHttpUtil.delete(url,headerName,token);
    }


    //查询任务状态
    public static String findTaskStatus(String id,String type) throws Exception{
        String url = urlHead + "serving/findTaskStatus";

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

    //模型管理分页查询,serving_model_record
    public List selectPage() throws Exception{
        String url = urlHead + "serving/model/selectPage";
        JSONObject request = new JSONObject();
        request.put("page",1);//当前页
        request.put("rows",50);//每页展示数
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        JSONObject data = respone.getJSONObject("data");
        JSONArray list = data.getJSONArray("records");
        //取出list中名字是id的集合,重新设置为list
        List<String> id = list.stream().map(o -> new JSONObject(o).getStr("id")).collect(Collectors.toList());

        return id;
    }


}
