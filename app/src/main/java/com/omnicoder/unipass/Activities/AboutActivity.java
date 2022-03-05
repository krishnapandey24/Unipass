package com.omnicoder.unipass.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omnicoder.unipass.BuildConfig;
import com.omnicoder.unipass.Others.PrivacyPolicy;
import com.omnicoder.unipass.R;

import static com.omnicoder.unipass.Others.PrivacyPolicy.privacy_policy;

public class AboutActivity extends AppCompatActivity {
    ImageView AppLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");
        AppLogo= findViewById(R.id.AppLogo);
        TextView versionView= findViewById(R.id.versionView);
        versionView.setText("v1.0");
        ImageView icon1, icon2, icon3, icon4,icon5;
        icon1= findViewById(R.id.icon1);
        icon2= findViewById(R.id.icon2);
        icon3= findViewById(R.id.icon3);
        icon4= findViewById(R.id.icon4);
        icon5= findViewById(R.id.icon5);
        icon1.setImageResource(R.drawable.ic_baseline_security_24);
        icon2.setImageResource(R.drawable.ic_light_bulb);
        icon3.setImageResource(R.drawable.ic_baseline_bookmark_24);
        icon4.setImageResource(R.drawable.ic_baseline_people_24);
        icon5.setImageResource(R.drawable.ic_baseline_email_24);

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivacyPolicy privacyPolicy= new PrivacyPolicy();
                privacyPolicy.show(getSupportFragmentManager(),"Privacy Policy");
            }
        });

         icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","omnicoderofficial@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My Suggestion");
                startActivity(Intent.createChooser(emailIntent, "Send Suggestion"));

            }
        });

         icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

         icon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

         icon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","omnicoderofficial@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, "Contact Us"));
            }
        });


    }
}