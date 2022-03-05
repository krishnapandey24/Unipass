package com.omnicoder.unipass.Services;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.omnicoder.unipass.Activities.BackupAndRestoreActivity;
import com.omnicoder.unipass.Activities.MyApplication;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AutoBackupService extends Service {
    private static String sprf;
    Dialog dialog;

    public String getSprf() {
        return sprf;
    }

    public void setSprf(String sprf) {
        AutoBackupService.sprf = sprf;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Dialog dialog= new Dialog(this);
        dialog.setContentView(R.layout.something_went_wrong);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.dialog_bg));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button okButton= dialog.findViewById(R.id.OkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            Date currentDate = new Date();
            SharedPreferences sharedPreferences = getSharedPreferences("autoBackupData", MODE_PRIVATE);
            Date previousDate = sdf.parse(sharedPreferences.getString("Date",sdf.format(currentDate)));
            String autoBackupDelay = sharedPreferences.getString("selectedDelayOption", "Weekly");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(previousDate);
            Log.d("Service", "Service is running.....");
            switch (autoBackupDelay) {
                case "Daily":
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                case "Weekly":
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                    break;
                case "Monthly":
                    calendar.add(Calendar.MONTH, 1);
                    break;
                case "Year":
                    calendar.add(Calendar.YEAR, 1);

            }
            Log.d("Service","the new sprf"+sprf);

            if (currentDate.equals(calendar.getTime()) || currentDate.after(calendar.getTime())) {
                BackupDatabase();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Date", currentDate.toString());
                editor.apply();
                Log.d("Service", "Service is working....");
                Log.d("Service", "Service result: " + sharedPreferences.getString("Date", "Failed!"));

            }
            else{
                Log.d("Service", "Its not the right time yet...");

            }

        } catch (ParseException e){
            e.printStackTrace();
            Log.d("Service","Parse Exception!");
        }
        Log.d("Service","Service Completed successfully");
    }

    public void BackupDatabase(){
        try {
            File BackupFolder = new File(Environment.getExternalStorageDirectory(), "Unipass");
            File backupFile = new File(BackupFolder.getAbsolutePath(), "unipass.db");
            File DBFile = new File(getDatabasePath("texts_db").getAbsolutePath());
            if (!BackupFolder.exists()) {
                BackupFolder.mkdirs();
            }
            byte[] dbFileBytes = new byte[(int) DBFile.length()];
            FileInputStream fileInputStream = new FileInputStream(DBFile);
            fileInputStream.read(dbFileBytes);
            byte[] encryptedDBBytes = Crypter.encryptFile(dbFileBytes, sprf);
            FileOutputStream fileOutputStream = new FileOutputStream(backupFile);
            fileOutputStream.write(encryptedDBBytes);
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Service","Its happening here!");


        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
