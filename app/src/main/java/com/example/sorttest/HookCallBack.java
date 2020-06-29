package com.example.sorttest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;

class HookCallBack implements Handler.Callback {
    private static final String TAG = "HookCallBack";
    private Handler mHandler;

    public HookCallBack(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == 100) {
            handleHookMsg(msg);
        }
        mHandler.handleMessage(msg);
        return false;
    }

    private void handleHookMsg(Message mMsg) {
        Object obj = mMsg.obj;
        try {
            Field intent = obj.getClass().getDeclaredField("intent");

            intent.setAccessible(true);
            Intent proxyIntent = (Intent) intent.get(obj);
            Intent realIntent = proxyIntent.getParcelableExtra("realObj");
            proxyIntent.setComponent(realIntent.getComponent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
