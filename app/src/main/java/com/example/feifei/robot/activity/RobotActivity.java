package com.example.feifei.robot.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.feifei.robot.R;
import com.example.feifei.robot.model.ChatMessage;
import com.example.feifei.robot.util.ContentValue;
import com.example.feifei.robot.util.TRUtil;
import com.example.feifei.robot.util.TTSUtil;
import com.example.feifei.robot.util.VoiceUtil;
import com.turing.androidsdk.TuringApiManager;
import com.turing.androidsdk.asr.VoiceRecognizeManager;
import com.turing.androidsdk.tts.TTSManager;

public class RobotActivity extends AppCompatActivity {
    private Context context;
    //主线程更新标识
    private static final int RECOGNIZE_END=1;
    private static final int RECOGNIZE_RESULT=2;
    private static final int RECOGNIZE_ERROR=3;
    private static final int RESULT_OK=4;
    private static final int SPEAK_OK=5;

    //语音识别管理
    private VoiceRecognizeManager mVoiceRecognizeManager;
    //图灵api管理
    private TuringApiManager mTuringApiManager;
    //语音合成管理
    private TTSManager ttsManager;

    private Button btn_chat;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RECOGNIZE_END:
                    btn_chat.setText("识别中");
                    break;
                case RECOGNIZE_RESULT:
                    //完成语音识别并将消息发送给图灵
                    btn_chat.setText("开始识别");
                    btn_chat.setClickable(true);
                    String result= (String) msg.obj;
                    mTuringApiManager.requestTuringAPI(result);
                    break;
                case RECOGNIZE_ERROR:
                    btn_chat.setText("开始识别");
                    btn_chat.setClickable(true);
                    break;
                case RESULT_OK:
                    //接受图灵回复消息并将文本转化为语音
                    String from=(String)msg.obj;
                    ttsManager.startTTS(from);
                    break;
                case SPEAK_OK:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_robot);
        btn_chat= (Button) findViewById(R.id.btn_chat_robot);

        //初始化语音识别
        mVoiceRecognizeManager= VoiceUtil.initVoice(context, handler);
        //初始化语音合成
        ttsManager= TTSUtil.initTTS(context, handler);
        //初始化图灵
        mTuringApiManager= TRUtil.InitTRapi(context, handler);

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceRecognizeManager.startRecognize();
                btn_chat.setText("说话中...");
                btn_chat.setClickable(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_robot, menu);
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
