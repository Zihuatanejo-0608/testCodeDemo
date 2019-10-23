package aiPaasTest.loginService;

import cn.hutool.core.io.file.FileReader;
import toolsUnit.Tools;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by andy on 2019/8/22.
 *
 */
public class Configure {
    public static Map<String,String> txt(){
        try {
            String path = Tools.filePath("testConfigure.txt");
            FileReader fileReader = new FileReader(path);
            List<String> list = fileReader.readLines();
            Map<String, String> map = list.stream().collect(Collectors.toMap(s -> s.split("=")[0], s -> s.split("=")[1]));
            return map;
        }catch (Exception e){
            System.out.println("读取配置文件失败!");
        }
        return null;
    }
}
