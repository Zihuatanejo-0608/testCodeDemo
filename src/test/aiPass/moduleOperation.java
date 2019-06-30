package aiPass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by andy on 2019/5/29.
 *
 * 模组化操作
 */
public class moduleOperation {
    //创建任务模组
    public static String createNoteBookTask(WebDriver webDriver){
        int time = 25000;
        try{
            //点击'自助建模'-'NB建模'
            basicOperation.noteBookModel(webDriver);
            Thread.sleep(3000);
            //点击'新增任务'
            basicOperation.aItrainAdd(webDriver);
            //新增'任务名称'
            String taskName = basicInfo.name(webDriver);
            //选择'训练框架'
            basicInfo.noteBookTrain(webDriver);
            //选择处理器资源
            basicInfo.noteBookDeploy(webDriver);
            //选择硬盘资源
            basicInfo.noteBookCapacity(webDriver);
            //确认按钮
            basicInfo.noteBookCreateSubmit(webDriver);
            System.out.println("容器创建中……请等待:" + time + "ms");
            Thread.sleep(time);
            basicOperation.aItrainReflesh(webDriver);
            return taskName;
        }catch (Exception e){
            System.out.println("创建NoteBook任务失败!");
        }
        return null;
    }

    //管理页,从hive拷贝按钮
    public static void hiveCopy(WebDriver webDriver,String name){
        String i = "3";

        try{
            //创建一个文件节点
            basicOperation.addList(webDriver,name);
            //从hive拷贝
            basicOperation.copyHive(webDriver,"default","adult2");
            //返回
            basicOperation.goBack(webDriver);
        }catch (Exception e){
            System.out.println("管理页异常!");
        }
    }

}
