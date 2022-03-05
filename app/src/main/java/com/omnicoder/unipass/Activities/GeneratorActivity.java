package com.omnicoder.unipass.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.omnicoder.unipass.Others.Generator;
import com.omnicoder.unipass.R;

public class GeneratorActivity extends AppCompatActivity {
    TextView lengthView,generatedPasswordView;
    SwitchCompat letterSwitch, numbersSwitch, symbolsSwitch;
    SeekBar seekBar;
    final String Letters= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    final String Numbers="0123456789";
    final String SpecialCharacter= "!@#$%^&*_-+=/;:,?/'{}[]()<>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        setTitle("Password Generator");

        letterSwitch= findViewById(R.id.lettersSwitch);
        numbersSwitch= findViewById(R.id.numbersSwitch);
        symbolsSwitch= findViewById(R.id.symbolsSwitch);
        lengthView= findViewById(R.id.TotalLengthTextView);
        generatedPasswordView= findViewById(R.id.generatedPasswordView);
        seekBar = findViewById(R.id.seekbar);
        Button generateButton= findViewById(R.id.generateButton);

        Generator generator= new Generator(Letters,8);
        generatedPasswordView.setText(generator.generate());
        letterSwitch.setChecked(true);

        letterSwitch.setOnCheckedChangeListener(onCheckedChangeListener());
        numbersSwitch.setOnCheckedChangeListener(onCheckedChangeListener());
        symbolsSwitch.setOnCheckedChangeListener(onCheckedChangeListener());

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordGenerator();
            }
        });




        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passwordGenerator();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        generatedPasswordView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboardManager= (ClipboardManager) GeneratorActivity.this.getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip= ClipData.newPlainText("generatedPassword",generatedPasswordView.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(GeneratorActivity.this,"Text copied successfully",Toast.LENGTH_SHORT).show();
                return true;
            }
        });


    }

    private void passwordGenerator(){
        int progress= seekBar.getProgress();
        if(progress<8){
            progress=8;
        }
        StringBuilder stringBuilder= new StringBuilder();
        stringBuilder.append("abc");
        if(letterSwitch.isChecked()){
            stringBuilder.append(Letters);
        }
        if(numbersSwitch.isChecked()){
            stringBuilder.append(Numbers);
        }
        if(symbolsSwitch.isChecked()){
            stringBuilder.append(SpecialCharacter);
        }
        String characters= stringBuilder.toString();
        Log.d("char","characters: "+characters);
        Generator generator= new Generator(characters,progress);
        generatedPasswordView.setText(generator.generate());
        lengthView.setText(String.valueOf(progress));



    }

    private SwitchCompat.OnCheckedChangeListener onCheckedChangeListener(){
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                passwordGenerator();
            }
        };
        return onCheckedChangeListener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.generate_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent generatedPasswordIntent= new Intent();
        generatedPasswordIntent.putExtra("generatedPassword",generatedPasswordView.getText().toString());
        setResult(1234,generatedPasswordIntent);
        finish();
        return true;
    }


}
