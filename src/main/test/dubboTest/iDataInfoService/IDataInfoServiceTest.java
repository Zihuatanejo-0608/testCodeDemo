package dubboTest.iDataInfoService;

import com.qq.aipaas.api.IDataInfoService;
import com.qq.aipaas.model.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by andy on 2019/5/16.
 */
public class IDataInfoServiceTest {

    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:consumer.xml");
    IDataInfoService iDataInfoService = (IDataInfoService) applicationContext.getBean("iDataInfoService");

    @Test
    //判断表是否存在,如果存在再判断是否是空表
    public void emptyTableTest(){
        try{
            DbInfoDTO dbInfoDTO = DbInfoDTO.builder().dbname("ods_wgcg_bdpms").flag(true).table("zengliangbiao").build();

            ResultBean<Boolean> resultBean = iDataInfoService.emptyTable(dbInfoDTO);
            System.out.println("是否请求成功:" + resultBean.getFlag());
            System.out.println("错误码:" + resultBean.getErrorCode());
            System.out.println("信息:" + resultBean.getErrorMsg());
            System.out.println("数据:" + resultBean.getData());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    //获取hive表信息
    //ods_wgcg_bdpms,库名,前缀一定为ods
    //zengliangbiao,增量表
    //quanliangbiao,全量表
    //waibubiao,外部表
    //增量表和全量表,路径不同DBS,路径表
    //TBLS,Hive信息表
    //SDS,表在HDFS上的存储路径
    public void getDataInfoTest(){
        try{
            //库名,表名,是否展示路径
            DbInfoDTO dbInfoDTO = DbInfoDTO.builder().dbname("default").flag(true).table("adult").build();

            ResultBean<DbInfpResp> resultBean = iDataInfoService.getDataInfo(dbInfoDTO);
            System.out.println("是否请求成功:" + resultBean.getFlag());
            System.out.println("错误码:" + resultBean.getErrorCode());
            System.out.println("错误信息:" + resultBean.getErrorMsg());
            System.out.println("************************");
            System.out.println("路径:" + resultBean.getData().getTableRealPath());
            System.out.println("类型:" + resultBean.getData().getTableType());
            System.out.println("文件:" + resultBean.getData().getTableField());
            System.out.println("大小:" + resultBean.getData().getFileSize());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getTableListTest(){
        try{
            TableInfoDTO tableInfoDTO = new TableInfoDTO();
            tableInfoDTO.setDbName("default");//ods_wgcg_bdpms

            ResultBean<TableInfoResp> respResultBean = iDataInfoService.getTableList(tableInfoDTO);
            System.out.println("是否请求成功:" + respResultBean.getFlag());
            System.out.println("错误码:" + respResultBean.getErrorCode());
            System.out.println("错误信息:" + respResultBean.getErrorMsg());
            if (respResultBean.getData() != null){
                List<String> list = respResultBean.getData().getTables();
                for(String table : list){
                    System.out.println(table + "\n************");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
