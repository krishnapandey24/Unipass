package com.omnicoder.unipass.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omnicoder.unipass.Activities.AddLoginActivity;
import com.omnicoder.unipass.Activities.HomeActivity;
import com.omnicoder.unipass.Adapters.LoginAdapterClass;
import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Database.ModelClass;
import com.omnicoder.unipass.Database.params;
import com.omnicoder.unipass.Others.LoginBottomSheet;
import com.omnicoder.unipass.R;

import java.util.ArrayList;
import java.util.Comparator;


public class LoginFragment extends Fragment implements HomeActivity.ButtonClickedActivity {
    private FloatingActionButton fab;
    private LoginAdapterClass adapter;
    private RecyclerView recyclerView;
    private ImageButton sortButton;
    private SharedPreferences sharedPreferences;
    private TextView textView;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    public SearchView searchView;
    ArrayList<ModelClass> LoginList = new ArrayList<>();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogStuff("Start");
//        fetchLogins();
        View view= inflater.inflate(R.layout.fragment_login2, container, false);
        Log.d("FETCHER","started");

        setHasOptionsMenu(true);
        textView= view.findViewById(R.id.textView);
        recyclerView= view.findViewById(R.id.recyclerView);
        radioGroup= view.findViewById(R.id.radioGroup);
        radioButton= view.findViewById(R.id.allButton);
        fab= view.findViewById(R.id.fabButton);
        sharedPreferences= getContext().getSharedPreferences("SortMethod",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("Method","DateAdded");
        editor.apply();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddLoginActivity.class));
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.allButton:
                        textView.setText("All Passwords");
                        showData();
                        break;
                    case R.id.favoritesButton:
                        textView.setText(params.FAVORITES);
                        showFavorites();
                        break;
                    case R.id.personalButton:
                        textView.setText(params.PERSONAL);
                        showDataByCategory(params.PERSONAL);
                        break;
                    case R.id.workButton:
                        textView.setText(params.WORK);
                        showDataByCategory(params.WORK);
                        break;
                    case R.id.socialMediaButton:
                        textView.setText(params.SOCIAL_MEDIA);
                        showDataByCategory(params.SOCIAL_MEDIA);
                        break;
                    case R.id.financeButton:
                        textView.setText(params.FINANCE);
                        showDataByCategory(params.FINANCE);
                        break;
                    case R.id.shoppingButton:
                        textView.setText(params.SHOPPING);
                        showDataByCategory(params.SHOPPING);
                        break;
                    case R.id.othersButton:
                        textView.setText(params.OTHERS);
                        showDataByCategory(params.OTHERS);
                        break;


                }









            }

        });
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        adapter = new LoginAdapterClass(getContext(), LoginList);
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
//        showData();
        LogStuff("End");
        return view;
    }
    private void showData(){
        Log.d("speed","3");
        ArrayList<ModelClass> LoginList = new ArrayList<>();
        DBHandler db = new DBHandler(getContext());
        Cursor cursor = db.displayItem();
        cursor.moveToNext();
        Log.d("speed","4");

        while (cursor.moveToNext()) {
            int mID = Integer.parseInt(cursor.getString(0));
            String title = cursor.getString(1);
            String username = cursor.getString(2);
            String created_time = cursor.getString(6);
            String modified_time = cursor.getString(7);
            ModelClass textClass = new ModelClass(mID, title, username, created_time, modified_time);
            LoginList.add(textClass);
        }
        Log.d("speed","5");
        getSortedArrayList(LoginList,getContext());
        Log.d("speed","6");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        adapter = new LoginAdapterClass(getContext(), LoginList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        Log.d("checkSpeed","End");

    }

    public void fetchLogins(Context context){
        Log.d("speed1","3");
        Runnable runnable= () -> {

            DBHandler db = new DBHandler(context);
            Cursor cursor = db.displayItem();
            cursor.moveToNext();
            Log.d("speed1","4");

            while (cursor.moveToNext()) {
                int mID = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String username = cursor.getString(2);
                String created_time = cursor.getString(6);
                String modified_time = cursor.getString(7);
                ModelClass textClass = new ModelClass(mID, title, username, created_time, modified_time);
                LoginList.add(textClass);
            }
            Log.d("speed1","5");
            getSortedArrayList(LoginList,context);
        };
        Thread thread= new Thread(runnable);
        thread.start();
        Log.d("FETCHER","iTMES FETCHED");



    }


    private void showDataByCategory(String category){
        DBHandler db = new DBHandler(getContext());
        ArrayList<ModelClass> LoginList = new ArrayList<>();
        Cursor cursor = db.displayByCategory(category);
        while (cursor.moveToNext()) {

            int mID = Integer.parseInt(cursor.getString(0));
            String title= cursor.getString(1);
            String username = cursor.getString(2);
            String created_time= cursor.getString(3);
            String modified_time= cursor.getString(4);
            ModelClass textClass = new ModelClass(mID, title,username,created_time,modified_time);

            LoginList.add(textClass);

        }
        Log.d("Category","Category we got inside the showDataByCategory() is:"+category);
        LoginList= getSortedArrayList(LoginList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        adapter = new LoginAdapterClass(getContext(), LoginList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
    }

    private void showFavorites(){
        DBHandler db = new DBHandler(getContext());
        ArrayList<ModelClass> LoginList = new ArrayList<>();
        Cursor cursor = db.displayFavorites();
        while (cursor.moveToNext()) {

            int mID = Integer.parseInt(cursor.getString(0));
            String title= cursor.getString(1);
            String username = cursor.getString(2);
            String created_time= cursor.getString(3);
            String modified_time= cursor.getString(4);
            ModelClass textClass = new ModelClass(mID, title,username,created_time,modified_time);

            LoginList.add(textClass);

        }
        LoginList= getSortedArrayList(LoginList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        adapter = new LoginAdapterClass(getContext(), LoginList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
    }

    @Override
    public void getButtonClicked(String method) {
        sharedPreferences= getContext().getSharedPreferences("SortMethod",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("Method",method);
        editor.apply();
        radioButton.setChecked(true);
        showData();
    }

    private ArrayList<ModelClass> getSortedArrayList(ArrayList<ModelClass> arrayList, Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences("SortMethod",Context.MODE_PRIVATE);
        String sortMethod=sharedPreferences.getString("Method","DateAdded");
        switch (sortMethod){
            case "AtoZ":
                arrayList.sort(alphabetical);
                break;
            case "DateAdded":
                arrayList.sort(dateAdded);
                break;
            case "Modified":
                arrayList.sort(modified);
                break;
            default:
                arrayList.sort(ByDate);

        }
        return arrayList;


    }

    public static Comparator<ModelClass> alphabetical= new Comparator<ModelClass>() {
        @Override
        public int compare(ModelClass m1, ModelClass m2) {

            return m2.getpTitle().compareTo(m1.getpTitle());
        }
    };
    public static Comparator<ModelClass> dateAdded= new Comparator<ModelClass>() {
        @Override
        public int compare(ModelClass m1, ModelClass m2) {
            return m1.getpCreated_time().compareTo(m2.getpCreated_time());
        }
    };

    public static Comparator<ModelClass> modified= new Comparator<ModelClass>() {
        @Override
        public int compare(ModelClass m1, ModelClass m2) {
            return m1.getpModified_time().compareTo(m2.getpModified_time());
        }
    };
    public static Comparator<ModelClass> ByDate= new Comparator<ModelClass>() {
        @Override
        public int compare(ModelClass m1, ModelClass m2) {
            String u1= m1.getpCreated_time();
            String u2= m2.getpCreated_time();

            return u2.compareTo(u1);
        }
    };

    public SearchView.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchView.setIconified(false);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        showData();
        radioButton.setChecked(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.login_fragment_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem= menu.findItem(R.id.search);
        SearchView searchView= (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort:
                LoginBottomSheet loginBottomSheet= new LoginBottomSheet();
                loginBottomSheet.show(getChildFragmentManager(),"LoginBottomSheet");
                break;

        }
        return true;
    }

    public void LogStuff(String msg){
        Log.d("LogStuff",""+msg);
    }

}