package testTools;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by andy on 2019/3/12.
 *
 *
 */
public class ReadTxt {
    //写入txt数据
    public static void writeFile() throws Exception{
        File file = new File("");
        //创建新文件,同名文件直接覆盖
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("ABC\r\n");
        bufferedWriter.write("ABC\r\n");
        bufferedWriter.flush();
    }

    //读取文件内所有内容转换成String
    public static List<String> testRead() throws IOException{
        File file = new File("src/main/resources/dataSource.txt");
        //读取文件内容转换成String
        List lines = FileUtils.readLines(file);

        return lines;
    }







}
