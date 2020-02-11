package co.nroma.utils.http;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class HttpRequestUtil {
    public String content;
    public Bitmap bitmap;
    public String cookie;
    public String err_msg;
    public int status_code;
    public boolean success;

    //get文本数据
    public static HttpResponseData getData(HttpRequestData requestData){
        HttpResponseData responseData=new HttpResponseData();
        try {
            //1.定义需要访问的地址
            URL url=new URL(requestData.url);
            //2.与服务器建立连接
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            //3.设置请求方式
            connection.setRequestMethod("GET");
            //4.设置连接超时的时间
            connection.setConnectTimeout(5000);
            //5.设置传递数据超时的时间
            connection.setReadTimeout(10000);
            //6.设置Http请求头 (Accept：告诉服务器，客户端支持的数据类型,*/* 任意类型)
            connection.setRequestProperty("Accept","*/*");
            /*通过逗号分割来携带多国语言。第一个会是首选的语言，其它语言会携带一个“q”值，来表示用户对该语言的喜好程度（0~1）*/
            connection.setRequestProperty("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
            /*"User_Agent"特征字符串,用来让网络协议的对端来识别发起请求的用户代理软件的应用类型、操作系统、软件开发商以及版本号*/
            /*Mozilla 现在一般浏览器都带*/
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
            /*告诉服务器,我可以解压这些格式的数据*/
            connection.setRequestProperty("Accept-Encoding","gzip,deflate");
            /*如果字符串有类型，定义内容类型*/
            if (requestData.content_type.equals("") != true) {
                connection.setRequestProperty("Content-Type", requestData.content_type);
            }
            /*定义该请求是同步还是异步*/
            if (requestData.x_requested_with.equals("") != true) {
                connection.setRequestProperty("X-Requested-With", requestData.x_requested_with);
            }
            /*告诉服务器来源*/
            if (requestData.referer.equals("") != true) {
                connection.setRequestProperty("Referer", requestData.referer);
            }
            /*设置Cookie*/
            if (requestData.cookie.equals("") != true) {
                connection.setRequestProperty("Cookie", requestData.cookie);
            }
            //7.true后可以使用getOutputStream().write() ，这样就可以向服务器写数据了;
            //connection.setDoOutput(true),将导致请求以post方式提交,即使设置了connection.setRequestMethod("GET")也没有卵用;坑了我几天
            //8.可以使用getInputStream().read() 方法，这样就可以获得服务器返回的数据
            connection.setDoInput(true);
            //9.连接
            connection.connect();

            responseData.content=getUnzipStream(connection.getInputStream(),
                    connection.getHeaderField("Content-Encoding"),
                    requestData.charset);
            //11.cookie,取到Resp返回的Header信息
            responseData.cookie=connection.getHeaderField("Set-Cookie");
            //12.status_code 返回状态码
            responseData.status_code=connection.getResponseCode();
            //13.切断连接
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseData;
    }
    /*数据处理封装
    * 功能有二
    * 1.普通inputStrean
    * 2.zip格式
    * */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
    public static String getUnzipStream(InputStream is, String content_encoding, String charset) {
        String resp_content = "";
        GZIPInputStream gzin = null;
        if (content_encoding != null && content_encoding.equals("") != true) {
            if (content_encoding.indexOf("gzip") >= 0) {
                try {
                    gzin = new GZIPInputStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (gzin == null) {
                resp_content = new String(readInputStream(is), charset);
            } else {
                resp_content = new String(readInputStream(gzin), charset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp_content;
    }
    /*
    * 封装连接
    *
    */

}
