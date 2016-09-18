package com.example.feifei.robot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.feifei.robot.R;

/**
 * 系统设置栏目View
 * Created by feifei on 16-9-16.
 */
public class ItemView extends RelativeLayout{
    private static final String tag = "SettingItemView";
    private CheckBox cb_box;
    private TextView tv_title;

    public ItemView(Context context) {

        super(context);

    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_item, this);

        cb_box = (CheckBox) findViewById(R.id.cb_setting);
        tv_title= (TextView) findViewById(R.id.tv_setting);
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

    public void setvisable(){
        cb_box.setVisibility(INVISIBLE);
    }

    public boolean isCheck(){
        return cb_box.isChecked();
    }

    /**
     * 设置栏目点击与单选框状态同步
     * @param isCheck   单选框状态
     */
    public void setCheck(boolean isCheck){
        cb_box.setChecked(isCheck);
    }
}
