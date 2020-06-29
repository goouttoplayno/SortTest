package com.example.sorttest;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class ActivityManagerDelegate implements InvocationHandler {
    private static final String TAG = "ActivityManagerDelegate";
    private Object mObject;
    private Context mContext;

    public ActivityManagerDelegate(Object mObject, Context mContext) {
        this.mObject = mObject;
        this.mContext = mContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("startActivity")) {
            Intent intent = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    intent = (Intent) args[i];

                    Intent mIntent = new Intent();
                    ComponentName componentName = new ComponentName(mContext, PlaceHolderActivity.class);

                    mIntent.setComponent(componentName);
                    mIntent.putExtra("realObj", intent);

                    args[i] = mIntent;
                }
            }
        }
        return method.invoke(mObject, args);
    }
}
