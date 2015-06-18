package com.rulf.dicthelper;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

public class FloatWindowBigView extends LinearLayout {

    public static final String TAG = FloatWindowBigView.class.getSimpleName();

    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;


    public FloatWindowBigView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button close = (Button) findViewById(R.id.close);
        Button back = (Button) findViewById(R.id.back);
        final TextView tvInfo = (TextView) findViewById(R.id.tvInfo);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.removeSmallWindow(context);
                Intent intent = new Intent(getContext(), FloatWindowService.class);
                context.stopService(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.createSmallWindow(context);
            }
        });

        ClipboardManager cbm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        CharSequence c = cbm.getText();
        if(c != null) {
            String translateText = c.toString();
            String url = "https://api.shanbay.com/bdc/search/?word="+translateText;
            //获取到文字 进行翻译
            JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        Log.e(TAG, response.toString());
                        tvInfo.setText(response.optJSONObject("data")
                                .optJSONObject("cn_definition").optString("defn"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) ;
            DictHelperApplication application = (DictHelperApplication) getContext().getApplicationContext();
            application.addRequstToQueue(jsonRequest);
        }
    }

}
