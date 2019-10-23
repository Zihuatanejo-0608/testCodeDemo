package aiPaasTest.aiPaasData;

import aiPaasTest.loginService.Configure;
import aiPaasTest.loginService.LoginTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Created by andy on 2019/8/13.
 *
 * 批量创建task
 */
public class BPCreateTask {
    static Map<String,String> map = Configure.txt();
    String token;
    String headerName = map.get("headerName");

    @BeforeClass(alwaysRun = true)
    public void tokenOut() throws Exception{
        token = LoginTest.loginApiTest(map.get("ckm"),map.get("ckmPwd"));
    }

    @Test
    public void batchCreate() throws Exception{
        String type = "modelserving";//notebook,fasttraining,modelserving
        BPCreateTask.batchCreateData(type,headerName,token);
    }

    //批量创建数据
    public static void batchCreateData(String type,String headerName,String token) throws Exception{
        for (int i = 0;i < 1; i++){
            switch (type){
                case "fasttraining":
                    //template=模板,user-defined=自定义
                    InitFastTrain.allFastTrainData(2,"user-defined",token);
                    break;
                case "notebook":
                    InitNoteBook.allNoteBookData(2,headerName,token);
                    break;
                case "modelserving":
                    //fileName = op.zip,ONNX.zip,adult.pmml
                    //type = pmml,tensorflow
                    InitServing.createModelData(4,headerName,token,"adult.pmml","pmml");
                    break;
                default:
                    System.out.println("不存在!");
            }
        }
    }

}
