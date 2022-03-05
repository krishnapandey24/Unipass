package com.omnicoder.unipass.Others;

import android.content.Context;

import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.R;


public class LoginLogos {
    Context context;
    public LoginLogos(Context context){
        this.context= context;

    }

    public void addLogo(){
        DBHandler db= new DBHandler(context);
        db.insertLogo("amazon", R.drawable.ic_amazon);
        db.insertLogo("apple",R.drawable.ic_apple);
        db.insertLogo("discord",R.drawable.ic_discord);
        db.insertLogo("dropbox",R.drawable.ic_dropbox);
        db.insertLogo("disneyplushotstar",R.drawable.disneyhotstar);
        db.insertLogo("facebook",R.drawable.ic_facebook);
        db.insertLogo("gaana",R.drawable.ic_gaana);
        db.insertLogo("google",R.drawable.googlelogo);
        db.insertLogo("instagram",R.drawable.ic_instagram);
        db.insertLogo("linkedin",R.drawable.ic_linkedin);
        db.insertLogo("messenger",R.drawable.ic_messenger);
        db.insertLogo("microsoft",R.drawable.ic_microsoft);
        db.insertLogo("netflix",R.drawable.ic_netflix);
        db.insertLogo("paytm",R.drawable.ic_paytm);
        db.insertLogo("phonepe",R.drawable.ic_phonepe);
        db.insertLogo("reddit",R.drawable.ic_reddit);
        db.insertLogo("sbi",R.drawable.ic_sbi);
        db.insertLogo("snapchat",R.drawable.ic_snapchat);
        db.insertLogo("spotify",R.drawable.ic_spotify);
        db.insertLogo("swiggy",R.drawable.ic_swiggy);
        db.insertLogo("telegram",R.drawable.ic_telegram);
        db.insertLogo("twitter",R.drawable.ic_twitter);
        db.insertLogo("zoom",R.drawable.ic_zoom);
        db.insertLogo("zomato",R.drawable.ic_zomato);
        db.insertLogo("twitch",R.drawable.ic_twitch);
        db.insertLogo("pubg",R.drawable.ic_pubg_1);
        db.insertLogo("signal",R.drawable.signal);
        db.insertLogo("tinder",R.drawable.tinder);
        db.insertLogo("bhimupi",R.drawable.ic_bhim_upi);
        db.insertLogo("airtel",R.drawable.airtel);
        db.insertLogo("bookmyshow",R.drawable.bookmyshow);
        db.insertLogo("Hotstar",R.drawable.hotstar);

    }

}
