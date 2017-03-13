package com.zearoconsulting.smartdisplay.presentation.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.zearoconsulting.smartdisplay.R;
import com.zearoconsulting.smartdisplay.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartdisplay.presentation.model.KOTHeader;
import com.zearoconsulting.smartdisplay.presentation.presenter.ITokenSelectedListener;
import com.zearoconsulting.smartdisplay.presentation.view.adapter.TokenAdapter;
import com.zearoconsulting.smartdisplay.presentation.view.dialogs.NetworkErrorDialog;
import com.zearoconsulting.smartdisplay.utils.AppConstants;
import com.zearoconsulting.smartdisplay.utils.NetworkUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class KOTItemDisplay extends DMBaseActivity {

    private Context mContext;
    private RecyclerView mTokensView;
    List<KOTHeader> mKOTHeaderList;
    private StaggeredGridLayoutManager mStagGridManager;
    private DefaultItemAnimator animator;

    Handler updateHandler = new Handler();
    Runnable runnable;

    private TokenAdapter mTokenAdapter = null;

    final Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            int type  = msg.getData().getInt("Type");
            String jsonStr = msg.getData().getString("OUTPUT");

            switch (type){
                case AppConstants.GET_KOT_HEADER_AND_lINES:
                    if(!AppConstants.isKOTParsing){
                        mParser.parseKOTData(jsonStr, mHandler);
                    }
                    break;
                case AppConstants.KOT_DATA_AVAILABLE:
                    displayKOTData();
                    break;
                case AppConstants.NO_KOT_DATA_AVAILABLE:
                    break;
                case AppConstants.POST_TERMINAL_KOT_FLAGS:
                    mParser.parseKOTResponse(jsonStr,mHandler);
                    break;
                case AppConstants.POST_KOT_DATA_RESPONSE:
                    mProDlg.dismiss();
                    displayKOTData();
                    break;
                case AppConstants.NO_DATA_RECEIVED:
                    mProDlg.dismiss();
                    break;
                case AppConstants.SERVER_ERROR:
                    mProDlg.dismiss();
                    Toast.makeText(KOTItemDisplay.this,"Server data error",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    ITokenSelectedListener mTokenSelectListener = new ITokenSelectedListener() {
        @Override
        public void OnTokenSelectedListener(KOTHeader tokenEntity) {
            updateKOTComplete(tokenEntity.getKotNumber());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kotitem_display);

        mContext = this;
        animator = new DefaultItemAnimator();

        mTokensView = (RecyclerView)findViewById(R.id.tokenRecyclerView);
        mTokensView.setHasFixedSize(true);
        mStagGridManager = new StaggeredGridLayoutManager(4, 1);
        mTokensView.setLayoutManager(mStagGridManager);

        animator.setRemoveDuration(1000);
        mTokensView.setItemAnimator(animator);

        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;

        mKOTHeaderList = mDBHelper.getKOTHeaders(mAppManager.getTerminalID());

        //if(mKOTHeaderList.size()!=0) {
            mTokenAdapter = new TokenAdapter(mContext, mDBHelper, mAppManager, mKOTHeaderList, mAppManager.getTerminalID());
            mTokensView.setAdapter(mTokenAdapter);
            mTokenAdapter.notifyDataSetChanged();
        //}

        //set the table select listener
        mTokenAdapter.setOnTokenSelectListener(mTokenSelectListener);

        mProDlg = new ProgressDialog(this);
        mProDlg.setIndeterminate(true);
        mProDlg.setCancelable(false);

        //Every 10 seconds app should check the data of terminal
        runnable = new Runnable(){
            public void run() {

                getTerminalKOTDetails(); // some action(s)

                updateHandler.postDelayed(this, 10* 1000);
            }
        };
        //updateHandler.postDelayed(runnable, 5000);

    }

    @Override
    protected void onResume() {
        try {
            updateHandler.postDelayed(runnable, 5000);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        updateHandler.removeCallbacks(runnable);
        super.onPause();
    }

    private void getTerminalKOTDetails(){
        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {

                //mProDlg.setMessage("Getting tables status...");
                //mProDlg.show();

                JSONObject mJsonObj = mParser.getParams(AppConstants.GET_KOT_HEADER_AND_lINES);
                Log.i("KOTJson", mJsonObj.toString());

                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_KOT_HEADER_AND_lINES);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(KOTItemDisplay.this).show();
        }
    }

    public void updateKOTComplete(long kotNumber){
        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {

                mProDlg.setMessage("Posting kot status...");
                mProDlg.show();

                JSONObject mJsonObj = mParser.getParams(AppConstants.POST_TERMINAL_KOT_FLAGS);
                mJsonObj.put("terminalId", mAppManager.getTerminalID());
                mJsonObj.put("KOTNumber", kotNumber);

                Log.i("KOTJson", mJsonObj.toString());

                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.POST_TERMINAL_KOT_FLAGS);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(KOTItemDisplay.this).show();
        }
    }

    private void displayKOTData(){

        AppConstants.isKOTParsing = false;

        if(mAppManager.getTerminalID()!=0)
            mKOTHeaderList = mDBHelper.getKOTHeaders(mAppManager.getTerminalID());
        else
            mKOTHeaderList = mDBHelper.getAllCompletedKOTHeaders();

        if(mTokenAdapter == null && mKOTHeaderList.size()!=0){
            mTokenAdapter = new TokenAdapter(mContext, mDBHelper, mAppManager, mKOTHeaderList, mAppManager.getTerminalID());
            mTokensView.setAdapter(mTokenAdapter);
            mTokenAdapter.notifyDataSetChanged();
        }else if(mKOTHeaderList.size() == 0){
            mTokensView.setAdapter(null);
            mTokenAdapter = null;
        }else{
            mTokenAdapter.refresh(mKOTHeaderList);
            mTokenAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {

        try {
            updateHandler.removeCallbacks(runnable);
        } catch (Exception e){
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    public void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(KOTItemDisplay.this);

        alertDialog.setTitle("Logout"); // Sets title for your alertbox

        alertDialog.setMessage("Are you sure you want to Logout ?"); // Message to be displayed on alertbox

        alertDialog.setIcon(R.mipmap.ic_launcher); // Icon for your alertbox

        /* When positive (yes/ok) is clicked */
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //AppConstants.posID = 0;
                finish();
            }
        });

        /* When negative (No/cancel) button is clicked*/
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
