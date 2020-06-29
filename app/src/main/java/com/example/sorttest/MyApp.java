package com.example.sorttest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyApp extends Application {
    private static final String KEY_EXTRA_TARGET_INTENT = "EXTRA_TARGET_INTENT";
    private static final String TAG = "MyApp";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {

            Class am_class = Class.forName("android.app.ActivityManager");
            Method getTaskService = am_class.getDeclaredMethod("getService", new Class[0]);
            getTaskService.setAccessible(true);
            Object IActivityTaskManagerSingleton_instance = getTaskService.invoke(am_class.newInstance(), null);
            Class singleton_class = Class.forName("androidx.util.Singleton");
            Field mInstance = singleton_class.getDeclaredField("mInstance");
            mInstance.setAccessible(true);
            final Object mInstance_instance = mInstance.get(IActivityTaskManagerSingleton_instance);

            Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), mInstance_instance.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
            });
            Log.i(TAG, KEY_EXTRA_TARGET_INTENT);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, KEY_EXTRA_TARGET_INTENT);
        }

    }
}
