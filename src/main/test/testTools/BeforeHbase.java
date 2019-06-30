package testTools;

import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by andy on 2019/3/19.
 *
 *
 */
public class BeforeHbase {
    //建表,读取txt,put到hbase
    @Test
    public static void beforeHbaseTest(){
        try{
            //定义对象类型和常量值
            String tableName = "api:t426";//t_fk_test
            String cf = "cf";
            String[] words = null;
            String rowKey = null;
            String cn = "score";
            String value = null;
            //判断表是否存在,并且建表
            HbaseTest.createTable(tableName,cf);
            //读取txt,生成list
            List lines = ReadTxt.testRead();
            //解析遍历list,分隔后赋值
            for (int i = 0; i<lines.size(); i++){
                String line = lines.get(i).toString();
                //对list进行分隔,赋值给words
                words = line.split(",");
                //赋值
                rowKey = words[0];
                value = words[1];

                System.out.println("rowKey:" +rowKey + " value:" + value);
                //put数据
                HbaseTest.putData(tableName,rowKey,cf,cn,value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
