package com.omnicoder.unipass.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;

import com.omnicoder.unipass.Activities.HomeActivity;
import com.omnicoder.unipass.Activities.ViewLoginActivity;
import com.omnicoder.unipass.Others.Generator;
import com.omnicoder.unipass.R;

import static android.content.Context.CLIPBOARD_SERVICE;


public class GeneratorFragment extends Fragment implements HomeActivity.ButtonClickedActivity {
    TextView lengthView,generatedPasswordView;
    SwitchCompat letterSwitch, numbersSwitch, symbolsSwitch;
    SeekBar seekBar;
    final String Letters= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    final String Numbers="0123456789";
    final String SpecialCharacter= "!@#$%^&*_-+=/;:,?/'{}[]()<>";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_generator, container, false);
        letterSwitch= view.findViewById(R.id.lettersSwitch);
        numbersSwitch= view.findViewById(R.id.numbersSwitch);
        symbolsSwitch= view.findViewById(R.id.symbolsSwitch);
        lengthView= view.findViewById(R.id.TotalLengthTextView);
        generatedPasswordView= view.findViewById(R.id.generatedPasswordView);
        seekBar = view.findViewById(R.id.seekbar);
        Button generateButton= view.findViewById(R.id.generateButton);

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
                ClipboardManager clipboardManager= (ClipboardManager) getContext().getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip= ClipData.newPlainText("generatedPassword",generatedPasswordView.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getContext(),"Text copied successfully",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return view;
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
        Generator generator= new Generator(characters,progress);
        generatedPasswordView.setText(generator.generate());
        lengthView.setText(String.valueOf(progress));



    }





    @Override
    public void getButtonClicked(String method) {

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
    public void onStart() {
        super.onStart();
        Log.d("lifecycler","Start");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lifecycler","Resume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("lifecycler","Pause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("lifecycler","Stop");

    }
}