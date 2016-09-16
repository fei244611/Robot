package com.example.feifei.robot.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.feifei.robot.R;
import com.example.feifei.robot.Service.RobotService;
import com.example.feifei.robot.util.ContentValue;
import com.example.feifei.robot.util.SPUtil;
import com.example.feifei.robot.view.ItemView;

public class SettingActivity extends AppCompatActivity {
    private Context context;
    private ItemView setting_desk;
    private ItemView setting_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_setting);
        setting_desk= (ItemView) findViewById(R.id.setting_desk);
        setting_chat= (ItemView) findViewById(R.id.setting_chat);
        setting_desk.setTitle("开启宠物桌面显示");
        setting_chat.setTitle("开启智能语音聊天");
        final Boolean desk=SPUtil.getBoolean(context, ContentValue.SETTING_DESK, false);
        Boolean chat=SPUtil.getBoolean(context, ContentValue.SETTING_CHAT, false);
        setting_desk.setCheck(desk);
        setting_chat.setCheck(chat);

        setting_desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check=setting_desk.isCheck();
                setting_desk.setCheck(!check);
                if (!check) {
                    startService(new Intent(context, RobotService.class));
                }else {
                    stopService(new Intent(context, RobotService.class));
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
