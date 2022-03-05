package com.omnicoder.unipass.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.omnicoder.unipass.Activities.HomeActivity;
import com.omnicoder.unipass.R;


public class NotesFragment extends Fragment implements HomeActivity.ButtonClickedActivity {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notes, container, false);

        return view;
    }

    @Override
    public void getButtonClicked(String method) {

    }
}