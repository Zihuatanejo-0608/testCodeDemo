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
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/8/13.
 *
 * 批量删除NB task
 */
public class BPNoteBook {
    static Map<String,String> map = Configure.txt();
    String token;
    static String headerName = map.get("headerName");
    static String urlHead = map.get("urlHead");

    @BeforeClass(alwaysRun = true)
    public void tokenOut() throws Exception{
        token = LoginTest.loginApiTest(map.get("ckm"),map.get("ckmPwd"));
    }

    @Test
    //批量处理nb的数据
    public void batchProcessNoteBookData() throws Exception{
        //查询全部noteBook任务数据,返回list
        List<String> taskIds = BPNoteBook.findNoteBookPage(token);
        //遍历解析list
        for (String taskId : taskIds){
            System.out.println(taskId);
            //解析数据,赋值
            //调停止接口并且判断返回是否为true,为true则执行删除任务
            BPNoteBook.deleteDeployment(taskId,token);
        }
    }

    //notebook分页查询
    public static List<String> findNoteBookPage(String token) throws Exception{
        String url = urlHead + "training/notebook/page";

        JSONObject request = new JSONObject();
        request.put("current",1);
        request.put("size",100);

        JSONObject respone = HuToolHttpUtil.post(url, headerName, token, request);
        JSONObject data = respone.getJSONObject("data");
        List<String> taskIds = data.getJSONArray("records").stream().map(JSONObject::new).map(o -> o.getStr("taskId")).collect(Collectors.toList());
        return taskIds;
    }

    //NoteBook 停止任务
    public static boolean stopDeployment(String taskId,String token) throws Exception{
        String url = urlHead + "training/notebook/stop?taskId=" + taskId;
        JSONObject respone = HuToolHttpUtil.get(url, headerName, token);
        boolean success = respone.getBool("success");
        if (success == true){
            return true;
        }
        return false;
    }

    //NoteBook 删除任务
    public static void deleteDeployment(String taskId,String token) throws Exception{
        String url = urlHead + "training/notebook/delete?taskId=" + taskId;
        HuToolHttpUtil.get(url,headerName,token);
    }

}
