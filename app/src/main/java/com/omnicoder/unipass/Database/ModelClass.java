package com.omnicoder.unipass.Database;

public class  ModelClass  {
    // Variables of Passwords //
    private int pID;
    private String pTitle;
    private String username;
    private String userText;
    private String url;
    private String pNotes;
    private String pCreated_time;
    private String pModified_time;
    private String category;
    private String pFavorite;

    // Variables of Notes //
    private int nID;
    private String nTitle;
    private String nNotes;
    private String nCreated_time;
    private String nModified_time;
    private boolean nFavorite;


    // Constructors for Passwords //

    public ModelClass(String pTitle, int id,String username, String pNotes, String pCreated_time, String pModified_time, String category) {
        this.pTitle= pTitle;
        this.pID = id;
        this.username= username;
        this.pNotes = pNotes;
        this.pCreated_time= pCreated_time;
        this.pModified_time = pModified_time;
        this.category= category;
    }

    public ModelClass(String pTitle, String username, String userText,String url, String pNotes, String pCreated_time, String pModified_time, String category, String pFavorite) {
        this.pTitle= pTitle;
        this.username= username;
        this.userText= userText;
        this.url= url;
        this.pNotes = pNotes;
        this.category= category;
        this.pFavorite= pFavorite;
        this.pCreated_time= pCreated_time;
        this.pModified_time= pModified_time;

    }
    public ModelClass(int pID, String pTitle, String username, String userText,String url, String pNotes,String pModified_time, String category, String pFavorite) {
        this.pID= pID;
        this.pTitle= pTitle;
        this.username= username;
        this.userText= userText;
        this.url= url;
        this.pNotes = pNotes;
        this.category= category;
        this.pFavorite= pFavorite;
        this.pModified_time= pModified_time;

    }

    public ModelClass(int id,String pTitle,String username, String created,String modified) {
        this.pID= id;
        this.pTitle= pTitle;
        this.username= username;
        this.pCreated_time=created;
        this.pModified_time= modified;
    }

    public ModelClass(int pID,String username, String pTitle, String pFavorite) {
        this.pID= pID;
        this.pTitle= pTitle;
        this.username= username;
        this.pFavorite= pFavorite;
    }

    public ModelClass(){

    }


    public String getpCreated_time() {
        return pCreated_time;
    }

    public String getpModified_time() {
        return pModified_time;
    }

    public String getnCreated_time() {
        return nCreated_time;
    }

    public String getnModified_time() {
        return nModified_time;
    }

    public int getpID() {
        return pID;
    }

    public String getpTitle() {
        return pTitle;
    }

    public String getUsername() {
        return username;
    }

    public String getUserText() {
        return userText;
    }

    public String getpNotes() {
        return pNotes;
    }

    public String getCategory() {
        return category;
    }

    public String getpFavorite() {
        return pFavorite;
    }

    public String getUrl() {
        return url;
    }


    public int getnID() {
        return nID;
    }

    public String getnTitle() {
        return nTitle;
    }

    public String getnNotes() {
        return nNotes;
    }


    public boolean getnFavorite() {
        return nFavorite;
    }
}
