package com.omnicoder.unipass.Services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.omnicoder.unipass.Activities.ViewLoginActivity;
import com.omnicoder.unipass.Others.DoClass;

public class ClipboardCleanupService extends Service {
    public ClipboardCleanupService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
            Log.d("Clipboard","One service is running!!");
            SharedPreferences sharedPreferences= getSharedPreferences("Settings",MODE_PRIVATE);
            long cleanupDelay= sharedPreferences.getInt("cleanupDelay",30) * 1000;
            CountDownTimer countDownTimer= new CountDownTimer(cleanupDelay,10) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ClipboardManager clipboardManager= (ClipboardManager) ClipboardCleanupService.this.getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                        clipboardManager.clearPrimaryClip();
                    }


                }
            };
            countDownTimer.start();
        }


    }


