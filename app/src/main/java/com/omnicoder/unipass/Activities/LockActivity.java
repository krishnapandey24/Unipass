package com.omnicoder.unipass.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Fragments.LoginFragment;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.Others.DoClass;
import com.omnicoder.unipass.R;
import com.omnicoder.unipass.Services.AutoBackupService;
import com.omnicoder.unipass.Services.AutoLockService;


public class LockActivity extends AppCompatActivity {
    TextInputLayout textInputLayout;
    DoClass Do;
    EditText editText;
    Button enterButton;
    Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        Do= new DoClass();
        new LoginFragment().fetchLogins(LockActivity.this);
        editText= findViewById(R.id.editText);
        enterButton= findViewById(R.id.confirmButton);
        textInputLayout= findViewById(R.id.textInputLayout);
        if(textInputLayout.isErrorEnabled()){
            textInputLayout.setErrorEnabled(false);
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        editText.setText("Pa$$w0rd");
        loadingDialog= Do.createDialog(LockActivity.this,loadingDialog,R.layout.loading_dialog);



//         Will remove this later //

//        editText.setText("Pa$$w0rd");
//        startActivity(new Intent(LockActivity.this,ImportExportCSVActivity.class));
//        finish();


        // Will remove this later //

        SharedPreferences sharedPreferences= getSharedPreferences("first",MODE_PRIVATE);
        boolean firstStart= sharedPreferences.getBoolean("firstStart",true);
        if(firstStart){
            startActivity(new Intent(LockActivity.this, NewUserActivity.class));
        }

        // Creating the error Dialog //



        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                String userPassword= editText.getText().toString();
                DBHandler db= new DBHandler(LockActivity.this);
//                Cursor data= db.getData(1);
//                data.moveToNext();
                String text= db.getFirstSPRF();
                try{
                    Crypter.decrypt(text,userPassword);
                    Log.d("Service","Password is not wrong!"+userPassword);
                    MyApplication myApplication= (MyApplication) getApplicationContext();
                    myApplication.setSPRF(userPassword);
                    Log.d("Service","sprf they gave"+myApplication.getSPRF());
                    editText.setText("");
                    if(Do.isFirstTime()){
                        startActivity(new Intent(LockActivity.this,HomeActivity.class));
                        if(getSharedPreferences("Settings",MODE_PRIVATE).getBoolean("autoBackupSwitch",false)){
                            AutoBackupService autoBackupService= new AutoBackupService();
                            autoBackupService.setSprf(userPassword);
                            startService(new Intent(LockActivity.this,AutoBackupService.class));
                        }
                        Do.setFirstTime(false);

                    }
                    Do.setAutoLockTimeout(false);
                    stopService(new Intent(LockActivity.this, AutoLockService.class));
                    Log.d("Application","Auto Lock Timeout here: "+Do.isAutoLockTimeout());
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                    loadingDialog.dismiss();
                    textInputLayout.setError("Incorrect Password");
                    Log.d("Service","Password is wrong!");
                }


            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        loadingDialog.dismiss();
    }
}