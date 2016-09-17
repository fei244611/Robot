package com.example.feifei.robot.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;

/**
 * Created by feifei on 16-9-17.
 */
public class TRUtil {
    private static final String TAG="TRUtil";
    private static final int RESULT_OK=4;
    private static TuringApiManager mTuringApiManager;

    public static TuringApiManager InitTRapi(final Context context,final Handler handler){

            //1.初始化sdkInit
            SDKInitBuilder builder = new SDKInitBuilder(context)
                    .setSecret(ContentValue.trSECRET_KEY).setTuringKey(ContentValue.trAPI_KEY)
                    .setUniqueId(ContentValue.trUNIQUE_ID);

            SDKInit.init(builder, new InitListener() {
                @Override
                public void onFail(String error) {
                    Log.i(TAG, error);
                }

                @Override
                public void onComplete() {
                    Log.i(TAG, "SDK Complete");
                    // 2.请求图灵服务器，需要请求必须在此回调成功，才可正确请求
                    mTuringApiManager = new TuringApiManager(context);
                    mTuringApiManager.setHttpListener(new HttpConnectionListener() {
                        @Override
                        public void onError(ErrorMessage errorMessage) {

                        }

                        @Override
                        public void onSuccess(RequestResult result) {
                            if (result != null) {
                                try {
                                    Log.i(TAG, result.getContent().toString());
                                    //解析图灵服务器返回消息
                                    JSONObject result_obj = new JSONObject(result.getContent().toString());
                                    if (result_obj.has("text")) {
                                        Log.i(TAG, result_obj.get("text").toString());
                                        handler.obtainMessage(RESULT_OK, result_obj.get("text")).sendToTarget();
                                    }
                                } catch (JSONException e) {
                                    Log.i(TAG, "JSONException:" + e.getMessage());
                                }
                            }
                        }
                    });
                }
            });

        return mTuringApiManager;
    }


}
