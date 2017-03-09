package com.zearoconsulting.smartdisplay.presentation.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.zearoconsulting.smartdisplay.AndroidApplication;
import com.zearoconsulting.smartdisplay.data.AppDataManager;
import com.zearoconsulting.smartdisplay.data.DBHelper;
import com.zearoconsulting.smartdisplay.domain.parser.JSONParser;
import com.zearoconsulting.smartdisplay.presentation.view.component.ReboundListener;

/**
 * Created by saravanan on 20-10-2016.
 */

public class AbstractDialogFragment extends DialogFragment {

    public AppDataManager mAppManager;
    public DBHelper mDBHelper;
    public JSONParser mParser;

    private SpringSystem mSpringSystem;
    public Spring mSpring;
    private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(100, 30);
    public ReboundListener mReboundListener;
    public ProgressDialog mProDlg;

    /*
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppManager = AndroidApplication.getInstance().getAppManager();
        mDBHelper = AndroidApplication.getInstance().getDBHelper();

        mParser = new JSONParser(AndroidApplication.getAppContext(), mAppManager, mDBHelper);

        mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        mSpring.setSpringConfig(ORIGAMI_SPRING_CONFIG);

    }
}
