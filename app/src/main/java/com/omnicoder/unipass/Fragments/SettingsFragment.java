package com.omnicoder.unipass.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.omnicoder.unipass.Activities.AboutActivity;
import com.omnicoder.unipass.Activities.BackupAndRestoreActivity;
import com.omnicoder.unipass.Activities.ChangeMasterPasswordActivity;
import com.omnicoder.unipass.Activities.ImportExportCSVActivity;
import com.omnicoder.unipass.Activities.HomeActivity;
import com.omnicoder.unipass.Activities.MyApplication;
import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Database.ModelClass;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.Others.DoClass;
import com.omnicoder.unipass.Others.FAQClass;
import com.omnicoder.unipass.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class SettingsFragment extends Fragment implements HomeActivity.ButtonClickedActivity {
    DoClass Do;
    Dialog cleanupDelayDialog,autoLockDelayDialog, deleteAllDialog, deleteAllLoadingDialog ;
    TextView cleanupDelayView,autoLockDelayView;
    TextInputLayout textInputLayout;
    EditText editText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
        SharedPreferences sharedPreference= getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreference.edit();
        editor.putString("Hello","hello");
        Log.d("delayText","Cleanup Delay: "+intToString(sharedPreference.getInt("cleanupDelay",30)));
        editor.apply();
        editor.commit();
        Do= new DoClass();

        cleanupDelayView= view.findViewById(R.id.cleanupDelayView);
        cleanupDelayView.setText(intToString(sharedPreference.getInt("cleanupDelay",30)));  
        autoLockDelayView= view.findViewById(R.id.autoLockDelayView);
        autoLockDelayView.setText(intToString(sharedPreference.getInt("autoLockDelay",30)));
        
        cleanupDelayDialog= Do.createDialog(getContext(),cleanupDelayDialog,R.layout.clipboard_cleanup_delay_dialog);



        TextView importCSV= view.findViewById(R.id.importCSV);
        importCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ImportExportCSVActivity.class));
            }
        });

        TextView cancelButton= cleanupDelayDialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanupDelayDialog.dismiss();

            }
        });
        RadioGroup cleanupOptions= cleanupDelayDialog.findViewById(R.id.radioGroup);

        cleanupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton= cleanupOptions.findViewById(cleanupOptions.getCheckedRadioButtonId());
                editor.putInt("cleanupDelay",StringToInt(radioButton.getText().toString()));
                editor.putInt("cleanupRadioButtonID",cleanupOptions.getCheckedRadioButtonId());
                editor.apply();
                editor.commit();
                cleanupDelayView.setText(intToString(sharedPreference.getInt("cleanupDelay",30)));
                Toast.makeText(getContext(),"Clipboard Cleanup Delay Time Selected",Toast.LENGTH_SHORT).show();
                cleanupDelayDialog.dismiss();

            }
        });

        autoLockDelayDialog= Do.createDialog(getContext(),autoLockDelayDialog,R.layout.autolock_delay_dialog);

        TextView cancelButton2= autoLockDelayDialog.findViewById(R.id.cancel_button);
        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("delayText","Cleanup Delay: "+intToString(sharedPreference.getInt("cleanupDelay",30)));
                autoLockDelayDialog.dismiss();

            }
        });
        RadioGroup autoLockOptions= autoLockDelayDialog.findViewById(R.id.radioGroup);

        autoLockOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String delay;
                RadioButton radioButton= autoLockOptions.findViewById(autoLockOptions.getCheckedRadioButtonId());
                editor.putInt("autoLockDelay",StringToInt(radioButton.getText().toString()));
                Log.d("buttonText",""+radioButton.getText().toString());
                delay= radioButton.getText().toString();
                Log.d("buttonText",""+sharedPreference.getInt("autoLockDelay",69));
                editor.putInt("autoLockRadioButtonID",autoLockOptions.getCheckedRadioButtonId());
                editor.apply();
                editor.commit();
                autoLockDelayView.setText(intToString(sharedPreference.getInt("autoLockDelay",30)));
                autoLockDelayDialog.dismiss();
                Toast.makeText(getContext(),"Auto Lock Delay Time Selected"+delay,Toast.LENGTH_SHORT).show();

            }
        });

        TextView changeMasterPassword= view.findViewById(R.id.changeMasterPassword);
        TextView BackupDatabase= view.findViewById(R.id.backupDatabase);
        changeMasterPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ChangeMasterPasswordActivity.class);
            }
        });

        BackupDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BackupAndRestoreActivity.class);
            }
        });

        SwitchCompat blockScreenshots = view.findViewById(R.id.blockScreenshots);
        blockScreenshots.setChecked(sharedPreference.getBoolean("blockScreenshots",false));
        blockScreenshots.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("blockScreenshots",blockScreenshots.isChecked());
                editor.apply();
                editor.commit();
                Toast.makeText(getContext(),"You have to restart the app.",Toast.LENGTH_SHORT).show();

            }
        });
        
        

        ConstraintLayout clipboardCleanupDelay= view.findViewById(R.id.clipboardCleanupDelay);
        clipboardCleanupDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cleanupDelayDialog.show();
                    RadioButton radioButton= cleanupDelayDialog.findViewById(sharedPreference.getInt("cleanupRadioButtonID",R.id.weekly));
                    radioButton.setChecked(true);
                    Log.d("timedelay",""+R.id.weekly);

                }
                else{
                    Toast.makeText(getContext(),"Your Phone doesn't support this feature!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        
        ConstraintLayout autoLockDelay= view.findViewById(R.id.autoLockDelay2);
        autoLockDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoLockDelayDialog.show();
                RadioButton radioButton= autoLockDelayDialog.findViewById(sharedPreference.getInt("autoLockRadioButtonID",R.id.weekly));
                radioButton.setChecked(true);
                radioButton.setChecked(true);
            }
        });
        

        
        SwitchCompat clearClipboard = view.findViewById(R.id.clearClipboard);
        clearClipboard.setChecked(sharedPreference.getBoolean("clearClipboard",false));
        clearClipboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    Toast.makeText(getContext(),"Your Phone doesn't support this feature!",Toast.LENGTH_SHORT).show();
                    clearClipboard.setChecked(false);
                }
                else {
                    editor.putBoolean("clearClipboard",clearClipboard.isChecked());
                    editor.apply();  
                    editor.commit();
                }
            }
        });

        SwitchCompat autoLockSwitch = view.findViewById(R.id.autoLockSwitch);
        autoLockSwitch.setChecked(sharedPreference.getBoolean("autoLockSwitch",false));
        autoLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("autoLockSwitch",autoLockSwitch.isChecked());
                editor.apply();
                editor.commit();

            }
        });

        TextView aboutUnipass= view.findViewById(R.id.aboutUnipass);
        aboutUnipass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        TextView reportABug= view.findViewById(R.id.reportABug);
        reportABug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","omnicoderofficial@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporting a bug");
                startActivity(Intent.createChooser(emailIntent, "Report a bug"));
            }
        });

        deleteAllLoadingDialog= Do.createDialog(getContext(),deleteAllLoadingDialog,R.layout.delete_all_loading_dialog);
        deleteAllDialog= Do.createDialog(getContext(),deleteAllDialog,R.layout.delete_all_dialog_2);
        deleteAllDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        textInputLayout=deleteAllDialog.findViewById(R.id.textInputLayout);

        editText= deleteAllDialog.findViewById(R.id.editText);
        TextView cancel = deleteAllDialog.findViewById(R.id.cancelButton);
        TextView confirm = deleteAllDialog.findViewById(R.id.confirmButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllDialog.dismiss();

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new MyApplication().confirmPassword(getContext(),editText.getText().toString())){
                    deleteAllDialog.dismiss();
                    deleteAllLoadingDialog.show();
                    DBHandler dbHandler= new DBHandler(getContext());
                    dbHandler.deleteAllData();
                    deleteAllLoadingDialog.dismiss();

                }
                else {
                    textInputLayout.setError("Incorrect Password");

                }
            }
        });

        TextView faq= view.findViewById(R.id.faq);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FAQClass().show(getChildFragmentManager(),"LoginBottomSheet");
            }
        });

        TextView deleteAll= view.findViewById(R.id.deleteAllData);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllDialog.show();
                if(textInputLayout.isErrorEnabled()){
                    textInputLayout.setErrorEnabled(false);
                }

            }
        });
        
        return view;
    }

    @Override
    public void getButtonClicked(String method) {
        
    }

    private void startActivity(Class<?> javaClass){
        startActivity(new Intent(getContext(), javaClass));

    }

    private int StringToInt(String delayTime){
        int delayTimeInInt=30;
        switch (delayTime) {
            case "Immediately":
                delayTimeInInt= 0;
                break;
            case "5 seconds":
                delayTimeInInt= 5;
                break;
            case "10 seconds":
                delayTimeInInt= 10;
                break;
            case "30 seconds":
                delayTimeInInt= 30;
                break;
            case "1 minute":
                delayTimeInInt= 60;
                break;
            case "2 minutes":
                delayTimeInInt= 120;
                break;
            case "5 minutes":
                delayTimeInInt= 300;
                break;
            case "10 minutes":
                delayTimeInInt= 600;
                break;
            case "15 minutes":
                delayTimeInInt= 900;
                break;
        }
        Log.d("StringToInt","Current time: "+delayTimeInInt);
        return delayTimeInInt;
        
    }

    private String intToString(int delayTime){
        String delayTimeInString="30 seconds";
        switch (delayTime) {
            case 0:
                delayTimeInString="Immediately";
                break;
            case 5 :
                delayTimeInString="5 seconds";
                break;
            case 10 :
                delayTimeInString="10 seconds";
                break;
            case 30 :
                delayTimeInString="30 seconds";
                break;
            case 60 :
                delayTimeInString="1 minutes";
                break;
            case 120 :
                delayTimeInString="2 minutes";
                break;
            case 300 :
                delayTimeInString="5 minutes";
                break;
            case 600 :
                delayTimeInString="10 minutes";
                break;
            case 900 :
                delayTimeInString="15 minutes";
                break;
        }
        Log.d("StringToInt","Current time in Int "+delayTimeInString);
        return delayTimeInString;
    }









}