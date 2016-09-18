package com.example.feifei.robot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.feifei.robot.R;
import com.example.feifei.robot.Service.RobotService;
import com.example.feifei.robot.util.ContentValue;
import com.example.feifei.robot.util.SPUtil;
import com.example.feifei.robot.view.ItemView;


public class SettingFragment extends Fragment {
    private Context context;
    private ItemView setting_desk;
    private ItemView setting_chat;


    public SettingFragment() {
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
        View rootView = inflater.inflate(R.layout.activity_setting, container, false);
        setting_desk= (ItemView) rootView.findViewById(R.id.setting_desk);
        setting_chat= (ItemView) rootView.findViewById(R.id.setting_chat);
        setting_desk.setTitle("开启宠物桌面显示");
        setting_chat.setTitle("开启智能语音聊天");
        final Boolean desk= SPUtil.getBoolean(context, ContentValue.SETTING_DESK, false);
        Boolean chat=SPUtil.getBoolean(context, ContentValue.SETTING_CHAT, false);
        setting_desk.setCheck(desk);
        setting_chat.setCheck(chat);

        setting_desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check=setting_desk.isCheck();
                setting_desk.setCheck(!check);
                if (!check) {
                    context.startService(new Intent(context, RobotService.class));
                }else {
                    context.stopService(new Intent(context, RobotService.class));
                }
            }
        });
        setting_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check=setting_chat.isCheck();
                setting_chat.setCheck(!check);
            }
        });

        // Inflate the layout for this fragment
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
