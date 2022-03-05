package com.omnicoder.unipass.Activities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.Others.DoClass;
import com.omnicoder.unipass.Services.AutoBackupService;
import com.omnicoder.unipass.Services.AutoLockService;


import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

public class MyApplication extends Application implements LifecycleObserver {
    protected String sprf;
    DoClass Do;

    @Override
    public void onCreate() {
        super.onCreate();
        Do= new DoClass();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        Log.d("Applications","App has started!");
        setupActivityListener();




    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        Log.d("Applications","App has gone background"+Do.isAutoLockTimeout());
        Log.d("Applications","First Time"+Do.isFirstTime()+"Auto Lock Timeout"+Do.isAutoLockTimeout());
        boolean autoLock= getSharedPreferences("Settings",MODE_PRIVATE).getBoolean("autoLockSwitch",false);
        if(autoLock) {
            if (!Do.isAutoLockTimeout()) {
                startService(new Intent(MyApplication.this, AutoLockService.class));
            } else {
                setSPRF(null);
            }
        }


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        Log.d("Applications","First Time"+Do.isFirstTime()+"Auto Lock Timeout"+Do.isAutoLockTimeout());
        if(!Do.isFirstTime() && Do.isAutoLockTimeout()){
            startActivity(new Intent(MyApplication.this, LockActivity.class));

        }
        else{
            stopService(new Intent(MyApplication.this,AutoLockService.class));
        }
        Log.d("Applications","App has came foreground");

    }

    protected String getSPRF() {
        Log.d("Service","sprf we sent  got!"+sprf);

        return sprf;
    }

    protected void setSPRF(String sprf) {
        Log.d("Service","sprf we got!"+sprf);
        this.sprf = sprf;
    }


    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if(getSharedPreferences("Settings",MODE_PRIVATE).getBoolean("blockScreenshots",false)) {
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }
    
    
    public boolean confirmPassword(Context context,String password){
        Log.d("Service","comfirm password; "+password);
        boolean Password= false;
        DBHandler db= new DBHandler(context);
        String text= db.getFirstSPRF();
        try{
            Crypter.decrypt(text,password);
            Password= true;
            Log.d("Service","Password is not wrong!");
            setSPRF(password);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Password;
    }
    



}
