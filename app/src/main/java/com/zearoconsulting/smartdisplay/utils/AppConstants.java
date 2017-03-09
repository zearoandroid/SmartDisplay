package com.zearoconsulting.smartdisplay.utils;

import android.os.Environment;

/**
 * Created by saravanan on 24-05-2016.
 * AppConstants for dhukan pos
 */
public class AppConstants {

    public static final String kURLHttp="http://";
    public static final String kURLServiceName="/CCLWebService/CCLPOSResource";
    public static final String kURLMethodTest="/test";
    public static final String kURLMethodApi="/api";

    /**POS PRODUCTION API URL*/
    //public static final String URL = "http://empower-erp.com:8080/ZearoPOSWebService/POSResource/api";

    /**POS UAT API URL*/
    //public static final String URL = "http://demo.empower-erp.com:8080/ZearoPOSWebService/POSResource/api";

    public static String URL = "http://demo.empower-erp.com:8080/CCLWebService/CCLPOSResource/api";

    /** call collect organization key */
    public static final int GET_ORGANIZATION_DATA = 1;

    /** received organization key */
    public static final int ORGANIZATION_DATA_RECEIVED = 2;

    /** call authenticate key */
    public static final int CALL_AUTHENTICATE = 3;

    /** authenticate success key */
    public static final int LOGIN_SUCCESS = 4;

    /** authenticate failure key */
    public static final int LOGIN_FAILURE = 5;

    /** call get cash customer data key */
    public static final int GET_CASH_CUSTOMER_DATA = 6;

    /** call business partners key */
    public static final int GET_BPARTNERS = 8;

    /** response common data key */
    public static final int COMMON_DATA_RECEIVED = 9;

    /** get pos number key */
    public static final int GET_POS_NUMBER = 10;

    /** response pos number key */
    public static final int POS_NUMBER_RECEIVED = 11;

    /** get category key */
    public static final int GET_CATEGORY = 12;

    /** response category key */
    public static final int CATEGORY_RECEIVED = 13;

    /** call get products key */
    public static final int GET_PRODUCTS = 14;

    /** get all products key*/
    public static final int GET_ALL_PRODUCTS = 15;

    /** response product received key */
    public static final int PRODUCTS_RECEIVED = 16;

    /** call get product price key */
    public static final int GET_PRODUCT_PRICE = 17;

    /** call get tables key */
    public static final int GET_TABLES = 18;

    /** response tables received key */
    public static final int TABLES_RECEIVED = 19;

    /** call get terminals key */
    public static final int GET_TERMINALS = 20;

    /** response terminals received key */
    public static final int TERMINALS_RECEIVED = 21;

    /** call get kot header key */
    public static final int GET_KOT_HEADER_AND_lINES = 22;

    /** response kot header received key */
    public static final int KOT_HEADER_AND_lINES_RECEIVED = 23;

    public static final int POST_KOT_DATA = 24;

    public static final int POST_KOT_DATA_RESPONSE = 25;

    /** call get kot table line items key */
    public static final int POST_KOT_FLAGS = 26;

    /** response for kot table line items received key */
    public static final int KOT_FLAGS_RESPONSE_RECEIVED = 27;

    /** call release work order key */
    public static final int CALL_RELEASE_POS_ORDER = 28;

    /** response for release work order success key */
    public static final int POS_ORDER_RELEASED_SUCCESS = 29;

    /** response for release work order failure key */
    public static final int POS_ORDER_RELEASED_FAILURE = 30;

    public static final int CREATE_SESSION_REQUEST = 31;

    public static final int CREATE_SESSION_RESPONSE = 32;

    public static final int RESUME_SESSION_REQUEST = 33;

    public static final int RESUME_SESSION_RESPONSE = 34;

    public static final int CLOSE_SESSION_REQUEST = 35;

    public static final int CLOSE_SESSION_RESPONSE = 36;

    public static final int POST_TERMINAL_KOT_FLAGS = 37;

    /** response no data received key */
    public static final int NO_DATA_RECEIVED = 38;

    /** response server error key */
    public static final int SERVER_ERROR = 39;

    /** response network error key */
    public static final int NETWORK_ERROR = 40;

    public static final int DEVICE_NOT_REGISTERED = 41;

    public static final int KOT_DATA_AVAILABLE = 42;

    public static final int NO_KOT_DATA_AVAILABLE = 43;

    /** notify response failure key */
    public static final String NETWORK_FAILURE = "Not connected to Internet";

    /** initializing posid */
    public static long posID = 0;

    public static long tableID = 0;

    /** initialized default currency code format */
    public static String currencyCode = "QR";

    /**initiated order posted default status */
    public static boolean isOrderPosted = false;

    /**initiated order printed default status */
    public static boolean isOrderPrinted = false;

    /**initiated device identity */
    public static boolean isMobile = false;

    /**initiated table service default */
    public static boolean isTableService = true;

    public static boolean isKOTParsing = false;
}
