package com.omnicoder.unipass.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.Others.DoClass;
import com.omnicoder.unipass.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BackupAndRestoreActivity extends AppCompatActivity {
    DoClass Do;
    Dialog loadingDialog;
    private TextView delayTextView;
    private String password;
    private Dialog backupRestoredSuccessfullyDialog,somethingWentWrongDialog,invalidDatabaseFileDialog,autoBackupDelayDialog,backupButtonDialog;
    private SwitchMaterial autoBackupSwitch;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_and_restore);
        Do= new DoClass();
        loadingDialog= Do.createDialog(BackupAndRestoreActivity.this,loadingDialog,R.layout.loading_dialog);
        Context context= BackupAndRestoreActivity.this;
        setTitle("Backup & Restore");
        Button backupButton= findViewById(R.id.backupButton);
        Button restoreButton= findViewById(R.id.restoreButton);
        TextView textView= findViewById(R.id.autoBackupDelay);
        autoBackupSwitch= findViewById(R.id.autoBackupSwitch);
        delayTextView= findViewById(R.id.delayTextView);

        ActivityCompat.requestPermissions(BackupAndRestoreActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
        MyApplication myApplication= (MyApplication) getApplicationContext();
        password= myApplication.getSPRF();

        Dialog backupSavedDialog= new Dialog(context);
        createDialogWithButton(backupSavedDialog,R.layout.backup_saved_successfully_dialog);

        Dialog incorrectRestorePasswordDialog= new Dialog(context);
        createDialogWithButton(incorrectRestorePasswordDialog,R.layout.incorrect_restore_password_dialog);

        somethingWentWrongDialog= new Dialog(context);
        createDialogWithButton(somethingWentWrongDialog,R.layout.something_went_wrong);

        backupRestoredSuccessfullyDialog= new Dialog(context);
        createDialogWithButton(backupRestoredSuccessfullyDialog,R.layout.backup_restored_successfully_dialog);

        invalidDatabaseFileDialog= new Dialog(context);
        createDialogWithButton(invalidDatabaseFileDialog,R.layout.invalid_database_file_dialog);

        autoBackupDelayDialog= Do.createDialog(context,autoBackupDelayDialog,R.layout.auto_backup_delay_dialog2);
        radioGroup= autoBackupDelayDialog.findViewById(R.id.radioGroup);
        TextView cancelButton= autoBackupDelayDialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoBackupDelayDialog.dismiss();
            }
        });
        RadioButton daily,weekly,monthly,yearly;
        daily= autoBackupDelayDialog.findViewById(R.id.immediately);
        weekly= autoBackupDelayDialog.findViewById(R.id.weekly);
        monthly= autoBackupDelayDialog.findViewById(R.id.monthly);
        yearly= autoBackupDelayDialog.findViewById(R.id.yearly);
        SharedPreferences sharedPreferences= getSharedPreferences("autoBackupData",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        String selectedDelayOption= sharedPreferences.getString("selectedDelayOption","Weekly");
        switch (selectedDelayOption) {
            case "Daily":
                daily.setChecked(true);
                daily.setOnClickListener(listener());
                break;
            case "Weekly":
                weekly.setChecked(true);
                weekly.setOnClickListener(listener());
                break;
            case "Monthly":
                monthly.setChecked(true);
                monthly.setOnClickListener(listener());
                break;
            case "Year":
                yearly.setChecked(true);
                yearly.setOnClickListener(listener());

        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Date date= new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
                int selectedButtonID= radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton= autoBackupDelayDialog.findViewById(selectedButtonID);
                String autoBackupDelay= radioButton.getText().toString();
                delayTextView.setText(autoBackupDelay);
                SharedPreferences sharedPreferences= getSharedPreferences("autoBackupData",MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("selectedDelayOption",autoBackupDelay);
                editor.putString("Date",sdf.format(date));
                editor.apply();
                BackupDatabase();
                autoBackupDelayDialog.dismiss();
                Toast.makeText(context,"Auto Backup Delay Selected",Toast.LENGTH_SHORT).show();
            }
        });

        backupButtonDialog= Do.createDialog(context,backupButtonDialog,R.layout.backup_note);
        Button okButton= backupButtonDialog.findViewById(R.id.OkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupDatabase();
                backupButtonDialog.dismiss();
                backupSavedDialog.show();
            }
        });


        //Created All Dialogs //


        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              backupButtonDialog.show();
            }
        });

        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                Toast.makeText(context,"Select a Backup File",Toast.LENGTH_SHORT).show();
                startActivityForResult(intent,10);
                loadingDialog.dismiss();

            }
        });

        autoBackupSwitch.setChecked(sharedPreferences.getBoolean("autoBackupSwitch",false));
        autoBackupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("autoBackupSwitch",autoBackupSwitch.isChecked());
                if(autoBackupSwitch.isChecked()){
                    autoBackupDelayDialog.show();

                }
            }
        });

        ConstraintLayout constraintLayout= findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoBackupDelayDialog.show();
            }
        });
        TextView autoBackupDelayTextView= findViewById(R.id.autoBackupDelay);
        autoBackupDelayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoBackupDelayDialog.show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
                String path = getFilePath(data);
                String fileExtension= "";
                int dotIndex= path.lastIndexOf(".");
                if(dotIndex>0){
                    fileExtension= path.substring(dotIndex+1);
                }

                if(fileExtension.equals("db")) {
                    File DBFile= new File(getDatabasePath("texts_db").getAbsolutePath());
                    File backupFile = new File(path);

                    try {
                        FileInputStream fileInputStream = new FileInputStream(backupFile);
                        byte[] BackupFileBytes = new byte[(int) backupFile.length()];
                        fileInputStream.read(BackupFileBytes);

                        byte[] decryptedBackupFileBytes= Crypter.decryptFile(BackupFileBytes,password);
                        FileOutputStream fileOutputStream= new FileOutputStream(DBFile);
                        fileOutputStream.write(decryptedBackupFileBytes);
                        backupRestoredSuccessfullyDialog.show();
                        Log.d("dialog","Backup restored successfully");

                    } catch (Exception e) {
                        e.printStackTrace();
                        somethingWentWrongDialog.show();
                        Log.d("dialog","Something went wrong.");
                    }

                }
                else {
                    invalidDatabaseFileDialog.show();
                    Log.d("dialog","Invalid Database file!");
                }



        }
    }

    public String getFilePath(Intent data) {
        Uri uri= data.getData();
        String fullPath= uri.getPath();
        Log.d("path","full path "+fullPath);

        int dotIndex= fullPath.lastIndexOf(":");

        String folderName="";
        if(dotIndex>0) {
            folderName = fullPath.substring(dotIndex + 1);
        }
        return Environment.getExternalStorageDirectory()+"/" + folderName;



    }
    public void autoBackup(){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
            Date currentDate = new Date();
            SharedPreferences sharedPreferences = getSharedPreferences("autoBackupData", MODE_PRIVATE);
            Date previousDate = sdf.parse(sharedPreferences.getString("Date",sdf.format(currentDate)));
            String autoBackupDelay = sharedPreferences.getString("autoBackupData", "Weekly");
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



    }

    public void BackupDatabase(){
        loadingDialog.show();
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
            byte[] encryptedDBBytes = Crypter.encryptFile(dbFileBytes, password);
            FileOutputStream fileOutputStream = new FileOutputStream(backupFile);
            fileOutputStream.write(encryptedDBBytes);
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            somethingWentWrongDialog.show();
            Log.d("Service","Its happening here!");


        }
        loadingDialog.dismiss();
    }

    private void createDialogWithButton(Dialog dialog, int dialogResourceFile){
        dialog.setContentView(dialogResourceFile);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(BackupAndRestoreActivity.this,R.drawable.dialog_bg));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button okButton= dialog.findViewById(R.id.OkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void onClickOrCheckChangeListener(){
        Date date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
        int selectedButtonID= radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton= autoBackupDelayDialog.findViewById(selectedButtonID);
        String autoBackupDelay= radioButton.getText().toString();
        delayTextView.setText(autoBackupDelay);
        SharedPreferences sharedPreferences= getSharedPreferences("autoBackupData",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("selectedDelayOption",autoBackupDelay);
        editor.putString("Date",sdf.format(date));
        editor.apply();
        BackupDatabase();
        autoBackupDelayDialog.dismiss();
        Toast.makeText(BackupAndRestoreActivity.this,"Auto Backup Delay Selected",Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener listener(){
        View.OnClickListener onClickListener;
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOrCheckChangeListener();
            }
        };
        return onClickListener;
    }

}