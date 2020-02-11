package co.nroma.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatDelegate;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import co.nroma.db.DaoMaster;
import co.nroma.db.DaoSession;

public class APP extends Application {
    private static APP app;
    public DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        /*讯飞ID*/
        SpeechUtility.createUtility(APP.this, SpeechConstant.APPID+"=5e02ff99");
        app=this;
        daoSession=initGreenDao();
    }
    public static  APP getInstance(){
        return app;
    }

    /*初始化GreenDao return DaoSession*/
    private DaoSession initGreenDao(){
        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"main.db");
        SQLiteDatabase sqLiteDatabase=helper.getWritableDatabase();
        DaoMaster daoMaster=new DaoMaster(sqLiteDatabase);
        return daoMaster.newSession();
    }
    public void setNightMode(boolean isE){
        if(isE){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }


}
