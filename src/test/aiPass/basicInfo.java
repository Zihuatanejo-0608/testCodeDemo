package aiPass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andy on 2019/5/6.
 *
 *
 */
public class basicInfo {
    //名称
    public static String name(WebDriver webDriver){
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String name = "N" + str;

        try{
            if (webDriver.findElement(By.id("NoteBookTaskName")).isDisplayed()){
                webDriver.findElement(By.id("NoteBookTaskName")).sendKeys(name);
                System.out.println("任务名称是:" + name);
            }
        }catch (Exception e){
            System.out.println("name异常!");
            return null;
        }
        return name;
    }

    //训练框架下拉框
    public static void noteBookTrain(WebDriver webDriver){
        String i = "1";
        try{
            if (webDriver.findElement(By.id("NoteBookTrain")).isDisplayed()){
                webDriver.findElement(By.id("NoteBookTrain")).click();
            }
            if (webDriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/ul/li[" + i + "]")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/ul/li[" + i + "]")).click();
            }
        }catch (Exception e){
            System.out.println("训练框架选择异常!");
        }
    }

    //选择配置,CPU
    public static void noteBookDeploy(WebDriver webDriver){
        String i = "1";
        Actions actions = new Actions(webDriver);
        try{

            if (webDriver.findElement(By.id("NoteBookDeploy")).isDisplayed()){
                webDriver.findElement(By.id("NoteBookDeploy")).click();
            }
            //移动到CPU选项悬停
            if (webDriver.findElement(By.xpath("/html/body/div[3]/ul[1]/li")).isDisplayed()){
                actions.moveToElement(webDriver.findElement(By.xpath("/html/body/div[3]/ul[1]/li"))).perform();
            }
            //模糊搜索的三种方法,contains(a, b),starts-with(a, b),ends-with(a, b)
            if (webDriver.findElement(By.xpath("//li[contains(@id,'-1-9')]")).isDisplayed()){
                webDriver.findElement(By.xpath("//li[contains(@id,'-1-9')]")).click();
            }
        }catch (Exception e){
            System.out.println("CPU选择异常!");
        }
    }

    //容量选择,HDD,SSD,NoteBookCapacity
    public static void noteBookCapacity(WebDriver webDriver){
        String i = "1";//1,2
        String f = "7";//HDD<=5,SSD<=4
        Actions actions = new Actions(webDriver);
        try{
            if (webDriver.findElement(By.id("NoteBookCapacity")).isDisplayed()){
                webDriver.findElement(By.id("NoteBookCapacity")).click();
            }
            //移动到HDD,SSD选项悬停
            if (webDriver.findElement(By.xpath("/html/body/div[4]/ul[1]/li["+ i +"]")).isDisplayed()){
                actions.moveToElement(webDriver.findElement(By.xpath("/html/body/div[4]/ul[1]/li["+ i +"]"))).perform();
            }
            if (webDriver.findElement(By.xpath("/html/body/div[4]/ul[2]/li["+ f +"]/span")).isDisplayed()){
                webDriver.findElement(By.xpath("/html/body/div[4]/ul[2]/li["+ f +"]/span")).click();
            }
        }catch (Exception e){
            System.out.println("容量选择异常!");
        }
    }

    //NoteBook提交确认
    public static void noteBookCreateSubmit(WebDriver webDriver){
        try{
            if (webDriver.findElement(By.id("NoteBookCreateSubmit")).isDisplayed()){
                webDriver.findElement(By.id("NoteBookCreateSubmit")).click();
            }
        }catch (Exception e){
            System.out.println("确认异常!");
        }
    }

}
