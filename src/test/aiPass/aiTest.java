package aiPass;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

/**
 * Created by andy on 2019/4/29.
 *
 *
 */
public class aiTest {
    @BeforeClass(alwaysRun = true)
    public static void setUp(ITestContext context) throws Exception{
        WebDriver webDriver = browserControl.firefoxBrowser();
        context.setAttribute(WebDriver.class.getName(),webDriver);
    }
    @BeforeMethod
    public static void login(ITestContext context) throws Exception{
        WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
        //打开url
        browserControl.url(webDriver);
        //登录
        loginQuit.login(webDriver);
    }
    @AfterMethod
    public static void logout(ITestContext context) throws Exception{
        WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
        loginQuit.logout(webDriver);
    }
    @AfterClass(alwaysRun = true)
    public static void quit(ITestContext context) throws Exception{
        WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
        Thread.sleep(10000);
        browserControl.close(webDriver);
    }

    String taskName = null;

    @Test(priority = 1)
    public void firestTest(ITestContext context){
        try{
            WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
            //创建任务
            taskName = moduleOperation.createNoteBookTask(webDriver);
            //表单操作
            tableOperation.operationData(webDriver,taskName,"管理");
        }catch (Exception e){
            System.out.println("测试失败!");
        }
    }

    @Test(priority = 2)
    public void secondTest(ITestContext context){
        try{
            WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
            //点击'自助建模'-'NB建模'
            basicOperation.noteBookModel(webDriver);
            //表单操作
            tableOperation.operationData(webDriver,taskName,"打开");
        }catch (Exception e){
            System.out.println("测试失败!");
        }
    }

    @Test(priority = 3)
    public void thirdTest(ITestContext context){
        try{
            WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
            //点击'自助建模'-'NB建模'
            basicOperation.noteBookModel(webDriver);
            //表单操作
            tableOperation.operationData(webDriver,taskName,"查看");
        }catch (Exception e){
            System.out.println("测试失败!");
        }
    }

    @Test(priority = 4)
    public void fourthTest(ITestContext context){
        try{
            WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
            //点击'自助建模'-'NB建模'
            basicOperation.noteBookModel(webDriver);
            //表单操作
            tableOperation.operationData(webDriver,taskName,"停止");
        }catch (Exception e){
            System.out.println("测试失败!");
        }
    }

    @Test(priority = 5)
    public void fifthTest(ITestContext context){
        try{
            WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
            //点击'自助建模'-'NB建模'
            basicOperation.noteBookModel(webDriver);
            //表单操作
            tableOperation.operationData(webDriver,taskName,"运行");
        }catch (Exception e){
            System.out.println("测试失败!");
        }
    }

    @Test(priority = 6)
    public void sixthTest(ITestContext context){
        try{
            WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
            //点击'自助建模'-'NB建模'
            basicOperation.noteBookModel(webDriver);
            //表单操作
            tableOperation.operationData(webDriver,taskName,"停止");
        }catch (Exception e){
            System.out.println("测试失败!");
        }
    }

    @Test(priority = 7)
    public void seventhTest(ITestContext context){
        try{
            WebDriver webDriver = (WebDriver) context.getAttribute(WebDriver.class.getName());
            //点击'自助建模'-'NB建模'
            basicOperation.noteBookModel(webDriver);
            //表单操作
            tableOperation.operationData(webDriver,taskName,"删除");
        }catch (Exception e){
            System.out.println("测试失败!");
        }
    }

}
