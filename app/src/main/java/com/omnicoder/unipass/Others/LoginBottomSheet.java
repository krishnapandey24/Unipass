package com.omnicoder.unipass.Others;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.omnicoder.unipass.R;

public class LoginBottomSheet extends BottomSheetDialogFragment{
    Button AtoZ,DateAdded,Modified;
    ButtonClicked buttonClicked;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_bottom_sheet, container, false);
        AtoZ = view.findViewById(R.id.AtoZ);
        DateAdded = view.findViewById(R.id.DateAdded);
        Modified = view.findViewById(R.id.Modified);



        AtoZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             buttonClicked.onButtonClicked("AtoZ");
                dismiss();
            }
        });
        DateAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked.onButtonClicked("DateAdded");
                dismiss();
            }
        });
        Modified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked.onButtonClicked("Modified");
                dismiss();
            }
        });
        return view;
    }
    public interface ButtonClicked{
        void onButtonClicked(String method);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        buttonClicked= (ButtonClicked) context;
    }


}
