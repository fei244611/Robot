package com.example.feifei.robot.activity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.feifei.robot.R;
import com.example.feifei.robot.model.ChatMessage;
import com.example.feifei.robot.util.ContentValue;
import com.example.feifei.robot.util.SPUtil;
import com.example.feifei.robot.util.TRUtil;
import com.example.feifei.robot.util.TTSUtil;
import com.example.feifei.robot.util.VoiceUtil;
import com.example.feifei.robot.view.ChatMessageAdapter;
import com.turing.androidsdk.TuringApiManager;
import com.turing.androidsdk.asr.VoiceRecognizeManager;
import com.turing.androidsdk.tts.TTSManager;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private Context context;
    private static final String TAG="ChatActivity";
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

    private ListView lv_chat;
    private EditText et_recognizer;
    private Button btn_send;
    private Button btn_recognizer;
    private List<ChatMessage> mDatas = new ArrayList<ChatMessage>();
    private ChatMessageAdapter mAdapter;
    private ChatMessage chatMessage;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RECOGNIZE_END:
                    btn_recognizer.setText("识别中");
                    break;
                case RECOGNIZE_RESULT:
                    //完成语音识别并将消息发送给图灵
                    btn_recognizer.setText("开始识别");
                    btn_recognizer.setClickable(true);
                    String result= (String) msg.obj;
                    //更新listView
                    chatMessage=new ChatMessage(ContentValue.OUTPUT,result);
                    mDatas.add(chatMessage);
                    mAdapter.notifyDataSetChanged();

                    mTuringApiManager.requestTuringAPI(result);
                    break;
                case RECOGNIZE_ERROR:
                    btn_recognizer.setText("开始识别");
                    btn_recognizer.setClickable(true);
                    break;
                case RESULT_OK:
                    //接受图灵回复消息并将文本转化为语音
                    String from=(String)msg.obj;
                    //更新listView
                    chatMessage=new ChatMessage(ContentValue.INPUT,from);
                    mDatas.add(chatMessage);
                    mAdapter.notifyDataSetChanged();
                    if (SPUtil.getBoolean(context,ContentValue.SETTING_CHAT,true)) {
                        ttsManager.startTTS(from);
                    }
                    break;
                case SPEAK_OK:
                    break;
            }
        }
    };


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        context=getActivity();

        lv_chat= (ListView) rootView.findViewById(R.id.lv_chat);
        et_recognizer= (EditText) rootView.findViewById(R.id.et_recognizer);
        btn_send= (Button) rootView.findViewById(R.id.btn_send);
        btn_recognizer= (Button) rootView.findViewById(R.id.btn_recognizer);

        //初始化语音识别
        mVoiceRecognizeManager= VoiceUtil.initVoice(context, handler);
        //初始化语音合成
        ttsManager= TTSUtil.initTTS(context, handler);
        //初始化图灵
        mTuringApiManager= TRUtil.InitTRapi(context, handler);
        //设置listview适配对象
        mDatas.add(new ChatMessage(ContentValue.INPUT,"你好啊，有什么想问的吗?"));
        mAdapter=new ChatMessageAdapter(context,mDatas);
        lv_chat.setAdapter(mAdapter);

        //文本聊天
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新ListView
                String str=et_recognizer.getText().toString();
                chatMessage=new ChatMessage(ContentValue.OUTPUT,str);
                mDatas.add(chatMessage);
                mAdapter.notifyDataSetChanged();

                et_recognizer.setText("");
                mTuringApiManager.requestTuringAPI(str);
            }
        });

        //语音聊天
        btn_recognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceRecognizeManager.startRecognize();
                btn_recognizer.setText("说话中...");
                btn_recognizer.setClickable(false);
            }
        });

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
