package com.zearoconsulting.smartdisplay.presentation.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.zearoconsulting.smartdisplay.R;
import com.zearoconsulting.smartdisplay.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartdisplay.presentation.view.activity.KOTItemDisplay;
import com.zearoconsulting.smartdisplay.presentation.view.activity.MainActivity;
import com.zearoconsulting.smartdisplay.presentation.view.activity.ManualSyncActivity;
import com.zearoconsulting.smartdisplay.presentation.view.dialogs.NetworkErrorDialog;
import com.zearoconsulting.smartdisplay.utils.AppConstants;
import com.zearoconsulting.smartdisplay.utils.NetworkUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingDialogFragment extends AbstractDialogFragment {


    private static Context context;
    private String username, password, screen;
    private TextView statusText, progressText;
    private ProgressWheel progressWheel;
    private List<Long> mDefaultIdList;
    private long mCategoryId = 0;
    private  Intent mIntent;

    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.getData().getInt("Type");
            String jsonStr = msg.getData().getString("OUTPUT");

            switch (type) {
                case AppConstants.GET_ORGANIZATION_DATA:
                    mParser.parseOrgJson(jsonStr, mHandler);
                    break;
                case AppConstants.CALL_AUTHENTICATE:
                    mParser.parseLoginJson(jsonStr, mHandler);
                    break;
                case AppConstants.ORGANIZATION_DATA_RECEIVED:
                    authenticate();
                    break;
                case AppConstants.LOGIN_SUCCESS:
                    getDefaultCustomerData();
                    break;
                case AppConstants.LOGIN_FAILURE:
                    progressWheel.stopSpinning();
                    Toast.makeText(context, "Invalid user name or password", Toast.LENGTH_SHORT).show();
                    dismissAllowingStateLoss();
                    break;
                case AppConstants.GET_CASH_CUSTOMER_DATA:
                    mParser.parseCommonJson(jsonStr, mHandler);
                    break;
                case AppConstants.COMMON_DATA_RECEIVED:
                    if (mAppManager.getUserName().equalsIgnoreCase(username) && mDBHelper.getAllProduct(mAppManager.getClientID(),mAppManager.getOrgID()).size() != 0) {
                        mAppManager.setLoggedIn(true);
                        progressWheel.stopSpinning();
                        dismissAllowingStateLoss();
                    } else {
                        mDBHelper.deletePOSRelatedTables();
                        getTables();
                    }
                    break;
                case AppConstants.GET_TABLES:
                    mParser.parseTables(jsonStr, mHandler);
                    break;
                case AppConstants.TABLES_RECEIVED:
                    getTerminals();
                    break;
                case AppConstants.GET_TERMINALS:
                    mParser.parseTerminals(jsonStr, mHandler);
                    break;
                case AppConstants.TERMINALS_RECEIVED:
                    getCategory();
                    break;
                case AppConstants.GET_CATEGORY:
                    mParser.parseCategorysJson(jsonStr, mHandler);
                    break;
                case AppConstants.CATEGORY_RECEIVED:
                    //progressWheel.stopSpinning();
                    getAllProducts();
                    break;
                case AppConstants.GET_ALL_PRODUCTS:
                    mParser.parseProductJson(jsonStr, mHandler);
                    break;
                case AppConstants.PRODUCTS_RECEIVED:
                    mAppManager.setLoggedIn(true);
                    if (screen.equalsIgnoreCase("LOGIN")) {
                        ((MainActivity) LoadingDialogFragment.this.getActivity()).updateTerminalSpinner();
                    } else if (screen.equalsIgnoreCase("SYNC")) {
                        Intent intent = new Intent(getActivity(), KOTItemDisplay.class);
                        startActivity(intent);
                    }
                    dismissAllowingStateLoss();
                    break;
                case AppConstants.NO_DATA_RECEIVED:
                    progressWheel.stopSpinning();
                    dismissAllowingStateLoss();
                    break;
                case AppConstants.SERVER_ERROR:
                    progressWheel.stopSpinning();
                    Toast.makeText(context, "Server data error", Toast.LENGTH_SHORT).show();
                    dismissAllowingStateLoss();
                    break;
                case AppConstants.DEVICE_NOT_REGISTERED:
                    progressWheel.stopSpinning();
                    Toast.makeText(context, "Device not registered to server!", Toast.LENGTH_SHORT).show();
                    dismissAllowingStateLoss();
                    break;
                default:
                    break;
            }
        }
    };

    public LoadingDialogFragment() {
        // Required empty public constructor
    }

    public static LoadingDialogFragment newInstance(Context paramContext, String paramString1, String paramString2, String paramString3) {
        LoadingDialogFragment localLoadingDialogFragment = new LoadingDialogFragment();
        Bundle localBundle = new Bundle();
        localBundle.putString("username", paramString1);
        localBundle.putString("password", paramString2);
        localBundle.putString("screen", paramString3);
        localLoadingDialogFragment.setArguments(localBundle);
        context = paramContext;
        return localLoadingDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading_dialog, container, false);
    }

    @Override
    public void onViewCreated(View paramView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(paramView, savedInstanceState);

        this.username = getArguments().getString("username", "");
        this.password = getArguments().getString("password", "");
        this.screen = getArguments().getString("screen", "");

        getDialog().getWindow().setSoftInputMode(3);
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCanceledOnTouchOutside(false);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,android.view.KeyEvent event) {

                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    //Stop back event here!!!
                    return true;
                }
                else
                    return false;
            }
        });

        this.statusText = ((TextView) paramView.findViewById(R.id.status_text));
        this.progressText = ((TextView) paramView.findViewById(R.id.progress_text));
        this.progressWheel = ((ProgressWheel) paramView.findViewById(R.id.progress_wheel));
        this.progressWheel.spin();
        this.statusText.setText("Please wait");

        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;

        if (screen.equalsIgnoreCase("LOGIN")) {
            retrieveTabletMenu();
        } else if (screen.equalsIgnoreCase("SYNC")) {
            getTables();
        }
    }

    private void retrieveTabletMenu() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_ORGANIZATION_DATA);

            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_ORGANIZATION_DATA);
            thread.start();
        } else {
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    private void getCategory() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            //GET THE CATEGORY DATA
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_CATEGORY);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_CATEGORY);
            thread.start();
        } else {
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    private void getAllProducts() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {
                JSONObject mJsonObj = mParser.getParams(AppConstants.GET_ALL_PRODUCTS);
                mJsonObj.put("categoryId", mCategoryId);
                mJsonObj.put("pricelistId", mAppManager.getPriceListID());
                mJsonObj.put("costElementId", mAppManager.getCostElementID());
                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_ALL_PRODUCTS);
                thread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    private void authenticate(){
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {
                mDefaultIdList = mDBHelper.getDefaultIds();
                mAppManager.setOrgID(mDefaultIdList.get(0));
                mAppManager.setWarehouseID(mDefaultIdList.get(1));
                mAppManager.setRoleID(mDefaultIdList.get(2));
                //mAppManager.setRoleName(mDBHelper.getRoleName(mDefaultIdList.get(2)));

                //call authenticate api
                JSONObject mJsonObj = mParser.getParams(AppConstants.CALL_AUTHENTICATE);

                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL,"",mHandler,mJsonObj.toString(),AppConstants.CALL_AUTHENTICATE);
                thread.start();
            }catch (Exception e){
                e.printStackTrace();
                progressWheel.stopSpinning();
            }

        }else{
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    private void getDefaultCustomerData() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_CASH_CUSTOMER_DATA);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_CASH_CUSTOMER_DATA);
            thread.start();
        } else {
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    public void getTables() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TABLES);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_TABLES);
            thread.start();
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    public void getTerminals() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TERMINALS);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_TERMINALS);
            thread.start();
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }
}
