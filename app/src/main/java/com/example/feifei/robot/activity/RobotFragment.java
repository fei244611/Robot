package com.example.feifei.robot.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.feifei.robot.R;
import com.example.feifei.robot.util.ContentValue;
import com.example.feifei.robot.util.SPUtil;
import com.example.feifei.robot.util.TRUtil;
import com.example.feifei.robot.util.TTSUtil;
import com.example.feifei.robot.util.VoiceUtil;
import com.turing.androidsdk.TuringApiManager;
import com.turing.androidsdk.asr.VoiceRecognizeManager;
import com.turing.androidsdk.tts.TTSManager;

import java.util.HashMap;


public class RobotFragment extends Fragment {

    private Context context;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;
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
    private TextView tv_chat;
    private ImageView iv_duola;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RECOGNIZE_END:
                    tv_chat.setText("识别中");
                    break;
                case RECOGNIZE_RESULT:
                    //完成语音识别并将消息发送给图灵
                    tv_chat.setText("");
                    btn_chat.setClickable(true);
                    String result= (String) msg.obj;
                    mTuringApiManager.requestTuringAPI(result);
                    break;
                case RECOGNIZE_ERROR:
                    tv_chat.setText("");
                    btn_chat.setClickable(true);
                    break;
                case RESULT_OK:
                    //接受图灵回复消息并将文本转化为语音
                    String from=(String)msg.obj;
                    if (SPUtil.getBoolean(context, ContentValue.SETTING_CHAT, true)) {
                        ttsManager.startTTS(from);
                    }
                    break;
                case SPEAK_OK:
                    break;
            }
        }
    };

    public RobotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=getActivity();
        View rootView = inflater.inflate(R.layout.fragment_robot, container, false);
        btn_chat= (Button) rootView.findViewById(R.id.btn_chat_robot);
        tv_chat= (TextView) rootView.findViewById(R.id.tv_chat_robot);
        iv_duola= (ImageView) rootView.findViewById(R.id.iv_duola_robot);

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(1, soundPool.load(context, R.raw.duola_start, 1));


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
                tv_chat.setText("说话中...");
                btn_chat.setClickable(false);
            }
        });

        soundPool.play(soundPoolMap.get(1),1, 1, 0, 0, 1);
        //设置view基础动画
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_duola.getDrawable();
        animationDrawable.start();
        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
