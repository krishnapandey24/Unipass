package com.omnicoder.unipass.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.omnicoder.unipass.Fragments.GeneratorFragment;
import com.omnicoder.unipass.Fragments.LoginFragment;
import com.omnicoder.unipass.Fragments.NotesFragment;
import com.omnicoder.unipass.Fragments.SettingsFragment;
import com.omnicoder.unipass.Others.DoClass;
import com.omnicoder.unipass.Others.LoginBottomSheet;
import com.omnicoder.unipass.R;

public class HomeActivity extends AppCompatActivity implements LoginBottomSheet.ButtonClicked {
    ButtonClickedActivity buttonClickedActivity;
    Fragment currentFragment;
    DoClass Do;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Passwords");
        currentFragment= new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout, new LoginFragment()).commit();
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("ResourceType")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            String title="Unipass";
            switch (item.getItemId()){
                case R.id.Logins:
                    selectedFragment= new LoginFragment();
                    title="Passwords";
                    break;
//                case R.id.Notes:
//                    selectedFragment= new NotesFragment();
//                    title="Notes");
//                    break;
                case R.id.Generator:
                    selectedFragment = new GeneratorFragment();
                    title="Password Generator";
                    break;
                case R.id.Settings:
                    selectedFragment = new SettingsFragment();
                    title="Settings";
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout,selectedFragment).commit();
            setTitle(title);
            return true;
        }
    };

    @Override
    public void onButtonClicked(String method) {
        buttonClickedActivity.getButtonClicked(method);
    }



    public interface ButtonClickedActivity{
        void getButtonClicked(String method);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        buttonClickedActivity= (ButtonClickedActivity) fragment;
    }





}