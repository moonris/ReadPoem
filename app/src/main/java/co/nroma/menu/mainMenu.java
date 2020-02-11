package co.nroma.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import co.nroma.R;
import co.nroma.activity.FavoriteActivity;
import co.nroma.app.APP;

public class mainMenu {
    private Context mContext;
    //private
    public mainMenu(Context context){
        this.mContext=context;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //我的收藏
            case R.id.menu_favorite:
                mContext.startActivity(new Intent(mContext, FavoriteActivity.class));
                return true;
            //夜间模式
            case R.id.menu_night_mode:
                APP.getInstance().setNightMode(true);
                ((AppCompatActivity)mContext).recreate();
                return true;
            //白天模式
            case  R.id.menu_daylight_mode:
                APP.getInstance().setNightMode(false);
                ((AppCompatActivity)mContext).recreate();
                return true;
            //update
            case R.id.menu_check_updates:
                Toast.makeText(mContext,"检查更新失败，暂时无法连接到服务器",Toast.LENGTH_LONG).show();
                //TODO
                return true;
            //about
            case R.id.menu_about:
                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setTitle("关于");
                //TODO
                AlertDialog dialog=builder.create();
                dialog.show();
                return true;
            default:
                return false;
        }
    }


}
