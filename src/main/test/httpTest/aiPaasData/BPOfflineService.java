package aiPaasTest.aiPaasData;

import aiPaasTest.loginService.Configure;
import aiPaasTest.loginService.LoginTest;
import cn.hutool.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import toolsUnit.HuToolHttpUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/9/12.
 *
 * 批量删除离线任务
 */
public class BPOfflineService {
    static Map<String,String> map = Configure.txt();
    String token;
    static String headerName = map.get("headerName");
    static String urlHead = map.get("urlHead");

    @BeforeClass(alwaysRun = true)
    public void tokenOut() throws Exception{
        token = LoginTest.loginApiTest(map.get("ckm"),map.get("ckmPwd"));
    }

    @Test
    public void batchProcessOfflineServiceData() throws Exception{
        List<String> ids = BPOfflineService.getPage(token);
        for (String id : ids){
            BPOfflineService.delete(id,token);
        }
    }

    public static List<String> getPage(String token) throws Exception{
        String url = urlHead + "serving/offline/service/getPage";
        JSONObject request = new JSONObject();
        request.put("current",1);
        request.put("size",100);
        JSONObject respones = HuToolHttpUtil.post(url, headerName, token, request);
        JSONObject data = respones.getJSONObject("data");
        List<String> taskIds = data.getJSONArray("records").stream().map(JSONObject::new).map(o -> o.getStr("id")).collect(Collectors.toList());
        System.out.println(taskIds);
        return taskIds;
    }

    public static void delete(String id,String token) throws Exception{
        System.out.println("id:" + id);
        String url = urlHead + "serving/offline/service/delete?id=" + id;
        HuToolHttpUtil.delete(url,headerName,token);
    }




}
