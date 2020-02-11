package co.nroma.utils.http;

import android.graphics.Bitmap;

public class HttpResponseData {
    public String content;
    public Bitmap bitmap;
    public String cookie;
    public String err_msg;
    public int status_code;//状态码
    public boolean success;//成功

    public HttpResponseData(){
        content="";//获取到的内容字符串
        bitmap=null;
        cookie="";
        err_msg="";
        status_code=0;
        success=true;
    }
}
