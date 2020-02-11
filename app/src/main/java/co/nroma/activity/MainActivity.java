package co.nroma.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.nroma.R;
import co.nroma.app.APP;
import co.nroma.bean.PoemBean;
import co.nroma.db.PoemFavoriteEntity;
import co.nroma.db.PoemFavoriteEntityDao;
import co.nroma.menu.mainMenu;
import co.nroma.tts.TTSUtil;
import co.nroma.utils.http.HttpRequestData;
import co.nroma.utils.http.HttpRequestUtil;
import co.nroma.utils.http.HttpResponseData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ct1_toolbar)
    CollapsingToolbarLayout ctlToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.sv_context)
    ScrollView svContext;
    @BindView(R.id.fab_refresh)
    FloatingActionButton fabRefresh;
    @BindView(R.id.fab_favorite)
    FloatingActionButton fabFavorite;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab_TTS)
    FloatingActionButton fabTts;

    private PoemBean Poem;
    public StringBuffer joinContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);//状态栏透明
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);//在Activity中绑定ButterKnife
        ViewCompat.setNestedScrollingEnabled(svContext,true);
        setSupportActionBar(toolbar);//使用ToolBar控件替代ActionBar控件
        initPermission();
        //设置初始古诗
        setPoem(new PoemBean(0,
                getString(R.string.main_author),
                getString(R.string.main_title),
                getString(R.string.main_context).replace('\n',',')
        ));//可有可无，因为我在xml设置过
    }
    @OnClick({R.id.fab_refresh,R.id.fab_favorite,R.id.fab_TTS})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.fab_refresh:
                /*刷新*/
                new GetPoemTask().execute();
                break;
            case R.id.fab_favorite:
                /*收藏*/
                PoemFavoriteEntityDao poemFavoriteEntityDao= APP.getInstance().daoSession.getPoemFavoriteEntityDao();
                //PoemFavoriteEntityDao
                PoemFavoriteEntity poemFavoriteEntity=new PoemFavoriteEntity(
                        Poem.getId(),
                        Poem.getTitle(),
                        Poem.getAuthor(),
                        joinContent.toString()
                );
                /*查询ID*/
                if (poemFavoriteEntityDao.load(poemFavoriteEntity.getId())==null){
                    poemFavoriteEntityDao.insert(poemFavoriteEntity);
                    //TODO Snackbar.make()
                }else{
                    poemFavoriteEntityDao.delete(poemFavoriteEntity);
                }
                break;
            case R.id.fab_TTS:
                /*朗读*/
                final String text=Poem.getTitle()+","+Poem.getAuthor()+","+Poem.getContent();
                TTSUtil.getInstance(getApplicationContext()).speaking(text);
                break;
        }
    }
    /*
    * UI上设置当前古诗
    * */
    private void setPoem(PoemBean poem){
        Poem=poem;
        tvAuthor.setText(poem.getAuthor());
        tvTitle.setText(poem.getTitle());
        joinContent =new StringBuffer();
        for(String s:poem.getContent()){
            joinContent.append(s).append("\n");
        }
        joinContent.deleteCharAt(joinContent.length()-1);
        tvContent.setText(joinContent.toString());
    }

    //选项菜单（optinosMenu）创建
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 此方法用于初始化菜单，其中menu参数就是即将要显示的Menu实例。
         * 返回true则显示该menu,false则不显示;
         * (只会在第一次初始化菜单时调用) Inflate the menu; this adds items to the action bar
         * if it is present.
         */
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    //菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * 菜单项被点击时调用，也就是菜单项的监听方法。
         * 对于Activity，同一时间只能显示和监听一个Menu 对象。
         * method stub
         */
        return new mainMenu(this).onOptionsItemSelected(item);
    }
    //加载古诗任务
    class GetPoemTask extends AsyncTask<Void,Integer,HttpResponseData>{
        //@Void：开始异步任务执行时传入的参数类型，对应excute（）中传递的参数
        //@Integer：异步任务执行过程中，返回下载进度值的类型
        //@HttpResponseData：异步任务执行完成后，返回的结果类型，与doInBackground()的返回值类型保持一致
        // 方法1：doInBackground（）
        // 作用：接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果，也就是古诗字符串
        // 注：必须复写，从而自定义线程任务
        @Override
        protected HttpResponseData doInBackground(Void... voids){
            HttpRequestData requestData=new HttpRequestData("https://dc.deali.cn/api/poem/tang");
            return HttpRequestUtil.getData(requestData);//TODO
        }
        // 方法2：onPostExecute（）
        // 作用：接收线程任务执行结果、将古诗显示到UI组件
        // 注：必须复写，从而自定义UI操作
        @Override
        protected void onPostExecute(HttpResponseData data){
            super.onPostExecute(data);
            if(data.success){
                try {
                    JSONObject jsonObject=new JSONObject(data.content);
                    /*如果JSONObjct对象中的value是一个JSONObject对象，即根据key获取对应的JSONObject对象*/
                    PoemBean poemBean=PoemBean.parsejsonString(jsonObject.getJSONObject("data").toString());
                    poemBean.setContent(jsonObject.getJSONObject("data").getJSONArray("content"));
                    //TODO 缓存
                    setPoem(poemBean);
                }catch (JSONException ex){
                    Log.d("JSON",ex.getMessage());
                }
            }else
                Snackbar.make(coordinatorLayout,"网络连接失败",Snackbar.LENGTH_LONG).show();
        }
    }

    //动态权限组
    String [] permissions =new String[]{Manifest.permission.INTERNET,
                                        Manifest.permission.ACCESS_NETWORK_STATE,
                                        Manifest.permission.ACCESS_WIFI_STATE,
                                        Manifest.permission.CHANGE_NETWORK_STATE,
                                        Manifest.permission.READ_PHONE_STATE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        };
    List<String> mPermissions=new ArrayList<>();
    private static final int req=1;
    private void initPermission(){
        mPermissions.clear();
        /*判断权限未授予*/
        for (int i=0;i<permissions.length;i++){
                /* //第一个参数Context,第二个参数需要检查的权限
                //被授权返回0，否则返回-1*/
            if(ContextCompat.checkSelfPermission(this,permissions[i])!= PackageManager.PERMISSION_GRANTED)
                mPermissions.add(permissions[i]);
        }
        /*判断是否为空*/
        if(mPermissions.isEmpty()){
        }else{
            String [] permissions=mPermissions.toArray(new String[mPermissions.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,req);
        }
    }
    //权限返回，TODO 这里不管用户是否拒绝，都进入首页，不再重复申请权限

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode){
            case req:
                //TODO
                break;
            default:
                //TODO
                break;
        }
    }

}
