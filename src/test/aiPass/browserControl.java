package aiPass;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by andy on 2019/4/28.
 *
 * 浏览器选择和操作
 */
public class browserControl {

    public static WebDriver firefoxBrowser() throws Exception{
        String path =Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //String chromedriver = new File(UriEncoder.decode(path),"geckodriver.exe").toString();
        System.setProperty("webdriver.gecko.driver","D:\\work\\2019\\Code\\aipassSelenium\\src\\test\\resources\\geckodriver.exe");
        System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe");

        WebDriver firefoxBrowser = new FirefoxDriver();
        //设置界面加载等待时间，全局设置，作用于driver，对所有后续界面加载都有效
        firefoxBrowser.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        //设置元素定位超时等待时间，全局设置，作用于driver，对所有后续元素定位都有效
        firefoxBrowser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        firefoxBrowser.manage().window().maximize();
        Thread.sleep(2000);

        System.out.println("firefoxDriver初始化完成");
        System.out.println("======================");
        return firefoxBrowser;
    }

    public static WebDriver chromeBrowser() throws Exception {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String chromedriver = new File(UriEncoder.decode(path), "chromedriver.exe").toString();
        System.setProperty("webdriver.chrome.driver", chromedriver);

        WebDriver chromeBrowser = new ChromeDriver();

        //设置界面加载等待时间，全局设置，作用于driver，对所有后续界面加载都有效
        chromeBrowser.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        //设置元素定位超时等待时间，全局设置，作用于driver，对所有后续元素定位都有效
        chromeBrowser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        chromeBrowser.manage().window().maximize();
        //chromeBrowser.get("http://omp.test1.xx.net/a?login");
        Thread.sleep(2000);

        System.out.println("chromeDriver初始化完成");
        System.out.println("===================");
        return chromeBrowser;
    }

    public static void url(WebDriver webDriver){
        try{
            //http://omp.test1.xx.net/a?login
            String url= "http://172.17.xx.85:30088/#/login";
            System.out.println("打开被测URL:" + url);
            webDriver.get(url);
        }catch (Exception e){
            System.out.println("打开URL异常!");
        }
    }

    public static void close(WebDriver webDriver){
        try{
            System.out.println("关闭浏览器,退出进程!");
            webDriver.quit();
        }catch (Exception e){
            System.out.println("关闭浏览器异常!");
        }
    }

}
