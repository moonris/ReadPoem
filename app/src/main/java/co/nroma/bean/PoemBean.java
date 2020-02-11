package co.nroma.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class PoemBean {
    private static final String TAG = "PoemBean";

    private long id;
    private String title="";
    private String author="";
    private ArrayList<String> content =new ArrayList<>();
    public PoemBean(){}

    public PoemBean(long id,String author,String title,String csvContent){
        this.setId(id);
        this.setAuthor(author);
        this.setTitle(title);

        //TODO
        this.setContent(csvContent);
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public ArrayList<String> getContent() { return content; }


    public void setContent(String csvcontext) {
        //分割字符串，放在数组里
        String[] contentList = csvcontext.split(",");
        //数组转集合
        this.content.addAll(Arrays.asList(contentList));
    }
    public void setContent(JSONArray contentArray){
        this.content.clear();//清空字符串
        for(int i=0;i<contentArray.length();i++){
            try{
                this.content.add(contentArray.getString(i));
            }catch (JSONException ex){
                //TODO
            }
        }
    }

    public static PoemBean parsejsonString(String jsopString){
        PoemBean poemBean=new PoemBean();
        try{
            JSONObject jsonObject=new JSONObject(jsopString);
            poemBean.setId(jsonObject.getInt("id"));
            poemBean.setAuthor(jsonObject.getString("author"));
            poemBean.setTitle(jsonObject.getString("title"));
            poemBean.setContent(jsonObject.getString("content"));
        }catch (JSONException ex){
            //Log("PoemBean",ex.getMessage());
        }
        return poemBean;
    }


}
