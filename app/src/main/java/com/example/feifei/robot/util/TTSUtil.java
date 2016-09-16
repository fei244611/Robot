package com.example.feifei.robot.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.turing.androidsdk.tts.TTSListener;
import com.turing.androidsdk.tts.TTSManager;

/**
 * Created by feifei on 16-9-16.
 */
public class TTSUtil {
    private static final String TAG="TTSUtil";

    public static TTSManager initTTS(Context context,final Handler handler){
        TTSManager ttsManager= new TTSManager(context,ContentValue.bdAPI_KEY, ContentValue.bdSECRET_KYE);
        ttsManager.setTTSListener(new TTSListener() {
            @Override
            public void onSpeechStart() {
                Log.i(TAG, "TTS Start!");
            }

            @Override
            public void onSpeechProgressChanged() {

            }

            @Override
            public void onSpeechPause() {
                Log.i(TAG, "TTS Pause!");
            }

            @Override
            public void onSpeechFinish() {
                Log.i(TAG, "TTS Finish!");
            }

            @Override
            public void onSpeechError(int errorCode) {
                Log.i(TAG, "TTS错误，错误码：" + errorCode);
            }


            @Override
            public void onSpeechCancel() {
                Log.i(TAG, "TTS Cancle!");
            }
        });
        return ttsManager;
    }
}
