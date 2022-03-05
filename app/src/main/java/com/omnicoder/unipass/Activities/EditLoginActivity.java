package com.omnicoder.unipass.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Database.ModelClass;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.Others.DoClass;
import com.omnicoder.unipass.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditLoginActivity extends AppCompatActivity {
    DoClass Do;
    Dialog loadingDialog;
    EditText editTitle,editUsername,editPassword, editNote, editURL;
    SwitchCompat favoriteSwitch;
    ImageButton generate;
    DBHandler db;
    Spinner spinner;
    private int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_login);
        setTitle("Edit Login");
        Do= new DoClass();
        db= new DBHandler(EditLoginActivity.this);
        editTitle= findViewById(R.id.editTitle);
        editUsername= findViewById(R.id.editUsername);
        editPassword= findViewById(R.id.editPassword);
        editURL= findViewById(R.id.editURL);
        editNote= findViewById(R.id.editNote);
        favoriteSwitch= findViewById(R.id.favoriteSwitch);
        spinner= findViewById(R.id.spinner);

        Intent intent= getIntent();
        itemID= intent.getIntExtra("itemID",-1);

        Cursor cursor= db.getData(itemID);
        cursor.moveToNext();

        ArrayList<String> categories= new ArrayList<>();
        categories.add("Personal");
        categories.add("Work");
        categories.add("Social Media");
        categories.add("Finance");
        categories.add("Shopping");
        categories.add("Others");
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(EditLoginActivity.this,R.layout.custom_spinner_layout,categories);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(arrayAdapter.getPosition(cursor.getString(8)));

        editTitle.setText(cursor.getString(1));
        editUsername.setText(cursor.getString(2));
        try {
            MyApplication myApplication= (MyApplication) getApplicationContext();
            editPassword.setText(Crypter.decrypt(cursor.getString(3),myApplication.getSPRF()));
        } catch (Exception e) {
            e.printStackTrace();
            editPassword.setText("(Unable to decrypt the password)");
        }
        editURL.setText(cursor.getString(4));
        editNote.setText(cursor.getString(5));
        favoriteSwitch.setChecked(Boolean.parseBoolean(cursor.getString(9)));

        generate= findViewById(R.id.generateButton);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(EditLoginActivity.this,GeneratorActivity.class);
                startActivityForResult(intent,1234);
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.edit_login_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            loadingDialog= Do.createDialog(EditLoginActivity.this,loadingDialog,R.layout.loading_dialog);
            loadingDialog.show();
            String category= spinner.getSelectedItem().toString();
            String title= editTitle.getText().toString();
            String username= editUsername.getText().toString();
            String url= editURL.getText().toString();
            String note= editNote.getText().toString();

            if(title.length()>0 && username.length()>0){
                MyApplication myApplication= (MyApplication) getApplicationContext();
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yy 'at' hh:mm a");
                String modifiedTime= simpleDateFormat.format(new Date());
                String password= null;
                try{
                    password= Crypter.encrypt(myApplication.getSPRF(),editPassword.getText().toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                String favoriteSwitchState="false";
                if(favoriteSwitch.isChecked()){
                    favoriteSwitchState="true";
                }
                ModelClass modelClass= new ModelClass(itemID,title,username,password,url,note,modifiedTime,category,favoriteSwitchState);
                db.updateLogin(modelClass);

                editTitle.setText("");
                editUsername.setText("");
                editPassword.setText("");
                editURL.setText("");
                editNote.setText("");
                startActivity(new Intent(EditLoginActivity.this,ViewLoginActivity.class).putExtra("itemID",itemID));
                finish();
            }
        }
        else if(item.getItemId() == R.id.home){
            startActivity(new Intent(EditLoginActivity.this,ViewLoginActivity.class).putExtra("itemID",itemID));
        }
        else if(item.getItemId()== R.id.deleteLogin){
            db.deleteData(itemID);
            startActivity(new Intent(EditLoginActivity.this,HomeActivity.class));
        }
        else{
            finish();
        };
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1234){
            String generatedPassword= data.getStringExtra("generatedPassword");
            editPassword.setText(generatedPassword);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditLoginActivity.this,ViewLoginActivity.class).putExtra("itemID",itemID));
        finish();
    }

}