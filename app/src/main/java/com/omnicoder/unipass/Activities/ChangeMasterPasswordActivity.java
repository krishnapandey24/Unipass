package com.omnicoder.unipass.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.Others.DoClass;
import com.omnicoder.unipass.R;

public class ChangeMasterPasswordActivity extends AppCompatActivity {
    DoClass Do;
    Dialog loadingDialog;
    EditText editCurrentPassword,editNewPassword,editConfirmPassword;
    private String currentPassword,newPassword, confirmedNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_master_password);
        Do= new DoClass();
        loadingDialog= Do.createDialog(ChangeMasterPasswordActivity.this,loadingDialog,R.layout.loading_dialog);
        editCurrentPassword= findViewById(R.id.editCurrentPassword);
        editNewPassword= findViewById(R.id.editNewPassword);
        editConfirmPassword= findViewById(R.id.editConfirmPassword);
        Button button= findViewById(R.id.changeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication myApplication= (MyApplication) getApplicationContext();
                currentPassword= editCurrentPassword.getText().toString();
                newPassword= editNewPassword.getText().toString();
                confirmedNewPassword= editConfirmPassword.getText().toString();
                if(newPassword.equals(confirmedNewPassword)){
                    loadingDialog.show();
                    DBHandler db= new DBHandler(ChangeMasterPasswordActivity.this);
                    String firstSPRF=db.getFirstSPRF();
                    Cursor cursor= db.displayItem();
                    try{
                        Crypter.decrypt(firstSPRF,currentPassword);
                        while (cursor.moveToNext()){
                            String plainText;
                            String userPassword= cursor.getString(3);
                            try{
                                plainText=Crypter.decrypt(userPassword,currentPassword);
                                String newLoginPassword= Crypter.encrypt(newPassword,plainText);
                                db.updateLoginPassword(newLoginPassword,cursor.getInt(0));
                                myApplication.setSPRF(newPassword);
                                Toast.makeText(ChangeMasterPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();
                                loadingDialog.dismiss();
                                finish();


                            } catch (Exception e) {
                                e.printStackTrace();
                                loadingDialog.dismiss();
                                Toast.makeText(ChangeMasterPasswordActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        loadingDialog.dismiss();
                        Toast.makeText(ChangeMasterPasswordActivity.this,"Current password is incorrect!",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ChangeMasterPasswordActivity.this,"Passwords doesn't match, please try again.",Toast.LENGTH_SHORT).show();

                }





            }
        });





    }
}