package com.example.feifei.robot.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.feifei.robot.R;
import com.example.feifei.robot.util.ContentValue;
import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;
import com.turing.androidsdk.asr.VoiceRecognizeListener;
import com.turing.androidsdk.asr.VoiceRecognizeManager;
import com.turing.androidsdk.tts.TTSListener;
import com.turing.androidsdk.tts.TTSManager;

import org.json.JSONException;
import org.json.JSONObject;

import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;

public class ChatActivity extends AppCompatActivity {

    private Context context;
    private static final String TAG="ChatActivity";
    private static final int RECOGNIZE_END=1;
    private static final int RECOGNIZE_RESULT=2;
    private static final int RECOGNIZE_ERROR=3;
    private static final int RESULT_OK=4;
    private static final int Speech_START=5;

    private VoiceRecognizeManager mVoiceRecognizeManager;
    private TuringApiManager mTuringApiManager;
    private TTSManager ttsManager;

    private TextView tv_recognizer;
    private TextView tv_result;
    private Button btn_recognizer;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RECOGNIZE_END:
                    btn_recognizer.setText("识别中");
                    break;
                case RECOGNIZE_RESULT:
                    btn_recognizer.setText("开始识别");
                    btn_recognizer.setClickable(true);
                    String result= (String) msg.obj;
                    tv_recognizer.setText("识别结果"+result);
                    break;
                case RECOGNIZE_ERROR:
                    btn_recognizer.setText("开始识别");
                    btn_recognizer.setClickable(true);
                    String error= (String) msg.obj;
                    tv_recognizer.setText(error);
                    break;
                case RESULT_OK:
                    tv_result.setText((String)msg.obj);
                    ttsManager.startTTS((String) msg.obj);
                    break;
                case Speech_START:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context=this;

        tv_recognizer= (TextView) findViewById(R.id.tv_recognizer);
        tv_result= (TextView) findViewById(R.id.tv_result);
        btn_recognizer= (Button) findViewById(R.id.btn_recognizer);

        //语音合成
        initTTS();
        //初始化SDK
        initSDK();
        //语音识别
        initVoice();

        btn_recognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceRecognizeManager.startRecognize();
                btn_recognizer.setText("说话中...");
                btn_recognizer.setClickable(false);
            }
        });

    }

    /**
     * 语音合成
     * */
    private void initTTS() {
        ttsManager = new TTSManager(this,ContentValue.bdAPI_KEY, ContentValue.bdSECRET_KYE);
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
    }

    /**
     * 初始化SDK
     * */
    private void initSDK() {

        SDKInitBuilder builder = new SDKInitBuilder(this)
                .setSecret(ContentValue.trSECRET_KEY).setTuringKey(ContentValue.trAPI_KEY)
                .setUniqueId(ContentValue.trUNIQUE_ID);

        SDKInit.init(builder, new InitListener() {
            @Override
            public void onFail(String error) {
                Log.i(TAG, error);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "SDK Complete");
                // 获取userid成功后，才可以请求Turing服务器，需要请求必须在此回调成功，才可正确请求
                mTuringApiManager = new TuringApiManager(context);
                mTuringApiManager.setHttpListener(myHttpConnectionListener);
            }
        });
    }

    /**
     * 网络请求回调
     */
    HttpConnectionListener myHttpConnectionListener = new HttpConnectionListener() {

        @Override
        public void onSuccess(RequestResult result) {
            if (result != null) {
                try {
                    Log.i(TAG, result.getContent().toString());
                    JSONObject result_obj = new JSONObject(result.getContent().toString());

                    if (result_obj.has("text")) {
                        Log.i(TAG, result_obj.get("text").toString());
                        handler.obtainMessage(RESULT_OK, result_obj.get("text")).sendToTarget();
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "JSONException:" + e.getMessage());
                }
            }
        }

        @Override
        public void onError(ErrorMessage errorMessage) {
            Log.i(TAG, "ERROR:" + errorMessage.getMessage());
        }
    };

    /**
     * 语音识别
     * */
    private void initVoice() {

        mVoiceRecognizeManager=new VoiceRecognizeManager(context, ContentValue.bdAPI_KEY,ContentValue.bdSECRET_KYE);

        mVoiceRecognizeManager.setVoiceRecognizeListener(new VoiceRecognizeListener() {
            @Override
            public void onStartRecognize() {
                Log.i(TAG, "Recognize Start1");
            }

            @Override
            public void onRecordStart() {
                Log.i(TAG, "Recognize Start2");
            }

            @Override
            public void onRecordEnd() {
                Log.i(TAG, "Recognize End");
                handler.sendEmptyMessage(RECOGNIZE_END);
            }

            @Override
            public void onRecognizeResult(String s) {
                Log.i(TAG,"识别成功"+s);
                if (s!=null) {
                    mTuringApiManager.requestTuringAPI(s);
                    handler.obtainMessage(RECOGNIZE_RESULT,s).sendToTarget();
                }else {
                    //无法识别的语音
                    handler.obtainMessage(RECOGNIZE_RESULT,"识别失败").sendToTarget();
                }
            }

            @Override
            public void onRecognizeError(String s) {
                Log.i(TAG,"识别错误"+s);
                if (s!=null) {
                    handler.obtainMessage(RECOGNIZE_ERROR, s).sendToTarget();
                }

            }

            @Override
            public void onVolumeChange(int i) {
                //讯飞调用
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
