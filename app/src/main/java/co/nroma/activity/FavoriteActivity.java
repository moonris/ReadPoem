package co.nroma.activity;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.Toolbar;

import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.nroma.Adapter.PoemAdapter;
import co.nroma.R;
import co.nroma.app.APP;
import co.nroma.db.PoemFavoriteEntity;
import co.nroma.db.PoemFavoriteEntityDao;

public class FavoriteActivity extends AppCompatActivity {

    @BindView(R.id.f_rv_content)
    RecyclerView rvContext;
    @BindView(R.id.f_srl_refresh)
    SwipeRefreshLayout swipeRefreshLayout;/*布局*/
    private ArrayList<PoemFavoriteEntity> poem_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(FavoriteActivity.this, getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
       /* android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);不好看不加了*/
        //卡片视图视图
        rvContext=findViewById(R.id.f_rv_content);
        rvContext.setItemAnimator(new DefaultItemAnimator()); //设置默认动画
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);//设置视图逆序，让显示在上面的诗永远是最新的
        linearLayoutManager.setReverseLayout(true);
        rvContext.setLayoutManager(linearLayoutManager);
        //rvContext.setItemAnimator();
        //rvContext.addOnItemTouchListener(rvContext,new
        swipeRefreshLayout.setOnRefreshListener(this::loadFavorite); //滑动刷新监听
        loadFavorite();
    }

    /*加载收藏夹*/
    private void loadFavorite() {
        PoemFavoriteEntityDao poemFavoriteEntityDao = APP.getInstance().daoSession.getPoemFavoriteEntityDao();
        poem_List = new ArrayList<PoemFavoriteEntity>();
        poem_List.addAll(poemFavoriteEntityDao.loadAll());
        rvContext.setAdapter(new PoemAdapter(poem_List));
        swipeRefreshLayout.setRefreshing(false);
    }
}
