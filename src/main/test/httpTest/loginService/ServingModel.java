package httpTest.loginService;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import toolsUnit.HttpMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/6/13.
 *
 * 模型服务
 */
public class ServingModel {

    private String token;
    private String headerName = "Authorization";
    private String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    @BeforeClass(alwaysRun = true)
    public String tokenOut() throws Exception{
        token = LoginTest.login("Zihuatanejo@qq.com.cn","Zihuatanejo@12345");
        return token;
    }

    //模型版本提供路径数据
    public static String findVersionsData(String modelId,String headerName,String token) throws Exception{
        boolean flag = false;

        long mid = 1906141024499460000L;
        modelId = String.valueOf(mid);
        String url = "http://172.17.xx.xx:30000/servingModel/findVersions?modelId=" + modelId;
        String respone = HttpMethod.httpGet(url,"",headerName,token,flag);
        JSONObject obj = JSONObject.parseObject(respone);
        JSONArray list = obj.getJSONArray("data");
        List<Object> fileObject = list.stream().filter(l -> JSONObject.parseObject(l.toString()).get("fileObject") != null).collect(Collectors.toList());
        if (fileObject.isEmpty()){
            return null;
        }
        Object data = fileObject.get(0);
        System.out.println(data.toString());
        String string = JSONObject.parseObject(data.toString()).getJSONObject("fileObject").getString("id");
        System.out.println(string);

        return string;
    }

    @Test
    //模型上传文件,serving_model_versions
    public void uploaderVersion() throws Exception{
        String url = "http://172.17.xx.xx:30000/servingModel/uploaderVersion";
        String file = "D:\\work\\2019\\Code\\bigdataTest\\src\\main\\resources\\csvData.csv";
        String path = ServingModel.findVersionsData("1906141024499460000",headerName,token);
        HttpResponse httpResponse = HttpUtil.createPost(url).header(headerName,token).form("file", FileUtil.file(file)).form("path", path).execute();
        String body = httpResponse.body();
        System.out.println(body);
    }


}
