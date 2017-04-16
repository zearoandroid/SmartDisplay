package com.zearoconsulting.smartdisplay.presentation.presenter;

/**
 * Created by saravanan on 16-04-2017.
 */

public class ITokenDeletedListener {

    public interface OnTokenDeletedListener {
        void onTokenDeleted();
    }

    private static ITokenDeletedListener mInstance;
    private OnTokenDeletedListener mListener;

    private ITokenDeletedListener() {}

    public static ITokenDeletedListener getInstance() {
        if(mInstance == null) {
            mInstance = new ITokenDeletedListener();
        }
        return mInstance;
    }

    public void setListener(OnTokenDeletedListener listener) {
        mListener = listener;
    }

    public void tokenStatus() {
        if(mListener != null) {
            mListener.onTokenDeleted();
        }
    }
}
