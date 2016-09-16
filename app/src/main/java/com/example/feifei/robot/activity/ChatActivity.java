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
import com.example.feifei.robot.util.TTSUtil;
import com.example.feifei.robot.util.VoiceUtil;
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
                    mTuringApiManager.requestTuringAPI(result);
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

        //语音识别
        mVoiceRecognizeManager=VoiceUtil.initVoice(context,handler);
        //语音合成
        ttsManager= TTSUtil.initTTS(context,handler);
        //初始化SDK
        initSDK();

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
