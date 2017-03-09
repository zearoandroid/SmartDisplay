package com.zearoconsulting.smartdisplay.presentation.presenter;

import com.zearoconsulting.smartdisplay.presentation.model.KOTHeader;

/**
 * Created by saravanan on 28-11-2016.
 */

public abstract interface ITokenSelectedListener {

    public abstract void OnTokenSelectedListener(KOTHeader tokenEntity);
}
