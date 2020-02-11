package co.nroma.tts;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class TTSUtil {
    //所有发音人
    public final static  String[] COLOUD_VOICERS_VALUE = {"xiaoyan", "xiaoyu", "catherine", "henry", "vimary", "vixy", "xiaoqi", "vixf", "xiaomei","xiaolin", "xiaorong", "xiaoqian", "xiaokun", "xiaoqiang", "vixying", "xiaoxin", "nannan", "vils",};
    //语音合成对象
    private static SpeechSynthesizer mTts;
    //上下文
    private Context mContext;
    //状态标记
    private volatile static TTSUtil instance;

    /*回调监听*/
    private static SynthesizerListener mTtsListener=new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            //开始
        }
        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            //缓冲进度
        }
        @Override
        public void onSpeakPaused() {
            //暂停
        }
        @Override
        public void onSpeakResumed() {
            //继续
        }
        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            //合成进度
        }
        @Override
        public void onCompleted(SpeechError speechError) {
            //完成
        }
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
    /*
    * 构造方法
    * 监听
    * */
    private TTSUtil(Context context){
        mContext=context;
        mTts=SpeechSynthesizer.createSynthesizer(mContext, new InitListener() {
            @Override
            public void onInit(int i) {
                //TODO
                if(1!=ErrorCode.SUCCESS){
                    Log.d("TTSUtil","cshsb:"+i);
                }
                Log.d("TTSUtil","cshsbq;"+i);
            }
        });
    }
    /*
    *判断标记，暂时用不到，
    * 功能：传入Context，传到构造方法那里
    * */
    public static TTSUtil getInstance(Context context){
        if (instance==null){
            synchronized (TTSUtil.class){
                if (instance==null){
                    instance=new TTSUtil(context);
                }
            }
        }
        return instance;
    }
    /*停止语音合成*/
    public static void stopSpeaking(){
        //对象非空and说话
        if(null!=mTts && mTts.isSpeaking()){//内置方法
            mTts.stopSpeaking();//内置方法
        }
    }
    /*判断没有语音合成*/
    public static boolean isSpeaking(){
        if(null!=mTts){//说话
            return mTts.isSpeaking();
        }else {
            return false;
        }
    }
    /*开始合成*/
    public void speaking(String text){
        if(TextUtils.isEmpty(text))
            return;
        int code =mTts.startSpeaking(text,mTtsListener);//合成 返回状态和回调都不判断了，没啥用
        //TODO 判断

    }
    /*参数*/
    private void setParam(){
        //清空参数
        mTts.setParameter(SpeechConstant.PARAMS,null);
        // 引擎类型 网络
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, COLOUD_VOICERS_VALUE[0]);
        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        // 设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "100");
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
    }




}
