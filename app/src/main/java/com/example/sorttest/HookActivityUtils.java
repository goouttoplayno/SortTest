package com.example.sorttest;

import android.app.ActivityManager;
import android.content.Context;
import android.drm.DrmStore;
import android.os.Build;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.crypto.spec.OAEPParameterSpec;

public class HookActivityUtils {
    private static final String TAG = "HookActivityUtils";
    private volatile static HookActivityUtils sHookActivityUtils;

    public static HookActivityUtils getInstance() {
        if (sHookActivityUtils == null) {
            synchronized (HookActivityUtils.class) {
                sHookActivityUtils = new HookActivityUtils();
            }
        }
        return sHookActivityUtils;
    }

    private HookActivityUtils() {
    }

    public void hooks(Context mContext) {
        Object object;
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                Field iActivityManagerSingleton = ActivityManager.class.getDeclaredField("IActivityManagerSingleton");
                iActivityManagerSingleton.setAccessible(true);
                object = iActivityManagerSingleton.get(null);
            } else {
                Field gDefault = Class.forName("android.app.ActivityManagerNative").getDeclaredField("gDefault");
                gDefault.setAccessible(true);
                object = gDefault.get(null);
            }
            Field mFieldInstance = Class.forName("android.util.Singleton").getDeclaredField("mInstance");
            mFieldInstance.setAccessible(true);
            Object mInstance = mFieldInstance.get(object);
            ActivityManagerDelegate managerDelegate = new ActivityManagerDelegate(mInstance, mContext);
            Class<?> aClass = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(aClass.getClassLoader(), new Class<?>[]{aClass},managerDelegate);
            mFieldInstance.set(object,proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void hookHandler(){
        try {
            Class<?> aClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = aClass.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object invoke = currentActivityThread.invoke(null);
            Field mH = aClass.getDeclaredField("mH");
            mH.setAccessible(true);

            Object handler = mH.get(invoke);
            Field mCallback = Handler.class.getDeclaredField("mCallback");
            mCallback.setAccessible(true);
            mCallback.set(handler,new HookCallBack((Handler)handler));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
