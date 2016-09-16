package com.example.feifei.robot.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.turing.androidsdk.asr.VoiceRecognizeListener;
import com.turing.androidsdk.asr.VoiceRecognizeManager;


/**
 * Created by feifei on 16-9-16.
 */
public class VoiceUtil {

    private static final int RECOGNIZE_END=1;
    private static final int RECOGNIZE_RESULT=2;
    private static final int RECOGNIZE_ERROR=3;
    private static final String TAG="VoiceUtil";

    public static VoiceRecognizeManager initVoice(Context context, final Handler handler){
        VoiceRecognizeManager manager=new VoiceRecognizeManager(context,ContentValue.bdAPI_KEY,ContentValue.bdSECRET_KYE);
        manager.setVoiceRecognizeListener(new VoiceRecognizeListener() {
            @Override
            public void onStartRecognize() {
                Log.i(TAG,"Recognize Start1");
            }

            @Override
            public void onRecordStart() {
                Log.i(TAG,"Recognize Start1");
            }

            @Override
            public void onRecordEnd() {
                Log.i(TAG,"Recognize End");
                handler.sendEmptyMessage(RECOGNIZE_END);
            }

            @Override
            public void onRecognizeResult(String s) {
                Log.i(TAG,"识别结果"+s);
                if (s!=null) {
                    handler.obtainMessage(RECOGNIZE_RESULT, s).sendToTarget();
                }else {
                    handler.obtainMessage(RECOGNIZE_RESULT,"识别失败").sendToTarget();
                }
            }

            @Override
            public void onRecognizeError(String s) {
                Log.i(TAG,"识别失败"+s);
                if (s!=null) {
                    handler.obtainMessage(RECOGNIZE_ERROR, s).sendToTarget();
                }
            }

            @Override
            public void onVolumeChange(int i) {

            }
        });
        return manager;
    }

}
