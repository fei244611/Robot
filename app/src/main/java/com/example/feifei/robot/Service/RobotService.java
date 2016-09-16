package com.example.feifei.robot.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.feifei.robot.R;

/**
 * Created by feifei on 16-9-16.
 */
public class RobotService extends Service {
    private WindowManager mWM;
    private int mScreenHeight;
    private int mScreenWidth;
    private WindowManager.LayoutParams params;
    private View mRocketView;

    @Override
    public void onCreate() {
        //获取窗体对象
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

        mScreenHeight = mWM.getDefaultDisplay().getHeight();
        mScreenWidth = mWM.getDefaultDisplay().getWidth();

        //开启火箭
        showRocket();
        super.onCreate();
    }

    private void showRocket() {
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	默认能够被触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //在响铃的时候显示吐司,和电话类型一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");

        //指定吐司的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT+Gravity.TOP;

        //定义吐司所在的布局,并且将其转换成view对象,添加至窗体(权限)

        mRocketView = View.inflate(this, R.layout.rocket_view, null);

        ImageView iv_rocket = (ImageView) mRocketView.findViewById(R.id.iv_rocket);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_rocket.getBackground();
        animationDrawable.start();

        mWM.addView(mRocketView, params);

        mRocketView.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX-startX;
                        int disY = moveY-startY;

                        params.x = params.x+disX;
                        params.y = params.y+disY;

                        //容错处理
                        if(params.x<0){
                            params.x = 0;
                        }

                        if(params.y<0){
                            params.y=0;
                        }

                        if(params.x>mScreenWidth-mRocketView.getWidth()){
                            params.x = mScreenWidth-mRocketView.getWidth();
                        }

                        if(params.y>mScreenHeight-mRocketView.getHeight()-22){
                            params.y = mScreenHeight-mRocketView.getHeight()-22;
                        }

                        //告知窗体吐司需要按照手势的移动,去做位置的更新
                        mWM.updateViewLayout(mRocketView, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        if(params.x>100 && params.x<200 && params.y>350){
                            //发射火箭

                        }
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mWM!=null && mRocketView!=null){
            mWM.removeView(mRocketView);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
