package com.omnicoder.unipass.Services;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.omnicoder.unipass.Activities.MyApplication;
import com.omnicoder.unipass.Others.DoClass;

public class AutoLockService extends Service {
    public AutoLockService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DoClass Do= new DoClass();
        Log.d("Applications","Auto Lock service has started!");
        SharedPreferences sharedPreferences= getSharedPreferences("AutoLock",MODE_PRIVATE);
        long autoLockDelay= sharedPreferences.getInt("autoLockDelay",10) * 1000;
        CountDownTimer countDownTimer= new CountDownTimer(autoLockDelay,10) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Do.setAutoLockTimeout(true);

            }
        };
        countDownTimer.start();

    }
}