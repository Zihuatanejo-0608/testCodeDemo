package toolsUnit;


import dataInit.ServiceApplication;

import dataInit.dataEntity.DictResourceSettingInfo;
import dataInit.dataEntity.DictStorageInfo;
import dataInit.service.MysqlService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.Test;

/**
 * Created by andy on 2019/7/3.
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)

public class MysqlTest {

    @Autowired
    private MysqlService mysqlService;


    @Test
    public void name() {

        DictResourceSettingInfo dictResourceSettingInfo = new DictResourceSettingInfo();
        dictResourceSettingInfo.setId(991);
        dictResourceSettingInfo.setCpu(1d);
        dictResourceSettingInfo.setGpu(0d);
        dictResourceSettingInfo.setMem(1d);
        dictResourceSettingInfo.setIsCpu("true");
        int result = mysqlService.dictResourceSettingInfoInit(dictResourceSettingInfo);
        System.out.println("result = " + result);
    }

    @Test
    public void test2(){
        DictStorageInfo dictStorageInfo = new DictStorageInfo();
        dictStorageInfo.setId(99);
        dictStorageInfo.setCapacity(0.0625d);
        dictStorageInfo.setStorage_type("HDD");//HDD,SSD

        int result = mysqlService.dictStorageInfoInit(dictStorageInfo);
        System.out.println("result = " + result);
    }


}
