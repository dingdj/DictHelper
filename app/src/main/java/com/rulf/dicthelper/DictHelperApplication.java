package com.rulf.dicthelper;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * description: <br/>
 * author: dingdongjin_91<br/>
 * date: 2015/6/18<br/>
 */
public class DictHelperApplication extends Application {

    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
    }


    public void addRequstToQueue(Request request) {
        requestQueue.add(request);
    }

}
