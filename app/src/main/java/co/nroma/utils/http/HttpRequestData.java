package co.nroma.utils.http;

public class HttpRequestData {
    public String url;
    public String cookie;
    public String referer;//来源
    public String content_type;//内容类型
    public String x_requested_with;
    public StringBuffer params;//参数
    public String charset;//字符集
    public String boundary;//边界

    public HttpRequestData(){
        url = "";
        cookie = "";
        referer = "";
        content_type = "";
        x_requested_with = "";
        params = new StringBuffer();
        charset = "utf-8";
        boundary = "";
    }
    public HttpRequestData(String url){
        this();
        this.url=url;
    }

}
