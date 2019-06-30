package testTools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.net.URLDecoder;

/**
 * Created by andy on 2019/2/28.
 *
 * Http测试框架
 */
public class HttpMethod{
    public static String httpPost(String url, String parameter, String headerName,String headerValue,boolean noNeedResponse) throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            //定义url编码格式
            url = URLDecoder.decode(url, "UTF-8");
            //创建http客户端

            //定义返回对象
            String response = null;
            //创建http post请求对象
            HttpPost httpPost = new HttpPost(url);
            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader(headerName,headerValue);
            if(null != parameter){
                //定义实体对象编码格式
                StringEntity stringEntity = new StringEntity(parameter, "utf-8");
                stringEntity.setContentEncoding("UTF-8");
                //定义实体类传输类型
                stringEntity.setContentType("application/json");
                //entity.setContentType("application/x-www-form-urlencoded");
                //将实体类放入请求对象httpPost
                httpPost.setEntity(stringEntity);
            }
            //发送请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //如果返回码==200,则输出结果
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                response = EntityUtils.toString(httpResponse.getEntity());
                //noNeedResponse=true则返回null,不打印结果
                if (noNeedResponse){
                    return null;
                }
                System.out.println(response);
            }
            return response;
        }finally {
            //关闭http连接
            try{
                if(httpClient != null){
                    httpClient.close();
                }
            }catch (Exception e){
                System.out.println("关闭http连接异常!");
            }
        }
    }

    public static String httpGet(String url, String parameter, String headerName,String headerValue,boolean noNeedResponse) throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            //定义url编码格式
            url = URLDecoder.decode(url, "UTF-8");
            //创建http客户端

            //定义返回对象
            String response = null;
            //创建http post请求对象
            //HttpPost httpPost = new HttpPost(url);
            HttpGet httpGet = new HttpGet(url);
            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(9000).build();
            httpGet.setConfig(requestConfig);
            httpGet.setHeader(headerName,headerValue);

            //发送请求
            HttpResponse httpResponse = httpClient.execute(httpGet);
            //如果返回码==200,则输出结果
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                response = EntityUtils.toString(httpResponse.getEntity());
                //noNeedResponse=true则返回null,不打印结果
                if (noNeedResponse){
                    return null;
                }
                System.out.println(response);
            }
            return response;
        }finally {
            //关闭http连接
            try{
                if(httpClient != null){
                    httpClient.close();
                }
            }catch (Exception e){
                System.out.println("关闭http连接异常!");
            }
        }
    }

    public static String httpDelete(String url, String parameter, String headerName,String headerValue,boolean noNeedResponse) throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            //定义url编码格式
            url = URLDecoder.decode(url, "UTF-8");
            //创建http客户端

            //定义返回对象
            String response = null;
            //创建http post请求对象
            HttpDelete httpDelete = new HttpDelete(url);
            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(300).build();
            httpDelete.setConfig(requestConfig);
            httpDelete.setHeader(headerName,headerValue);

            //发送请求
            HttpResponse httpResponse = httpClient.execute(httpDelete);
            //如果返回码==200,则输出结果
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                response = EntityUtils.toString(httpResponse.getEntity());
                //noNeedResponse=true则返回null,不打印结果
                if (noNeedResponse){
                    return null;
                }
                System.out.println(response);
            }
            return response;
        }finally {
            //关闭http连接
            try{
                if(httpClient != null){
                    httpClient.close();
                }
            }catch (Exception e){
                System.out.println("关闭http连接异常!");
            }
        }
    }

    public static String httpPut(String url, String parameter, String headerName,String headerValue,boolean noNeedResponse) throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            //定义url编码格式
            url = URLDecoder.decode(url, "UTF-8");
            //创建http客户端

            //定义返回对象
            String response = null;
            //创建http post请求对象
            HttpPut httpPut = new HttpPut(url);
            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(300).build();
            httpPut.setConfig(requestConfig);
            httpPut.setHeader(headerName,headerValue);
            if(null != parameter){
                //定义实体对象编码格式
                StringEntity stringEntity = new StringEntity(parameter, "utf-8");
                stringEntity.setContentEncoding("UTF-8");
                //定义实体类传输类型
                stringEntity.setContentType("application/json");
                //entity.setContentType("application/x-www-form-urlencoded");
                //将实体类放入请求对象httpPost
                httpPut.setEntity(stringEntity);
            }
            //发送请求
            HttpResponse httpResponse = httpClient.execute(httpPut);
            //如果返回码==200,则输出结果
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                response = EntityUtils.toString(httpResponse.getEntity());
                //noNeedResponse=true则返回null,不打印结果
                if (noNeedResponse){
                    return null;
                }
                System.out.println(response);
            }
            return response;
        }finally {
            //关闭http连接
            try{
                if(httpClient != null){
                    httpClient.close();
                }
            }catch (Exception e){
                System.out.println("关闭http连接异常!");
            }
        }
    }

    public static String httpUpLoad(String url, File file, String headerName, String headerValue, boolean noNeedResponse) throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            //定义url编码格式
            url = URLDecoder.decode(url, "UTF-8");
            //创建http客户端

            //定义返回对象
            String response = null;
            //创建http post请求对象
            HttpPost httpPost = new HttpPost(url);
            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(300).build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader(headerName,headerValue);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addBinaryBody("file",file);
            multipartEntityBuilder.addTextBody("comment", "this is comment");

            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);

            //发送请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //如果返回码==200,则输出结果
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                response = EntityUtils.toString(httpResponse.getEntity());
                //noNeedResponse=true则返回null,不打印结果
                if (noNeedResponse){
                    return null;
                }
                System.out.println(response);
            }
            return response;
        }finally {
            //关闭http连接
            try{
                if(httpClient != null){
                    httpClient.close();
                }
            }catch (Exception e){
                System.out.println("关闭http连接异常!");
            }
        }
    }
}
