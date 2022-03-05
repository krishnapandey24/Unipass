package com.omnicoder.unipass.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.R;
import com.omnicoder.unipass.Services.ClipboardCleanupService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;

public class ViewLoginActivity extends AppCompatActivity {
    private EditText editUsername;
    private TextView passwordScoreView, passwordStrengthView;
    private TextView progressBarScoreView;
    private int itemID;
    private int passwordScore,image;
    private String title,username,category,password,url,note,created,modified;
    private boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                DBHandler db = new DBHandler(ViewLoginActivity.this);
                MyApplication myApplication = (MyApplication) getApplicationContext();
                Intent intent = getIntent();
                itemID= intent.getIntExtra("itemID",-1);
                Cursor cursor= db.getData(itemID);
                cursor.moveToNext();
                title=cursor.getString(1);
                image= db.getImageResource(title);
                username=cursor.getString(2);
                Log.d("Service","View login"+myApplication.getSPRF());

                try {
                    password= Crypter.decrypt(cursor.getString(3),myApplication.getSPRF());
                } catch (Exception e) {
                    e.printStackTrace();
                    password= "ERROR";
                }
                url= cursor.getString(4);
                note= cursor.getString(5);
                if(cursor.getString(6).equals(cursor.getString(7))){
                    modified= "(This Login is not modified yet)";
                }
                else{
                    modified= cursor.getString(7);
                }
                created= cursor.getString(6);
                category= cursor.getString(8);
                favorite= Boolean.parseBoolean(cursor.getString(9));
                try {
                    passwordScore= getTotal(Crypter.decrypt(cursor.getString(3),myApplication.getSPRF()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setScore(passwordScore);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPasswordStrength(passwordScore);
                    }
                });


            }
        };
        Thread thread= new Thread(runnable);
        thread.start();



        setContentView(R.layout.activity_view_login);
        setTitle("View Login");
        Log.d("CheckSpeed","3");
        EditText editPassword = findViewById(R.id.editPassword);
        EditText editURL = findViewById(R.id.editURL);
        EditText editNote = findViewById(R.id.editNote);
        TextView titleView = findViewById(R.id.titleView);
        TextView categoryView = findViewById(R.id.categoryView);
        TextView modifiedDateView = findViewById(R.id.modifiedDateView);
        TextView createdDateView = findViewById(R.id.createdDateView);
        ImageView logoImageView = findViewById(R.id.logoImageView);
        ImageView favoriteImageView = findViewById(R.id.favoriteImageView);
        Button editButton = findViewById(R.id.editButton);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBarScoreView= findViewById(R.id.progressBarScoreView);
        passwordScoreView= findViewById(R.id.passwordScoreView);
        editUsername= findViewById(R.id.editUsername);
        Log.d("CheckSpeed","Password Score"+passwordScore);
//        setScore(passwordScore);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editActivityIntent= new Intent(ViewLoginActivity.this,EditLoginActivity.class);
                editActivityIntent.putExtra("itemID",itemID);
                startActivity(editActivityIntent);
            }
        });

        titleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboardManager= (ClipboardManager) ViewLoginActivity.this.getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip= ClipData.newPlainText("username",titleView.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                boolean clearClipboard= getSharedPreferences("Setting",MODE_PRIVATE).getBoolean("clearClipboard",false);
                if(clearClipboard){
                    startService(new Intent(ViewLoginActivity.this, ClipboardCleanupService.class));
                }
                Toast.makeText(ViewLoginActivity.this,"Title copied successfully",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        editUsername.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyItem(editUsername);
                Toast.makeText(ViewLoginActivity.this,"Username copied successfully",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        editPassword.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyItem(editPassword);
                Toast.makeText(ViewLoginActivity.this,"Password copied successfully",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        editURL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyItem(editURL);
                Toast.makeText(ViewLoginActivity.this,"URL copied successfully",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        editNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyItem(editUsername);
                Toast.makeText(ViewLoginActivity.this,"Note copied successfully",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        titleView.setText(title);
        logoImageView.setImageResource(image);
        editUsername.setText(username);
        editPassword.setText(password);
        editURL.setText(url);
        editNote.setText(note);
        modifiedDateView.setText(modified);
        createdDateView.setText(created);
        categoryView.setText(category);
        if(favorite){
            favoriteImageView.setVisibility(View.VISIBLE);
        }
        disableEditText(editUsername);
        disableEditText(editPassword);
        disableEditText(editURL);
        disableEditText(editNote);
        Log.d("CheckSpeed","2");
        Log.d("CheckSpeed","Password Score"+passwordScore);
        passwordStrengthView= findViewById(R.id.passwordStrengthView);
        if(passwordScore<=25){
            passwordStrengthView.setTextColor(Color.RED);
            passwordStrengthView.setText("Very Weak");
        }
        else if(passwordScore < 50){
            passwordStrengthView.setTextColor(Color.RED);
            passwordStrengthView.setText("Weak");
        }
        else if(passwordScore >=50 && passwordScore < 75){
            passwordStrengthView.setTextColor(getResources().getColor(R.color.blue,getTheme()));
            passwordStrengthView.setText("Average");
        }
        else if(passwordScore >= 75 && passwordScore <90){
            passwordStrengthView.setTextColor(getResources().getColor(R.color.GREEN,getTheme()));
            passwordStrengthView.setText("Strong");
        }
        else if(passwordScore >= 90){
            passwordStrengthView.setTextColor(getResources().getColor(R.color.GREEN,getTheme()));
            passwordStrengthView.setText("Very Strong");
        }











    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            onBackPressed();
        }
        else{
            finish();
        };
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewLoginActivity.this,HomeActivity.class));
        finish();
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
//        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setTextColor(Color.BLACK);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }





    public int getTotal(String temp){
        int length = 0, uppercase = 0, lowercase = 0, digits = 0, symbols = 0, requirements = 0;
        boolean uniqueSPRF= true;
        length = temp.length();
        System.out.println(length);
        for (int i = 0; i < temp.length(); i++) {
            if (Character.isUpperCase(temp.charAt(i)))
                uppercase++;
            else if (Character.isLowerCase(temp.charAt(i)))
                lowercase++;
            else if (Character.isDigit(temp.charAt(i)))
                digits++;

            symbols = length - uppercase - lowercase - digits;

        }
        InputStream inputStream= getResources().openRawResource(R.raw.common_passwords);
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        try {
            String common_password;
            String common_passwords[]= null;

            while ((common_password= bufferedReader.readLine()) != null){
                common_passwords= common_password.split(" ");
                for(String cp: common_passwords){
                    if(cp.equals(temp)){
                        uniqueSPRF= false;
                    }
                }
            }
            Log.d("readFile","We are inside!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(length<8){
            requirements--;
        }

        if (uppercase > 0) {
            requirements++;
        }

        if (lowercase > 0) {
            requirements++;
        }

        if (digits > 0) {
            requirements++;
        }

        if (symbols > 0) {
            requirements++;
        }

        if(uniqueSPRF){
            requirements++;
        }
        int Total= (length*2)+(requirements*10);
        return Total;
    }


    private void setScore(int score) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(score<100){
                    passwordScoreView.setText(score+"%");
                    progressBarScoreView.setText(score+"%");
                }
                else{
                    passwordScoreView.setText("100%");
                    progressBarScoreView.setText("100%");
                }
            }
        });

    }

    private void copyItem(EditText editText){
        String itemToCopy= editText.getText().toString();
        ClipboardManager clipboardManager= (ClipboardManager) ViewLoginActivity.this.getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip= ClipData.newPlainText(itemToCopy,itemToCopy);
        clipboardManager.setPrimaryClip(clip);
        SharedPreferences sharedPreferences= getSharedPreferences("Settings",MODE_PRIVATE);
        boolean clearClipboard= sharedPreferences.getBoolean("clearClipboard",false);
        if(clearClipboard){
            startService(new Intent(ViewLoginActivity.this, ClipboardCleanupService.class));
        }

    }
    private void setPasswordStrength(int passwordScore){
        ProgressBar progressBar= findViewById(R.id.progressBar);
        progressBar.setProgress(passwordScore);
        setScore(passwordScore);
        if(passwordScore<=25){
            passwordStrengthView.setTextColor(Color.RED);
            passwordStrengthView.setText("Very Weak");
        }
        else if(passwordScore < 50){
            passwordStrengthView.setTextColor(Color.RED);
            passwordStrengthView.setText("Weak");
        }
        else if(passwordScore >=50 && passwordScore < 75){
            passwordStrengthView.setTextColor(getResources().getColor(R.color.blue,getTheme()));
            passwordStrengthView.setText("Average");
        }
        else if(passwordScore >= 75 && passwordScore <90){
            passwordStrengthView.setTextColor(getResources().getColor(R.color.GREEN,getTheme()));
            passwordStrengthView.setText("Strong");
        }
        else if(passwordScore >= 90){
            passwordStrengthView.setTextColor(getResources().getColor(R.color.GREEN,getTheme()));
            passwordStrengthView.setText("Very Strong");
        }
    }








}