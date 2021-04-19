package com.ultramega.shop.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ultramega.shop.util.UserPreferenceManager;

public class UltramegaShopDatabaseHelper extends SQLiteOpenHelper {

    //new is 17
    private static final int DATABASE_VERSION = 18;
    private static final String DATABASE_NAME = "ultramegashop";
    private Context context;
    public UltramegaShopDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //DROP ALL THE TABLES IF EXIST
        dropAllTables(db);
        //CREATE ALL TABLES IF DOESNT EXIST
        createAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_MY_LIST);
            db.execSQL(UltramegaShopUtilities.CREATE_TABLE_MY_LIST);
        }
    }

    private void truncateAll(SQLiteDatabase db){

        UserPreferenceManager.clearPreference(context);
        //set notifications
        UserPreferenceManager.saveIntegerPreference(context, UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT, 0);

        //set up login to false
        UserPreferenceManager.saveBooleanPreference(context, UserPreferenceManager.KEY_IS_LOGGED_IN, false);

        db.execSQL(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
        db.execSQL(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
        db.execSQL(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
        db.execSQL(UltramegaShopUtilities.TABLE_MY_LIST);
        db.execSQL(UltramegaShopUtilities.TABLE_NOTIFICATION);
        db.execSQL(UltramegaShopUtilities.TABLE_CONSUMER_PROFILE);
        db.execSQL(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
        db.execSQL(UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
        db.execSQL(UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);
        db.execSQL(UltramegaShopUtilities.TABLE_WHOLESALER_PROFILE);
        db.execSQL(UltramegaShopUtilities.TABLE_BRANCHES);
    }


    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_REGULAR);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_ITEM_SKUS);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_CONSUMER_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_CONSUMER_BEHAVIOUR_PATTERN);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_WHOLESALER_BEHAVIOUR_PATTERN);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_ADMIN_BANK_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_BRANCHES);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_MY_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_WHOLESALER_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_SUPPLIER);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_SUPPLIER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_PROMOS);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_PROMOS);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_RECENT_SEARCHES);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_ORDER_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_PROMOPTS);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_DELIVERY_CHARGE);
        db.execSQL("DROP TABLE IF EXISTS " + UltramegaShopUtilities.TABLE_ULTRAMEGA_SERVICE_CONFIG);
    }

    private void createAllTables(SQLiteDatabase db) {
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_CATEGORIES);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_CATEGORY_ITEMS_REGULAR);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_CATEGORY_ITEMS_DAILYFINDS);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_ITEM_SKUS);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_SHOPPING_CARTS_QUEUE);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_CONSUMER_PROFILE);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_CONSUMER_WALLET);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_CONSUMER_BEHAVIOUR_PATTERN);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_WHOLESALER_BEHAVIOUR_PATTERN);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_CONSUMER_ADDRESS);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_ADMIN_BANK_DETAILS);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_ORDERS_QUEUE);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_ORDERS_HISTORY);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_BRANCHES);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_MY_LIST);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_WHOLESALER_PROFILE);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_SUPPLIER);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_SUPPLIER_ITEMS);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_WALLET_RELOAD_QUEUE);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_PROMOS);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_CATEGORY_ITEMS_PROMOS);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_RECENT_SEARCHES);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_ORDER_PAYMENTS);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_PROMOPTS);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_NOTIFICATION);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_DELIVERY_CHARGE);
        db.execSQL(UltramegaShopUtilities.CREATE_TABLE_ULTRAMEGA_SERVICE_CONFIG);
    }
}
