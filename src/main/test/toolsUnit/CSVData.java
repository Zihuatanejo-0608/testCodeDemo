package toolsUnit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by andy on 2019/6/6.
 */
public class CSVData implements Iterator<Object[]> {
    BufferedReader bufferedReader;
    List<String> csvList = new ArrayList<String>();
    int rowNum = 0;//行数
    int columnNum = 0;//列数
    int curRowNo = 0;//当前行数
    String columnName[];//列名

    public CSVData(String fileName) throws Exception {

        String absolutePath = Tools.getPath(fileName);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath),"GBK"));
        while (bufferedReader.ready()){
            csvList.add(bufferedReader.readLine());
            this.rowNum++;
        }

        String[] str = csvList.get(0).split(",");
        this.columnNum = str.length;
        columnName = new String[columnNum];

        for (int i =0; i<columnNum; i++){
            columnName[i] = str[i];
        }
        this.curRowNo++;
    }

    @Override
    public boolean hasNext(){
        if (rowNum==0 || curRowNo>=rowNum){
            try{
                if (bufferedReader != null){
                    bufferedReader.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object[] next(){
        Map<String,String> map = new TreeMap<String, String>();
        String csvCell[] = csvList.get(curRowNo).split(",");

        for (int i =0; i<this.columnNum;i++){
            map.put(columnName[i],csvCell[i]);
        }
        Object[] objects = new Object[1];
        objects[0]=map;
        this.curRowNo++;

        return objects;
    }

    @Override
    public void remove(){
        throw new UnsupportedOperationException("remove unsupported");
    }



}
