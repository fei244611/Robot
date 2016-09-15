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
import com.turing.androidsdk.asr.VoiceRecognizeListener;
import com.turing.androidsdk.asr.VoiceRecognizeManager;

public class ChatActivity extends AppCompatActivity {

    private Context context;
    private static final int RECOGNIZE_END=1;
    private static final int RECOGNIZE_RESULT=2;
    private static final int RECOGNIZE_ERROR=3;

    private VoiceRecognizeManager vrmanager;

    private TextView tv_recognizer;
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context=getApplicationContext();

        tv_recognizer= (TextView) findViewById(R.id.tv_recognizer);
        btn_recognizer= (Button) findViewById(R.id.btn_recognizer);

        //语音识别
        initVoice();

        btn_recognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vrmanager.startRecognize();
                btn_recognizer.setText("说话中...");
                btn_recognizer.setClickable(false);
            }
        });

    }


    /**
     * 语音识别
     * */
    private void initVoice() {

        vrmanager=new VoiceRecognizeManager(context, ContentValue.bdAPI_KEY,ContentValue.bdSECRET_KYE);

        vrmanager.setVoiceRecognizeListener(new VoiceRecognizeListener() {
            @Override
            public void onStartRecognize() {
                Log.i("Recognize", "Start1");
            }

            @Override
            public void onRecordStart() {
                Log.i("Recognize", "Start2");
            }

            @Override
            public void onRecordEnd() {
                Log.i("Recognize", "End");
                handler.sendEmptyMessage(RECOGNIZE_END);
            }

            @Override
            public void onRecognizeResult(String s) {
                if (s!=null) {
                    Log.i("Recognize","识别成功"+s);
                    handler.obtainMessage(RECOGNIZE_RESULT,s).sendToTarget();
                }else {
                    //无法识别的语音
                    handler.obtainMessage(RECOGNIZE_RESULT,"识别失败").sendToTarget();
                }
            }

            @Override
            public void onRecognizeError(String s) {
                Log.i("Recognize","识别错误"+s);
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
