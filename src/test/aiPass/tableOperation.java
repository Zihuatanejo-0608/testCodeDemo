package aiPass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by andy on 2019/5/6.
 *
 *
 */
public class tableOperation {
    //noteBook建模表格,导入操作
    public static void tableNoteBook(WebDriver webDriver,String taskName){
        WebElement table = webDriver.findElement(By.id("NoteBookTable"));
        if (table.isDisplayed()){
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            for (int i=1; i < rows.size(); i++){
                String rowsInfo = rows.get(i).getText();
                System.out.println(rowsInfo);
                System.out.println("**********************");
                //状态栏
                String stateXpath = "/html/body/div[1]/section/section/main/div/div[1]/div[3]/table/tbody/tr["+ i +"]/td[6]";
                //操作栏
                ///html/body/div[1]/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr[1]/td[7]/div/a
                ///html/body/div[1]/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr[1]/td[7]/div/button[1]
                ///html/body/div[1]/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr[1]/td[7]/div/button[2]
                String operationXpath = "/html/body/div[1]/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr["+ i +"]/td[7]";
                //数据栏,导入按钮
                String dataXpath = "/html/body/div[1]/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr[" + i + "]/td[8]/div/button";
                //判断任务名称和任务状态,如果一致则点击导入
                if (rowsInfo.contains(taskName) && webDriver.findElement(By.xpath(stateXpath)).getText().contains("Running")){//&& webDriver.findElement(By.xpath(stateXpath)).getText().contains("Running")
                    //打印找到的字符串
                    System.out.println("当前状态:" + webDriver.findElement(By.xpath(stateXpath)).getText());
                    System.out.println("当前操作:" + webDriver.findElement(By.xpath(operationXpath)).getText());
                    //点击导入
                    //rows.get(i).findElement(By.xpath(dataXpath)).click();

                    break;
                }else {
                    System.out.println("没有找到任务记录!");
                }
            }
        }else {
            System.out.println("NoteBookTable不存在!");
        }
    }

    //读取表单状态
    public static String readState(WebDriver webDriver,String taskName){
        WebElement table = webDriver.findElement(By.id("NoteBookTable"));
        if (table.isDisplayed()){
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            for (int i=1; i < rows.size(); i++){
                //行信息
                String rowsInfo = rows.get(i).getText();
                //状态
                String stateXpath = "/html/body/div[1]/section/section/main/div/div[1]/div[3]/table/tbody/tr["+ i +"]/td[6]";
                String state = webDriver.findElement(By.xpath(stateXpath)).getText();

                if (rowsInfo.contains(taskName)){

                    System.out.println("当前状态:" + webDriver.findElement(By.xpath(stateXpath)).getText());

                    return state;
                }
            }
        }
        return null;
    }

    //根据读取的状态进行操作
    public static void operationData(WebDriver webDriver,String taskName,String operation){
        WebElement table = webDriver.findElement(By.id("NoteBookTable"));
        try{
            if (table.isDisplayed()){
                List<WebElement> rows = table.findElements(By.tagName("tr"));

                for (int i=1; i < rows.size(); i++){
                    //行信息
                    String rowsInfo = rows.get(i).getText();

                    //操作,打开,停止/运行,删除
                    String openXpath = "/html/body/div/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr[" + i + "]/td[7]/div/a";
                    String stopXpath = "/html/body/div/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr[" + i + "]/td[7]/div/button[1]";
                    String deleteXpath = "/html/body/div/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr[" + i + "]/td[7]/div/button[2]";
                    //数据,管理按钮
                    String dataXpath = "/html/body/div/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr[" + i + "]/td[8]/div/button";
                    //日志,打开按钮
                    String logXpath = "/html/body/div/section/section/main/div/div[1]/div[4]/div[2]/table/tbody/tr[" + i + "]/td[9]/div/button";

                    String state = tableOperation.readState(webDriver,taskName);

                    if (rowsInfo.contains(taskName) && state.equals("Running")){
                        switch (operation){
                            case "打开":
                                System.out.println("执行'打开'操作");
                                webDriver.findElement(By.xpath(openXpath)).click();
                                Thread.sleep(20000);
                                basicOperation.windowSwitchToJupyter(webDriver);
                                //Thread.sleep(15000);
                                basicOperation.queryJupyter(webDriver);
                                break;
                            case "停止":
                                System.out.println("执行'停止'操作");
                                webDriver.findElement(By.xpath(stopXpath)).click();
                                Thread.sleep(5000);
                                break;
                            case "管理":
                                System.out.println("执行'管理'操作");
                                webDriver.findElement(By.xpath(dataXpath)).click();
                                moduleOperation.hiveCopy(webDriver,"testPath");
                                break;
                            case "查看":
                                System.out.println("执行'查看'操作");
                                webDriver.findElement(By.xpath(logXpath)).click();
                                break;
                            default:
                                System.out.println("没有找到对应操作!");
                        }
                        break;
                    }else if (rowsInfo.contains(taskName) && (state.equals("Stop") || state.equals("Terminated"))){
                        switch (operation){
                            case "运行":
                                System.out.println("执行'运行'操作");
                                webDriver.findElement(By.xpath(openXpath)).click();
                                Thread.sleep(25000);
                                basicOperation.aItrainReflesh(webDriver);
                                break;
                            case "删除":
                                System.out.println("执行'删除'操作");
                                webDriver.findElement(By.xpath(deleteXpath)).click();
                                Thread.sleep(3000);
                                basicOperation.deleteTask(webDriver);
                                break;
                            default:
                                System.out.println("没有找到对应操作!");
                        }
                        break;
                    }else {
                        System.out.println("没有相关数据!");
                    }
                }
            }else {
                System.out.println("表不存在");
            }
        }catch (Exception e){
            System.out.println("表单操作异常!");
        }
    }


}
