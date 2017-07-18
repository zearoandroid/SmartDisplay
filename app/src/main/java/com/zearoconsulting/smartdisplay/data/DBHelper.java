package com.zearoconsulting.smartdisplay.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zearoconsulting.smartdisplay.presentation.model.BPartner;
import com.zearoconsulting.smartdisplay.presentation.model.Category;
import com.zearoconsulting.smartdisplay.presentation.model.Customer;
import com.zearoconsulting.smartdisplay.presentation.model.KOTHeader;
import com.zearoconsulting.smartdisplay.presentation.model.KOTLineItems;
import com.zearoconsulting.smartdisplay.presentation.model.Organization;
import com.zearoconsulting.smartdisplay.presentation.model.POSLineItem;
import com.zearoconsulting.smartdisplay.presentation.model.POSOrders;
import com.zearoconsulting.smartdisplay.presentation.model.POSPayment;
import com.zearoconsulting.smartdisplay.presentation.model.Product;
import com.zearoconsulting.smartdisplay.presentation.model.Role;
import com.zearoconsulting.smartdisplay.presentation.model.Tables;
import com.zearoconsulting.smartdisplay.presentation.model.Terminals;
import com.zearoconsulting.smartdisplay.presentation.model.Warehouse;
import com.zearoconsulting.smartdisplay.presentation.presenter.ITokenDeletedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saravanan on 24-05-2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "smartDisplayDB";

    //organization table
    private static final String TABLE_ORGANIZATION = "organization";

    //warehouse table
    private static final String TABLE_WAREHOUSE = "warehouse";

    // role table
    private static final String TABLE_ROLE = "role";

    // role table
    private static final String TABLE_ROLE_ACESS = "roleAccess";

    // category table
    private static final String TABLE_CATEGORY = "category";

    // product table
    private static final String TABLE_PRODUCT = "product";

    // BPartner table
    private static final String TABLE_BPARTNERS = "businessPartner";

    // BPartner table
    private static final String TABLE_SUPERVISOR = "supervisor";

    // pos order number table
    private static final String TABLE_POS_ORDER_NUMBER = "posOrderNumber";

    // pos order header table
    private static final String TABLE_POS_ORDER_HEADER = "posOrderHeader";

    // pos line item detail table
    private static final String TABLE_POS_LINES = "posLineItems";

    // pos payment detail table
    private static final String TABLE_POS_PAYMENT_DETAIL = "posPaymentDetail";

    // kot tables table
    private static final String TABLE_KOT_TABLE = "kotTables";

    // kot terminals table
    private static final String TABLE_KOT_TERMINALS = "kotTerminals";

    // kot header table
    private static final String TABLE_KOT_HEADER = "kotHeader";

    // kot line item detail table
    private static final String TABLE_KOT_LINES = "kotLineItems";

    //COLUMNS
    private static final String KEY_ID = "_id";
    private static final String KEY_ORG_ID = "orgId";
    private static final String KEY_CLIENT_ID = "clientId";
    private static final String KEY_ORG_NAME = "orgName";
    private static final String KEY_ORG_ARABIC_NAME = "orgArabicName";
    private static final String KEY_ORG_IMAGE = "orgImage";
    private static final String KEY_ORG_PHONE = "orgPhone";
    private static final String KEY_ORG_EMAIL = "orgEmail";
    private static final String KEY_ORG_ADDRESS = "orgAddress";
    private static final String KEY_ORG_CITY = "orgCity";
    private static final String KEY_ORG_COUNTRY = "orgCountry";
    private static final String KEY_ORG_WEB_URL = "orgWebUrl";

    private static final String KEY_WAREHOUSE_ID = "warehouseId";
    private static final String KEY_WAREHOUSE_NAME = "warehouseName";

    private static final String KEY_ROLE_ID = "roleId";
    private static final String KEY_ROLE_NAME = "roleName";

    private static final String KEY_CATEGORY_ID = "categoryId";
    private static final String KEY_CATEGORY_NAME = "categoryName";
    private static final String KEY_CATEGORY_VALUE = "categoryValue";
    private static final String KEY_CATEGORY_IMAGE = "categoryImage";
    private static final String KEY_SHOWN_DIGITAL_MENU = "shownDigitalMenu";

    private static final String KEY_POS_ID = "posId";
    private static final String KEY_BP_ID = "bpId";
    private static final String KEY_CUSTOMER_NAME = "customerName";
    private static final String KEY_PRICELIST_ID = "pricelistId";
    private static final String KEY_CUSTOMER_VALUE = "value";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NUMBER = "mobilenumber";
    private static final String KEY_IS_CASH_CUSTOMER = "isCashCustomer";

    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_PRODUCT_NAME = "productName";
    private static final String KEY_PRODUCT_ARABIC_NAME = "productArabicName";
    private static final String KEY_PRODUCT_VALE = "productValue";
    private static final String KEY_PRODUCT_UOM_ID = "uomId";
    private static final String KEY_PRODUCT_UOM_VALUE = "uomValue";
    private static final String KEY_PRODUCT_QTY = "qty";
    private static final String KEY_PRODUCT_STD_PRICE = "stdPrice";
    private static final String KEY_PRODUCT_COST_PRICE = "costPrice";
    private static final String KEY_PRODUCT_TOTAL_PRICE = "totalPrice";
    private static final String KEY_PRODUCT_IMAGE = "productImage";
    private static final String KEY_PRODUCT_DISCOUNT_TYPE = "productDiscType";
    private static final String KEY_PRODUCT_DISCOUNT_VALUE = "productDiscValue";
    private static final String KEY_TOTAL_DISCOUNT_TYPE = "totalDiscType";
    private static final String KEY_TOTAL_DISCOUNT_VALUE = "totalDiscValue";
    private static final String KEY_PRODUCT_VIDEO = "productVideo";
    private static final String KEY_PRODUCT_CALORIES = "calories";
    private static final String KEY_PRODUCT_PREPARATION_TIME = "preparationTime";
    private static final String KEY_PRODUCT_DESCRIPTION = "description";
    private static final String KEY_PRODUCT_TERMINAL_ID = "terminalId";

    private static final String KEY_IS_DEFAULT = "isDefault";
    private static final String KEY_IS_UPDATED = "isUpdated";
    private static final String KEY_IS_DELETED = "isDeleted";
    private static final String KEY_IS_POSTED = "isPosted";
    private static final String KEY_IS_KOT = "isKOT";
    private static final String KEY_KOT_TYPE = "kotType";
    private static final String KEY_ORDER_TYPE = "orderType";
    private static final String KEY_IS_KOT_GENERATED = "isKOTGenerated";
    private static final String KEY_IS_ORDER_AVAILABLE = "isOrderAvailable";
    private static final String KEY_IS_PRINTED = "isPrinted";
    private static final String KEY_IS_LINE_DISCOUNTED = "isLineDiscounted";
    private static final String KEY_IS_TOTAL_DISCOUNTED = "isTotalDiscounted";
    private static final String KEY_AUTHORIZE_CODE = "authorizeCode";

    private static final String KEY_PAYMENT_CASH = "paymentCash";
    private static final String KEY_PAYMENT_AMEX = "paymentAmex";
    private static final String KEY_PAYMENT_GIFT = "paymentGift";
    private static final String KEY_PAYMENT_MASTER = "paymentMaster";
    private static final String KEY_PAYMENT_VISA = "paymentVisa";
    private static final String KEY_PAYMENT_OTHER = "paymentOther";
    private static final String KEY_PAYMENT_RETURN = "paymentReturn";

    private static final String KEY_START_NUMBER = "startNumber";
    private static final String KEY_END_NUMBER = "endNumber";

    private static final String KEY_KOT_TABLE_ID = "kotTableId";
    private static final String KEY_KOT_LINE_ID = "kotLineId";
    private static final String KEY_KOT_TABLE_NAME = "kotTableName";

    private static final String KEY_KOT_TERMINAL_ID = "kotTerminalId";
    private static final String KEY_KOT_TERMINAL_NAME = "kotTerminalName";
    private static final String KEY_KOT_TERMINAL_IP = "kotTerminalIP";

    private static final String KEY_KOT_NUMBER = "kotNumber";
    private static final String KEY_INVOICE_NUMBER = "invoiceNumber";
    private static final String KEY_KOT_TOTAL_AMOUNT = "kotTotalAmount";
    private static final String KEY_KOT_ORDER_BY = "kotOrderBy";
    private static final String KEY_IS_SELECTED = "isSelected";
    private static final String KEY_CREATE_TIME = "createTime";
    private static final String KEY_KOT_ITEM_NOTES = "kotItemNotes";
    private static final String KEY_KOT_REF_LINE_ID = "kotRefLineId";
    private static final String KEY_KOT_EXTRA_PRODUCT = "isExtraProduct";

    private static final String KEY_IS_COVERS_LEVEL = "isCoversLevel";
    private static final String KEY_M_TABLE_GROUP_ID = "m_table_group_id";
    private static final String KEY_COVERS_DETAILS = "covers_details";

    //create query for TABLE_ORGANIZATION
    private static final String ORGANIZATION_CREATE_QUERY = "CREATE TABLE "
            + TABLE_ORGANIZATION + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_ORG_NAME + " TEXT, " + KEY_ORG_ARABIC_NAME + " TEXT, "
            + KEY_ORG_IMAGE + " TEXT, " + KEY_ORG_ADDRESS + " TEXT, " + KEY_ORG_PHONE + " TEXT, " + KEY_ORG_EMAIL + " TEXT, " + KEY_ORG_CITY + " TEXT, "
            + KEY_ORG_COUNTRY + " TEXT, " + KEY_ORG_WEB_URL + " TEXT, " + KEY_IS_DEFAULT + " TEXT);";

    //create query for TABLE_WAREHOUSE
    private static final String WAREHOUSE_CREATE_QUERY = "CREATE TABLE "
            + TABLE_WAREHOUSE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_WAREHOUSE_ID + " NUMERIC, " + KEY_WAREHOUSE_NAME
            + " TEXT, " + KEY_IS_DEFAULT + " TEXT);";

    //create query for TABLE_ROLE
    private static final String ROLE_CREATE_QUERY = "CREATE TABLE "
            + TABLE_ROLE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_ROLE_ID + " NUMERIC, " + KEY_ROLE_NAME
            + " TEXT, " + KEY_IS_DEFAULT + " TEXT);";

    //create query for TABLE_ROLE
    private static final String ROLE_ACCESS_CREATE_QUERY = "CREATE TABLE "
            + TABLE_ROLE_ACESS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_ROLE_ID + " NUMERIC);";

    //create query for TABLE_ROLE
    private static final String CATEGORY_CREATE_QUERY = "CREATE TABLE "
            + TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_CATEGORY_ID + " NUMERIC, " + KEY_CATEGORY_NAME + " TEXT, " + KEY_CATEGORY_VALUE
            + " TEXT, " + KEY_CATEGORY_IMAGE + " TEXT," + KEY_SHOWN_DIGITAL_MENU + " TEXT);";

    //create query for TABLE_PRODUCT
    private static final String PRODUCT_CREATE_QUERY = "CREATE TABLE "
            + TABLE_PRODUCT + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_CATEGORY_ID
            + " NUMERIC, " + KEY_PRODUCT_ID + " NUMERIC, " + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_PRODUCT_VALE + " TEXT, " + KEY_PRODUCT_UOM_ID + " NUMERIC, " + KEY_PRODUCT_UOM_VALUE + " TEXT, "
            + KEY_PRODUCT_QTY + " INTEGER, " + KEY_PRODUCT_STD_PRICE + " NUMERIC, "
            + KEY_PRODUCT_COST_PRICE + " NUMERIC, " + KEY_PRODUCT_IMAGE + " TEXT," + KEY_PRODUCT_ARABIC_NAME + " TEXT,"
            + KEY_PRODUCT_DESCRIPTION + " TEXT," + KEY_SHOWN_DIGITAL_MENU + " TEXT," + KEY_PRODUCT_VIDEO + " TEXT,"
            + KEY_PRODUCT_CALORIES + " TEXT," + KEY_PRODUCT_PREPARATION_TIME + " TEXT, " + KEY_PRODUCT_TERMINAL_ID + " NUMERIC);";

    //create query for TABLE_BPARTNER
    private static final String BPARTNER_CREATE_QUERY = "CREATE TABLE "
            + TABLE_BPARTNERS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_BP_ID + " NUMERIC, " + KEY_CUSTOMER_NAME + " TEXT, " + KEY_CUSTOMER_VALUE
            + " NUMERIC, " + KEY_PRICELIST_ID + " NUMERIC, " + KEY_EMAIL + " TEXT, " + KEY_NUMBER + " NUMERIC);";

    //create query for TABLE_SUPERVISOR
    private static final String SUPERVISOR_CREATE_QUERY = "CREATE TABLE "
            + TABLE_SUPERVISOR + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_ORG_ID + " NUMERIC, " + KEY_AUTHORIZE_CODE + " TEXT);";

    //create query for TABLE_POS_ORDER_NUMBER
    private static final String ORDER_NUMBER_CREATE_QUERY = "CREATE TABLE "
            + TABLE_POS_ORDER_NUMBER + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_START_NUMBER + " NUMERIC, " + KEY_END_NUMBER + " NUMERIC );";

    //create query for TABLE_POS_ORDER_HEADER
    private static final String POS_ORDER_HEADER_CREATE_QUERY = "CREATE TABLE "
            + TABLE_POS_ORDER_HEADER + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_POS_ID
            + " NUMERIC, " + KEY_BP_ID + " NUMERIC, " + KEY_CUSTOMER_NAME + " TEXT, "
            + KEY_PRICELIST_ID + " NUMERIC, " + KEY_CUSTOMER_VALUE + " NUMERIC, "
            + KEY_EMAIL + " TEXT, " + KEY_NUMBER + " NUMERIC, " + KEY_TOTAL_DISCOUNT_TYPE + " INTEGER, "
            + KEY_TOTAL_DISCOUNT_VALUE + " INTEGER, " + KEY_IS_CASH_CUSTOMER + " INTEGER, " + KEY_IS_TOTAL_DISCOUNTED + " TEXT, "
            + KEY_IS_POSTED + " TEXT, " + KEY_IS_PRINTED + " TEXT, " + KEY_KOT_TYPE + " TEXT," + KEY_IS_KOT + " TEXT);";

    //create query for TABLE_POS_LINES
    private static final String POS_LINE_ITEM_CREATE_QUERY = "CREATE TABLE "
            + TABLE_POS_LINES + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_POS_ID
            + " NUMERIC, " + KEY_PRODUCT_TERMINAL_ID + " NUMERIC, " + KEY_CATEGORY_ID + " NUMERIC, " + KEY_PRODUCT_ID + " NUMERIC, " + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_PRODUCT_ARABIC_NAME + " TEXT, " + KEY_PRODUCT_VALE + " TEXT, " + KEY_PRODUCT_UOM_ID + " NUMERIC, " + KEY_PRODUCT_UOM_VALUE + " TEXT, "
            + KEY_PRODUCT_QTY + " INTEGER, " + KEY_PRODUCT_STD_PRICE + " NUMERIC, " + KEY_PRODUCT_COST_PRICE + " INTEGER, "
            + KEY_PRODUCT_DISCOUNT_TYPE + " INTEGER, " + KEY_PRODUCT_DISCOUNT_VALUE + " INTEGER, " + KEY_PRODUCT_TOTAL_PRICE + " NUMERIC, "
            + KEY_IS_LINE_DISCOUNTED + " TEXT, " + KEY_IS_UPDATED + " TEXT, " + KEY_IS_POSTED + " TEXT, " + KEY_IS_KOT_GENERATED + " TEXT);";

    //create query for TABLE_POS_PAYMENT_DETAIL
    private static final String POS_PAYMENT_CREATE_QUERY = "CREATE TABLE "
            + TABLE_POS_PAYMENT_DETAIL + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_POS_ID + " NUMERIC, " + KEY_PAYMENT_CASH + " NUMERIC, " + KEY_PAYMENT_AMEX
            + " NUMERIC, " + KEY_PAYMENT_GIFT + " NUMERIC, " + KEY_PAYMENT_MASTER + " NUMERIC, " + KEY_PAYMENT_VISA + " NUMERIC, " + KEY_PAYMENT_OTHER + " NUMERIC, "
            + KEY_PAYMENT_RETURN + " NUMERIC);";

    //create query for KOT_TABLES
    private static final String KOT_TABLES_CREATE_QUERY = "CREATE TABLE "
            + TABLE_KOT_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_KOT_TABLE_ID + " NUMERIC, " + KEY_KOT_TABLE_NAME + " TEXT, " + KEY_IS_ORDER_AVAILABLE + " TEXT  );";

    //create query for KOT_TERMINALS
    private static final String KOT_TERMINALS_CREATE_QUERY = "CREATE TABLE "
            + TABLE_KOT_TERMINALS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_KOT_TERMINAL_ID + " NUMERIC, " + KEY_KOT_TERMINAL_NAME + " TEXT, "
            + KEY_KOT_TERMINAL_IP + " TEXT );";

    //create query for KOT_HEADER
    private static final String KOT_HEADER_CREATE_QUERY = "CREATE TABLE "
            + TABLE_KOT_HEADER + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_KOT_TABLE_ID + " NUMERIC, " + KEY_KOT_NUMBER + " NUMERIC, "
            + KEY_INVOICE_NUMBER + " NUMERIC, " + KEY_KOT_TERMINAL_ID + " NUMERIC, " + KEY_KOT_TOTAL_AMOUNT + " NUMERIC, " + KEY_KOT_ORDER_BY + " TEXT,"
            + KEY_KOT_TYPE + " TEXT," + KEY_ORDER_TYPE + " TEXT," + KEY_IS_KOT + " TEXT, " + KEY_IS_PRINTED + " TEXT, " + KEY_IS_POSTED + " TEXT, " + KEY_IS_SELECTED + " TEXT, " + KEY_CREATE_TIME + " NUMERIC);";

    //create query for TABLE_KOT_LINES
    private static final String KOT_LINE_ITEM_CREATE_QUERY = "CREATE TABLE "
            + TABLE_KOT_LINES + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_KOT_TABLE_ID
            + " NUMERIC, " + KEY_KOT_LINE_ID + " NUMERIC, " + KEY_KOT_NUMBER + " NUMERIC, " + KEY_INVOICE_NUMBER + " NUMERIC, " + KEY_CATEGORY_ID + " NUMERIC, " + KEY_PRODUCT_ID + " NUMERIC, " + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_PRODUCT_ARABIC_NAME + " TEXT, " + KEY_PRODUCT_VALE + " TEXT, " + KEY_PRODUCT_UOM_ID + " NUMERIC, " + KEY_PRODUCT_UOM_VALUE + " TEXT, "
            + KEY_PRODUCT_STD_PRICE + " NUMERIC, " + KEY_PRODUCT_COST_PRICE + " INTEGER, " + KEY_KOT_TERMINAL_ID + " NUMERIC, " + KEY_PRODUCT_QTY + " INTEGER, "
            + KEY_PRODUCT_TOTAL_PRICE + " NUMERIC, " + KEY_KOT_ITEM_NOTES + " TEXT, " + KEY_IS_PRINTED + " TEXT," + KEY_IS_POSTED + " TEXT, " + KEY_KOT_REF_LINE_ID + " NUMERIC, " + KEY_KOT_EXTRA_PRODUCT + " TEXT, "
            + KEY_CREATE_TIME + " NUMERIC," + KEY_PRODUCT_PREPARATION_TIME + " TEXT, " + KEY_IS_DELETED + " TEXT);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ORGANIZATION_CREATE_QUERY);
        db.execSQL(WAREHOUSE_CREATE_QUERY);
        db.execSQL(ROLE_CREATE_QUERY);
        db.execSQL(ROLE_ACCESS_CREATE_QUERY);
        db.execSQL(CATEGORY_CREATE_QUERY);
        db.execSQL(PRODUCT_CREATE_QUERY);
        db.execSQL(BPARTNER_CREATE_QUERY);
        db.execSQL(SUPERVISOR_CREATE_QUERY);
        db.execSQL(ORDER_NUMBER_CREATE_QUERY);
        db.execSQL(POS_ORDER_HEADER_CREATE_QUERY);
        db.execSQL(POS_LINE_ITEM_CREATE_QUERY);
        db.execSQL(POS_PAYMENT_CREATE_QUERY);

        db.execSQL(KOT_TABLES_CREATE_QUERY);
        db.execSQL(KOT_TERMINALS_CREATE_QUERY);
        db.execSQL(KOT_HEADER_CREATE_QUERY);
        db.execSQL(KOT_LINE_ITEM_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion) {
            db.execSQL("ALTER TABLE " + TABLE_KOT_HEADER + " ADD COLUMN " + KEY_COVERS_DETAILS + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_KOT_TABLE + " ADD COLUMN " + KEY_IS_COVERS_LEVEL + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_KOT_TABLE + " ADD COLUMN " + KEY_M_TABLE_GROUP_ID + " NUMERIC");
        }
    }

    /**
     * @param org
     */
    public void addOrganization(Organization org) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, org.getClientId());
            values.put(KEY_ORG_ID, org.getOrgId());
            values.put(KEY_ORG_NAME, org.getOrgName());
            values.put(KEY_ORG_ARABIC_NAME, org.getOrgArabicName());
            values.put(KEY_ORG_ADDRESS, org.getOrgAddress());
            values.put(KEY_ORG_PHONE, org.getOrgPhone());
            values.put(KEY_ORG_EMAIL, org.getOrgEmail());
            values.put(KEY_ORG_CITY, org.getOrgCity());
            values.put(KEY_ORG_COUNTRY, org.getOrgCountry());
            values.put(KEY_ORG_WEB_URL, org.getOrgWebUrl());
            values.put(KEY_IS_DEFAULT, org.getIsDefault());

            // Inserting Row
            db.insert(TABLE_ORGANIZATION, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            Cursor mCount = db.rawQuery("select categoryId from category where categoryId='" + category.getCategoryId() + "'", null);
            //mCount.moveToFirst();
            long categoryId = 0;

            while (mCount.moveToNext()) {
                categoryId = mCount.getLong(0);
            }
            mCount.close();

            if (categoryId == 0) {

                ContentValues values = new ContentValues();
                values.put(KEY_CLIENT_ID, category.getClientId());
                values.put(KEY_ORG_ID, category.getOrgId());
                values.put(KEY_CATEGORY_ID, category.getCategoryId());
                values.put(KEY_CATEGORY_NAME, category.getCategoryName());
                values.put(KEY_CATEGORY_VALUE, category.getCategoryValue());
                values.put(KEY_CATEGORY_IMAGE, category.getCategoryImage());
                values.put(KEY_SHOWN_DIGITAL_MENU, category.getShowDigitalMenu());

                // Inserting Row
                db.insert(TABLE_CATEGORY, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param orgId
     * @param warehouseId
     * @param warehouseName
     */
    public void addWarehouse(long clientId, long orgId, long warehouseId, String warehouseName, String isDefault) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, clientId);
            values.put(KEY_ORG_ID, orgId);
            values.put(KEY_WAREHOUSE_ID, warehouseId);
            values.put(KEY_WAREHOUSE_NAME, warehouseName);
            values.put(KEY_IS_DEFAULT, isDefault);

            // Inserting Row
            db.insert(TABLE_WAREHOUSE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param roleId
     * @param roleName
     */
    public void addRole(long roleId, String roleName, String isDefault) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ROLE_ID, roleId);
            values.put(KEY_ROLE_NAME, roleName);
            values.put(KEY_IS_DEFAULT, isDefault);

            // Inserting Row
            db.insert(TABLE_ROLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param roleId
     * @param orgId
     */
    public void addRoleAccess(long clientId, long orgId, long roleId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, clientId);
            values.put(KEY_ORG_ID, orgId);
            values.put(KEY_ROLE_ID, roleId);

            // Inserting Row
            db.insert(TABLE_ROLE_ACESS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void addAuthorizeId(long orgId, String authorizeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ORG_ID, orgId);
            values.put(KEY_AUTHORIZE_CODE, authorizeId);

            // Inserting Row
            db.insert(TABLE_SUPERVISOR, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param tables
     */
    public void addTables(Tables tables) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, tables.getClientId());
            values.put(KEY_ORG_ID, tables.getOrgId());
            values.put(KEY_KOT_TABLE_ID, tables.getTableId());
            values.put(KEY_KOT_TABLE_NAME, tables.getTableName());
            values.put(KEY_IS_ORDER_AVAILABLE, tables.getOrderAvailable());
            values.put(KEY_IS_COVERS_LEVEL, tables.getIsCoversLevel());
            values.put(KEY_M_TABLE_GROUP_ID, tables.getTableGroupId());

            // Inserting Row
            db.insert(TABLE_KOT_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param terminals
     */
    public void addTerminals(Terminals terminals) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, terminals.getClientId());
            values.put(KEY_ORG_ID, terminals.getOrgId());
            values.put(KEY_KOT_TERMINAL_ID, terminals.getTerminalId());
            values.put(KEY_KOT_TERMINAL_NAME, terminals.getTerminalName());
            values.put(KEY_KOT_TERMINAL_IP, terminals.getTerminalIP());

            // Inserting Row
            db.insert(TABLE_KOT_TERMINALS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @return terminalsList
     */
    public List<Terminals> getTerminals() {
        List<Terminals> terminalsList = new ArrayList<Terminals>();
        Terminals terminal = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select kotTerminalId, kotTerminalName,  kotTerminalIP from kotTerminals", null);

            /*Terminals terminal1 = new Terminals();
            terminal1.setTerminalId(0);
            terminal1.setTerminalName("All");
            terminal1.setTerminalIP("");
            terminalsList.add(terminal1);*/

            Terminals terminal2 = new Terminals();
            terminal2.setTerminalId(0);
            terminal2.setTerminalName("Completed List");
            terminal2.setTerminalIP("");
            terminalsList.add(terminal2);

            while (cursor.moveToNext()) {
                terminal = new Terminals();
                terminal.setTerminalId(cursor.getLong(0));
                terminal.setTerminalName(cursor.getString(1));
                terminal.setTerminalIP(cursor.getString(1));

                terminalsList.add(terminal);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return terminalsList;
    }

    /**
     * @param kotHeader
     */
    public boolean addKOTHeader(KOTHeader kotHeader) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean inserted = false;

        try {

            Cursor mCount = db.rawQuery("select kotTableId from kotHeader where kotNumber='" + kotHeader.getKotNumber() + "'", null);
            //mCount.moveToFirst();
            long kotTableId = -1;

            while (mCount.moveToNext()) {
                kotTableId = mCount.getLong(0);
            }
            mCount.close();

            if (kotTableId == -1) {
                ContentValues values = new ContentValues();
                values.put(KEY_KOT_TABLE_ID, kotHeader.getTablesId());
                values.put(KEY_KOT_NUMBER, kotHeader.getKotNumber());
                values.put(KEY_INVOICE_NUMBER, kotHeader.getInvoiceNumber());
                values.put(KEY_KOT_TERMINAL_ID, kotHeader.getTerminalId());
                values.put(KEY_KOT_TOTAL_AMOUNT, kotHeader.getTotalAmount());
                values.put(KEY_KOT_ORDER_BY, kotHeader.getOrderBy());
                values.put(KEY_KOT_TYPE, kotHeader.getKotType());
                values.put(KEY_ORDER_TYPE, kotHeader.getOrderType());
                values.put(KEY_IS_KOT, kotHeader.getIsKOT());
                values.put(KEY_IS_PRINTED, kotHeader.getPrinted());
                values.put(KEY_IS_POSTED, kotHeader.getPosted());
                values.put(KEY_IS_SELECTED, kotHeader.getSelected());
                values.put(KEY_CREATE_TIME, kotHeader.getCreateTime());
                values.put(KEY_COVERS_DETAILS, kotHeader.getCoversDetails());

                // Inserting Row
                db.insert(TABLE_KOT_HEADER, null, values);

                inserted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            inserted = false;
        } finally {
            inserted = true;
            db.close(); // Closing database connection
        }

        return inserted;
    }

    public List<String> getAuthorizeId(long orgId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> authIds = new ArrayList<>();
        try {
            Cursor mCursor = db.rawQuery("select authorizeCode from supervisor where orgId='" + orgId + "'", null);

            while (mCursor.moveToNext()) {
                authIds.add(mCursor.getString(0));
            }
            mCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return authIds;
    }

    /**
     * @param posId
     * @param bPId
     * @param name
     * @param pricelistId
     * @param value
     * @param email
     * @param mobile
     */
    public void addPOSHeader(long posId, long bPId, String name, long pricelistId, long value, String email, long mobile, int isCashCustomer, String isKOT) {

        SQLiteDatabase db = this.getWritableDatabase();
        boolean isAvail = false;

        try {

            Cursor mCount = db.rawQuery("select bpId from posOrderHeader where posId='" + posId + "'", null);

            while (mCount.moveToNext()) {

                if (bPId != mCount.getLong(0)) {
                    String updateSQL = "update posOrderHeader set bpId='" + bPId + "', customerName='" + name + "' where posId='" + posId + "';";
                    db.execSQL(updateSQL);
                }
                isAvail = true;
            }
            mCount.close();

            if (isAvail) {

                Log.i("ADD POS CUSTOMER", "Already inserted");
                return;

            } else {
                ContentValues values = new ContentValues();
                values.put(KEY_POS_ID, posId);
                values.put(KEY_BP_ID, bPId);
                values.put(KEY_CUSTOMER_NAME, name);
                values.put(KEY_PRICELIST_ID, pricelistId);
                values.put(KEY_CUSTOMER_VALUE, value);
                values.put(KEY_EMAIL, email);
                values.put(KEY_NUMBER, mobile);
                values.put(KEY_TOTAL_DISCOUNT_TYPE, 0);
                values.put(KEY_TOTAL_DISCOUNT_VALUE, 0);
                values.put(KEY_IS_CASH_CUSTOMER, isCashCustomer);
                values.put(KEY_IS_TOTAL_DISCOUNTED, "N");
                values.put(KEY_IS_POSTED, "N");
                values.put(KEY_IS_PRINTED, "N");
                values.put(KEY_IS_KOT, isKOT);

                // Inserting Row
                db.insert(TABLE_POS_ORDER_HEADER, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void updatePOSHeader(long posId, String isKOT) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String updateSQL = "update posOrderHeader set isKOT='" + isKOT + "' where posId='" + posId + "';";
            db.execSQL(updateSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public POSOrders getPosHeader(long posId) {

        POSOrders posOrder = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select bpId, customerName, isCashCustomer, isPrinted, isKOT from posOrderHeader where posId='" + posId + "';", null);

            while (cursor.moveToNext()) {
                posOrder = new POSOrders();
                posOrder.setBpId(cursor.getLong(0));
                posOrder.setCustomerName(cursor.getString(1));
                posOrder.setIsCashCustomer(cursor.getInt(2));
                posOrder.setIsPrinted(cursor.getString(3));
                posOrder.setIsKOT(cursor.getString(4));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return posOrder;
    }

    /**
     * If DISCOUNT_TYPE is 0 means %, 1 means amount
     *
     * @param posId
     * @param productId
     * @param productName
     * @param uomId
     * @param productValue
     * @param qty
     * @param stdPrice
     * @param costPrice
     * @param totalPrice
     */
    public void addPOSLineItem(long posId, long terminalId, long productId, long categoryId, String productName, String productValue, long uomId, String uomValue, int qty, double stdPrice, double costPrice, double totalPrice, String prodArabicName) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            Cursor mCount = db.rawQuery("select qty, productDiscType, productDiscValue from posLineItems where posId='" + posId + "' and productId='" + productId + "'", null);
            //mCount.moveToFirst();
            int exQty = 0;
            int prodDiscType = 0;
            int prodDiscValue = 0;
            String isupdated = "Y";
            while (mCount.moveToNext()) {
                exQty = mCount.getInt(0);
                prodDiscType = mCount.getInt(1);
                prodDiscValue = mCount.getInt(2);
            }
            mCount.close();

            if (exQty != 0) {

                String updateSQL = "update posLineItems set isUpdated='N' where posId='" + posId + "';";
                db.execSQL(updateSQL);

                qty = qty + exQty;
                totalPrice = qty * stdPrice;
                if (prodDiscType == 0) {
                    totalPrice = totalPrice - (totalPrice * prodDiscValue / 100);
                } else {
                    totalPrice = totalPrice - prodDiscValue;
                }

                String strSQL = "update posLineItems set isUpdated='Y', qty='" + qty
                        + "', totalPrice='" + totalPrice + "', productDiscValue='" + prodDiscValue + "' where posId='"
                        + posId + "' and productId = '" + productId + "';";
                db.execSQL(strSQL);

            } else {
                try {
                    String updateSQL = "update posLineItems set isUpdated='N' where posId='" + posId + "';";
                    db.execSQL(updateSQL);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();
                values.put(KEY_POS_ID, posId);
                values.put(KEY_PRODUCT_TERMINAL_ID, terminalId);
                values.put(KEY_CATEGORY_ID, categoryId);
                values.put(KEY_PRODUCT_ID, productId);
                values.put(KEY_PRODUCT_NAME, productName);
                values.put(KEY_PRODUCT_ARABIC_NAME, prodArabicName);
                values.put(KEY_PRODUCT_VALE, productValue);
                values.put(KEY_PRODUCT_UOM_ID, uomId);
                values.put(KEY_PRODUCT_UOM_VALUE, uomValue);
                values.put(KEY_PRODUCT_QTY, qty);
                values.put(KEY_PRODUCT_STD_PRICE, stdPrice);
                values.put(KEY_PRODUCT_COST_PRICE, costPrice);
                values.put(KEY_PRODUCT_DISCOUNT_TYPE, 0);
                values.put(KEY_PRODUCT_DISCOUNT_VALUE, 0);
                totalPrice = qty * stdPrice;
                values.put(KEY_PRODUCT_TOTAL_PRICE, totalPrice);
                values.put(KEY_IS_LINE_DISCOUNTED, "N");
                values.put(KEY_IS_UPDATED, "N");
                values.put(KEY_IS_POSTED, "N");
                values.put(KEY_IS_KOT_GENERATED, "N");

                // Inserting Row
                db.insert(TABLE_POS_LINES, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void addKOTtoPOSLineItem(long posId, KOTLineItems kotLineItems, Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            Cursor mCount = db.rawQuery("select qty from posLineItems where posId='" + posId + "' and productId='" + product.getProdId() + "'", null);
            int exQty = 0;
            while (mCount.moveToNext()) {
                exQty = mCount.getInt(0);
            }
            mCount.close();

            if (exQty == 0) {

                ContentValues values = new ContentValues();
                values.put(KEY_POS_ID, posId);
                values.put(KEY_PRODUCT_TERMINAL_ID, product.getTerminalId());
                values.put(KEY_CATEGORY_ID, product.getCategoryId());
                values.put(KEY_PRODUCT_ID, product.getProdId());
                values.put(KEY_PRODUCT_NAME, product.getProdName());
                values.put(KEY_PRODUCT_ARABIC_NAME, product.getProdArabicName());
                values.put(KEY_PRODUCT_VALE, product.getProdValue());
                values.put(KEY_PRODUCT_UOM_ID, product.getUomId());
                values.put(KEY_PRODUCT_UOM_VALUE, product.getUomValue());
                values.put(KEY_PRODUCT_QTY, kotLineItems.getQty());
                values.put(KEY_PRODUCT_STD_PRICE, product.getSalePrice());
                values.put(KEY_PRODUCT_COST_PRICE, product.getCostPrice());
                values.put(KEY_PRODUCT_DISCOUNT_TYPE, 0);
                values.put(KEY_PRODUCT_DISCOUNT_VALUE, 0);
                values.put(KEY_PRODUCT_TOTAL_PRICE, kotLineItems.getTotalPrice());
                values.put(KEY_IS_LINE_DISCOUNTED, "N");
                values.put(KEY_IS_UPDATED, "N");
                values.put(KEY_IS_POSTED, "N");
                values.put(KEY_IS_KOT_GENERATED, "Y");

                // Inserting Row
                db.insert(TABLE_POS_LINES, null, values);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void updatePOSLineItem(long posId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String strSQL = "update posLineItems set posId='" + posId
                    + "' where posId='"
                    + 0 + "' ;";
            db.execSQL(strSQL);

            db.execSQL("delete from posOrderHeader where posId = '" + 0 + "'");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void removePOSLineItem(long posId, long productId, long categoryId, String productName, String productValue, long uomId, String uomValue, int qty, double stdPrice, double costPrice, double totalPrice) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            Cursor mCount = db.rawQuery("select qty, productDiscType, productDiscValue from posLineItems where posId='" + posId + "' and productId='" + productId + "'", null);
            //mCount.moveToFirst();
            int exQty = 0;
            int prodDiscType = 0;
            int prodDiscValue = 0;

            while (mCount.moveToNext()) {
                exQty = mCount.getInt(0);
                prodDiscType = mCount.getInt(1);
                prodDiscValue = mCount.getInt(2);
            }
            mCount.close();

            if (exQty != 0) {
                qty = exQty - qty;
                if (qty == 0) {
                    db.execSQL("delete from posLineItems where posId = '" + posId + "' and productId = '" + productId + "'");
                } else {
                    totalPrice = qty * stdPrice;
                    if (prodDiscType != 0) {
                        totalPrice = totalPrice - prodDiscValue;
                    } else {
                        totalPrice = totalPrice - (totalPrice * prodDiscValue / 100);
                    }

                    String strSQL = "update posLineItems set isUpdated='Y', qty='" + qty
                            + "', totalPrice='" + totalPrice + "', productDiscValue='" + prodDiscValue + "' where posId='"
                            + posId + "' and productId = '" + productId + "';";
                    db.execSQL(strSQL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public int getProdDiscType(long posId, long productId) {

        SQLiteDatabase db = this.getWritableDatabase();
        int prodDiscType = 0;
        try {
            Cursor mCount = db.rawQuery("select productDiscType from posLineItems where posId='" + posId + "' and productId='" + productId + "'", null);
            while (mCount.moveToNext()) {
                prodDiscType = mCount.getInt(0);
            }
            mCount.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prodDiscType;
    }

    public int getProdDiscValue(long posId, long productId) {

        SQLiteDatabase db = this.getWritableDatabase();
        int prodDiscValue = 0;
        try {
            Cursor mCount = db.rawQuery("select productDiscValue from posLineItems where posId='" + posId + "' and productId='" + productId + "'", null);
            while (mCount.moveToNext()) {
                prodDiscValue = mCount.getInt(0);
            }
            mCount.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prodDiscValue;
    }

    public int getProdQty(long posId, long productId) {

        SQLiteDatabase db = this.getWritableDatabase();
        int qty = 0;
        try {
            Cursor mCount = db.rawQuery("select qty from posLineItems where posId='" + posId + "' and productId='" + productId + "'", null);
            while (mCount.moveToNext()) {
                qty = mCount.getInt(0);
            }
            mCount.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qty;
    }

    public int getTotalDiscType(long posId) {

        SQLiteDatabase db = this.getWritableDatabase();
        int totalDiscType = 0;
        try {
            Cursor mCount = db.rawQuery("select totalDiscType from posOrderHeader where posId='" + posId + "' ", null);
            while (mCount.moveToNext()) {
                totalDiscType = mCount.getInt(0);
                break;
            }
            mCount.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalDiscType;
    }

    public int getTotalDiscValue(long posId) {

        SQLiteDatabase db = this.getWritableDatabase();
        int totalDiscValue = 0;
        try {
            Cursor mCount = db.rawQuery("select totalDiscValue from posOrderHeader where posId='" + posId + "' ", null);
            while (mCount.moveToNext()) {
                totalDiscValue = mCount.getInt(0);
                break;
            }
            mCount.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalDiscValue;
    }

    public void addPOSPayments(POSPayment payment) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(KEY_POS_ID, payment.getPosId());
            values.put(KEY_PAYMENT_CASH, payment.getCash());
            values.put(KEY_PAYMENT_AMEX, payment.getAmex());
            values.put(KEY_PAYMENT_GIFT, payment.getGift());
            values.put(KEY_PAYMENT_MASTER, payment.getMaster());
            values.put(KEY_PAYMENT_VISA, payment.getVisa());
            values.put(KEY_PAYMENT_OTHER, payment.getOther());
            values.put(KEY_PAYMENT_RETURN, payment.getChange());

            // Inserting Row
            db.insert(TABLE_POS_PAYMENT_DETAIL, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public POSPayment getPaymentDetails(long posId) {

        SQLiteDatabase db = this.getWritableDatabase();

        POSPayment payment = null;

        try {
            Cursor cursor = db.rawQuery("select * from posPaymentDetail where posId = '" + posId + "' ", null);

            while (cursor.moveToNext()) {
                payment = new POSPayment();
                payment.setPosId(cursor.getLong(0));
                payment.setCash(cursor.getDouble(1));
                payment.setAmex(cursor.getDouble(1));
                payment.setGift(cursor.getDouble(1));
                payment.setMaster(cursor.getDouble(1));
                payment.setVisa(cursor.getDouble(1));
                payment.setOther(cursor.getDouble(1));
                payment.setChange(cursor.getDouble(1));
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return null;
    }

    /**
     * @param categoryId
     * @param product
     */
    public void addProduct(long categoryId, Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            Cursor mCount = db.rawQuery("select productId  from product where categoryId='" + categoryId + "' and productId='" + product.getProdId() + "'", null);
            //mCount.moveToFirst();
            long productId = 0;

            while (mCount.moveToNext()) {
                productId = mCount.getLong(0);
            }
            mCount.close();

            if (productId == 0) {
                ContentValues values = new ContentValues();
                values.put(KEY_CLIENT_ID, product.getClientId());//KEY_CLIENT_ID
                values.put(KEY_ORG_ID, product.getOrgId());//KEY_ORG_ID
                values.put(KEY_CATEGORY_ID, categoryId);//KEY_CATEGORY_ID
                values.put(KEY_PRODUCT_ID, product.getProdId());//KEY_PRODUCT_ID
                values.put(KEY_PRODUCT_NAME, product.getProdName());//KEY_PRODUCT_NAME
                values.put(KEY_PRODUCT_VALE, product.getProdValue());//KEY_PRODUCT_VALE
                values.put(KEY_PRODUCT_UOM_ID, product.getUomId());//KEY_PRODUCT_UOM_ID
                values.put(KEY_PRODUCT_UOM_VALUE, product.getUomValue());//KEY_PRODUCT_UOM_VALUE
                values.put(KEY_PRODUCT_QTY, 1);//KEY_PRODUCT_QTY
                values.put(KEY_PRODUCT_STD_PRICE, product.getSalePrice());//KEY_PRODUCT_STD_PRICE
                values.put(KEY_PRODUCT_COST_PRICE, product.getCostPrice());//KEY_PRODUCT_COST_PRICE
                values.put(KEY_PRODUCT_IMAGE, product.getProdImage());//KEY_PRODUCT_IMAGE
                values.put(KEY_PRODUCT_ARABIC_NAME, product.getProdArabicName());//KEY_PRODUCT_ARABIC_NAME
                values.put(KEY_PRODUCT_DESCRIPTION, product.getDescription());//KEY_PRODUCT_DESCRIPTION
                values.put(KEY_SHOWN_DIGITAL_MENU, product.getShowDigitalMenu());//KEY_SHOWN_DIGITAL_MENU
                values.put(KEY_PRODUCT_VIDEO, product.getProdVideoPath());//KEY_PRODUCT_VIDEO
                values.put(KEY_PRODUCT_CALORIES, product.getCalories());//KEY_PRODUCT_CALORIES
                values.put(KEY_PRODUCT_PREPARATION_TIME, product.getPreparationTime());//KEY_PRODUCT_PREPARATION_TIME
                values.put(KEY_PRODUCT_TERMINAL_ID, product.getTerminalId());//KEY_PRODUCT_TERMINAL_ID

                // Inserting Row
                db.insert(TABLE_PRODUCT, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param bpId
     * @param name
     * @param value
     */
    public void addBPartner(long clientId, long orgId, long bpId, String name, long value, long priceListId, String email, long number) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, clientId);
            values.put(KEY_ORG_ID, orgId);
            values.put(KEY_BP_ID, bpId);
            values.put(KEY_CUSTOMER_NAME, name);
            values.put(KEY_CUSTOMER_VALUE, value);
            values.put(KEY_PRICELIST_ID, priceListId);
            values.put(KEY_EMAIL, email);
            values.put(KEY_NUMBER, number);

            // Inserting Row
            db.insert(TABLE_BPARTNERS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

    }

    public Organization getOrganizationDetail(long orgId) {

        Organization org = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from organization where orgId ='" + orgId + "'", null);

            while (cursor.moveToNext()) {
                org = new Organization();
                org.setClientId(cursor.getLong(1));
                org.setOrgId(cursor.getLong(2));
                org.setOrgName(cursor.getString(3));
                org.setOrgArabicName(cursor.getString(4));
                org.setOrgImage(cursor.getString(5));
                org.setOrgAddress(cursor.getString(6));
                org.setOrgPhone(cursor.getString(7));
                org.setOrgEmail(cursor.getString(8));
                org.setOrgCity(cursor.getString(9));
                org.setOrgCountry(cursor.getString(10));
                org.setOrgWebUrl(cursor.getString(11));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return org;
    }

    /**
     * @return orgList
     */
    public List<Organization> getOrganizations() {

        List<Organization> orgList = new ArrayList<Organization>();
        Organization org = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select orgId, orgName from organization ", null);

            while (cursor.moveToNext()) {
                org = new Organization();
                org.setOrgId(cursor.getLong(0));
                org.setOrgName(cursor.getString(1));

                orgList.add(org);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return orgList;
    }

    /**
     * @param roleId
     * @return mOrgIdList
     */
    public List<Long> getOrgId(long roleId) {
        long orgId = 0;
        List<Long> mOrgIdList = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select orgId from roleAccess where roleId='" + roleId + "'", null);

            mOrgIdList = new ArrayList<Long>();
            while (cursor.moveToNext()) {
                mOrgIdList.add(cursor.getLong(0));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return mOrgIdList;
    }

    /**
     * @param orgId
     * @return org
     */
    public Organization getOrgDetails(long orgId) {

        Organization org = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("select orgId, orgName from organization where orgId='" + orgId + "'", null);

            while (cursor.moveToNext()) {
                org = new Organization();
                org.setOrgId(cursor.getLong(0));
                org.setOrgName(cursor.getString(1));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return org;
    }

    /**
     * @return roleList
     */
    public List<Role> getRole() {
        List<Role> roleList = new ArrayList<Role>();
        Role role = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select roleId, roleName from role", null);

            while (cursor.moveToNext()) {
                role = new Role();
                role.setRoleId(cursor.getLong(0));
                role.setRoleName(cursor.getString(1));

                roleList.add(role);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return roleList;
    }

    /**
     * @param orgId
     * @return warehouseList
     */
    public List<Warehouse> getWarehouse(long orgId) {
        List<Warehouse> warehouseList = new ArrayList<Warehouse>();
        Warehouse warehouse = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select warehouseId, warehouseName from warehouse where orgId='" + orgId + "' ", null);

            while (cursor.moveToNext()) {
                warehouse = new Warehouse();
                warehouse.setWarehouseId(cursor.getLong(0));
                warehouse.setWarehouseName(cursor.getString(1));

                warehouseList.add(warehouse);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return warehouseList;
    }

    public List<Long> getDefaultIds() {

        String isDefault = "Y";
        List<Long> defaultIdList = new ArrayList<Long>();
        List<Long> mRoleIdList;

        long orgId = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            //get the default orgid
            Cursor cursor = db.rawQuery("select orgId from organization where isdefault='" + isDefault + "' ", null);
            while (cursor.moveToNext()) {
                orgId = cursor.getLong(0);
                defaultIdList.add(orgId);
            }
            cursor.close();

            //get the default warehouse id
            Cursor cursor2 = db.rawQuery("select warehouseId from warehouse where orgId='" + orgId + "' and isdefault='" + isDefault + "'", null);
            while (cursor2.moveToNext()) {
                defaultIdList.add(cursor2.getLong(0));
            }
            cursor2.close();

            //get the all role id based on org
            Cursor cursor3 = db.rawQuery("select roleId from roleAccess where orgId='" + orgId + "'", null);
            mRoleIdList = new ArrayList<Long>();
            while (cursor3.moveToNext()) {
                mRoleIdList.add(cursor3.getLong(0));
            }
            cursor3.close();

            for (int i = 0; i < mRoleIdList.size(); i++) {
                //get the default role id
                Cursor cursor4 = db.rawQuery("select roleId from role where isdefault='" + isDefault + "'", null);
                while (cursor4.moveToNext()) {
                    defaultIdList.add(cursor4.getLong(0));
                }
                cursor4.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return defaultIdList;
    }

    /**
     * @return
     */
    public List<Category> getCategory(long clientId, long orgId) {
        List<Category> categoryList = new ArrayList<Category>();
        Category category = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select categoryId, categoryName, categoryValue from category where clientId = '" + clientId + "' and orgId = '" + orgId + "' ORDER BY categoryName ASC", null);

            while (cursor.moveToNext()) {
                category = new Category();
                category.setCategoryId(cursor.getLong(0));
                category.setCategoryName(cursor.getString(1));
                category.setCategoryValue(cursor.getString(2));

                Cursor cur = db.rawQuery("select * from product where categoryId = '" + cursor.getLong(0) + "' and clientId = '" + clientId + "' and orgId = '" + orgId + "' ORDER BY productName COLLATE NOCASE ASC ", null);
                int cnt = cur.getCount();
                cur.close();

                if (cnt > 0)
                    categoryList.add(category);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return categoryList;
    }

    public List<Tables> getTables(long clientId, long orgId) {
        List<Tables> tableList = new ArrayList<Tables>();
        Tables table = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select kotTableId, kotTableName, isOrderAvailable from kotTables where clientId = '" + clientId + "' and orgId = '" + orgId + "' ORDER BY kotTableId ASC", null);

            while (cursor.moveToNext()) {
                table = new Tables();
                table.setTableId(cursor.getLong(0));
                table.setTableName(cursor.getString(1));
                table.setOrderAvailable(cursor.getString(2));
                tableList.add(table);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return tableList;
    }

    public List<String> getTableIDList(long clientId, long orgId) {
        List<String> tableList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select kotTableId from kotTables where clientId = '" + clientId + "' and orgId = '" + orgId + "' ORDER BY kotTableId ASC", null);

            while (cursor.moveToNext()) {
                tableList.add(String.valueOf(cursor.getLong(0)));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return tableList;
    }

    public Tables getTableData(long clientId, long orgId, long tableId) {

        Tables table = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select kotTableId, kotTableName, isOrderAvailable from kotTables where clientId = '" + clientId + "' and orgId = '" + orgId + "' and kotTableId = '" + tableId + "' ", null);

            while (cursor.moveToNext()) {
                table = new Tables();
                table.setTableId(cursor.getLong(0));
                table.setTableName(cursor.getString(1));
                table.setOrderAvailable(cursor.getString(2));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return table;
    }

    /**
     * @param terminalId
     * @return
     */
    public Terminals getTerminalData(long clientId, long orgId, long terminalId) {

        Terminals terminals = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select kotTerminalId, kotTerminalName, kotTerminalIP from kotTerminals where clientId = '" + clientId + "' and orgId = '" + orgId + "' and kotTerminalId = '" + terminalId + "' ", null);

            while (cursor.moveToNext()) {
                terminals = new Terminals();
                terminals.setTerminalId(cursor.getLong(0));
                terminals.setTerminalName(cursor.getString(1));
                terminals.setTerminalIP(cursor.getString(2));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return terminals;
    }

    /**
     * @return posOrdersList
     */
    public List<POSOrders> getPOSOrders() {
        List<POSOrders> posOrdersList = new ArrayList<POSOrders>();
        POSOrders posOrders = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select posId, customerName, isCashCustomer from posOrderHeader where isPosted='N' ", null);

            posOrders = new POSOrders();
            posOrders.setPosId(0);
            posOrders.setCustomerName("");
            posOrders.setIsCashCustomer(0);

            posOrdersList.add(posOrders);

            while (cursor.moveToNext()) {
                posOrders = new POSOrders();
                posOrders.setPosId(cursor.getLong(0));
                posOrders.setCustomerName(cursor.getString(1));
                posOrders.setIsCashCustomer(cursor.getInt(2));

                posOrdersList.add(posOrders);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return posOrdersList;
    }

    /**
     * @param posId
     * @return posLineItemList
     */
    public List<POSLineItem> getPOSLineItems(long posId, long categoryId) {

        List<POSLineItem> posLineItemList = new ArrayList<POSLineItem>();
        POSLineItem posLineItems = null;
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (categoryId == 0)
                cursor = db.rawQuery("select * from posLineItems where posId = '" + posId + "' ", null);
            else
                cursor = db.rawQuery("select * from posLineItems where posId = '" + posId + "' and categoryId = '" + categoryId + "' ", null);

            while (cursor.moveToNext()) {
                posLineItems = new POSLineItem();
                posLineItems.setPosId(cursor.getLong(1));
                posLineItems.setTerminalId(cursor.getLong(2));
                posLineItems.setCategoryId(cursor.getLong(3));
                posLineItems.setProductId(cursor.getLong(4));
                posLineItems.setProductName(cursor.getString(5));
                posLineItems.setProdArabicName(cursor.getString(6));
                posLineItems.setProductValue(cursor.getString(7));
                posLineItems.setPosUOMId(cursor.getLong(8));
                posLineItems.setPosUOMValue(cursor.getString(9));
                posLineItems.setPosQty(cursor.getInt(10));
                posLineItems.setStdPrice(cursor.getDouble(11));
                posLineItems.setCostPrice(cursor.getDouble(12));
                posLineItems.setDiscType(cursor.getInt(13));
                posLineItems.setDiscValue(cursor.getInt(14));
                posLineItems.setTotalPrice(cursor.getDouble(15));
                posLineItems.setIsDiscounted(cursor.getString(16));
                posLineItems.setIsUpdated(cursor.getString(17));
                posLineItems.setIsPosted(cursor.getString(18));
                posLineItems.setIsKOTGenerated(cursor.getString(19));

                posLineItemList.add(posLineItems);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return posLineItemList;
    }

    /**
     * @param categoryId
     * @return
     */
    public List<Product> getProducts(long clientId, long orgId, long categoryId) {

        List<Product> productList = new ArrayList<Product>();
        Product product = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' and categoryId = '" + categoryId + "' ORDER BY productName COLLATE NOCASE ASC ", null);

            while (cursor.moveToNext()) {
                product = new Product();
                product.setClientId(cursor.getLong(1)); //KEY_CLIENT_ID
                product.setOrgId(cursor.getLong(2)); //KEY_ORG_ID
                product.setCategoryId(cursor.getLong(3)); //KEY_CATEGORY_ID
                product.setProdId(cursor.getLong(4)); //KEY_PRODUCT_ID
                product.setProdName(cursor.getString(5));//KEY_PRODUCT_NAME
                product.setProdValue(cursor.getString(6));//KEY_PRODUCT_VALE
                product.setUomId(cursor.getLong(7));//KEY_PRODUCT_UOM_ID
                product.setUomValue(cursor.getString(8));//KEY_PRODUCT_UOM_VALUE
                //KEY_PRODUCT_QTY
                product.setSalePrice(cursor.getDouble(10));//KEY_PRODUCT_STD_PRICE
                product.setCostPrice(cursor.getDouble(11));//KEY_PRODUCT_COST_PRICE
                product.setProdImage(cursor.getString(12));//KEY_PRODUCT_IMAGE
                product.setProdArabicName(cursor.getString(13));//KEY_PRODUCT_ARABIC_NAME
                product.setDescription(cursor.getString(14));//KEY_PRODUCT_DESCRIPTION
                product.setShowDigitalMenu(cursor.getString(15));//KEY_SHOWN_DIGITAL_MENU
                product.setProdVideoPath(cursor.getString(16));//KEY_PRODUCT_VIDEO
                product.setCalories(cursor.getString(17));//KEY_PRODUCT_CALORIES
                product.setPreparationTime(cursor.getString(18));//KEY_PRODUCT_PREPARATION_TIME
                product.setTerminalId(cursor.getLong(19));//KEY_PRODUCT_TERMINAL_ID
                product.setDefaultQty(0);

                productList.add(product);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return productList;
    }

    public String getCategoryImage(long clientId, long orgId, long categoryId) {

        String imgPath = "";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select productImage from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' and categoryId = '" + categoryId + "' ", null);

            while (cursor.moveToNext()) {
                imgPath = cursor.getString(0);
                break;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return imgPath;

    }

    public List<Long> getProductIDs(long clientId, long orgId, long categoryId) {

        List<Long> productIDList = new ArrayList<Long>();
        long productId = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select productId from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' and categoryId = '" + categoryId + "' ORDER BY productName COLLATE NOCASE ASC ", null);

            while (cursor.moveToNext()) {
                productId = cursor.getLong(0);
                productIDList.add(productId);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return productIDList;
    }


    public Product getProduct(long clientId, long orgId, String prodId) {

        Product product = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' and productValue = '" + prodId + "' ", null);

            while (cursor.moveToNext()) {
                product = new Product();
                product.setClientId(cursor.getLong(1)); //KEY_CLIENT_ID
                product.setOrgId(cursor.getLong(2)); //KEY_ORG_ID
                product.setCategoryId(cursor.getLong(3)); //KEY_CATEGORY_ID
                product.setProdId(cursor.getLong(4)); //KEY_PRODUCT_ID
                product.setProdName(cursor.getString(5));//KEY_PRODUCT_NAME
                product.setProdValue(cursor.getString(6));//KEY_PRODUCT_VALE
                product.setUomId(cursor.getLong(7));//KEY_PRODUCT_UOM_ID
                product.setUomValue(cursor.getString(8));//KEY_PRODUCT_UOM_VALUE
                //KEY_PRODUCT_QTY
                product.setSalePrice(cursor.getDouble(10));//KEY_PRODUCT_STD_PRICE
                product.setCostPrice(cursor.getDouble(11));//KEY_PRODUCT_COST_PRICE
                product.setProdImage(cursor.getString(12));//KEY_PRODUCT_IMAGE
                product.setProdArabicName(cursor.getString(13));//KEY_PRODUCT_ARABIC_NAME
                product.setDescription(cursor.getString(14));//KEY_PRODUCT_DESCRIPTION
                product.setShowDigitalMenu(cursor.getString(15));//KEY_SHOWN_DIGITAL_MENU
                product.setProdVideoPath(cursor.getString(16));//KEY_PRODUCT_VIDEO
                product.setCalories(cursor.getString(17));//KEY_PRODUCT_CALORIES
                product.setPreparationTime(cursor.getString(18));//KEY_PRODUCT_PREPARATION_TIME
                product.setTerminalId(cursor.getLong(19));//KEY_PRODUCT_TERMINAL_ID
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return product;
    }

    /**
     * @param prodId
     * @return
     */
    public Product getProduct(long clientId, long orgId, long prodId) {

        Product product = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' and productId = '" + prodId + "' ", null);

            while (cursor.moveToNext()) {
                product = new Product();
                product.setClientId(cursor.getLong(1)); //KEY_CLIENT_ID
                product.setOrgId(cursor.getLong(2)); //KEY_ORG_ID
                product.setCategoryId(cursor.getLong(3)); //KEY_CATEGORY_ID
                product.setProdId(cursor.getLong(4)); //KEY_PRODUCT_ID
                product.setProdName(cursor.getString(5));//KEY_PRODUCT_NAME
                product.setProdValue(cursor.getString(6));//KEY_PRODUCT_VALE
                product.setUomId(cursor.getLong(7));//KEY_PRODUCT_UOM_ID
                product.setUomValue(cursor.getString(8));//KEY_PRODUCT_UOM_VALUE
                //KEY_PRODUCT_QTY
                product.setSalePrice(cursor.getDouble(10));//KEY_PRODUCT_STD_PRICE
                product.setCostPrice(cursor.getDouble(11));//KEY_PRODUCT_COST_PRICE
                product.setProdImage(cursor.getString(12));//KEY_PRODUCT_IMAGE
                product.setProdArabicName(cursor.getString(13));//KEY_PRODUCT_ARABIC_NAME
                product.setDescription(cursor.getString(14));//KEY_PRODUCT_DESCRIPTION
                product.setShowDigitalMenu(cursor.getString(15));//KEY_SHOWN_DIGITAL_MENU
                product.setProdVideoPath(cursor.getString(16));//KEY_PRODUCT_VIDEO
                product.setCalories(cursor.getString(17));//KEY_PRODUCT_CALORIES
                product.setPreparationTime(cursor.getString(18));//KEY_PRODUCT_PREPARATION_TIME
                product.setTerminalId(cursor.getLong(19));//KEY_PRODUCT_TERMINAL_ID
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return product;
    }

    /**
     * @return
     */
    public List<Product> getAllProduct(long clientId, long orgId) {

        List<Product> productList = new ArrayList<Product>();
        Product product = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' ORDER BY productName COLLATE NOCASE ASC", null);

            while (cursor.moveToNext()) {
                product = new Product();
                product.setClientId(cursor.getLong(1)); //KEY_CLIENT_ID
                product.setOrgId(cursor.getLong(2)); //KEY_ORG_ID
                product.setCategoryId(cursor.getLong(3)); //KEY_CATEGORY_ID
                product.setProdId(cursor.getLong(4)); //KEY_PRODUCT_ID
                product.setProdName(cursor.getString(5));//KEY_PRODUCT_NAME
                product.setProdValue(cursor.getString(6));//KEY_PRODUCT_VALE
                product.setUomId(cursor.getLong(7));//KEY_PRODUCT_UOM_ID
                product.setUomValue(cursor.getString(8));//KEY_PRODUCT_UOM_VALUE
                //KEY_PRODUCT_QTY
                product.setSalePrice(cursor.getDouble(10));//KEY_PRODUCT_STD_PRICE
                product.setCostPrice(cursor.getDouble(11));//KEY_PRODUCT_COST_PRICE
                product.setProdImage(cursor.getString(12));//KEY_PRODUCT_IMAGE
                product.setProdArabicName(cursor.getString(13));//KEY_PRODUCT_ARABIC_NAME
                product.setDescription(cursor.getString(14));//KEY_PRODUCT_DESCRIPTION
                product.setShowDigitalMenu(cursor.getString(15));//KEY_SHOWN_DIGITAL_MENU
                product.setProdVideoPath(cursor.getString(16));//KEY_PRODUCT_VIDEO
                product.setCalories(cursor.getString(17));//KEY_PRODUCT_CALORIES
                product.setPreparationTime(cursor.getString(18));//KEY_PRODUCT_PREPARATION_TIME
                product.setTerminalId(cursor.getLong(19));//KEY_PRODUCT_TERMINAL_ID

                productList.add(product);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return productList;
    }

    /**
     * @return mBPList
     */
    public List<BPartner> getBPartners(long clientId, long orgId) {

        List<BPartner> mBPList = new ArrayList<BPartner>();

        BPartner bPartner = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from businessPartner where clientId = '" + clientId + "' and orgId = '" + orgId + "'", null);

            while (cursor.moveToNext()) {
                bPartner = new BPartner();
                bPartner.setClientId(cursor.getLong(1));
                bPartner.setOrgId(cursor.getLong(2));
                bPartner.setClientId(cursor.getLong(3));
                bPartner.setOrgId(cursor.getLong(4));
                bPartner.setBpId(cursor.getLong(5));
                bPartner.setBpName(cursor.getString(6));
                bPartner.setBpValue(cursor.getLong(7));
                bPartner.setBpPriceListId(cursor.getLong(8));
                bPartner.setBpEmail(cursor.getString(9));
                bPartner.setBpNumber(cursor.getLong(10));

                mBPList.add(bPartner);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return mBPList;
    }

    public void updatePOSLineItems(long posId, long productId, int qty, int prodDiscType, int prodDiscValue, double totalPrice, String isDiscounted) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String strSQL = "update posLineItems set isUpdated='Y', qty='" + qty
                    + "', isLineDiscounted='" + isDiscounted + "', totalPrice='" + totalPrice + "', productDiscType='" + prodDiscType + "', productDiscValue='" + prodDiscValue + "' where posId='"
                    + posId + "' and productId = '" + productId + "' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * If generated kot from counter sales
     *
     * @param posId
     * @param productId
     * @param isKOT
     */
    public void updatePOSLineItem(long posId, long productId, String isKOT) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String strSQL = "update posLineItems set isUpdated='Y', isKOTGenerated='" + isKOT
                    + "' where posId='" + posId + "' and productId = '" + productId + "' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * Update the line item based on productId and posId
     *
     * @param posId
     * @param productId
     * @param qty
     * @param totalPrice
     */
    public void updatePOSLineItem(long posId, long productId, int qty, int discount, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String strSQL = "update posLineItems set isUpdated='Y', qty='" + qty
                    + "', totalPrice='" + totalPrice + "', discount='" + discount + "' where posId='"
                    + posId + "' and productId = '" + productId + "' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void releasePOSOrder(long posId) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String strOrderSQL = "update posOrderHeader set isPosted='Y' where posId='" + posId + "' ;";
            db.execSQL(strOrderSQL);

            String strLineSQL = "update posLineItems set isPosted='Y' where posId='" + posId + "' ;";
            db.execSQL(strLineSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param kotHeaderList
     */
    public void releaseKOTOrder(List<KOTHeader> kotHeaderList) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            for (int i = 0; i < kotHeaderList.size(); i++) {
                db.execSQL("update kotTables set isOrderAvailable='N' where kotTableId = '" + kotHeaderList.get(i).getTablesId() + "'");
                db.execSQL("delete from kotHeader where kotNumber = '" + kotHeaderList.get(i).getKotNumber() + "'");
                db.execSQL("delete from kotLineItems where kotNumber = '" + kotHeaderList.get(i).getKotNumber() + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void prePrintOrder(long posId, String status) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String strOrderSQL = "update posOrderHeader set isPrinted='" + status + "' where posId='" + posId + "' ;";
            db.execSQL(strOrderSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void updatePOSTotalAmount(long posId, int totalDiscType, int totalDiscValue) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String strSQL = "update posOrderHeader set totalDiscType='" + totalDiscType + "', totalDiscValue='" + totalDiscValue + "' where posId='" + posId + "' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param posId
     * @return
     */
    public Map<String, Object> sumOfProductPriceWithoutDiscount(long posId) {

        SQLiteDatabase db = this.getWritableDatabase();
        Map<String, Object> dict = null;

        try {
            Cursor cursor = db.rawQuery("select sum(qty), sum(totalPrice) from posLineItems where posId = '" + posId + "' and isLineDiscounted = 'N' ", null);

            dict = new HashMap<String, Object>();
            while (cursor.moveToNext()) {
                dict.put("QTY", cursor.getInt(0));
                dict.put("TOTAL", cursor.getDouble(1));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return dict;
    }

    public double sumOfProducts(long posId) {
        double total = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select sum(totalPrice) from posLineItems where posId = '" + posId + "' ", null);

            while (cursor.moveToNext()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return total;
    }

    public Map<String, Object> sumOfProductPriceWithDiscount(long posId) {

        SQLiteDatabase db = this.getWritableDatabase();
        Map<String, Object> dict = null;

        try {
            Cursor cursor = db.rawQuery("select sum(qty), sum(totalPrice) from posLineItems where posId = '" + posId + "' and isLineDiscounted = 'Y' ", null);

            dict = new HashMap<String, Object>();
            while (cursor.moveToNext()) {
                dict.put("QTY", cursor.getInt(0));
                dict.put("TOTAL", cursor.getDouble(1));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return dict;
    }

    public Customer getPOSCustomer(long posId) {

        SQLiteDatabase db = this.getWritableDatabase();
        Customer customer = null;

        try {
            Cursor cursor = db.rawQuery("select * from posOrderHeader where posId = '" + posId + "' ", null);

            while (cursor.moveToNext()) {
                customer = new Customer();
                customer.setPosId(cursor.getLong(1));
                customer.setBpId(cursor.getLong(2));
                customer.setCustomerName(cursor.getString(3));
                customer.setPriceListId(cursor.getLong(4));
                customer.setCustomerValue(cursor.getLong(5));
                customer.setEmail(cursor.getString(6));
                customer.setMobile(cursor.getLong(7));
                customer.setIsCashCustomer(cursor.getInt(8));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return customer;
    }

    /**
     * Delete all tables
     */
    public void deleteAllTables() {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from organization");
            db.execSQL("delete from warehouse");
            db.execSQL("delete from role");
            db.execSQL("delete from roleAccess");

            db.execSQL("delete from businessPartner");
            db.execSQL("delete from kotTables");
            db.execSQL("delete from kotTerminals");
            db.execSQL("delete from category");
            db.execSQL("delete from product");
            db.execSQL("delete from posOrderHeader");
            db.execSQL("delete from posLineItems");
            db.execSQL("delete from posPaymentDetail");

            db.execSQL("delete from kotHeader");
            db.execSQL("delete from kotLineItems");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * DELETE ALL ORGANIZATION RELATED TABLES
     * Its used for reLogin with same user
     */
    public void deleteOrgRelatedTables() {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from organization");
            db.execSQL("delete from warehouse");
            db.execSQL("delete from role");
            db.execSQL("delete from roleAccess");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param posId
     */
    public void deletePOSTables(long posId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from posOrderHeader where posId = '" + posId + "'");
            db.execSQL("delete from posLineItems where posId = '" + posId + "'");
            db.execSQL("delete from posPaymentDetail where posId = '" + posId + "'");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * Delete all POS related tables
     * It's mainly used for manual sync
     */
    public void deletePOSRelatedTables() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from kotTables");
            db.execSQL("delete from kotTerminals");
            db.execSQL("delete from category");
            db.execSQL("delete from product");
            db.execSQL("delete from posOrderHeader");
            db.execSQL("delete from posLineItems");
            db.execSQL("delete from posPaymentDetail");
            db.execSQL("delete from kotHeader");
            db.execSQL("delete from kotLineItems");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void deleteKOTTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from kotHeader");
            db.execSQL("delete from kotLineItems");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void deleteKOTTableData(long kotNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from kotHeader where kotNumber = '" + kotNumber + "'");
            db.execSQL("delete from kotLineItems where kotNumber = '" + kotNumber + "'");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void deleteBPartner() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("delete from businessPartner");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void deletePOSLineItems(List<POSLineItem> lineItems) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            for (int i = 0; i < lineItems.size(); i++) {
                db.execSQL("delete from posLineItems where posId = '" + lineItems.get(i).getPosId() + "' and productId = '" + lineItems.get(i).getProductId() + "' ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void addKOTLineItems(KOTLineItems kotLineItems, Product product, int qty) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            double totalPrice = 0;
            Cursor mCount = db.rawQuery("select kotLineId from kotLineItems where kotNumber='" + kotLineItems.getKotNumber() + "' and kotLineId = '" + kotLineItems.getKotLineId() + "' ", null);
            //mCount.moveToFirst();
            long kotLineId = -1;

            while (mCount.moveToNext()) {
                kotLineId = mCount.getLong(0);
            }
            mCount.close();

            if (kotLineId == -1) {
                ContentValues values = new ContentValues();

                values.put(KEY_KOT_TABLE_ID, kotLineItems.getTableId());
                values.put(KEY_KOT_LINE_ID, kotLineItems.getKotLineId());
                values.put(KEY_KOT_NUMBER, kotLineItems.getKotNumber());
                values.put(KEY_INVOICE_NUMBER, kotLineItems.getInvoiceNumber());
                values.put(KEY_CATEGORY_ID, product.getCategoryId());
                values.put(KEY_PRODUCT_ID, product.getProdId());
                values.put(KEY_PRODUCT_NAME, product.getProdName());
                values.put(KEY_PRODUCT_ARABIC_NAME, product.getProdArabicName());
                values.put(KEY_PRODUCT_VALE, product.getProdValue());
                values.put(KEY_PRODUCT_UOM_ID, product.getUomId());
                values.put(KEY_PRODUCT_UOM_VALUE, product.getUomValue());
                values.put(KEY_PRODUCT_STD_PRICE, product.getSalePrice());
                values.put(KEY_PRODUCT_COST_PRICE, product.getCostPrice());
                values.put(KEY_KOT_TERMINAL_ID, product.getTerminalId());

                values.put(KEY_PRODUCT_QTY, qty);
                totalPrice = qty * product.getSalePrice();
                values.put(KEY_PRODUCT_TOTAL_PRICE, totalPrice);
                values.put(KEY_KOT_ITEM_NOTES, kotLineItems.getNotes());
                values.put(KEY_IS_PRINTED, "N");
                values.put(KEY_IS_POSTED, "N");
                values.put(KEY_KOT_REF_LINE_ID, kotLineItems.getRefRowId());
                values.put(KEY_KOT_EXTRA_PRODUCT, kotLineItems.getIsExtraProduct());
                values.put(KEY_CREATE_TIME, kotLineItems.getCreateTime());
                values.put(KEY_PRODUCT_PREPARATION_TIME, product.getPreparationTime());
                values.put(KEY_IS_DELETED, kotLineItems.getIsDeleted());

                // Inserting Row
                db.insert(TABLE_KOT_LINES, null, values);
            } else {

                Cursor mCheck = db.rawQuery("select kotLineId from kotLineItems where kotNumber='" + kotLineItems.getKotNumber() + "' and kotLineId = '" + kotLineItems.getKotLineId() + "' and isDeleted='N' ", null);
                long kotItemId = -1;

                while (mCheck.moveToNext()) {
                    kotItemId = mCheck.getLong(0);
                }
                mCheck.close();

                if (kotItemId != -1 && kotLineItems.getIsDeleted().equalsIgnoreCase("Y")) {
                    //send the notification to activity
                    ITokenDeletedListener.getInstance().tokenStatus();
                }

                String strSQL = "update kotLineItems set isDeleted='" + kotLineItems.getIsDeleted() + "' where kotLineId='" + kotLineId + "' and kotNumber = '" + kotLineItems.getKotNumber() + "'  ;";
                db.execSQL(strSQL);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void updateRelatedKOTLineIdToPOSLineItem(long posId, long kotLineId, long prodId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String strSQL = "update posLineItems set kotLineId='" + kotLineId + "' where posId = '" + posId + "' and productId = '" + prodId + "' and kotLineId=0 and isExtraProduct='Y' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void updateKOTLineIdToPOSLineItem(long posId, long kotLineId, long prodId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String strSQL = "update posLineItems set kotLineId='" + kotLineId + "' where posId = '" + posId + "' and productId = '" + prodId + "' and kotLineId=0 and isExtraProduct='N' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public List<Long> getKOTInvoiceNumbers(long tableId) {
        List<Long> invoiceNumberList = new ArrayList<Long>();
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            Cursor cursor = db.rawQuery("select invoiceNumber from kotLineItems where kotTableId = '" + tableId + "' and invoiceNumber <> 0 GROUP BY invoiceNumber ", null);

            while (cursor.moveToNext()) {
                invoiceNumberList.add(cursor.getLong(0));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return invoiceNumberList;
    }

    public List<KOTHeader> getKOTTables(boolean printed) {

        List<KOTHeader> kotHeaderList = new ArrayList<KOTHeader>();
        KOTHeader kotHeader = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            if (printed)
                cursor = db.rawQuery("select kotTableId, invoiceNumber, sum(kotTotalAmount), isSelected from kotHeader where isPrinted='Y' and kotTableId <> 0 GROUP BY kotTableId ", null);
            else
                cursor = db.rawQuery("select kotTableId, invoiceNumber, sum(kotTotalAmount), isSelected from kotHeader where isPrinted='N' and kotTableId <> 0 GROUP BY kotTableId ", null);

            //if(!posted)
            //cursor = db.rawQuery("select kotTableId, sum(kotTotalAmount) from kotHeader where isPosted='N' GROUP BY kotTableId ", null);

            while (cursor.moveToNext()) {
                kotHeader = new KOTHeader();
                kotHeader.setTablesId(cursor.getLong(0));
                kotHeader.setInvoiceNumber(cursor.getLong(1));
                kotHeader.setTotalAmount(cursor.getDouble(2));
                kotHeader.setSelected(cursor.getString(3));
                kotHeaderList.add(kotHeader);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return kotHeaderList;

    }

    public List<KOTHeader> getDataAvailableKOTTables() {

        List<KOTHeader> kotHeaderList = new ArrayList<KOTHeader>();
        KOTHeader kotHeader = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            cursor = db.rawQuery("select kotTableId, sum(kotTotalAmount), isSelected from kotHeader GROUP BY kotTableId ", null);

            while (cursor.moveToNext()) {
                kotHeader = new KOTHeader();
                kotHeader.setTablesId(cursor.getLong(0));
                kotHeader.setTotalAmount(cursor.getDouble(1));
                kotHeader.setSelected(cursor.getString(2));
                kotHeaderList.add(kotHeader);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return kotHeaderList;

    }

    public List<KOTHeader> getKOTHeaders(long tableId, boolean printed) {
        List<KOTHeader> kotHeaderList = new ArrayList<KOTHeader>();
        KOTHeader kotHeader = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            if (tableId != 0) {
                cursor = db.rawQuery("select * from kotHeader where kotTableId='" + tableId + "' ", null);
            } else {
                if (printed)
                    cursor = db.rawQuery("select * from kotHeader where isPrinted='Y' ", null);
                else
                    cursor = db.rawQuery("select * from kotHeader where isPrinted='N' ", null);
            }

            while (cursor.moveToNext()) {
                kotHeader = new KOTHeader();

                kotHeader.setTablesId(cursor.getLong(1));
                kotHeader.setKotNumber(cursor.getLong(2));
                kotHeader.setInvoiceNumber(cursor.getLong(3));
                kotHeader.setTerminalId(cursor.getLong(4));
                kotHeader.setTotalAmount(cursor.getDouble(5));
                kotHeader.setOrderBy(cursor.getString(6));
                kotHeader.setKotType(cursor.getString(7));
                kotHeader.setOrderType(cursor.getString(8));
                kotHeader.setIsKOT(cursor.getString(9));
                kotHeader.setPrinted(cursor.getString(10));
                kotHeader.setPosted(cursor.getString(11));
                kotHeader.setSelected(cursor.getString(12));

                kotHeaderList.add(kotHeader);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return kotHeaderList;
    }

    public List<KOTHeader> getKOTHeadersNotPrinted() {
        List<KOTHeader> kotHeaderList = new ArrayList<KOTHeader>();
        KOTHeader kotHeader = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            cursor = db.rawQuery("select * from kotHeader where isPrinted='N' ", null);


            while (cursor.moveToNext()) {
                kotHeader = new KOTHeader();

                kotHeader.setTablesId(cursor.getLong(1));
                kotHeader.setKotNumber(cursor.getLong(2));
                kotHeader.setInvoiceNumber(cursor.getLong(3));
                kotHeader.setTerminalId(cursor.getLong(4));
                kotHeader.setTotalAmount(cursor.getDouble(5));
                kotHeader.setOrderBy(cursor.getString(6));
                kotHeader.setKotType(cursor.getString(7));
                kotHeader.setOrderType(cursor.getString(8));
                kotHeader.setIsKOT(cursor.getString(9));
                kotHeader.setPrinted(cursor.getString(10));
                kotHeader.setPosted(cursor.getString(11));
                kotHeader.setSelected(cursor.getString(12));

                kotHeaderList.add(kotHeader);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return kotHeaderList;
    }

    public List<KOTHeader> getKOTHeaders(long terminalId) {
        List<KOTHeader> kotHeaderList = new ArrayList<KOTHeader>();
        KOTHeader kotHeader = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            cursor = db.rawQuery("select * from kotHeader where kotTerminalId='" + terminalId + "' ", null);


            while (cursor.moveToNext()) {
                kotHeader = new KOTHeader();

                kotHeader.setTablesId(cursor.getLong(1));
                kotHeader.setKotNumber(cursor.getLong(2));
                kotHeader.setInvoiceNumber(cursor.getLong(3));
                kotHeader.setTerminalId(cursor.getLong(4));
                kotHeader.setTotalAmount(cursor.getDouble(5));
                kotHeader.setOrderBy(cursor.getString(6));
                kotHeader.setKotType(cursor.getString(7));
                kotHeader.setOrderType(cursor.getString(8));
                kotHeader.setIsKOT(cursor.getString(9));
                kotHeader.setPrinted(cursor.getString(10));
                kotHeader.setPosted(cursor.getString(11));
                kotHeader.setSelected(cursor.getString(12));
                kotHeader.setCreateTime(cursor.getLong(13));
                kotHeader.setCoversDetails(cursor.getString(14));

                kotHeaderList.add(kotHeader);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return kotHeaderList;
    }

    public List<KOTHeader> getAllCompletedKOTHeaders() {
        List<KOTHeader> kotHeaderList = new ArrayList<KOTHeader>();
        KOTHeader kotHeader = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            cursor = db.rawQuery("select * from kotHeader", null);


            while (cursor.moveToNext()) {
                kotHeader = new KOTHeader();

                kotHeader.setTablesId(cursor.getLong(1));
                kotHeader.setKotNumber(cursor.getLong(2));
                kotHeader.setInvoiceNumber(cursor.getLong(3));
                kotHeader.setTerminalId(cursor.getLong(4));
                kotHeader.setTotalAmount(cursor.getDouble(5));
                kotHeader.setOrderBy(cursor.getString(6));
                kotHeader.setKotType(cursor.getString(7));
                kotHeader.setOrderType(cursor.getString(8));
                kotHeader.setIsKOT(cursor.getString(9));
                kotHeader.setPrinted(cursor.getString(10));
                kotHeader.setPosted(cursor.getString(11));
                kotHeader.setSelected(cursor.getString(12));
                kotHeader.setCreateTime(cursor.getLong(13));

                kotHeaderList.add(kotHeader);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return kotHeaderList;
    }

    public List<String> getSelectedCoverNames(String CoverIDs) {
        List<String> tableList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            for (String selectedCover : CoverIDs.split(",")) {
                String qry = "select kotTableId, kotTableName, isOrderAvailable from kotTables where " +
                        " isCoversLevel = 'Y' and kotTableId = '" + Long.parseLong(selectedCover) + "' ORDER BY kotTableId ASC";
                Cursor cursor = db.rawQuery(qry, null);
                while (cursor.moveToNext()) {
                    tableList.add(cursor.getString(1));
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return tableList;
    }

    public long getKOTInvoiceNumber(long tableId) {
        long invoiceNumber = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select invoiceNumber from kotHeader where kotTableId = '" + tableId + "' GROUP BY kotTableId ", null);

            while (cursor.moveToNext()) {
                invoiceNumber = cursor.getLong(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return invoiceNumber;
    }

    public List<KOTHeader> getKOTNumbers(long posId) {

        List<KOTHeader> kotHeaderList = new ArrayList<KOTHeader>();
        KOTHeader kotHeader = null;

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery("select * from kotHeader where invoiceNumber = '" + posId + "'", null);

            while (cursor.moveToNext()) {
                kotHeader = new KOTHeader();

                kotHeader.setTablesId(cursor.getLong(1));
                kotHeader.setKotNumber(cursor.getLong(2));
                kotHeader.setInvoiceNumber(cursor.getLong(3));
                kotHeader.setTerminalId(cursor.getLong(4));
                kotHeader.setTotalAmount(cursor.getDouble(5));
                kotHeader.setOrderBy(cursor.getString(6));
                kotHeader.setKotType(cursor.getString(7));
                kotHeader.setOrderType(cursor.getString(8));
                kotHeader.setIsKOT(cursor.getString(9));
                kotHeader.setPrinted(cursor.getString(10));
                kotHeader.setPosted(cursor.getString(11));
                kotHeader.setSelected(cursor.getString(12));

                kotHeaderList.add(kotHeader);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return kotHeaderList;
    }

    public boolean isKOTAvailable(long posId) {
        boolean isKOTAvail = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select kotTableId from kotHeader where invoiceNumber = '" + posId + "' and kotTableId <> 0 GROUP BY kotTableId ", null);

            while (cursor.moveToNext()) {
                isKOTAvail = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return isKOTAvail;
    }

    /**
     * @param tableId
     * @return
     */
    public List<KOTLineItems> getKOTLineItems(long tableId) {

        List<KOTLineItems> kotLineItemList = new ArrayList<KOTLineItems>();

        KOTLineItems kotLineItems = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from kotLineItems where kotTableId='" + tableId + "'", null);

            while (cursor.moveToNext()) {
                kotLineItems = new KOTLineItems();
                Product product = new Product();

                kotLineItems.setTableId(cursor.getLong(1));
                kotLineItems.setKotNumber(cursor.getLong(2));

                product.setCategoryId(cursor.getLong(3));
                product.setProdId(cursor.getLong(4));
                product.setProdName(cursor.getString(5));
                product.setProdArabicName(cursor.getString(6));
                product.setProdValue(cursor.getString(7));
                product.setUomId(cursor.getLong(8));
                product.setUomValue(cursor.getString(9));
                product.setSalePrice(cursor.getDouble(10));
                product.setCostPrice(cursor.getDouble(11));
                product.setTerminalId(cursor.getLong(12));

                kotLineItems.setProduct(product);
                kotLineItems.setQty(cursor.getInt(13));
                kotLineItems.setTotalPrice(cursor.getDouble(14));
                kotLineItems.setNotes(cursor.getString(15));
                kotLineItems.setPrinted(cursor.getString(16));
                kotLineItems.setStatus(cursor.getString(17));
                kotLineItemList.add(kotLineItems);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return kotLineItemList;
    }

    /**
     * @param kotNumber
     * @return
     */
    public List<KOTLineItems> getKOTLineItem(long kotNumber) {

        List<KOTLineItems> kotLineItemList = new ArrayList<KOTLineItems>();

        KOTLineItems kotLineItems = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from kotLineItems where kotNumber ='" + kotNumber + "' and isExtraProduct='N' and isDeleted='N' ", null);

            while (cursor.moveToNext()) {
                kotLineItems = new KOTLineItems();
                Product product = new Product();

                kotLineItems.setTableId(cursor.getLong(1));
                kotLineItems.setKotLineId(cursor.getLong(2));
                kotLineItems.setKotNumber(cursor.getLong(3));
                kotLineItems.setInvoiceNumber(cursor.getLong(4));

                product.setCategoryId(cursor.getLong(5));
                product.setProdId(cursor.getLong(6));
                product.setProdName(cursor.getString(7));
                product.setProdArabicName(cursor.getString(8));
                product.setProdValue(cursor.getString(9));
                product.setUomId(cursor.getLong(10));
                product.setUomValue(cursor.getString(11));
                product.setSalePrice(cursor.getDouble(12));
                product.setCostPrice(cursor.getDouble(13));
                product.setTerminalId(cursor.getLong(14));

                kotLineItems.setQty(cursor.getInt(15));
                kotLineItems.setTotalPrice(cursor.getDouble(16));
                kotLineItems.setNotes(cursor.getString(17));
                kotLineItems.setPrinted(cursor.getString(18));
                kotLineItems.setStatus(cursor.getString(19));
                kotLineItems.setRefRowId(cursor.getLong(20));
                kotLineItems.setIsExtraProduct(cursor.getString(21));
                kotLineItems.setCreateTime(cursor.getLong(22));

                product.setPreparationTime(cursor.getString(23));
                kotLineItems.setIsDeleted(cursor.getString(24));
                kotLineItems.setProduct(product);

                kotLineItemList.add(kotLineItems);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return kotLineItemList;
    }

    public long getKOTLineItemPreTime(long kotNumber) {

        long preTime = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor mCursor = db.rawQuery("select MAX(createTime) from kotLineItems where kotNumber='" + kotNumber + "'", null);

            while (mCursor.moveToNext()) {
                preTime = mCursor.getLong(0);
            }
            mCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return preTime;
    }

    /**
     * @param kotNumber
     */
    public void updateKOTStatusPrinted(long kotNumber, long invoiceNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL;
        try {
            strSQL = "update kotHeader set isPrinted='Y' where kotNumber = '" + kotNumber + "' ;";
            db.execSQL(strSQL);

            strSQL = "update kotLineItems set isPrinted='Y' where kotNumber = '" + kotNumber + "' ;";
            db.execSQL(strSQL);

            strSQL = "update posLineItems set isKOTGenerated='Y' where posId='" + invoiceNumber + "';";
            db.execSQL(strSQL);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void updateKOTSelectedStatus(long tableId, String isSelect) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL;
        try {
            strSQL = "update kotHeader set isSelected='" + isSelect + "' where kotTableId = '" + tableId + "' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * @param tableId
     * @param invoiceNumber
     */
    public void updateInvoiceToKOT(long tableId, long invoiceNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL;
        try {
            strSQL = "update kotHeader set invoiceNumber='" + invoiceNumber + "' where kotTableId = '" + tableId + "' ;";
            db.execSQL(strSQL);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public List<String> getCounterSaleKOTItemTerminals(long posId) {

        List<String> kotTerminalList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select terminalId from posLineItems where posId = '" + posId + "' and isKOTGenerated='N' GROUP BY terminalId ", null);
            while (cursor.moveToNext()) {
                kotTerminalList.add(String.valueOf(cursor.getLong(0)));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return kotTerminalList;
    }


    /**
     * @param posId
     * @param terminalId
     * @return
     */
    public double sumOfCounterSalesTerminalItemsTotal(long posId, long terminalId) {
        double totalPrice = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select sum(totalPrice) from posLineItems where posId = '" + posId + "' and terminalId = '" + terminalId + "' ", null);

            while (cursor.moveToNext()) {
                totalPrice = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return totalPrice;
    }

    /**
     * @param posId
     * @param terminalId
     * @return
     */
    public List<Product> getCounterSaleKOTLineItems(long posId, long terminalId) {

        List<Product> itemList = new ArrayList<Product>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from posLineItems where posId='" + posId + "' and isKOTGenerated='N' and terminalId = '" + terminalId + "'", null);

            while (cursor.moveToNext()) {

                Product product = new Product();

                product.setCategoryId(cursor.getLong(3));
                product.setProdId(cursor.getLong(4));
                product.setProdName(cursor.getString(5));
                product.setProdArabicName(cursor.getString(6));
                product.setProdValue(cursor.getString(7));
                product.setUomId(cursor.getLong(8));
                product.setUomValue(cursor.getString(9));
                product.setQty(cursor.getInt(10));
                product.setSalePrice(cursor.getDouble(11));
                product.setCostPrice(cursor.getDouble(12));
                product.setTotalPrice(cursor.getDouble(15));
                product.setDescription("");

                itemList.add(product);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return itemList;
    }

    public void updateCounterSaleKOTLineItems(long posId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String strSQL = "update posLineItems set isKOTGenerated='Y' where posId = '" + posId + "' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }

    public void updateOrderAvailableTable(long tableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String strSQL = "update kotTables set isOrderAvailable='Y' where kotTableId = '" + tableId + "' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
    }
}
