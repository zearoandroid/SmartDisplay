package com.zearoconsulting.smartdisplay.presentation.view.fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.zearoconsulting.smartdisplay.AndroidApplication;
import com.zearoconsulting.smartdisplay.data.AppDataManager;
import com.zearoconsulting.smartdisplay.data.DBHelper;
import com.zearoconsulting.smartdisplay.domain.parser.JSONParser;

/**
 * Created by saravanan on 26-05-2016.
 */
public class AbstractFragment extends Fragment {

    public AppDataManager mAppManager;
    public DBHelper mDBHelper;
    public JSONParser mParser;
    public ProgressDialog mProDlg;

    public Typeface mGothamLight,mGothamMedium,mGothamBold,mGothamBook;

    private SpringSystem mSpringSystem;
    public Spring mSpring;

    /*
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppManager = AndroidApplication.getInstance().getAppManager();
        mDBHelper = AndroidApplication.getInstance().getDBHelper();

        mParser = new JSONParser(AndroidApplication.getAppContext(), mAppManager, mDBHelper);

        mGothamMedium = AndroidApplication.getGothamRoundedMedium();
        /*mGothamLight = AndroidApplication.getGothamRoundedLight();

        mGothamBold = AndroidApplication.getGothamRoundedBold();
        mGothamBook = AndroidApplication.getGothamRoundedBook();*/

        mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
    }

}
