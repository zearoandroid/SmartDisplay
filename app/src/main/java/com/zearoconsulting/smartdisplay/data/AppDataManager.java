package com.zearoconsulting.smartdisplay.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.zearoconsulting.smartdisplay.presentation.model.BPartner;

/**
 * Created by saravanan on 24-05-2016.
 */
public class AppDataManager  {

    //local Variables
    private Context mContext;
    private SharedPreferences mUserSessionData,mCashCustomerData, mCommonData, mCustomerData, mPOSNumberData, mServerData;

    //User variables
    private long mUserID, mUserBPID, mClientID, mOrgID, mRoleID, mWarehouseID, mTerminalID;
    private String mUserName, mUserPassword, mRoleName, mAppDownloadPath, mRemindMe;
    private boolean isLoggedIn, isSalesRep;

    //Cash customer variables
    private long mCashCustomerID, mCashCustomerPriceListID;
    private String mCashCustomerName;

    //Customer variables
    private long mCustomerBPId,mCustomerValue,mCustomerNumber,mCustomerPriceId;
    private String mCustomerEmail, mCustomerName;

    //Common variables
    private long mCostElementID, mCurrencyID, mCashBookID, mPeriodID, mPaymentTermID, mAdTableID, mAcctSchemaID;

    //POS Number variables
    private long mStartNo, mEndNo;
    private String mServerAddress;
    private int mServerPort;

    public AppDataManager(Context context){

        mContext = context;
        mUserSessionData = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mCashCustomerData = mContext.getSharedPreferences("CashCustomerInfo", Context.MODE_PRIVATE);
        mCommonData = mContext.getSharedPreferences("CommonInfo", Context.MODE_PRIVATE);
        mCustomerData = mContext.getSharedPreferences("CustomerInfo", Context.MODE_PRIVATE);
        mPOSNumberData = mContext.getSharedPreferences("POSNumberInfo", Context.MODE_PRIVATE);
        mServerData = mContext.getSharedPreferences("ServerInfo", Context.MODE_PRIVATE);

        mUserID = mUserSessionData.getLong("userID", 0);
        mUserBPID = mUserSessionData.getLong("userBPID", 0);
        mClientID = mUserSessionData.getLong("clientID", 0);
        mTerminalID = mUserSessionData.getLong("terminalID", 0);
        mOrgID = mUserSessionData.getLong("orgID", 0);
        mRoleID = mUserSessionData.getLong("roleID", 0);
        mRoleName = mUserSessionData.getString("roleName", "");
        mWarehouseID = mUserSessionData.getLong("warehouseID", 0);
        mUserName = mUserSessionData.getString("username", "");
        mUserPassword = mUserSessionData.getString("password", "");
        isLoggedIn = mUserSessionData.getBoolean("isLoggedIn", false);
        isSalesRep = mUserSessionData.getBoolean("isSalesRep", false);

        mCashCustomerID = mCashCustomerData.getLong("cashCustomerID", 0);
        mCashCustomerPriceListID = mCashCustomerData.getLong("cashCustomerPriceListID", 0);
        mCashCustomerName = mCashCustomerData.getString("cashCustomerName", "");

        mCostElementID = mUserSessionData.getLong("costElementID", 0);
        mCurrencyID = mUserSessionData.getLong("currencyID", 0);
        mCashBookID = mUserSessionData.getLong("cashBookID", 0);
        mPeriodID = mUserSessionData.getLong("periodID", 0);
        mPaymentTermID = mUserSessionData.getLong("paymentTermID", 0);
        mAdTableID = mUserSessionData.getLong("adTableID", 0);
        mAcctSchemaID = mUserSessionData.getLong("acctSchemaID", 0);

        mCustomerBPId = mCustomerData.getLong("customerBPID",0);
        mCustomerName = mCustomerData.getString("customerName","");
        mCustomerValue = mCustomerData.getLong("customerValue",0);
        mCustomerPriceId = mCustomerData.getLong("customerPriceID",0);
        mCustomerEmail = mCustomerData.getString("customerEmail","");
        mCustomerNumber = mCustomerData.getLong("customerNumber",0);

        mStartNo = mPOSNumberData.getLong("startNo", 0);
        mEndNo = mPOSNumberData.getLong("endNo", 0);

        mServerAddress = mServerData.getString("server_name", "");
        mServerPort = mServerData.getInt("server_port", 0);
        mAppDownloadPath = mServerData.getString("app_path", "");
        mRemindMe = mServerData.getString("remindMe", "N");
    }

    public void saveUserData(long userID,long userBPID,long clientID,long orgID,long roleID, long warehouseID,String username,String password, boolean isSalesRep){

        // write userSession detail to UserInfo.xml file
        try {
            SharedPreferences.Editor ed = mUserSessionData.edit();
            ed.putLong("userID", userID);
            ed.putLong("userBPID", userBPID);
            ed.putLong("clinetID", clientID);
            ed.putLong("orgID",orgID);
            ed.putLong("roleID",roleID);
            ed.putLong("warehouseID",warehouseID);
            ed.putString("username",username);
            ed.putString("password",password);
            ed.putBoolean("isSalesRep",isSalesRep);
            ed.putBoolean("isLoggedIn", true);

            ed.commit();
        } catch (NullPointerException npEx) {
            Log.e("UserSession", "Save Usersession NullPointer Exception");
        }
    }

    /**
     *
     * @param serverAddr
     * @param serverPort
     */
    public void saveServerData(String serverAddr,int serverPort){

        // write userSession detail to UserInfo.xml file
        try {
            SharedPreferences.Editor ed = mServerData.edit();
            ed.putString("server_name", serverAddr);
            ed.putInt("server_port", serverPort);
            ed.commit();
        } catch (NullPointerException npEx) {
            Log.e("ServerData", "Save Serverdata NullPointer Exception");
        }
    }

    public void setUsername(String username) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putString("username", username);
        ed.commit();
    }

    public void clearUserSessionData(){
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.clear();
        ed.commit(); // commit changes
    }
    public void setPassword(String password) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putString("password", password);
        ed.commit();
    }

    public void setUserID(long userID) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putLong("userID", userID);
        ed.commit();
    }

    public void setUserBPID(long userBPID) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putLong("userBPID", userBPID);
        ed.commit();
    }

    public void setClientID(long clientID) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putLong("clinetID", clientID);
        ed.commit();
    }

    public void setTerminalID(long terminalID) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putLong("terminalID", terminalID);
        ed.commit();
    }

    public void setOrgID(long orgID) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putLong("orgID", orgID);
        ed.commit();
    }

    public void setRoleID(long roleID) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putLong("roleID", roleID);
        ed.commit();
    }

    public void setRoleName(String roleName) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putString("roleName", roleName);
        ed.commit();
    }

    public void setWarehouseID(long warehouseID) {
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putLong("warehouseID", warehouseID);
        ed.commit();
    }

    public void setSalesRep(boolean isSalesRep){
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putBoolean("isSalesRep", isSalesRep);
        ed.commit();
    }

    public void setLoggedIn(boolean isLoggedIn){
        SharedPreferences.Editor ed = mUserSessionData.edit();
        ed.putBoolean("isLoggedIn", isLoggedIn);
        ed.commit();
    }

    /**
     *
     * @param cashCustomerName
     * @param cashCustomerID
     */
    public void saveCashCustomerData(String cashCustomerName,long cashCustomerID){
        // write userSession detail to CashCustomerInfo.xml file
        try {
            SharedPreferences.Editor ed = mCashCustomerData.edit();
            ed.putString("cashCustomerName",cashCustomerName);
            ed.putLong("cashCustomerID", cashCustomerID);


            ed.commit();
        } catch (NullPointerException npEx) {
            Log.e("CashCustomer", "Save CashCustomer NullPointer Exception");
        }
    }

    /**
     *
     * @param costElementID
     * @param currencyID
     * @param cashBookID
     * @param periodID
     * @param paymentTermID
     * @param adTableID
     * @param acctSchemaID
     * @param priceListID
     */
    public void saveCommonData(long costElementID, long currencyID, long cashBookID,long periodID,long paymentTermID,long adTableID,long acctSchemaID, long priceListID,String currencyCode){
        // write userSession detail to CommonInfo.xml file
        try {
            SharedPreferences.Editor ed = mCommonData.edit();
            ed.putLong("costElementID", costElementID);
            ed.putLong("currencyID", currencyID);
            ed.putLong("cashBookID", cashBookID);
            ed.putLong("periodID",periodID);
            ed.putLong("paymentTermID",paymentTermID);
            ed.putLong("adTableID",adTableID);
            ed.putLong("acctSchemaID",acctSchemaID);
            ed.putLong("priceListID", priceListID);
            ed.putString("currencyCode", currencyCode);

            ed.commit();
        } catch (NullPointerException npEx) {
            Log.e("Common", "Save Common NullPointer Exception");
        }
    }

    /**
     *
     * @param bPartner
     */
    public void saveCustomerData(BPartner bPartner){

        // write selecte customer detail to CustomerInfo.xml file
        try {
            SharedPreferences.Editor ed = mCustomerData.edit();
            ed.putLong("customerBPID", bPartner.getBpId());
            ed.putString("customerName", bPartner.getBpName());
            ed.putLong("customerValue", bPartner.getBpValue());
            ed.putLong("customerPriceID",bPartner.getBpPriceListId());
            ed.putString("customerEmail",bPartner.getBpEmail());
            ed.putLong("customerNumber",bPartner.getBpNumber());

            ed.commit();
        } catch (NullPointerException npEx) {
            Log.e("Customer", "Save Customer NullPointer Exception");
        }
    }

    /**
     *
     * @param startNo
     * @param endNo
     */
    public void savePOSNumberData(Long startNo,Long endNo){
        // write selecte customer detail to CustomerInfo.xml file
        try {
            SharedPreferences.Editor ed = mPOSNumberData.edit();
            ed.putLong("startNo", startNo);
            ed.putLong("endNo", endNo);

            ed.commit();
        } catch (NullPointerException npEx) {
            Log.e("POSNumber", "Save POSNumber NullPointer Exception");
        }
    }

    /**GET USER SESSION DATA*/

    public long getUserID(){
        return mUserSessionData.getLong("userID", 0);
    }

    public String getUserName(){
        return mUserSessionData.getString("username", "");
    }

    public String getUserPassword(){
        return mUserSessionData.getString("password", "");
    }

    public long getUserBPID(){
        return mUserSessionData.getLong("userBPID", 0);
    }

    public long getClientID(){
        return mUserSessionData.getLong("clinetID", 0);
    }

    public long getTerminalID(){
        return mUserSessionData.getLong("terminalID", 0);
    }

    public long getOrgID(){
        return  mUserSessionData.getLong("orgID", 0);
    }

    public long getRoleID(){
        return  mUserSessionData.getLong("roleID", 0);
    }

    public String getRoleName(){
        return  mUserSessionData.getString("roleName", "");
    }

    public long getWarehouseID(){
        return  mUserSessionData.getLong("warehouseID", 0);
    }

    public boolean getUserLoggedIn(){
        return mUserSessionData.getBoolean("isLoggedIn",false);
    }

    public boolean getUserIsSalesRep(){
        return mUserSessionData.getBoolean("isSalesRep",false);
    }

    /**GET CASH CUSTOMER DATA*/

    public long getCashCustomerID(){ return  mCashCustomerData.getLong("cashCustomerID", 0); }

    public String getCashCustomerName(){ return mCashCustomerData.getString("cashCustomerName",""); }

    /**GET COMMON DATA*/

    public long getCostElementID(){
        return mCommonData.getLong("costElementID", 0);
    }

    public long getCurrencyID(){
        return mCommonData.getLong("currencyID", 0);
    }

    public long getCashbookID(){
        return mCommonData.getLong("cashBookID", 0);
    }

    public long getPeriodID(){
        return  mCommonData.getLong("periodID", 0);
    }

    public long getPaymentTermID(){
        return  mCommonData.getLong("paymentTermID", 0);
    }

    public long getAdTableID(){
        return  mCommonData.getLong("adTableID", 0);
    }

    public long getAcctSchemaID(){
        return  mCommonData.getLong("acctSchemaID", 0);
    }

    public long getPriceListID(){
        return  mCommonData.getLong("priceListID", 0);
    }

    public String getCurrencyCode(){
        return mCommonData.getString("currencyCode", "QR.");
    }

    /** GET CUSTOMER DATA */

    public long getCustomerBPId(){ return mCustomerData.getLong("customerBPID",0);}

    public String getCustomerName(){ return mCustomerData.getString("customerName","");}

    public long getCustomerValue(){ return mCustomerData.getLong("customerValue",0);}

    public long getCustomerPriceId(){ return mCustomerData.getLong("customerPriceID",0); }

    public String getCustomerEmail(){ return  mCustomerData.getString("customerEmail",""); }

    public long getCustomerNumber(){ return  mCustomerData.getLong("customerNumber",0);}

    /**GET POS NUMBER DATA */
    public Long getStartNumber(){
        return mPOSNumberData.getLong("startNo", 0);
    }

    public void setStartNumber(Long startNo){
        try {
            SharedPreferences.Editor ed = mPOSNumberData.edit();
            ed.putLong("startNo", startNo);
            ed.commit();
        } catch (NullPointerException npEx) {
            Log.e("POSNumber", "Save POSNumber NullPointer Exception");
        }
    }

    public Long getEndNumber(){
        return mPOSNumberData.getLong("endNo", 0);
    }

    /**
     *
     * @return mServerAddress
     */
    public String getServerAddress(){
        mServerAddress = mServerData.getString("server_name","");
        return mServerAddress;
    }

    /**
     *
     * @return mServerPort
     */
    public int getServerPort(){
        mServerPort = mServerData.getInt("server_port", 0);
        return mServerPort;
    }

    public void saveAppPath(String appDownloadPath){
        try {
            SharedPreferences.Editor ed = mServerData.edit();
            ed.putString("app_path", appDownloadPath);
            ed.commit();
        } catch (NullPointerException npEx) {
            Log.e("app_path", "Save app_path NullPointer Exception");
        }
    }

    public void setRemindMe(String remindMe){
        try {
            SharedPreferences.Editor ed = mServerData.edit();
            ed.putString("remindMe", remindMe);
            ed.commit();
        } catch (NullPointerException npEx) {
            Log.e("remindMe", "Save remindMe NullPointer Exception");
        }
    }

    public String getAppDownloadPath(){
        mAppDownloadPath = mServerData.getString("app_path","");
        return mAppDownloadPath;
    }

    public String getRemindMeStatus(){
        mRemindMe = mServerData.getString("remindMe","N");
        return mRemindMe;
    }

}
