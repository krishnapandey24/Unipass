package com.omnicoder.unipass.Others;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.omnicoder.unipass.Activities.BackupAndRestoreActivity;
import com.omnicoder.unipass.Database.ModelClass;
import com.omnicoder.unipass.R;

public class DoClass {
    public static boolean firstTime= true;
    public static boolean autoLockTimeout= false;

    public boolean isAutoLockTimeout() {
        return autoLockTimeout;
    }

    public void setAutoLockTimeout(boolean b) {
        autoLockTimeout=b;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean b){
        firstTime=b;
    }

    public void nothing(){

    }

    public Dialog createDialog(Context context, Dialog dialog, int dialogResourceFile){
        dialog= new Dialog(context);
        dialog.setContentView(dialogResourceFile);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_bg));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        return dialog;

    }





}
