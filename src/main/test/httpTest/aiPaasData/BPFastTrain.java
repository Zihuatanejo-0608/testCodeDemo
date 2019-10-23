package aiPaasTest.aiPaasData;

import aiPaasTest.loginService.Configure;
import aiPaasTest.loginService.LoginTest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import toolsUnit.HuToolHttpUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by andy on 2019/8/13.
 *
 * 批量删除FT task
 */
public class BPFastTrain {
    static Map<String,String> map = Configure.txt();
    String token;
    static String headerName = map.get("headerName");
    static String urlHead = map.get("urlHead");

    @BeforeClass(alwaysRun = true)
    public void tokenOut() throws Exception{
        token = LoginTest.loginApiTest(map.get("ckm"),map.get("ckmPwd"));
    }

    @Test
    //批量处理FT数据
    public void batchProcessFastTrainData() throws Exception{
        List taskIds = BPFastTrain.findPage(token);
        for (Object obj : taskIds){
            String taskId = new JSONObject(obj.toString()).getStr("taskId");
            System.out.println(taskId);
            BPFastTrain.deleteTrainFast(taskId,token);
        }
    }

    public static List findPage(String token) throws Exception{
        String url = urlHead + "training/fasttrain/getFastTrainListPage";
        JSONObject request = new JSONObject();
        request.put("current",1);
        request.put("size",100);
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        JSONObject data = respone.getJSONObject("data");
        JSONArray list = data.getJSONArray("records");
        return list;
    }

    //场景建模停止任务,fasttrain_record（成功后 更新state=Stop）
    public static boolean stopTrainFast(String taskId,String token) throws Exception{
        String url = urlHead + "training/fasttrain/stop?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, null);
        boolean success = respone.getBool("success");
        if (success == true){
            return true;
        }
        return false;
    }

    //场景建模删除任务,train_fast_list,dept_resource_quota_used
    public static void deleteTrainFast(String taskId,String token) throws Exception{
        String url = urlHead + "training/fasttrain/delete?taskId=" + taskId;
        HuToolHttpUtil.get(url,headerName,token);
    }



}
