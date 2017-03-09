package com.zearoconsulting.smartdisplay.presentation.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Message;
import com.zearoconsulting.smartdisplay.R;
import com.zearoconsulting.smartdisplay.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartdisplay.presentation.model.Product;
import com.zearoconsulting.smartdisplay.presentation.model.Terminals;
import com.zearoconsulting.smartdisplay.presentation.view.adapter.TerminalSpinner;
import com.zearoconsulting.smartdisplay.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartdisplay.presentation.view.dialogs.NetworkErrorDialog;
import com.zearoconsulting.smartdisplay.presentation.view.fragment.LoadingDialogFragment;
import com.zearoconsulting.smartdisplay.presentation.view.fragment.ServerConfigFragment;
import com.zearoconsulting.smartdisplay.utils.AppConstants;
import com.zearoconsulting.smartdisplay.utils.NetworkUtil;

import org.json.JSONObject;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends DMBaseActivity {

    private EditText mEdtUserName;
    private EditText mEdtUserPassword;
    private Button mLoginBtn;
    private String username,password;
    private static final int PERMISSION_REQUEST_CODE = 1;

    TerminalSpinner mTerminalAdapter;
    List<Terminals> mTerminalList ;
    Spinner mSpnTerminal;
    private long mTerminalId = 0;
    int mCurrentTerminalSelection;
    private ImageView mImgCofig;

    final Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            int type  = msg.getData().getInt("Type");
            String jsonStr = msg.getData().getString("OUTPUT");

            switch (type){
                case AppConstants.CALL_AUTHENTICATE:
                    mParser.parseLoginJson(jsonStr,mHandler);
                    break;
                case AppConstants.NO_DATA_RECEIVED:
                    mProDlg.dismiss();
                    break;
                case AppConstants.LOGIN_SUCCESS:
                    mProDlg.dismiss();
                    mAppManager.setTerminalID(mTerminalId);
                    //goto intent
                    Intent intent = new Intent(MainActivity.this, KOTItemDisplay.class);
                    startActivity(intent);
                    break;
                case AppConstants.LOGIN_FAILURE:
                    mProDlg.dismiss();
                    Toast.makeText(MainActivity.this, "Invalid user name or password", Toast.LENGTH_SHORT).show();
                    //clear all data and update spinner
                    mTerminalList.clear();
                    mSpnTerminal.setAdapter(null);
                case AppConstants.SERVER_ERROR:
                    mProDlg.dismiss();
                    //show the server error dialog
                    Toast.makeText(MainActivity.this,"Server data error",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mReboundListener = new ReboundListener();
        mEdtUserName= (EditText) findViewById(R.id.editTextUser);
        mEdtUserPassword= (EditText) findViewById(R.id.editTextPassword);
        mSpnTerminal = (Spinner)findViewById(R.id.spinnerTerminal);
        mImgCofig = (ImageView)findViewById(R.id.configImgView);
        mLoginBtn = (Button)findViewById(R.id.btnSignIn);

        mProDlg = new ProgressDialog(this);
        mProDlg.setIndeterminate(true);
        mProDlg.setCancelable(false);

        //check server address is already available or not
        if(mAppManager.getServerAddress().equals("")){
            showConfiguration();
        }

        mSpnTerminal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (mCurrentTerminalSelection != position && mTerminalList.size()!=0){
                    mTerminalId = mTerminalList.get(position).getTerminalId();
                }
                mCurrentTerminalSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mCurrentTerminalSelection = mSpnTerminal.getSelectedItemPosition();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                Log.e("value", "Permission already Granted, Now you can save image.");
            } else {
                requestPermission();
            }
        } else {
            Log.e("value", "Not required for requesting runtime permission");
        }

        mImgCofig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfiguration();
            }
        });

        // Add an OnTouchListener to the root view.
        mLoginBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mLoginBtn);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // When pressed start solving the spring to 1.
                        mSpring.setEndValue(1);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // When released start solving the spring to 0.
                        mSpring.setEndValue(0);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                signIn();
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mAppManager.getUserName().equals("")){
            mEdtUserName.setText(mAppManager.getUserName());
            mEdtUserPassword.setText(mAppManager.getUserPassword());
        }
        //Add a listener to the spring
        mSpring.addListener(mReboundListener);

        //check the terminal table and update the spinner
        mTerminalList = mDBHelper.getTerminals();
        if(mTerminalList.size()!=0)
            updateTerminalSpinner();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //Remove a listener to the spring
        mSpring.removeListener(mReboundListener);
    }


    private void signIn(){
        //Crashlytics.log("Login");
        username = mEdtUserName.getText().toString().trim();
        password = mEdtUserPassword.getText().toString().trim();

        mDBHelper.deleteKOTTable();

        List<Product> productList = mDBHelper.getAllProduct(mAppManager.getClientID(),mAppManager.getOrgID());

        if(mEdtUserName.getText().toString().trim().equals("")){
            mEdtUserName.setError("Username should not be empty");
        }else if(mEdtUserPassword.getText().toString().trim().equals("")){
            mEdtUserPassword.setError("Password should not be empty");
        }
        else if (!mAppManager.getUserName().equalsIgnoreCase(username)){
            mDBHelper.deleteAllTables();
            mAppManager.clearUserSessionData();
            mAppManager.setUsername(username);
            mAppManager.setPassword(password);
            showLoading();
        }else if(mTerminalList.size()!=0 && productList.size()!=0){
            mTerminalId = mTerminalList.get(mCurrentTerminalSelection).getTerminalId();
            authenticate();
        }else{
            mDBHelper.deleteOrgRelatedTables();
            mAppManager.clearUserSessionData();
            mAppManager.setUsername(username);
            mAppManager.setPassword(password);
            showLoading();
        }
    }


    private void showConfiguration()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ServerConfigFragment serverConfigFragment = new ServerConfigFragment();
        serverConfigFragment.show(fragmentManager, "ServerConfigFragment");
    }

    private void showLoading()
    {
        //check server address is already available or not
        if(mAppManager.getServerAddress().equals("")){
            Toast.makeText(MainActivity.this,"Please config server details...",Toast.LENGTH_SHORT).show();
        }else{
            FragmentManager localFragmentManager = getSupportFragmentManager();
            LoadingDialogFragment.newInstance(MainActivity.this, username, password).show(localFragmentManager, "loading");
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can save image .");
                } else {
                    Log.e("value", "Permission Denied, You cannot save image.");
                }
                break;
        }
    }


    public void updateTerminalSpinner(){
        mTerminalList = mDBHelper.getTerminals();
        if(mTerminalList.size()!=0) {
            mTerminalAdapter = new TerminalSpinner(this, mTerminalList);
            mSpnTerminal.setAdapter(mTerminalAdapter);
            mTerminalAdapter.notifyDataSetChanged();
        }
    }

    private void authenticate(){
        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {
                mProDlg.setMessage("Authenticating...");
                mProDlg.show();
                //call authenticate api
                AppConstants.isKOTParsing = false;

                JSONObject mJsonObj = mParser.getParams(AppConstants.CALL_AUTHENTICATE);

                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL,"",mHandler,mJsonObj.toString(),AppConstants.CALL_AUTHENTICATE);
                thread.start();
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            mProDlg.dismiss();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(MainActivity.this).show();
        }
    }

}
