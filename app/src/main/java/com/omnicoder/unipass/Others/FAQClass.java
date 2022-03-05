package com.omnicoder.unipass.Others;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.omnicoder.unipass.R;

public class FAQClass extends BottomSheetDialogFragment {
    TextView Q1,A1,Q2,A2,Q3,A3,Q4,A4;
    ImageView Image1,Image2,Image3,Image4;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faq_bottom_sheet, container, false);
        Q1=view.findViewById(R.id.Q1);
        A1=view.findViewById(R.id.A1);
        Q2=view.findViewById(R.id.Q2);
        A2=view.findViewById(R.id.A2);
        Q3=view.findViewById(R.id.Q3);
        A3=view.findViewById(R.id.A3);
        Q4=view.findViewById(R.id.Q4);
        A4=view.findViewById(R.id.A4);

        Image1= view.findViewById(R.id.ImgBtn1);
        Image2= view.findViewById(R.id.ImgBtn2);
        Image3= view.findViewById(R.id.ImgBtn3);
        Image4= view.findViewById(R.id.ImgBtn4);

        Image1.setOnClickListener(Answer(A1,Q1,Image1));
        Image2.setOnClickListener(Answer(A2,Q2,Image2));
        Image3.setOnClickListener(Answer(A3,Q3,Image3));
        Image4.setOnClickListener(Answer(A4,Q4,Image4));

        Q1.setOnClickListener(Answer(A1,Q1,Image1));
        Q2.setOnClickListener(Answer(A2,Q2,Image2));
        Q3.setOnClickListener(Answer(A3,Q3,Image3));
        Q4.setOnClickListener(Answer(A4,Q4,Image4));


        return view;
    }
    private View.OnClickListener Question(TextView answer, TextView question, ImageView imageView){
        View.OnClickListener clickListener;
        clickListener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_up));
                answer.setVisibility(View.GONE);
                imageView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.rotate_down) );
                question.setOnClickListener(Answer(answer,question,imageView));
            }
        };
        return clickListener;
    }

    private View.OnClickListener Answer(TextView answer,TextView question, ImageView imageView){
        View.OnClickListener clickListener;
        clickListener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_down));
                answer.setVisibility(View.VISIBLE);
                imageView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.rotate_up) );
                question.setOnClickListener(Question(answer,question,imageView));
            }
        };
        return clickListener;
    }



}

