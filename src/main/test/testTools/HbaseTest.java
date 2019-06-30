package testTools;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by andy on 2019/3/5.
 *
 * hbase.zookeeper.fk.quorum=172.17.xx.87,172.17.xx.88,172.17.xx.89
 * hbase.zookeeper.quorum=172.17.xx.87,172.17.xx.88,172.17.xx.89
 */
public class HbaseTest {

    public static Connection hbaseConnection() throws Exception {
        //Hbase配置文件
        Configuration configuration = HBaseConfiguration.create();
        //不填端口,默认2181
        configuration.set("hbase.zookeeper.quorum","172.17.xx.87");
        //获取连接对象
        Connection connection = ConnectionFactory.createConnection(configuration);
        //Admin admin = connection.getAdmin();

        return connection;
    }
    //Connection connection
    public static void hbaseClose(Admin admin) throws Exception {
        //关闭资源
//        if (connection != null){
//            connection.close();
//        }
        if (admin != null){
            admin.close();
        }
    }

    //创建表
    public static void createTable(String tableName,String cf) throws Exception{
        Connection connection = hbaseConnection();
        Admin admin = connection.getAdmin();
        if(admin.tableExists(TableName.valueOf(tableName))){
            System.out.println("表已经存在!");
            return;
        }
        //创建表描述器
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
        //创建列描述器
        HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
        //设置最大列族版本
        hColumnDescriptor.setMaxVersions(1);
        //添加列族
        hTableDescriptor.addFamily(hColumnDescriptor);
        //创建
        admin.createTable(hTableDescriptor);

        System.out.println("表创建成功!");
        hbaseClose(admin);
    }

    //删除表
    public static void deleteTable(String tableName) throws Exception{
        Connection connection = hbaseConnection();
        Admin admin = connection.getAdmin();

        if(!admin.tableExists(TableName.valueOf(tableName))){
            System.out.println("表不存在!");
            return;
        }
        //使表不可用
        admin.disableTable(TableName.valueOf(tableName));
        //删表
        admin.deleteTable(TableName.valueOf(tableName));
        System.out.println("表已删除!");

        hbaseClose(admin);

    }

    //添加数据
    public static void putData(String tableName,String rowKey,String cf,String cn,String value)throws Exception{
        Connection connection = hbaseConnection();
        //获取Table对象
        Table table = connection.getTable(TableName.valueOf(tableName));
        //创建put对象
        Put put = new Put(Bytes.toBytes(rowKey));
        //添加数据
        put.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn),Bytes.toBytes(value));
        //添加操作
        table.put(put);
        table.close();
    }

    //删除数据
    public static void deleteData(String tableName,String rowKey,String cf,String cn) throws Exception{
        Connection connection = hbaseConnection();
        //获取Table对象
        Table table = connection.getTable(TableName.valueOf(tableName));
        //创建Del对象
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumns(Bytes.toBytes(cf),Bytes.toBytes(cn));
        //删除操作
        table.delete(delete);
        table.close();
    }

    //查全表数据
    public static void scanTable(String tableName) throws Exception{
        Connection connection = hbaseConnection();
        //获取Table对象
        Table table = connection.getTable(TableName.valueOf(tableName));
        //创建扫描器
        Scan scan = new Scan();

        ResultScanner resultScanner = table.getScanner(scan);
        //遍历数据并打印
        for (Result result : resultScanner){
            Cell[] cells = result.rawCells();
            for (Cell cell : cells){
                System.out.println("rowKey:" + Bytes.toString(CellUtil.cloneRow(cell)) + ",cf:" + Bytes.toString(CellUtil.cloneFamily(cell)) + ",cn:" + Bytes.toString(CellUtil.cloneQualifier(cell)) + ",value:" + Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        table.close();
    }

    //查询指定数据
    public static void getData(String tableName,String rowKey,String cf,String cn) throws Exception{
        Connection connection = hbaseConnection();
        //获取Table对象
        Table table = connection.getTable(TableName.valueOf(tableName));
        //创建一个Get对象
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn));
        //获取数据操作
        Result result = table.get(get);
        Cell[] cells = result.rawCells();
        for (Cell cell : cells){
            System.out.println("rowKey:" + Bytes.toString(CellUtil.cloneRow(cell)) + ",cf:" + Bytes.toString(CellUtil.cloneFamily(cell)) + ",cn:" + Bytes.toString(CellUtil.cloneQualifier(cell)) + ",value:" + Bytes.toString(CellUtil.cloneValue(cell)));
        }
        table.close();
    }

    public static void main(String[] args) throws Exception{
        //tableExist("api:tmp_dsj_fenxi_hongbao_yunying_user_internal");
        //getResult();
        //createTable("t6","cf");
        //putData("t6","1001","cf","age","18");
        //deleteData("t6","1002","cf","age");
        scanTable("t6");
        System.out.println("****************");
        getData("t6","1001","cf","name");
    }
}
