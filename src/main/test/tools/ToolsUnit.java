package tools;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by andy on 2019/6/28.
 */
public class ToolsUnit {

    public static String randomData(){
        try{
            int r = (int)((Math.random()*9+1)*100000000);
            return String.valueOf(r);
        }catch (Exception e){
            System.out.println("随机数生成失败!");
            return null;
        }
    }

    public static String filePath(String fileName) throws Exception{
        File file = new File(".");
        String path = ".src.main.resources.";
        String absolutePath = file.getCanonicalPath() + path.replaceAll("\\.", Matcher.quoteReplacement("\\")) + fileName;
        System.out.println("资源数据路径:" +absolutePath);

        return absolutePath;
    }


    public static void writeTxt(String file,List<String> list) throws Exception{
        String path = ToolsUnit.filePath(file);

        FileUtil.writeLines(list,path, Charset.defaultCharset(),true);
        System.out.println("写入" + file + "成功!");

    }




}
