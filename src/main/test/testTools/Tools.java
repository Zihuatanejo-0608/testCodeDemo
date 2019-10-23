package toolsUnit;

import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by andy on 2019/6/28.
 */
public class Tools {

    public static String randomData() throws Exception{
        int r = (int)((Math.random()*9+1)*100000000);
        //System.out.println(String.valueOf(r));
        return String.valueOf(r);
    }

//    @Test
//    //统计类中含有多少个方法
//    public void sumClass() {
//        int length = lei.class.getMethods().length;
//        System.out.println(length);
//    }

    public static String filePath(String fileName) throws Exception{
        File file = new File(".");
        String path = ".src.main.resources.";
        String absolutePath = file.getCanonicalPath() + path.replaceAll("\\.", Matcher.quoteReplacement("\\")) + fileName;
        System.out.println("资源数据路径:" +absolutePath);

        return absolutePath;
    }

    private Logger logger = LoggerFactory.getLogger(Tools.class);

    public static void writeTxt(String file,List<String> list) throws Exception{

        String path = Tools.filePath(file);

        FileUtil.writeLines(list,path, Charset.defaultCharset(),false);
        //logger.info("写入" + file + "成功!");

    }




}
