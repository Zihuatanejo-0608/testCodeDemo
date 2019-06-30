package testTools;

import dataEntity.T_SERVICE_KEY_INFO;
import dataEntity.T_SERVICE_KEY_RELA;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * Created by andy on 2019/3/1.
 *
 *
 */
public class SelectKeyRela {
    //rela表查询
    public static List<T_SERVICE_KEY_RELA> selectRela(String type) throws Exception{
        //读取一级配置文件
        InputStream inputStream = Resources.getResourceAsStream("BasicDataConfig.xml");
        //构建sql仓库
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //开启连接
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //将实体类设定为list，并且执行sql
        List<T_SERVICE_KEY_RELA> list = sqlSession.selectList("ServiceKey_Rela_Info.keyRela",type);
        System.out.println(list);
        //提交
        sqlSession.commit();

        return list;
    }
    //info表查询
    public static List<T_SERVICE_KEY_INFO> selectInfo(String key) throws Exception{
        InputStream inputStream = Resources.getResourceAsStream("BasicDataConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<T_SERVICE_KEY_INFO> list = sqlSession.selectList("ServiceKey_Rela_Info.keyInfo",key);
        sqlSession.commit();

        return list;
    }

    public static void main(String[] args) throws Exception{
        String type = "phoenix-fk-000001";
        String key = null;
        List<T_SERVICE_KEY_RELA> listRela = SelectKeyRela.selectRela(type);
        if (listRela != null){
            for (T_SERVICE_KEY_RELA rela : listRela){
                System.out.println("**************");
                System.out.println("key:" + rela.getKey());
                key = rela.getKey();
                System.out.println("inst_interface_type:" + rela.getInst_interface_type());
            }
        }

        List<T_SERVICE_KEY_INFO> listInfo = SelectKeyRela.selectInfo(key);
        if (listInfo != null){
            for (T_SERVICE_KEY_INFO info : listInfo){
                System.out.println("**************");
                System.out.println("user_name:" + info.getUser_name());
                System.out.println("key_id:" + info.getKey_id());
                System.out.println("key:" + info.getKey());
                System.out.println("key_decr:" + info.getKey_decr());
            }
        }
    }
}
