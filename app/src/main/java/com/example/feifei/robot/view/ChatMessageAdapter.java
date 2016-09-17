package com.example.feifei.robot.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.feifei.robot.R;
import com.example.feifei.robot.model.ChatMessage;
import com.example.feifei.robot.util.ContentValue;

import java.util.List;

/**
 * Created by feifei on 16-9-16.
 */
public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;
    private Context context;

    public ChatMessageAdapter(Context context, List<ChatMessage> datas)
    {
        this.context=context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取ListView 包含的view类别总数
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * 获取当前栏目的view类别
     * */
    @Override
    public int getItemViewType(int position) {
        ChatMessage msg=mDatas.get(position);
        return msg.getType().equals(ContentValue.OUTPUT) ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = mDatas.get(position);
        ViewHolder viewHolder;
        //复用convertView和对应组件
        if (convertView==null){
            int type = getItemViewType(position);
            if (type==1) {
                viewHolder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.message_right,null);
                viewHolder.tv_createDate = (TextView) convertView.findViewById(R.id.rtime);
                viewHolder.tv_content = (TextView) convertView.findViewById(R.id.rtv);
                viewHolder.iv_image= (ImageView) convertView.findViewById(R.id.riv);
                //设置view标识
                convertView.setTag(viewHolder);
            }else {
                viewHolder=new ViewHolder();
                convertView=mInflater.inflate(R.layout.message_left,null);
                viewHolder.tv_createDate = (TextView) convertView.findViewById(R.id.ltime);
                viewHolder.tv_content = (TextView) convertView.findViewById(R.id.ltv);
                convertView.setTag(viewHolder);
            }
        }else {
            //获取view标识
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_content.setText(chatMessage.getMsg());
        viewHolder.tv_createDate.setText(chatMessage.getDateStr());
        return convertView;
    }

    private class ViewHolder{
        public TextView tv_createDate;
        public TextView tv_content;
        private ImageView iv_image;
    }
}
