package com.zearoconsulting.smartdisplay;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.zearoconsulting.smartdisplay.data.AppDataManager;
import com.zearoconsulting.smartdisplay.data.DBHelper;
import com.zearoconsulting.smartdisplay.domain.receivers.ConnectivityReceiver;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by saravanan on 31-10-2016.
 */

public class AndroidApplication extends Application {

    private static AndroidApplication sInstance;
    private static AppDataManager mManager;
    private static DBHelper mDBHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/GothamRounded-Medium.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    public static AndroidApplication getInstance(){
        if (sInstance == null) {
            sInstance = new AndroidApplication();
        }
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    public AppDataManager getAppManager(){

        if(mManager == null){
            mManager = new AppDataManager(getApplicationContext());
        }

        return mManager;
    }

    public DBHelper getDBHelper(){

        if(mDBHelper == null){
            mDBHelper = new DBHelper(getApplicationContext());
        }

        return mDBHelper;
    }

    public static Typeface getGothamRoundedLight(){
        return Typeface.createFromAsset( sInstance.getApplicationContext().getAssets(),
                "fonts/GothamRounded-Light.otf");
    }

    public static Typeface getGothamRoundedMedium(){
        return Typeface.createFromAsset( sInstance.getApplicationContext().getAssets(),
                "fonts/GothamRounded-Medium.otf");
    }

    public static Typeface getGothamRoundedBold(){
        return Typeface.createFromAsset( sInstance.getApplicationContext().getAssets(),
                "fonts/GothamRounded-Bold.otf");
    }

    public static Typeface getGothamRoundedBook(){
        return Typeface.createFromAsset( sInstance.getApplicationContext().getAssets(),
                "fonts/GothamRounded-Book.otf");
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
