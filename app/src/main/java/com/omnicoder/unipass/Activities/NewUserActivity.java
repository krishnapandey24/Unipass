
package com.omnicoder.unipass.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Others.Crypter;
import com.omnicoder.unipass.Others.FAQClass;
import com.omnicoder.unipass.Others.LoginLogos;
import com.omnicoder.unipass.R;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewUserActivity extends AppCompatActivity {
    private Context context;
    private Button button;
    private Dialog dialog,dialog2;
    private TextView textView;
    private EditText editText;
    private String text1="text1", text2="text2";
    private ImageView checkIcon1, checkIcon2, checkIcon3, checkIcon4;
    private TextSwitcher textSwitcher,textSwitcher2;
    private final String[] textViewText= {"Setup your password", "Confirm your password"};
    private final String[] textViewText2= {"Please follow the instructions below", "Type your password once again"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        context= NewUserActivity.this;
        checkIcon1=findViewById(R.id.checkIcon1);
        checkIcon2=findViewById(R.id.checkIcon2);
        checkIcon3=findViewById(R.id.checkIcon3);
        checkIcon4=findViewById(R.id.checkIcon4);

        editText= findViewById(R.id.editText);
        editText.addTextChangedListener(textWatcher());
        button= findViewById(R.id.confirmButton);
        button.setEnabled(false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textSwitcher=findViewById(R.id.textSwitcher1);
        textSwitcher2=findViewById(R.id.textSwitcher2);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView= new TextView(context);
                textView.setTextSize(20);
                textView.setTextColor(getResources().getColor(R.color.blue,getTheme()));
                Typeface typeface = ResourcesCompat.getFont(NewUserActivity.this, R.font.roboto_medium);
                textView.setTypeface(typeface);
                return textView;
            }
        });
        textSwitcher2.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView= new TextView(context);
                textView.setTextSize(15);
                Typeface typeface = ResourcesCompat.getFont(NewUserActivity.this, R.font.roboto_medium);
                textView.setTypeface(typeface);
                return textView;
            }
        });
        textSwitcher.setCurrentText(textViewText[0]);
        textSwitcher2.setCurrentText(textViewText2[0]);

        // Creating Dialogs  //
        dialog= new Dialog(context);
        dialog.setContentView(R.layout.new_success_dialog);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.dialog_bg));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button okButton= dialog.findViewById(R.id.OkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,HomeActivity.class));
            }
        });

        dialog2= new Dialog(context);
        dialog2.setContentView(R.layout.new_user_error_dialog);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.dialog_bg));
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        Button error_dialog_ok_button;
        error_dialog_ok_button= dialog2.findViewById(R.id.new_user_OkButton);
        error_dialog_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,NewUserActivity.class));
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1=editText.getText().toString();
                editText.setText("");
                editText.addTextChangedListener(textWatcher());
                textSwitcher.setText(textViewText[1]);
                textSwitcher2.setText(textViewText2[1]);
                button.setEnabled(false);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text2=editText.getText().toString();
                        editText.setText("");
                        if(text1.equals(text2)){
                            MyApplication myApplication= (MyApplication) getApplicationContext();
                            myApplication.setSPRF(text1);
                            SharedPreferences sharedPreferences= getSharedPreferences("first",MODE_PRIVATE);
                            SharedPreferences.Editor editor= sharedPreferences.edit();
                            editor.putBoolean("firstStart",false);
                            editor.apply();
                            LoginLogos loginLogos= new LoginLogos(NewUserActivity.this);
                            loginLogos.addLogo();
                            DBHandler dbHandler= new DBHandler(NewUserActivity.this);
                            try {
                                dbHandler.addFirstSPRF(Crypter.encrypt(text1,"$%UNIPASS%$"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(NewUserActivity.this,"Somethings went wrong \n Please try again.", Toast.LENGTH_SHORT).show();
                            }
                            dialog.show();
                        }
                        else{
                            dialog2.show();
                        }
                    }
                });

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.new_user_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.faq:
                FAQClass faqClass= new FAQClass();
                faqClass.show(getSupportFragmentManager(),"FAQ");
                break;
            case R.id.restore:
                Toast.makeText(context,"Restore",Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

    private TextWatcher textWatcher() {
        TextWatcher textWatcher;
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ItHave("CapitalLetter", s) && ItHave("SmallLetter", s)) {
                    checkIcon1.setColorFilter(getResources().getColor(R.color.blue, getTheme()));
                } else {
                    checkIcon1.setColorFilter(getResources().getColor(R.color.closeToWhite, getTheme()));
                }

                if (ItHave("number", s)) {
                    checkIcon2.setColorFilter(getResources().getColor(R.color.blue, getTheme()));
                } else {
                    checkIcon2.setColorFilter(getResources().getColor(R.color.closeToWhite, getTheme()));
                }

                if (ItHave("symbols", s)) {
                    checkIcon3.setColorFilter(getResources().getColor(R.color.blue, getTheme()));
                } else {
                    checkIcon3.setColorFilter(getResources().getColor(R.color.closeToWhite, getTheme()));
                }

                if (s.length() >= 8) {
                    checkIcon4.setColorFilter(getResources().getColor(R.color.blue, getTheme()));
                } else {
                    checkIcon4.setColorFilter(getResources().getColor(R.color.closeToWhite, getTheme()));
                }

                button.setEnabled(ItHave("All", s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return textWatcher;
    }
    private static boolean ItHave(String choice, CharSequence enteredText){
        Pattern CapitalLetters = Pattern.compile("[A-Z]");
        Pattern SmallLetters = Pattern.compile("[a-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[!@#$%&*()'_+=|<>?{}\\[\\]~-]");

        Matcher hasCapitalLetters = CapitalLetters.matcher(enteredText);
        Matcher hasSmallLetters = SmallLetters.matcher(enteredText);
        Matcher hasDigit = digit.matcher(enteredText);
        Matcher hasSpecial = special.matcher(enteredText);

        switch (choice) {
            case "CapitalLetter":
                return hasCapitalLetters.find();
            case "SmallLetter":
                return hasSmallLetters.find();
            case "number":
                return hasDigit.find();
            case "symbols":
                return hasSpecial.find();
            default:
                return hasCapitalLetters.find() && hasSmallLetters.find() && hasDigit.find() && hasSpecial.find() && enteredText.length()>=8;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context,NewUserActivity.class));
    }
}