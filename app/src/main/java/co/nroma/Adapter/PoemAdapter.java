package co.nroma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import co.nroma.R;
import co.nroma.db.PoemFavoriteEntity;


/*listView适配器*/
public class PoemAdapter extends RecyclerView.Adapter<PoemAdapter.ContextHolder> {

    private ArrayList<PoemFavoriteEntity>poems;


    public PoemAdapter(ArrayList<PoemFavoriteEntity> poems){
        this.poems=poems;
    }

    /*负责加载子项的布局*/
    @NonNull
    @Override
    public ContextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false);
        ContextHolder holder=new ContextHolder(view);
        view.setOnClickListener(holder);
        return holder;
    }
    /*负责将每个子项的Holder绑定数据*/
    @Override
    public void onBindViewHolder(@NonNull ContextHolder contextHolder, int i) {
        if(i<poems.size()){
            PoemFavoriteEntity poem=poems.get(i);
            contextHolder.tv_title.setText(poem.getTitle());
            contextHolder.tv_author.setText(poem.getAuthor());
            //将换行替换为空格，扩大阅读面积，不用正则表达式也可
            String context=poem.getContent().replaceAll("\n","  ");
            //TODO 奇偶逗号句号替换
            Log.d("11111",context);
            contextHolder.tv_content.setText(context);
            /*TODO 点击获取*/
        }
    }
    //返回数据
    @Override
    public int getItemCount() {
        return poems.size();
    }

    public class ContextHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_title;
        public TextView tv_content;
        public TextView tv_author;
        public ContextHolder(@NonNull View itemView) {
            super(itemView);
            tv_author=itemView.findViewById(R.id.i_tv_author);
            tv_content=itemView.findViewById(R.id.i_tv_content);
            tv_title=itemView.findViewById(R.id.i_tv_title);
        }
        @Override
        public void onClick(View v) {
            //TODO 点击事件 HolderContext 响应点击事件,暂时不做，只做显示，
            //TODO implements View.OnClickListener 预留接口
        }
    }

}
