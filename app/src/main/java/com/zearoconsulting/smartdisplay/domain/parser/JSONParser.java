package com.zearoconsulting.smartdisplay.domain.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zearoconsulting.smartdisplay.AndroidApplication;
import com.zearoconsulting.smartdisplay.R;
import com.zearoconsulting.smartdisplay.data.AppDataManager;
import com.zearoconsulting.smartdisplay.data.DBHelper;
import com.zearoconsulting.smartdisplay.presentation.model.BPartner;
import com.zearoconsulting.smartdisplay.presentation.model.Category;
import com.zearoconsulting.smartdisplay.presentation.model.Customer;
import com.zearoconsulting.smartdisplay.presentation.model.KOTHeader;
import com.zearoconsulting.smartdisplay.presentation.model.KOTLineItems;
import com.zearoconsulting.smartdisplay.presentation.model.Organization;
import com.zearoconsulting.smartdisplay.presentation.model.POSLineItem;
import com.zearoconsulting.smartdisplay.presentation.model.Product;
import com.zearoconsulting.smartdisplay.presentation.model.Tables;
import com.zearoconsulting.smartdisplay.presentation.model.Terminals;
import com.zearoconsulting.smartdisplay.utils.AppConstants;
import com.zearoconsulting.smartdisplay.utils.Common;
import com.zearoconsulting.smartdisplay.utils.FileUtils;
import com.zearoconsulting.smartdisplay.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by saravanan on 25-05-2016.
 */
public class JSONParser {

    private AppDataManager mAppManager;
    private DBHelper mDBHelper;
    private Context mContext;
    private Bundle b = new Bundle();

    /**
     * @param context
     * @param appDataManager
     * @param dbHelper
     */
    public JSONParser(Context context, AppDataManager appDataManager, DBHelper dbHelper) {

        this.mContext = context;
        this.mAppManager = appDataManager;
        this.mDBHelper = dbHelper;
    }

    /**
     * @param methodType
     * @return mJsonObj
     */
    public JSONObject getParams(int methodType) {

        JSONObject mJsonObj = new JSONObject();

        try {
            mJsonObj.put("macAddress", Common.getMacAddr());
            mJsonObj.put("username", mAppManager.getUserName());
            mJsonObj.put("password", mAppManager.getUserPassword());
            mJsonObj.put("userId", mAppManager.getUserID());
            mJsonObj.put("clientId", mAppManager.getClientID());
            mJsonObj.put("roleId", mAppManager.getRoleID());
            mJsonObj.put("orgId", mAppManager.getOrgID());
            mJsonObj.put("warehouseId", mAppManager.getWarehouseID());
            mJsonObj.put("businessPartnerId", mAppManager.getUserBPID());
            mJsonObj.put("version", 1.0);
            mJsonObj.put("appName", "SmartDisplay");

            switch (methodType) {
                case AppConstants.GET_ORGANIZATION_DATA:
                    mJsonObj.put("operation", "POSOrganization");
                    break;
                case AppConstants.CALL_AUTHENTICATE:
                    mJsonObj.put("operation", "POSLogin");
                    break;
                case AppConstants.GET_CASH_CUSTOMER_DATA:
                    mJsonObj.put("operation", "POSCashCustomer");
                    break;
                case AppConstants.GET_POS_NUMBER:
                    mJsonObj.put("operation", "POSOrderNumber");
                    break;
                case AppConstants.GET_CATEGORY:
                    mJsonObj.put("operation", "POSCategory");
                    break;
                case AppConstants.GET_PRODUCTS:
                    mJsonObj.put("operation", "POSProducts");
                    break;
                case AppConstants.GET_ALL_PRODUCTS:
                    mJsonObj.put("operation", "POSAllProducts");
                    break;
                case AppConstants.GET_PRODUCT_PRICE:
                    mJsonObj.put("operation", "POSProductPrice");
                    break;
                case AppConstants.GET_TABLES:
                    mJsonObj.put("operation", "POSTables");
                    break;
                case AppConstants.GET_TERMINALS:
                    mJsonObj.put("operation", "POSTerminals");
                    break;
                case AppConstants.GET_KOT_HEADER_AND_lINES:
                    mJsonObj.put("operation", "getTerminalKotDetails");
                    mJsonObj.put("terminalId", mAppManager.getTerminalID());
                    break;
                case AppConstants.POST_KOT_FLAGS:
                    mJsonObj.put("operation", "UpdateKOTFlags");
                    break;
                case AppConstants.CALL_RELEASE_POS_ORDER:
                    mJsonObj.put("operation", "POSReleaseOrder");
                    break;
                case AppConstants.GET_BPARTNERS:
                    mJsonObj.put("operation", "POSCustomers");
                    break;
                case AppConstants.POST_KOT_DATA:
                    mJsonObj.put("operation", "KOTData");
                    mJsonObj.put("currencyId", mAppManager.getCurrencyID());
                    mJsonObj.put("paymentTermId", mAppManager.getPaymentTermID());
                    mJsonObj.put("pricelistId", mAppManager.getPriceListID());
                    break;
                case AppConstants.CREATE_SESSION_REQUEST:
                    mJsonObj.put("operation", "createSession");
                    break;
                case AppConstants.RESUME_SESSION_REQUEST:
                    mJsonObj.put("operation", "resumeSession");
                    break;
                case AppConstants.CLOSE_SESSION_REQUEST:
                    mJsonObj.put("operation", "closeSession");
                    break;
                case AppConstants.POST_TERMINAL_KOT_FLAGS:
                    mJsonObj.put("operation", "updateTerminalKot");
                    break;
                case AppConstants.POST_DELIVERED_KOT_FLAGS:
                    mJsonObj.put("operation", "updateKotDelivered");
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mJsonObj;
    }

    /**
     * @param posId
     * @param customer
     * @param totalLine
     * @param totalAmount
     * @param paidAmount
     * @param dueAmount
     * @return mHeaderObj
     */
    public JSONObject getHeaderObj(long posId, Customer customer, int totalLine, double totalAmount, double paidAmount, double dueAmount, double cashAmount, double cardAmount) {

        JSONObject mHeaderObj = new JSONObject();

        try {

            mHeaderObj.put("posId", AppConstants.posID);
            mHeaderObj.put("clientId", mAppManager.getClientID());
            mHeaderObj.put("orgId", mAppManager.getOrgID());
            mHeaderObj.put("userId", mAppManager.getUserID());
            mHeaderObj.put("businessPartnerId", customer.getBpId());
            mHeaderObj.put("periodId", mAppManager.getPeriodID());
            mHeaderObj.put("accountSchemaId", mAppManager.getAcctSchemaID());
            mHeaderObj.put("adTableId", mAppManager.getAdTableID());
            mHeaderObj.put("totalLines", totalLine);
            mHeaderObj.put("totalAmount", totalAmount);
            mHeaderObj.put("currencyId", mAppManager.getCurrencyID());
            mHeaderObj.put("paymentTermId", mAppManager.getPaymentTermID());
            mHeaderObj.put("warehouseId", mAppManager.getWarehouseID());
            mHeaderObj.put("pricelistId", mAppManager.getPriceListID());
            mHeaderObj.put("cashbookId", mAppManager.getCashbookID());
            mHeaderObj.put("paidAmount", paidAmount);
            mHeaderObj.put("dueAmount", 0);
            mHeaderObj.put("customerName", customer.getCustomerName());
            mHeaderObj.put("cashAmount", cashAmount);
            if (cashAmount == 0)
                mHeaderObj.put("IsCash", "N");
            else
                mHeaderObj.put("IsCash", "Y");
            mHeaderObj.put("cardAmount", cardAmount);
            if (cardAmount == 0)
                mHeaderObj.put("IsCard", "N");
            else
                mHeaderObj.put("IsCard", "Y");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mHeaderObj;
    }

    /**
     * @param jsonStr
     * @param mHandler
     */
    public void parseOrgJson(String jsonStr, Handler mHandler) {

        Log.i("RESPONSE", jsonStr);
        JSONObject json;
        JSONArray orgArray;
        JSONArray roleArray;
        JSONArray roleAccessArray;
        JSONArray warehouseArray;

        Message msg = new Message();

        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {

                //mDBHelper.deleteAllTables();

                mAppManager.setUserID(json.getLong("userId"));
                mAppManager.setUserBPID(json.getLong("businessPartnerId"));
                mAppManager.setClientID(json.getLong("clientId"));

                orgArray = json.getJSONArray("orgDetails");
                for (int i = 0; i < orgArray.length(); i++) {
                    JSONObject orgJson = (JSONObject) orgArray.get(i);
                    Organization mOrg = new Organization();
                    mOrg.setOrgId(orgJson.getLong("orgId"));
                    mOrg.setOrgName(orgJson.getString("orgName"));
                    mOrg.setIsDefault(orgJson.getString("isdefault"));

                    //checking org arabic name is available or not
                    if (orgJson.has("orgArabic"))
                        mOrg.setOrgArabicName(orgJson.getString("orgArabic"));
                    else
                        mOrg.setOrgArabicName("");

                    //checking org image is available or not
                    if (orgJson.has("orgImage")){
                        String imagePath = FileUtils.storeImage(orgJson.getString("orgImage"),orgJson.getLong("orgId"),null);
                        mOrg.setOrgImage(imagePath);
                    }
                    else
                        mOrg.setOrgImage("");

                    //checking org phone is available or not
                    if (orgJson.has("orgPhone"))
                        mOrg.setOrgPhone(orgJson.getString("orgPhone"));
                    else
                        mOrg.setOrgPhone("");

                    //checking org email is available or not
                    if (orgJson.has("orgEmail"))
                        mOrg.setOrgEmail(orgJson.getString("orgEmail"));
                    else
                        mOrg.setOrgEmail("");

                    //checking org address is available or not
                    if (orgJson.has("orgAddress"))
                        mOrg.setOrgAddress(orgJson.getString("orgAddress"));
                    else
                        mOrg.setOrgAddress("");

                    //checking org city is available or not
                    if (orgJson.has("orgCity"))
                        mOrg.setOrgCity(orgJson.getString("orgCity"));
                    else
                        mOrg.setOrgCity("");

                    //checking org country is available or not
                    if (orgJson.has("orgCountry"))
                        mOrg.setOrgCountry(orgJson.getString("orgCountry"));
                    else
                        mOrg.setOrgCountry("");

                    //checking org web address is available or not
                    if (orgJson.has("orgWebUrl"))
                        mOrg.setOrgWebUrl(orgJson.getString("orgWebUrl"));
                    else
                        mOrg.setOrgWebUrl("");

                    mOrg.setClientId(mAppManager.getClientID());

                    mDBHelper.addOrganization(mOrg);
                }

                roleArray = json.getJSONArray("roleDetails");
                for (int i = 0; i < roleArray.length(); i++) {
                    JSONObject roleJson = (JSONObject) roleArray.get(i);
                    mDBHelper.addRole(roleJson.getLong("roleId"), roleJson.getString("roleName"), roleJson.getString("isdefault"));
                }

                roleAccessArray = json.getJSONArray("roleAccessDetails");
                for (int i = 0; i < roleAccessArray.length(); i++) {
                    JSONObject roleAccessJson = (JSONObject) roleAccessArray.get(i);
                    mDBHelper.addRoleAccess(mAppManager.getClientID(),roleAccessJson.getLong("orgId"), roleAccessJson.getLong("roleId"));
                }

                warehouseArray = json.getJSONArray("warehouseDetails");
                for (int i = 0; i < warehouseArray.length(); i++) {
                    JSONObject warehouseJson = (JSONObject) warehouseArray.get(i);
                    mDBHelper.addWarehouse(mAppManager.getClientID(), warehouseJson.getLong("orgId"), warehouseJson.getLong("warehouseId"), warehouseJson.getString("warehouseName"), warehouseJson.getString("isdefault"));
                }

                b.putInt("Type", AppConstants.ORGANIZATION_DATA_RECEIVED);
                b.putString("OUTPUT", "");

            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 101) {
                b.putInt("Type", AppConstants.LOGIN_FAILURE);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.NO_DATA_RECEIVED);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    /**
     * @param jsonStr
     * @param mHandler
     */
    public void parseLoginJson(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {
                if (json.getString("isSalesRep").equalsIgnoreCase("Yes"))
                    mAppManager.setSalesRep(true);
                else
                    mAppManager.setSalesRep(false);

                b.putInt("Type", AppConstants.LOGIN_SUCCESS);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.LOGIN_FAILURE);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    /**
     * @param jsonStr
     * @param mHandler
     */
    public void parseCommonJson(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {

                BPartner bPartner = new BPartner();
                bPartner.setBpNumber(json.getLong("businessPartnerId"));
                bPartner.setBpName(json.getString("customerName"));
                bPartner.setBpId(json.getLong("businessPartnerId"));
                bPartner.setBpPriceListId(json.getLong("pricelistId"));
                bPartner.setBpEmail("");
                bPartner.setBpNumber(0);

                mAppManager.saveCustomerData(bPartner);
                //mAppManager.saveCashCustomerData(json.getString("customerName"),json.getLong("businessPartnerId"));
                mAppManager.saveCommonData(json.getLong("costElementId"), json.getLong("currencyId"), json.getLong("cashBookId"), json.getLong("periodId"), json.getLong("paymentTermId"), json.getLong("adTableId"), json.getLong("accountSchemaId"), json.getLong("pricelistId"), json.getString("currencyCode"));

                b.putInt("Type", AppConstants.COMMON_DATA_RECEIVED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.NO_DATA_RECEIVED);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    public void parseTables(String jsonStr, Handler mHandler){
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        JSONArray jsonArray;
        List<Tables> tableList = null;
        int length = 0;
        try {

            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {

                tableList = new ArrayList<Tables>();
                jsonArray = json.getJSONArray("tables");

                length = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    Tables tables = new Tables();
                    tables.setClientId(mAppManager.getClientID());
                    tables.setOrgId(mAppManager.getOrgID());
                    tables.setTableId(obj.getLong("tablesId"));
                    tables.setTableName(obj.getString("tablesName"));
                    tables.setOrderAvailable("N");

                    mDBHelper.addTables(tables);

                    tableList.add(tables);
                }
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        } finally {

            if (length == tableList.size()) {
                b.putInt("Type", AppConstants.TABLES_RECEIVED);
                b.putString("OUTPUT", "");

                msg.setData(b);
                mHandler.sendMessage(msg);
            }
        }
    }

    public void parseTerminals(String jsonStr, Handler mHandler){
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        JSONArray jsonArray;
        List<Terminals> terminalsList = null;
        int length = 0;
        try {

            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {

                terminalsList = new ArrayList<Terminals>();
                jsonArray = json.getJSONArray("terminals");

                length = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    Terminals terminals = new Terminals();
                    terminals.setClientId(mAppManager.getClientID());
                    terminals.setOrgId(mAppManager.getOrgID());
                    terminals.setTerminalId(obj.getLong("terminalId"));
                    terminals.setTerminalName(obj.getString("terminalName"));
                    terminals.setTerminalIP(obj.getString("terminalIP"));

                    mDBHelper.addTerminals(terminals);

                    terminalsList.add(terminals);
                }
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        } finally {

            if (length == terminalsList.size()) {
                b.putInt("Type", AppConstants.TERMINALS_RECEIVED);
                b.putString("OUTPUT", "");

                msg.setData(b);
                mHandler.sendMessage(msg);
            }
        }
    }

    /**
     * @param jsonStr
     * @return
     */
    public void parseCategorysJson(String jsonStr, Handler mHandler) {
        Runnable myThread = new ParseCategoryThread(jsonStr, mHandler);
        new Thread(myThread).start();
    }

    /**
     * @param jsonStr
     * @return
     */
    public void parseProductJson(String jsonStr, Handler mHandler) {
        Runnable myThread = new ParseProductThread(jsonStr, mHandler);
        new Thread(myThread).start();
    }

    public class ParseCategoryThread implements Runnable {

        String mJsonStr;
        Handler mHandler;

        Message msg = new Message();
        JSONObject json;
        JSONArray jsonArray;
        List<Category> categoryList = null;
        int length = 0;

        public ParseCategoryThread(String jsonStr, Handler handler) {
            // store parameter for later user
            mJsonStr = jsonStr;
            mHandler = handler;
        }

        @Override
        public void run() {
            try {

                json = new JSONObject(mJsonStr);
                if (json.getInt("responseCode") == 200) {

                    categoryList = new ArrayList<Category>();
                    jsonArray = json.getJSONArray("category");

                    length = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        Category category = new Category();
                        category.setCategoryId(obj.getLong("categoryId"));
                        category.setCategoryName(obj.getString("categoryName"));
                        category.setCategoryValue(obj.getString("categoryValue"));

                        //load image to sdcard and store the path to db
                        if (obj.has("categoryImage")) {
                            String imagePath = FileUtils.storeImage(obj.getString("categoryImage"),obj.getLong("categoryId"),null);
                            category.setCategoryImage(imagePath);
                        } else {
                            // Retrieve the image from the res folder
                            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                    R.drawable.no_product);
                            String imagePath = FileUtils.storeImage("",obj.getLong("categoryId"),bitmap);
                            category.setCategoryImage(imagePath);
                        }

                        if (obj.has("showDigitalMenu")) {
                            category.setShowDigitalMenu(obj.getString("showDigitalMenu"));
                        }else{
                            category.setShowDigitalMenu("Y");
                        }

                        category.setClientId(mAppManager.getClientID());
                        category.setOrgId(mAppManager.getOrgID());

                        //add category
                        mDBHelper.addCategory(category);

                        categoryList.add(category);
                    }
                } else if (json.getInt("responseCode") == 301) {
                    b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                    b.putString("OUTPUT", "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            } finally {

                if (length == categoryList.size()) {
                    b.putInt("Type", AppConstants.CATEGORY_RECEIVED);
                    b.putString("OUTPUT", "");

                    msg.setData(b);
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    public class ParseProductThread implements Runnable {

        Message msg = new Message();
        List<Product> productList = null;
        JSONObject json;
        JSONArray jsonArray;
        long categoryId;
        int length = 0;
        String mJsonStr;
        Handler mHandler;

        public ParseProductThread(String jsonStr, Handler handler) {
            // store parameter for later user
            mJsonStr = jsonStr;
            mHandler = handler;
        }

        @Override
        public void run() {
            try {
                json = new JSONObject(mJsonStr);
                if (json.getInt("responseCode") == 200) {

                    productList = new ArrayList<Product>();
                    jsonArray = json.getJSONArray("products");
                    length = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        Product product = new Product();
                        categoryId = obj.getLong("categoryId");
                        product.setProdId(obj.getLong("productId"));
                        product.setProdName(obj.getString("productName"));
                        product.setProdValue(obj.getString("productValue"));
                        product.setUomId(obj.getLong("productUOMId"));
                        product.setUomValue(obj.getString("productUOMValue"));
                        product.setSalePrice(Double.parseDouble(obj.getString("sellingPrice")));
                        product.setCostPrice(Double.parseDouble(obj.getString("costprice")));
                        product.setTerminalId(obj.getLong("terminalId"));

                        //load image to sdcard and store the path to db
                        if (obj.has("productImage")) {
                            String imagePath = FileUtils.storeImage(obj.getString("productImage"),obj.getLong("productId"),null);
                            product.setProdImage(imagePath);
                        } else {
                            // Retrieve the image from the res folder
                            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                    R.drawable.no_product);
                            String imagePath = FileUtils.storeImage("",obj.getLong("productId"),bitmap);
                            product.setProdImage(imagePath);
                        }

                        if (obj.has("productArabicName")) {
                            product.setProdArabicName(obj.getString("productArabicName"));
                        } else {
                            product.setProdArabicName("");
                        }

                        if (obj.has("description")) {
                            product.setDescription(obj.getString("description"));
                        } else {
                            product.setDescription("");
                        }

                        if (obj.has("showDigitalMenu")) {
                            product.setShowDigitalMenu(obj.getString("showDigitalMenu"));
                        } else {
                            product.setShowDigitalMenu("Y");
                        }

                        if (obj.has("productVideoPath")) {
                            product.setProdVideoPath(obj.getString("productVideoPath"));
                        } else {
                            product.setProdVideoPath("N");
                        }

                        if (obj.has("calories")) {
                            product.setCalories(obj.getString("calories"));
                        } else {
                            product.setCalories("");
                        }

                        if (obj.has("preparationTime")) {
                            product.setPreparationTime(obj.getString("preparationTime"));
                        } else {
                            product.setPreparationTime("");
                        }

                        if (obj.has("prTime")) {
                            product.setPreparationTime(obj.getString("prTime"));
                        } else {
                            product.setPreparationTime("00:00");
                        }

                        product.setClientId(mAppManager.getClientID());
                        product.setOrgId(mAppManager.getOrgID());

                        mDBHelper.addProduct(categoryId, product);
                        productList.add(product);
                    }

                }else if (json.getInt("responseCode") == 301) {
                    b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                    b.putString("OUTPUT", "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            } finally {
                if (length == productList.size()) {
                    b.putInt("Type", AppConstants.PRODUCTS_RECEIVED);
                    b.putString("OUTPUT", "");

                    msg.setData(b);
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    /**
     * Parse KOT DATA HEADER AND LINE ITEMS
     *
     * @param jsonStr
     * @param mHandler
     */
    public void parseKOTData(String jsonStr, Handler mHandler){
        AppConstants.isKOTParsing = true;
        Runnable myThread = new ParseKOTDataThread(jsonStr, mHandler);
        new Thread(myThread).start();
    }

    public class ParseKOTDataThread implements Runnable {

        String mJsonStr;
        Handler mHandler;

        JSONObject json;
        JSONArray jsonTableArray = null, jsonTokenArray, jsonProductArray, jsonTokenCompletedArray=null;

        long kotNumber, invoiceNumber, terminalID, tableId;
        double totalAmount;
        String notes, kotType, waiterName, printed="N", orderType = "Running";
        int qty;
        Message msg = new Message();

        public ParseKOTDataThread(String jsonStr, Handler handler) {
            // store parameter for later user
            mJsonStr = jsonStr;
            mHandler = handler;
        }

        @Override
        public void run() {
            try {
                json = new JSONObject(mJsonStr);
                if (json.getInt("responseCode") == 200) {

                    if(json.has("tables"))
                    jsonTableArray = json.getJSONArray("tables");

                    if(json.has("KotcompletedArray"))
                    jsonTokenCompletedArray = json.getJSONArray("KotcompletedArray");

                    if(jsonTableArray!=null) {
                        //Loop for getting tables
                        for (int i = 0; i < jsonTableArray.length(); i++) {

                            JSONObject tableObj = (JSONObject) jsonTableArray.get(i);
                            tableId = tableObj.getLong("tableId");
                            jsonTokenArray = tableObj.getJSONArray("tokens");

                            //update orderAvailable = Y
                            mDBHelper.updateOrderAvailableTable(tableId);

                            //Loop for getting tokens
                            for (int j = 0; j < jsonTokenArray.length(); j++) {

                                JSONObject tokenObj = (JSONObject) jsonTokenArray.get(j);

                                kotNumber = tokenObj.getLong("KOTNumber");
                                invoiceNumber = tokenObj.getLong("invoiceNumber");
                                terminalID = tokenObj.getLong("terminalId");
                                totalAmount = tokenObj.getLong("totalAmount");
                                kotType = tokenObj.getString("kotType");
                                waiterName = tokenObj.getString("waiterName");

                                if (tokenObj.has("isPrinted"))
                                    printed = tokenObj.getString("isPrinted");

                                if (tokenObj.has("orderType"))
                                    orderType = tokenObj.getString("orderType");

                                KOTHeader kotHeader = new KOTHeader();
                                kotHeader.setTablesId(tableId);
                                kotHeader.setKotNumber(kotNumber);

                                if (tableId != 0) {
                                    long invoiceNum = mDBHelper.getKOTInvoiceNumber(tableId);
                                    if (invoiceNum == 0) {
                                        invoiceNumber = invoiceNum;
                                    } else {
                                        invoiceNumber = invoiceNum;
                                    }
                                }

                                kotHeader.setInvoiceNumber(invoiceNumber);
                                kotHeader.setTerminalId(terminalID);
                                kotHeader.setTotalAmount(totalAmount);
                                kotHeader.setOrderBy(waiterName);
                                kotHeader.setKotType(kotType);
                                kotHeader.setOrderType(orderType);
                                kotHeader.setIsKOT("Y");
                                kotHeader.setPrinted(printed);
                                kotHeader.setPosted("N");
                                kotHeader.setSelected("N");

                                Date date = new Date(System.currentTimeMillis());
                                long mCurrentTime = date.getTime();

                                kotHeader.setCreateTime(System.currentTimeMillis());

                                //insert kot header data to db
                                boolean inserted = mDBHelper.addKOTHeader(kotHeader);

                                Log.i("KOT-HEADER INSERTED: ", "" + inserted);

                                if (inserted) {
                                    //getting products array
                                    jsonProductArray = tokenObj.getJSONArray("products");

                                    //Loop for getting products
                                    for (int k = 0; k < jsonProductArray.length(); k++) {

                                        JSONObject productObj = (JSONObject) jsonProductArray.get(k);

                                        Product prod = mDBHelper.getProduct(mAppManager.getClientID(), mAppManager.getOrgID(), productObj.getLong("productId"));

                                        notes = productObj.getString("description");

                                        Product product = new Product();
                                        product.setCategoryId(prod.getCategoryId());
                                        product.setProdId(productObj.getLong("productId"));
                                        product.setProdName(prod.getProdName());
                                        product.setProdArabicName(prod.getProdArabicName());
                                        product.setProdValue(prod.getProdValue());
                                        product.setUomId(productObj.getLong("productUOMId"));
                                        product.setUomValue(prod.getUomValue());
                                        product.setSalePrice(productObj.getDouble("sellingPrice"));
                                        product.setCostPrice(prod.getCostPrice());
                                        product.setTerminalId(terminalID);
                                        product.setPreparationTime(prod.getPreparationTime());

                                        long kotLineTime = 0;
                                        if(prod.getPreparationTime().contains(":")) {
                                            String[] units = product.getPreparationTime().split(":"); //will break the string up into an array
                                            int hour = Integer.parseInt(units[0]); //first element
                                            int minute = Integer.parseInt(units[1]); //second element

                                            long hours = hour * 60 * 60 * 1000;
                                            long minutes = minute * 60 * 1000;

                                            kotLineTime = System.currentTimeMillis()+hours+minutes;

                                        }

                                        qty = productObj.getInt("qty");

                                        long kotLineId = productObj.getLong("KotLineID");
                                        long invNumber = productObj.getLong("invoiceNumber");

                                        if (invNumber != 0)
                                            mDBHelper.updateKOTLineIdToPOSLineItem(invNumber, kotLineId, productObj.getLong("productId"));

                                        List<Long> invNumList = mDBHelper.getKOTInvoiceNumbers(tableId);

                                        int size = invNumList.size();

                                        if (size > 0)
                                            invoiceNumber = invNumList.get(size - 1);

                                        KOTLineItems kotLineItems = new KOTLineItems();
                                        kotLineItems.setTableId(tableId);
                                        kotLineItems.setKotLineId(kotLineId);
                                        kotLineItems.setKotNumber(kotNumber);
                                        kotLineItems.setInvoiceNumber(invoiceNumber);
                                        kotLineItems.setNotes(productObj.getString("description"));
                                        kotLineItems.setRefRowId(0);
                                        kotLineItems.setIsExtraProduct("N");
                                        if(kotLineTime == 0)
                                        kotLineItems.setCreateTime(System.currentTimeMillis());
                                        else
                                        kotLineItems.setCreateTime(kotLineTime);

                                        //insert kot line items
                                        mDBHelper.addKOTLineItems(kotLineItems, product, qty);

                                        if (productObj.has("relatedProductsArray"))
                                            parseRelatedProducts(tableId, terminalID, kotNumber, invoiceNumber, kotLineId, productObj.getJSONArray("relatedProductsArray"));

                                    }
                                }
                            }
                        }
                    }

                    if(jsonTokenCompletedArray!=null){
                        for(int l=0; l<jsonTokenCompletedArray.length(); l++){
                            JSONObject tokenObj = (JSONObject) jsonTokenCompletedArray.get(l);
                            kotNumber = tokenObj.getLong("KOTNumber");
                            mDBHelper.deleteKOTTableData(kotNumber);
                        }
                    }

                    b.putInt("Type", AppConstants.KOT_DATA_AVAILABLE);
                    b.putString("OUTPUT", "");

                } else if (json.getInt("responseCode") == 601) {
                    b.putInt("Type", AppConstants.NO_KOT_DATA_AVAILABLE);
                    b.putString("OUTPUT", "");
                } else if (json.getInt("responseCode") == 301) {
                    b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                    b.putString("OUTPUT", "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            } finally {
                msg.setData(b);
                mHandler.sendMessage(msg);
            }
        }
    }

    private void parseRelatedProducts(long tableId, long terminalID, long kotNumber, long invNumber, long refLineId, JSONArray mRelatedProdArray){

        try {
            //getting products array
            JSONArray jsonProductArray = mRelatedProdArray;

            //Loop for getting products
            for (int k = 0; k < jsonProductArray.length(); k++) {

                JSONObject productObj = (JSONObject) jsonProductArray.get(k);

                Product prod = mDBHelper.getProduct(mAppManager.getClientID(), mAppManager.getOrgID(), productObj.getLong("productId"));

                Product product = new Product();
                product.setCategoryId(prod.getCategoryId());
                product.setProdId(productObj.getLong("productId"));
                product.setProdName(prod.getProdName());
                product.setProdArabicName(prod.getProdArabicName());
                product.setProdValue(prod.getProdValue());
                product.setUomId(productObj.getLong("productUOMId"));
                product.setUomValue(prod.getUomValue());
                product.setSalePrice(productObj.getDouble("sellingPrice"));
                product.setCostPrice(prod.getCostPrice());
                product.setTerminalId(terminalID);
                product.setPreparationTime(prod.getPreparationTime());

                long kotLineTime = 0;
                if(prod.getPreparationTime().contains(":")) {
                    String[] units = product.getPreparationTime().split(":"); //will break the string up into an array
                    int hour = Integer.parseInt(units[0]); //first element
                    int minute = Integer.parseInt(units[1]); //second element

                    long hours = hour * 60 * 60 * 1000;
                    long minutes = minute * 60 * 1000;

                    kotLineTime = System.currentTimeMillis()+hours+minutes;

                }

                long kotLineId = productObj.getLong("KotLineID");

                if (invNumber != 0)
                    mDBHelper.updateRelatedKOTLineIdToPOSLineItem(invNumber, kotLineId, productObj.getLong("productId"));

                KOTLineItems kotLineItems = new KOTLineItems();
                kotLineItems.setTableId(tableId);
                kotLineItems.setKotLineId(kotLineId);
                kotLineItems.setKotNumber(kotNumber);
                kotLineItems.setInvoiceNumber(invNumber);
                kotLineItems.setNotes(productObj.getString("description"));
                kotLineItems.setRefRowId(refLineId);
                kotLineItems.setIsExtraProduct("Y");
                if(kotLineTime == 0)
                    kotLineItems.setCreateTime(System.currentTimeMillis());
                else
                    kotLineItems.setCreateTime(kotLineTime);

                //insert addOn kot line items
                mDBHelper.addKOTLineItems(kotLineItems, product, productObj.getInt("qty"));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parseKOTStatus(String jsonStr, Handler mHandler){
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.length() == 0) {
                if (NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
                    b.putInt("Type", AppConstants.NETWORK_ERROR);
                    b.putString("OUTPUT", "");
                } else {
                    b.putInt("Type", AppConstants.NO_DATA_RECEIVED);
                    b.putString("OUTPUT", "");
                }
            } else {
                if (json.length() != 0 && json.getInt("responseCode") == 200) {
                    b.putInt("Type", AppConstants.KOT_FLAGS_RESPONSE_RECEIVED);
                    b.putString("OUTPUT", "");
                } else if (json.getInt("responseCode") == 301) {
                    b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                    b.putString("OUTPUT", "");
                } else {
                    b.putInt("Type", AppConstants.NO_DATA_RECEIVED);
                    b.putString("OUTPUT", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    public void parseKOTResponse(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {
                mDBHelper.deleteKOTTableData(json.getLong("KOTNumber"));
                b.putInt("Type", AppConstants.POST_KOT_DATA_RESPONSE);
                b.putString("OUTPUT", "");
            }
            else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    public void parseKOTDeliveredResponse(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {
                mDBHelper.deleteKOTTableData(json.getLong("KOTNumber"));
                b.putInt("Type", AppConstants.POST_KOT_DATA_RESPONSE);
                b.putString("OUTPUT", "");
            }
            else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }
}
