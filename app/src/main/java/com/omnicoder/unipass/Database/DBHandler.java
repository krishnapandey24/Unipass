package com.omnicoder.unipass.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.omnicoder.unipass.R;

import java.util.ArrayList;
import java.util.List;

import static com.omnicoder.unipass.Database.params.KEY_ID;
import static com.omnicoder.unipass.Database.params.KEY_USERNAME;
import static com.omnicoder.unipass.Database.params.TABLE_NAME;

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, params.DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE texts_table(ID INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT, username  TEXT,usertext TEXT,url TEXT,notes TEXT,created_time TEXT,modified_time,category,favorite TEXT)";
        String notes= "CREATE TABLE notes_table(ID INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,notes TEXT,created_time TEXT,modified_time,favorite TEXT)";
        String logos="CREATE TABLE logos_table(ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,path INTEGER)";
        db.execSQL(logos);
        db.execSQL(query);
        db.execSQL(notes);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addLogin(ModelClass modelClass)  {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(params.KEY_TITLE, modelClass.getpTitle());
        values.put(params.KEY_USERNAME, modelClass.getUsername());
        values.put(params.KEY_TEXT, modelClass.getUserText());
        values.put(params.KEY_URL,modelClass.getUrl());
        values.put(params.KEY_NOTES, modelClass.getpNotes());
        values.put(params.KEY_CREATED_TIME, modelClass.getpCreated_time());
        values.put(params.KEY_MODIFIED_TIME,modelClass.getpModified_time());
        values.put(params.FAVORITE,modelClass.getpFavorite());
        values.put(params.CATEGORY,modelClass.getCategory());
        db.insert(TABLE_NAME,null,values);

    }
    public void updateLogin(ModelClass modelClass){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(params.KEY_TITLE, modelClass.getpTitle());
        values.put(params.KEY_USERNAME, modelClass.getUsername());
        values.put(params.KEY_TEXT, modelClass.getUserText());
        values.put(params.KEY_URL,modelClass.getUrl());
        values.put(params.KEY_NOTES, modelClass.getpNotes());
        values.put(params.KEY_MODIFIED_TIME,modelClass.getpModified_time());
        values.put(params.FAVORITE,modelClass.getpFavorite());
        values.put(params.CATEGORY,modelClass.getCategory());
        db.update(TABLE_NAME,values, params.KEY_ID + "=?",new String[]{String.valueOf(modelClass.getpID())});


    }

    public Cursor getItemID(String username) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + KEY_ID + " FROM " + TABLE_NAME +
                " WHERE " + KEY_USERNAME + " = '" + username + "'";
        Cursor data = db.rawQuery(query, new String[]{});

        return data;
    }

    public Cursor displayItem(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return data;
    }
    public Cursor getData(int ID){
        SQLiteDatabase db = getWritableDatabase();
        String query="SELECT ID,title,username,usertext,url,notes,created_time,modified_time,category,favorite  FROM texts_table WHERE id="+ID;
        Cursor data = db.rawQuery(query, new String[]{});

        return data;
    }

    public void deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID+"=?", new String[]{String.valueOf(id)});

    }
    public Cursor displayByCategory(String category){
        SQLiteDatabase db= getReadableDatabase();
        String query="SELECT id,title,username,created_time,modified_time  FROM texts_table WHERE category='"+category+"'";
        Cursor data= db.rawQuery(query,new String[]{});

        return data;
    }
    public Cursor displayFavorites(){
        SQLiteDatabase db= getReadableDatabase();
        String query="SELECT id,title,username,created_time,modified_time  FROM texts_table WHERE favorite='true'";
        Cursor data= db.rawQuery(query,new String[]{});

        return data;
    }
    public void addFirstSPRF(String firstSPRF){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(params.KEY_TEXT, firstSPRF);
        db.insert(TABLE_NAME,null,values);


    }

    public String getFirstSPRF(){
        SQLiteDatabase db= getReadableDatabase();
        String query="SELECT usertext FROM texts_table WHERE id=1";
        Cursor data= db.rawQuery(query,new String[]{});
        data.moveToNext();

        return data.getString(0);
    }

    public void updateLoginPassword(String userText,int id){
        SQLiteDatabase db = getWritableDatabase();
        String query="UPDATE texts_table SET usertext= '"+userText+"' WHERE ID="+id;
        db.execSQL(query);

    }

    public void insertLogo(String name, int path){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("name",name);
        values.put("path",path);
        db.insert("logos_table",null,values);

    }

//    public Cursor getLogoPath(String name){
//        SQLiteDatabase db= getWritableDatabase();
//        String query= "SELECT path FROM logos_table WHERE name='"+name+"'";
//        Cursor cursor;
//        cursor= db.rawQuery(query,new String[]{});
//        return cursor;
//
//    }

    public int getImageResource(String name){
        SQLiteDatabase db= getReadableDatabase();
        String query= "SELECT path FROM logos_table WHERE name='"+name.toLowerCase().trim()+"'";
        Cursor cursor;
        cursor= db.rawQuery(query,new String[]{});

        if(cursor.getCount()>0){
            cursor.moveToNext();
            int path;
            path= cursor.getInt(0);
            return path;
        }
        else{
            return R.drawable.ic_otherss___copy;
        }


    }

    public void deleteAllData(){
        String sprf= getFirstSPRF();
        SQLiteDatabase db= getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS texts_table");
        String query= "CREATE TABLE texts_table(ID INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT, username  TEXT,usertext TEXT,url TEXT,notes TEXT,created_time TEXT,modified_time,category,favorite TEXT)";
        db.execSQL(query);
        addFirstSPRF(sprf);


    }




}
