package com.example.feifei.robot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.feifei.robot.R;

/**
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
        View view = View.inflate(context, R.layout.view_item, null);
        this.addView(view);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        tv_title= (TextView) findViewById(R.id.tv_title);

    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

    public boolean isCheck(){
        return cb_box.isChecked();
    }

    public void setCheck(boolean isCheck){
        cb_box.setChecked(isCheck);
    }
}
