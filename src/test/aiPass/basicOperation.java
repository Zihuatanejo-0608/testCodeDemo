package aiPass;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by andy on 2019/5/6.
 *
 * 基础路径操作,一些基本的选择路径等
 */
public class basicOperation {
    //NoteBook建模
    public static void noteBookModel(WebDriver webDriver){
        try{
            //左侧栏-自助建模
            if (webDriver.findElement(By.xpath("/html/body/div/section/section/aside/div/div[1]/div/ul/li[3]/div")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div/section/section/aside/div/div[1]/div/ul/li[3]/div")).click();
            }
            //左侧栏-自助建模-NoteBook建模
            if (webDriver.findElement(By.xpath("/html/body/div/section/section/aside/div/div[1]/div/ul/li[3]/ul/li/ul/li[1]")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div/section/section/aside/div/div[1]/div/ul/li[3]/ul/li/ul/li[1]")).click();
            }
        }catch (Exception e){
            System.out.println("NoteBookModel失败!");
        }
    }

    //新增任务按钮
    public static void aItrainAdd(WebDriver webDriver){
        try {
            if (webDriver.findElement(By.id("AItrainAdd")).isDisplayed()){
                webDriver.findElement(By.id("AItrainAdd")).click();
            }
            Thread.sleep(3000);
        }catch (Exception e){
            System.out.println("NoteBookModel新增任务失败!");
        }
    }

    //点击新增
    public static void addFile(WebDriver webDriver){
        try{
            if (webDriver.findElement(By.xpath("/html/body/div[1]/section/section/main/div/div[1]/div[2]/div[2]/div/p/div/div/p[2]/span[1]/button")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div[1]/section/section/main/div/div[1]/div[2]/div[2]/div/p/div/div/p[2]/span[1]/button")).click();
            }
        }catch (Exception e){
            System.out.println("新增文件按钮异常!");
        }
    }

    //新增目录,目录名称
    public static void amendListName(WebDriver webDriver,String name){
        try{
            if (webDriver.findElement(By.id("AmendListName")).isDisplayed()){
                webDriver.findElement(By.id("AmendListName")).sendKeys(name);
            }
            if (webDriver.findElement(By.id("AmendListSubmit")).isDisplayed()){
                webDriver.findElement(By.id("AmendListSubmit")).click();
            }
        }catch (Exception e){
            System.out.println("新增目录异常!");
        }
    }

    //上传文件
    public static void uploadFile(WebDriver webDriver){
        try{
            if (webDriver.findElement(By.xpath("/html/body/div[1]/section/section/main/div/div[1]/div[2]/div[2]/div/p/div/p/div/div/p[2]/span[3]/div/div/button")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div[1]/section/section/main/div/div[1]/div[2]/div[2]/div/p/div/p/div/div/p[2]/span[3]/div/div/button")).sendKeys("D:\\work\\2019\\April\\testData.txt");
            }
        }catch (Exception e){
            System.out.println("上传文件异常!");
        }
    }

    //创建一个文件节点,输入名称,确认
    public static void addList(WebDriver webDriver,String name){
        try{
            if (webDriver.findElement(By.xpath("/html/body/div[1]/section/section/main/div/div[1]/div[2]/div[2]/div/p/div/div/p[2]/span[1]/button")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div[1]/section/section/main/div/div[1]/div[2]/div[2]/div/p/div/div/p[2]/span[1]/button")).sendKeys(Keys.ENTER);
            }
            //输入文件夹名称
            if (webDriver.findElement(By.id("AmendListName")).isDisplayed()){
                webDriver.findElement(By.id("AmendListName")).sendKeys(name);
            }
            //确定
            if (webDriver.findElement(By.id("AmendListSubmit")).isDisplayed()){
                webDriver.findElement(By.id("AmendListSubmit")).click();
            }
        }catch (Exception e){
            System.out.println("新增节点异常!");
        }
    }

    public static void copyHive(WebDriver webDriver,String ku,String biao){
        Actions actions = new Actions(webDriver);
        try{
            //点击,从hive上传按钮
            if (webDriver.findElement(By.xpath("/html/body/div[1]/section/section/main/div/div[1]/div[2]/div[2]/div/p/div/p/div/div/p[2]/span[2]/button")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div[1]/section/section/main/div/div[1]/div[2]/div[2]/div/p/div/p/div/div/p[2]/span[2]/button")).sendKeys(Keys.ENTER);
            }
            //点击库表选择框
            if (webDriver.findElement(By.id("AmendDBInfo")).isDisplayed()){
                webDriver.findElement(By.id("AmendDBInfo")).click();
            }
            //鼠标滑动到父节点
            if (webDriver.findElement(By.xpath("//span[contains(text(),'" + ku + "')]")).isDisplayed()){
                actions.moveToElement(webDriver.findElement(By.xpath("//span[contains(text(),'" + ku + "')]"))).perform();
            }
            //选择第二个选项,adult2
            if (webDriver.findElement(By.xpath("//span[contains(text(),'" + biao + "')]")).isDisplayed()){
                webDriver.findElement(By.xpath("//span[contains(text(),'" + biao + "')]")).click();
            }
            //库表信息提交确定
            if (webDriver.findElement(By.id("AmendDBSuubmit")).isDisplayed()){
                webDriver.findElement(By.id("AmendDBSuubmit")).click();
            }
            //提示框确认
            if (webDriver.findElement(By.xpath("//button[contains(@class,'el-button el-button--default el-button--small el-button--primary ')]")).isDisplayed()){
                webDriver.findElement(By.xpath("//button[contains(@class,'el-button el-button--default el-button--small el-button--primary ')]")).sendKeys(Keys.ENTER);
            }
            Thread.sleep(30000);
        }catch (Exception e){
            System.out.println("copy hive异常!");
        }
    }

    //管理页,返回按钮
    public static void goBack(WebDriver webDriver){
        try{
            if (webDriver.findElement(By.id("FStrainSearch")).isDisplayed()){
                webDriver.findElement(By.id("FStrainSearch")).click();
            }
        }catch (Exception e){
            System.out.println("返回按钮异常!");
        }
    }

    //删除任务提示确认
    public static void deleteTask(WebDriver webDriver){
        try{
            if (webDriver.findElement(By.xpath("/html/body/div[2]/div/div[3]/button[2]")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div[2]/div/div[3]/button[2]")).sendKeys(Keys.ENTER);
            }else {
                System.out.println("没有发现删除确认按钮!");
            }
        }catch (Exception e){
            System.out.println("删除提示出错!");
        }
    }

    //打开jupyter
    public static void windowSwitchToJupyter(WebDriver webDriver){
        String pwd = "spark";
        try{
            //获得全部窗口句柄
            Set<String> set = webDriver.getWindowHandles();
            List<String> list = new ArrayList<>(set);
            //切换窗口到1,0开始
            webDriver.switchTo().window(list.get(1));
            //输入密码
            if (webDriver.findElement(By.id("password_input")).isDisplayed()){
                webDriver.findElement(By.id("password_input")).sendKeys(pwd);
            }
            //点击登录
            if (webDriver.findElement(By.id("login_submit")).isDisplayed()){
                webDriver.findElement(By.id("login_submit")).sendKeys(Keys.ENTER);
            }
        }catch (Exception e){
            System.out.println("切换jupyter异常!");
        }
    }

    //查询hive资源
    public static String queryJupyter(WebDriver webDriver){
        try{
            //点击文件夹路径
            if (webDriver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[2]/div[2]/div/a/span")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[2]/div[2]/div/a/span")).click();
            }
            String hiveData = webDriver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[2]/div[3]/div/a/span")).getText();
            if (webDriver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[2]/div[3]/div/a/span")).isDisplayed()){
                System.out.println("hive资源:" + hiveData);
            }
            return hiveData;
        }catch (Exception e){
            System.out.println("查询hive资源异常!");
        }
        return null;
    }

    //刷新按钮
    public static void aItrainReflesh(WebDriver webDriver){
        try{
            if (webDriver.findElement(By.id("AItrainReflesh")).isDisplayed()){
                webDriver.findElement(By.id("AItrainReflesh")).click();
            }
        }catch (Exception e){
            System.out.println("刷新按钮异常!");
        }

    }



}
