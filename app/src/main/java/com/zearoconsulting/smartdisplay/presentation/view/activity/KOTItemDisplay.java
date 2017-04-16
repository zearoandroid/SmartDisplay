package com.zearoconsulting.smartdisplay.presentation.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.zearoconsulting.smartdisplay.R;
import com.zearoconsulting.smartdisplay.data.AppLog;
import com.zearoconsulting.smartdisplay.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartdisplay.presentation.model.KOTHeader;
import com.zearoconsulting.smartdisplay.presentation.model.Terminals;
import com.zearoconsulting.smartdisplay.presentation.presenter.ITokenDeletedListener;
import com.zearoconsulting.smartdisplay.presentation.presenter.ITokenSelectedListener;
import com.zearoconsulting.smartdisplay.presentation.view.adapter.TokenAdapter;
import com.zearoconsulting.smartdisplay.presentation.view.component.GridSpacingItemDecoration;
import com.zearoconsulting.smartdisplay.presentation.view.dialogs.NetworkErrorDialog;
import com.zearoconsulting.smartdisplay.utils.AppConstants;
import com.zearoconsulting.smartdisplay.utils.NetworkUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class KOTItemDisplay extends DMBaseActivity implements ITokenDeletedListener.OnTokenDeletedListener {

    private Context mContext;
    private RecyclerView mTokensView;
    List<KOTHeader> mKOTHeaderList;
    private StaggeredGridLayoutManager mStagGridManager;
    private DefaultItemAnimator animator;
    private Menu optionsMenu;
    Handler updateHandler = new Handler();
    Runnable runnable;
    ToneGenerator mToneGen;

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
                    AppConstants.isKOTParsing = true;
                    break;
                case AppConstants.POST_TERMINAL_KOT_FLAGS:
                    mParser.parseKOTResponse(jsonStr,mHandler);
                    break;
                case AppConstants.POST_DELIVERED_KOT_FLAGS:
                    mParser.parseKOTDeliveredResponse(jsonStr,mHandler);
                    break;
                case AppConstants.POST_KOT_DATA_RESPONSE:
                    mProDlg.dismiss();
                    displayKOTData();
                    break;
                case AppConstants.NO_DATA_RECEIVED:
                    mProDlg.dismiss();
                    AppConstants.isKOTParsing = true;
                    break;
                case AppConstants.SERVER_ERROR:
                    AppConstants.isKOTParsing = true;
                    if(mProDlg.isShowing())
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

        if(mAppManager.getTerminalID() == 0)
            setTitle("Smart Display - Completed KOT");
        else{
            Terminals terminal = mDBHelper.getTerminalData(mAppManager.getClientID(),mAppManager.getOrgID(),mAppManager.getTerminalID());
            setTitle("Smart Display - "+terminal.getTerminalName());
        }

        setContentView(R.layout.activity_kotitem_display);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mContext = this;
        animator = new DefaultItemAnimator();

        mTokensView = (RecyclerView)findViewById(R.id.tokenRecyclerView);
        mTokensView.setHasFixedSize(true);
        mStagGridManager = new StaggeredGridLayoutManager(4, 1);
        mTokensView.setLayoutManager(mStagGridManager);

        int spanCount =4;
        int spacing = 10;
        boolean includeEdge = true;
        mTokensView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        animator.setRemoveDuration(1000);
        mTokensView.setItemAnimator(animator);

        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;

        mDBHelper.deleteKOTTable();

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

        mToneGen = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);


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

            //register the order state listener
            ITokenDeletedListener.getInstance().setListener(this);

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
        if (NetworkUtil.isOnline()) {
            try {

                //mProDlg.setMessage("Getting tables status...");
                //mProDlg.show();
                AppLog.e("Internet Connection", "Good! Connected to Internet");
                JSONObject mJsonObj = mParser.getParams(AppConstants.GET_KOT_HEADER_AND_lINES);
                Log.i("KOTJson", mJsonObj.toString());

                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_KOT_HEADER_AND_lINES);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AppLog.e("Internet Connection", "Sorry! Not connected to internet");
            //show network failure dialog or toast
            showNetworkErrorDialog();
        }
    }

    private void showNetworkErrorDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);

        // set title
        alertDialogBuilder.setTitle("Warning");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please Check Your Internet Connection!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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
            showNetworkErrorDialog();
        }
    }

    public void updateKOTDelivered(long kotNumber){
        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {

                mProDlg.setMessage("Posting kot status...");
                mProDlg.show();

                JSONObject mJsonObj = mParser.getParams(AppConstants.POST_DELIVERED_KOT_FLAGS);
                mJsonObj.put("terminalId", mAppManager.getTerminalID());
                mJsonObj.put("KOTNumber", kotNumber);

                Log.i("KOTJson", mJsonObj.toString());

                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.POST_DELIVERED_KOT_FLAGS);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //show network failure dialog or toast
            showNetworkErrorDialog();
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

            //mToneGen.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
            mp.start();

        }else if(mKOTHeaderList.size() == 0){
            mTokensView.setAdapter(null);
            mTokenAdapter = null;
        }else{
            List<KOTHeader> exitList = mTokenAdapter.getExistsTokenList();
            if(exitList!=null){
                if(mKOTHeaderList.size()>exitList.size()){
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
                    mp.start();
                }
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_display, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                Intent mIntent = new Intent(KOTItemDisplay.this, ManualSyncActivity.class);
                startActivity(mIntent);
                finish();
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return false;
        }
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
                mAppManager.setLoggedIn(false);
                Intent mIntent = new Intent(KOTItemDisplay.this, MainActivity.class);
                startActivity(mIntent);
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

    @Override
    public void onTokenDeleted() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
    }
}
