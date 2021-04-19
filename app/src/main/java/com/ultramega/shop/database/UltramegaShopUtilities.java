package com.ultramega.shop.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.AccountNumberReference;
import com.ultramega.shop.pojo.BankNameReference;
import com.ultramega.shop.pojo.BankReference;
import com.ultramega.shop.pojo.Branches;
import com.ultramega.shop.pojo.Category;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.pojo.ConsumerAddress;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;
import com.ultramega.shop.pojo.ConsumerCharge;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ConsumerWallet;
import com.ultramega.shop.pojo.ItemSKU;
import com.ultramega.shop.pojo.MyList;
import com.ultramega.shop.pojo.Notification;
import com.ultramega.shop.pojo.OrderPayments;
import com.ultramega.shop.pojo.OrdersQueue;
import com.ultramega.shop.pojo.PromoPts;
import com.ultramega.shop.pojo.Promos;
import com.ultramega.shop.pojo.ShoppingCartSummary;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.pojo.ShoppingModeConfig;
import com.ultramega.shop.pojo.Supplier;
import com.ultramega.shop.pojo.SupplierItems;
import com.ultramega.shop.pojo.WalletReload;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.rest.RetrofitBuild;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class UltramegaShopUtilities {

    //DB Col separator, must have space before and after the value
    private static final String COMMA = " , ";
    private static final String CT_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    private static final String GENERIC_ID = " ( _id ";
    private static final String GENERIC_STATEMENT_ENDER = " )";

    //Database Constraint, must have space before and after the value
    private static final String CONSTRAINT_PRIMARY_KEY = " PRIMARY KEY ";
    private static final String CONSTRAINT_AUTOINCREMENT = " AUTOINCREMENT ";

    //Database DATA-TYPE, must have space before and after the value
    private static final String DATA_TYPE_TEXT = " TEXT ";
    private static final String DATA_TYPE_INTEGER = " INTEGER ";

    //TABLE NAMES
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_PROMOS = "promos";
    public static final String TABLE_CATEGORIES_ITEMS_DAILYFINDS = "category_items_dailyfinds";
    public static final String TABLE_CATEGORIES_ITEMS_REGULAR = "category_items_regular";
    public static final String TABLE_CATEGORIES_ITEMS_PROMOS = "category_items_promos";
    public static final String TABLE_ITEM_SKUS = "item_skus";
    public static final String TABLE_SHOPPING_CARTS_QUEUE = "shopping_carts_queue";
    public static final String TABLE_CONSUMER_PROFILE = "consumer_profile";
    public static final String TABLE_CONSUMER_WALLET = "consumer_wallet";
    public static final String TABLE_CONSUMER_BEHAVIOUR_PATTERN = "consumer_behaviour_pattern";
    public static final String TABLE_WHOLESALER_BEHAVIOUR_PATTERN = "wholesaler_behaviour_pattern";
    public static final String TABLE_CONSUMER_ADDRESS = "consumer_address";
    public static final String TABLE_ADMIN_BANK_DETAILS = "admin_bank_details";
    public static final String TABLE_ORDERS_QUEUE = "orders_queue";
    public static final String TABLE_ORDERS_HISTORY = "orders_history";
    public static final String TABLE_BRANCHES = "branches";
    public static final String TABLE_MY_LIST = "customer_mylist";
    public static final String TABLE_WHOLESALER_PROFILE = "wholesaler_profile";
    public static final String TABLE_SUPPLIER = "suppliers";
    public static final String TABLE_SUPPLIER_ITEMS = "supplier_items";
    public static final String TABLE_WALLET_RELOAD_QUEUE = "wallet_reload_queue";
    public static final String TABLE_RECENT_SEARCHES = "recent_searches";
    public static final String TABLE_ORDER_PAYMENTS = "order_payments";
    public static final String TABLE_PROMOPTS = "promopts";
    public static final String TABLE_NOTIFICATION = "notification";
    public static final String TABLE_DELIVERY_CHARGE = "table_delivery_charge";

    //SERVICE CONFIG
    public static final String TABLE_ULTRAMEGA_SERVICE_CONFIG = "table_ultramega_service_config";

    //categories
    private static final String KEY_CATEGORY = "Category";
    private static final String KEY_ID = "ID";
    private static final String KEY_CATEGORY_ID = "CategoryID";
    private static final String KEY_CATEGORY_TYPE = "CategoryType";
    private static final String KEY_NAME = "Name";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_DATE_TIME_ADDED = "DateTimeAdded";
    private static final String KEY_DATE_TIME_UPDATED = "DateTimeUpdated";
    private static final String KEY_STATUS = "Status";
    private static final String KEY_EXTRA_1 = "Extra1";
    private static final String KEY_EXTRA_2 = "Extra2";
    private static final String KEY_EXTRA_3 = "Extra3";
    private static final String KEY_EXTRA_4 = "Extra4";
    private static final String KEY_NOTES_1 = "Notes1";
    private static final String KEY_NOTES_2 = "Notes2";

    //promos
    private static final String KEY_PROMO_ID = "PromoID";

    //category_items_regular
    private static final String KEY_CAT_CLASS = "CatClass";
    private static final String KEY_ITEM_ID = "ItemID";
    private static final String KEY_ITEM_TYPE = "ItemType";
    private static final String KEY_ITEM_NAME = "ItemName";
    private static final String KEY_ITEM_DESCRIPTION = "ItemDescription";
    private static final String KEY_BRAND_NAME = "BrandName";
    private static final String KEY_ITEM_GROUP_PIC_URL = "ItemGroupPicUrl";

    //item_skus
    private static final String KEY_ITEM_CODE = "ItemCode";
    private static final String KEY_PACKAGE_CODE = "PackageCode";
    private static final String KEY_PRICE = "Price";
    private static final String KEY_GROSS_PRICE = "GrossPrice";
    private static final String KEY_IS_PROMO = "isPromo";
    private static final String KEY_PROMO_DETAILS = "PromoDetails";
    private static final String KEY_MINIMUM_ORDER_QTY = "MinimumOrderQTY";
    private static final String KEY_INCREMENTAL_ORDER_QTY = "IncrementalOrderQTY";
    private static final String KEY_BARCODE = "Barcode";
    private static final String KEY_PACK_SIZE = "PackSize";

    //
    private static final String KEY_ITEM_PIC_URL = "ItemPicUrl";
    private static final String KEY_XML_DETAILS = "XMLDetails";
    private static final String KEY_PACKAGE_DESCRIPTION = "PackageDescription";

    //shopping_carts_queue
    private static final String KEY_CUSTOMER_TYPE = "CustomerType";
    private static final String KEY_CUSTOMER_ID = "CustomerID";
    private static final String KEY_IMEI = "IMEI";
    private static final String KEY_QUANTITY = "Quantity";
    private static final String KEY_TOTAL_AMOUNT = "TotalAmount";
    private static final String KEY_IS_REGISTERED = "isRegistered";
    private static final String KEY_PRICING_GROUP = "PricingGroup";
    private static final String KEY_IS_BULK = "isBulk";

    //consumer_profile
    private static final String KEY_FACEBOOK_ID = "FacebookID";
    private static final String KEY_CONSUMER_ID = "ConsumerID";
    private static final String KEY_USER_ID = "UserID";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_CONSUMER_TYPE = "ConsumerType";
    private static final String KEY_FIRST_NAME = "FirstName";
    private static final String KEY_LAST_NAME = "LastName";
    private static final String KEY_MIDDLE_NAME = "MiddleName";
    private static final String KEY_BIRTH_DATE = "BirthDate";
    private static final String KEY_GENDER = "Gender";
    private static final String KEY_OCCUPATION = "Occupation";
    private static final String KEY_INTEREST = "Interest";
    private static final String KEY_EMAIL_ADDRESS = "EmailAddress";
    private static final String KEY_IS_VERIFIED = "isVerified";
    private static final String KEY_COUNTRY_CODE = "CountryCode";
    private static final String KEY_MOBILE_NO = "MobileNo";
    private static final String KEY_PROFILE_PIC_URL = "ProfilePicURL";
    private static final String KEY_DATE_TIME_REGISTERED = "DateTimeRegistered";
    private static final String KEY_LAST_LOGIN = "LastLogin";
    private static final String KEY_REWARD_POINTS = "RewardPoints";

    //consumer_wallet
    private static final String KEY_CURRENCY_ID = "CurrencyID";
    private static final String KEY_TOTAL_WALLET = "TotalWallet";
    private static final String KEY_WALLET_TYPE = "WalletType";
    private static final String KEY_LAST_WALLET_RELOAD_DATE = "LastWalletReloadDate";
    private static final String KEY_LAST_WALLET_RELOAD_AMOUNT = "LastWalletReloadAmount";
    private static final String KEY_LAST_WALLET_RELOAD_PRE_WALLET = "LastWalletReloadPreWallet";
    private static final String KEY_LAST_WALLET_RELOAD_POST_WALLET = "LastWalletReloadPostWallet";
    private static final String KEY_LAST_ORDER_PAYMENT_DATE = "LastOrderPaymentDate";
    private static final String KEY_LAST_ORDER_PAYMENT_AMOUNT = "LastOrderPaymentAmount";
    private static final String KEY_LAST_ORDER_PAYMENT_PRE_WALLET = "LastOrderPaymentPreWallet";
    private static final String KEY_LAST_ORDER_PAYMENT_POST_WALLET = "LastOrderPaymentPostWallet";
    private static final String KEY_PAYMENTTYPE = "PaymentType";
    //consumer_behaviour_pattern
    private static final String KEY_DATE_TIME_IN = "DateTimeIN";
    private static final String KEY_CATEGORY_NAME = "CategoryName";

    //consumer_address
    private static final String KEY_ADDRESS_ID = "AddressID";
    private static final String KEY_STREET_ADDRESS = "StreetAddress";
    private static final String KEY_CITY = "City";
    private static final String KEY_PROVINCE = "Province";
    private static final String KEY_ZIP = "Zip";
    private static final String KEY_COUNTRY = "Country";
    private static final String KEY_LONGITUDE = "Longitude";
    private static final String KEY_LATITUDE = "Latitude";
    private static final String KEY_IS_DEFAULT = "isDefault";

    //admin_bank_details
    private static final String KEY_NETWORK_ID = "NetworkID";
    private static final String KEY_BANK_CODE = "BankCode";
    private static final String KEY_BANK_NAME = "BankName";
    private static final String KEY_BANK_ACCOUNT_NO = "BankAccountNo";
    private static final String KEY_BANK_ACCOUNT_NAME = "BankAccountName";

    //orders_queue
    private static final String KEY_ORDER_TXN_ID = "OrderTxnID";
    private static final String KEY_ORDER_DATE_TIME = "OrderDateTime";
    private static final String KEY_COMPLETED_DATE_TIME = "CompletedDateTime";
    private static final String KEY_ORDER_TYPE = "OrderType";
    private static final String KEY_CUSTOMER_NAME = "CustomerName";
    private static final String KEY_CUSTOMER_MOBILE_NUMBER = "CustomerMobileNumber";
    private static final String KEY_CUSTOMER_EMAIL_ADDRESS = "CustomerEmailAddress";
    private static final String KEY_CUSTOMER_DELIVERY_ADDRESS = "CustomerDeliveryAddress";
    private static final String KEY_ACCOUNT_TYPE = "AccountType";
    private static final String KEY_ACCOUNT_ID = "AccountID";
    private static final String KEY_ACCOUNT_USER_ID = "AccountUserID";
    private static final String KEY_TOTAL_ITEMS = "TotalItems";
    private static final String KEY_TOTAL_ITEM_AMOUNT = "TotalItemAmount";
    private static final String KEY_DELIVERY_CHARGE = "DeliveryCharge";
    private static final String KEY_ORDER_MEDIUM = "OrderMedium";
    private static final String KEY_NO_OF_ROUTE = "NoOfRoute";
    private static final String KEY_ROUTE_DATE_TIME = "RouteDateTime";
    private static final String KEY_APPROVED_ORDER_DATE_TIME = "ApprovedOrderDateTime";
    private static final String KEY_PAYMENT_TXN_NO = "PaymentTxnNo";
    private static final String KEY_PAYMENT_DATE_TIME = "PaymentDateTime";
    private static final String KEY_CONFIRM_PAYMENT_BY = "ConfirmPaymentBy";
    private static final String KEY_CONFIRM_PAYMENT_DATE_TIME = "ConfirmPaymentDateTime";
    private static final String KEY_ON_DELIVERY_BY = "OnDeliveryBy";
    private static final String KEY_ON_DELIVERY_DATE_TIME = "OnDeliveryDateTime";
    private static final String KEY_IS_EXPIRED_ORDER = "isExpireOrder";
    private static final String KEY_EXPIRED_DATE_TIME = "ExpiredDateTime";
    private static final String KEY_IS_RE_OPEN_ORDER = "isReOpenOrder";
    private static final String KEY_RE_OPEN_DATE_TIME = "ReOpenDateTime";
    private static final String KEY_COMPLETED_BY = "CompletedBy";
    private static final String KEY_REMARKS = "Remarks";
    private static final String KEY_LAST_UPDATE_DATE_TIME = "LastUpdateDateTime";
    private static final String KEY_CUSTOMER_RATING = "CustomerRating";
    private static final String KEY_CUSTOMER_REMARKS = "CustomerRemarks";
    private static final String KEY_IS_TAGGED_AS_COMPLETED_BY_CUSTOMER = "isTaggedAsCompletedByCustomer";
    private static final String KEY_IS_ASSIGNED = "isAssigned";
    private static final String KEY_ASSIGNED_BY = "AssignedBy";
    //order_details_queue, some col name are reused from orders table
    private static final String KEY_ADDED_DATE_TIME = "AddedDateTime";
    // // //
    private static final String KEY_TOTAL_ORDER_AMOUNT = "TotalOrderAmount";
    private static final String KEY_TOTAL_PACKAGE_AMOUNT = "TotalPackageAmount";
    private static final String KEY_QUANTITY_SERVED = "QuantityServed";
    // // //
    //branches
    private static final String KEY_BRANCH_ID = "BranchID";
    private static final String KEY_BRANCH_NAME = "BranchName";
    private static final String KEY_BRANCH_TYPE = "BranchType";
    private static final String KEY_BRANCH_GROUP = "BranchGroup";
    private static final String KEY_AUTHORISED_REPRESENTATIVE = "AuthorisedRepresentative";
    private static final String KEY_AUTHORISED_EMAIL_ADDRESS = "AuthorisedEmailAddress";
    private static final String KEY_AUTHORISED_TEL_NO = "AuthorisedTelNo";
    private static final String KEY_AUTHORISED_COUNTRY_CODE = "AuthorisedCountryCode";
    private static final String KEY_AUTHORISED_MOBILE_NO = "AuthorisedMobileNo";
    private static final String KEY_FAX = "Fax";
    private static final String KEY_CURRENT_ORDER_COUNT = "CurrentOrderCount";
    private static final String KEY_CALCULATED_DISTANCE = "CalculatedDistance";
    private static final String KEY_STOREOPEN = "StoreHoursStart";
    private static final String KEY_STORECLOSE= "StoreHoursEnd";

    //wholesaler_profile
    private static final String KEY_WHOLESALER_ID = "WholesalerID";
    private static final String KEY_USER_PASSWORD = "UserPassword";
    private static final String KEY_IS_ONLINE = "isOnline";
    private static final String KEY_COMPANY = "Company";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_IS_CREDIT = "isCredit";
    private static final String KEY_CREDIT_LINE = "CreditLine";
    private static final String KEY_PRICE_GROUP = "PriceGroup";
    private static final String KEY_IS_PARTNER = "isPartner";
    private static final String KEY_PARTNER_ID = "PartnerID";
    private static final String KEY_DATE_TIME_PARTNERED = "DateTimePartnered";

    //suppliers
    private static final String KEY_SUPPLIER_ID = "SupplierID";
    private static final String KEY_SUPPLIER_NAME = "SupplierName";
    private static final String KEY_RANK = "Rank";
    private static final String KEY_IS_SPONSOR = "isSponsor";
    private static final String KEY_PHOTO_PATH = "PhotoPath";

    //wallet_reload
    private static final String KEY_PARTNER_PAYMENT_TXN_NO = "PartnerPaymentTxnNo";
    private static final String KEY_DATE_TIME_COMPLETED = "DateTimeCompleted";
    private static final String KEY_AMOUNT_PAID = "AmountPaid";
    private static final String KEY_PAYMENT_OPTION = "PaymentOption";
    private static final String KEY_PAYMENT_DETAILS = "PaymentDetails";
    private static final String KEY_PREACCOUNT_WALLET = "PreAccountWallet";
    private static final String KEY_POSTACCOUNT_WALLET = "PostAccountWallet";
    private static final String KEY_ACTED_BY = "ActedBy";
    private static final String KEY_DATE_TO_MONTH = "DateToMonth";
    private static final String KEY_DATE_TO_YEAR = "DateToYear";

    //recent_searches
    private static final String KEY_RECENT_SEARCH_KEY = "search_key";

    //promopts
    private static final String KEY_POINTS = "Points";
    private static final String KEY_DATE = "Date";

    //notification
    private static final String KEY_CHANNEL = "Channel";
    private static final String KEY_SUBJECT = "Subject";
    private static final String KEY_MESSAGE = "Message";
    private static final String KEY_SENDER = "Sender";
    private static final String KEY_SENDER_LOGO = "SenderLogo";
    private static final String KEY_PAYLOAD = "PayLoad";
    private static final String KEY_NOTIFICATION_TNX_NO = "TnxNo";

    //table_delivery_charge
    private static final String KEY_CHARGE_CLASS = "ChargeClass";
    private static final String KEY_VARIABLE_FEE = "VariableFee";
    private static final String KEY_BASE_FEE = "BaseFee";
    private static final String KEY_AMOUNT_FROM = "AmountFrom";
    private static final String KEY_AMOUNT_TO = "AmountTo";

    //push_notifications
    private static final String KEY_READ_STATUS = "ReadStatus";

    private static final String KEY_TYPE = "Type";

    private static final String KEY_PAYMENT_STATUS = "PaymentStatus";

    //table_ultramega_service_config
    private static final String KEY_CONFIG_TYPE = "ConfigType";
    private static final String KEY_CONFIG_DESCRIPTION = "ConfigDescription";

    //TABLE CREATIONS
    public static final String CREATE_TABLE_ULTRAMEGA_SERVICE_CONFIG = CT_IF_NOT_EXISTS + TABLE_ULTRAMEGA_SERVICE_CONFIG + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_CONFIG_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_CONFIG_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_CATEGORIES = CT_IF_NOT_EXISTS + TABLE_CATEGORIES + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_CATEGORY + DATA_TYPE_TEXT + COMMA +
            KEY_DESCRIPTION + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_CATEGORY_ITEMS_PROMOS = CT_IF_NOT_EXISTS + TABLE_CATEGORIES_ITEMS_PROMOS + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_PROMO_ID + DATA_TYPE_TEXT + COMMA +
            KEY_CATEGORY + DATA_TYPE_TEXT + COMMA +
            KEY_CAT_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_IS_PROMO + DATA_TYPE_INTEGER + COMMA +
            KEY_ITEM_GROUP_PIC_URL + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_CATEGORY_ITEMS_REGULAR = CT_IF_NOT_EXISTS + TABLE_CATEGORIES_ITEMS_REGULAR + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_CATEGORY + DATA_TYPE_TEXT + COMMA +
            KEY_CAT_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_IS_PROMO + DATA_TYPE_INTEGER + COMMA +
            KEY_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_GROUP_PIC_URL + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_CATEGORY_ITEMS_DAILYFINDS = CT_IF_NOT_EXISTS + TABLE_CATEGORIES_ITEMS_DAILYFINDS + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_CATEGORY + DATA_TYPE_TEXT + COMMA +
            KEY_CAT_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_IS_PROMO + DATA_TYPE_INTEGER + COMMA +
            KEY_DATE_TIME_IN + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_GROUP_PIC_URL + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_ITEM_SKUS = CT_IF_NOT_EXISTS + TABLE_ITEM_SKUS + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ITEM_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_PACK_SIZE + DATA_TYPE_TEXT + COMMA +
            KEY_CATEGORY + DATA_TYPE_TEXT + COMMA +
            KEY_CAT_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_SUPPLIER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_PACKAGE_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_PRICE + DATA_TYPE_INTEGER + COMMA +
            KEY_GROSS_PRICE + DATA_TYPE_INTEGER + COMMA +
            KEY_IS_PROMO + DATA_TYPE_INTEGER + COMMA +
            KEY_PROMO_DETAILS + DATA_TYPE_TEXT + COMMA +
            KEY_MINIMUM_ORDER_QTY + DATA_TYPE_INTEGER + COMMA +
            KEY_INCREMENTAL_ORDER_QTY + DATA_TYPE_INTEGER + COMMA +
            KEY_BARCODE + DATA_TYPE_INTEGER + COMMA +
            KEY_PACKAGE_DESCRIPTION + DATA_TYPE_INTEGER + COMMA +
            KEY_IS_BULK + DATA_TYPE_INTEGER + COMMA +
            KEY_ITEM_PIC_URL + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_SHOPPING_CARTS_QUEUE = CT_IF_NOT_EXISTS + TABLE_SHOPPING_CARTS_QUEUE + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_DATE_TIME_ADDED + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_IMEI + DATA_TYPE_TEXT + COMMA +
            KEY_CAT_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_PACKAGE_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_PRICE + DATA_TYPE_INTEGER + COMMA +
            KEY_GROSS_PRICE + DATA_TYPE_INTEGER + COMMA +
            KEY_QUANTITY + DATA_TYPE_INTEGER + COMMA +
            KEY_MINIMUM_ORDER_QTY + DATA_TYPE_INTEGER + COMMA +
            KEY_INCREMENTAL_ORDER_QTY + DATA_TYPE_INTEGER + COMMA +
            KEY_TOTAL_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_ITEM_PIC_URL + DATA_TYPE_TEXT + COMMA +
            KEY_REMARKS + DATA_TYPE_TEXT + COMMA +
            KEY_LAST_UPDATE_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_IS_REGISTERED + DATA_TYPE_INTEGER + COMMA +
            KEY_ORDER_TXN_ID + DATA_TYPE_TEXT + COMMA +
            KEY_ORDER_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_PACKAGE_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_PACK_SIZE + DATA_TYPE_TEXT + COMMA +
            KEY_IS_BULK + DATA_TYPE_INTEGER + COMMA +
            KEY_PRICING_GROUP + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_CONSUMER_PROFILE = CT_IF_NOT_EXISTS + TABLE_CONSUMER_PROFILE + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_CONSUMER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_FACEBOOK_ID + DATA_TYPE_TEXT + COMMA +
            KEY_USER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_PASSWORD + DATA_TYPE_TEXT + COMMA +
            KEY_CONSUMER_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_FIRST_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_LAST_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_BIRTH_DATE + DATA_TYPE_TEXT + COMMA +
            KEY_GENDER + DATA_TYPE_TEXT + COMMA +
            KEY_OCCUPATION + DATA_TYPE_TEXT + COMMA +
            KEY_INTEREST + DATA_TYPE_TEXT + COMMA +
            KEY_EMAIL_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_IS_VERIFIED + DATA_TYPE_INTEGER + COMMA +
            KEY_COUNTRY_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_MOBILE_NO + DATA_TYPE_TEXT + COMMA +
            KEY_IMEI + DATA_TYPE_TEXT + COMMA +
            KEY_PROFILE_PIC_URL + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_REGISTERED + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_UPDATED + DATA_TYPE_TEXT + COMMA +
            KEY_LAST_LOGIN + DATA_TYPE_TEXT + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + COMMA +
            KEY_REWARD_POINTS + DATA_TYPE_INTEGER + COMMA +
            KEY_EXTRA_1 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_2 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_3 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_4 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_1 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_2 + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_CONSUMER_WALLET = CT_IF_NOT_EXISTS + TABLE_CONSUMER_WALLET + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_CONSUMER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_CURRENCY_ID + DATA_TYPE_TEXT + COMMA +
            KEY_TOTAL_WALLET + DATA_TYPE_INTEGER + COMMA +
            KEY_WALLET_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_LAST_WALLET_RELOAD_DATE + DATA_TYPE_TEXT + COMMA +
            KEY_LAST_WALLET_RELOAD_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_LAST_WALLET_RELOAD_PRE_WALLET + DATA_TYPE_INTEGER + COMMA +
            KEY_LAST_WALLET_RELOAD_POST_WALLET + DATA_TYPE_INTEGER + COMMA +
            KEY_LAST_ORDER_PAYMENT_DATE + DATA_TYPE_TEXT + COMMA +
            KEY_LAST_ORDER_PAYMENT_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_LAST_ORDER_PAYMENT_PRE_WALLET + DATA_TYPE_INTEGER + COMMA +
            KEY_LAST_ORDER_PAYMENT_POST_WALLET + DATA_TYPE_INTEGER + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_1 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_2 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_3 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_4 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_1 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_2 + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_CONSUMER_BEHAVIOUR_PATTERN = CT_IF_NOT_EXISTS + TABLE_CONSUMER_BEHAVIOUR_PATTERN + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_DATE_TIME_IN + DATA_TYPE_TEXT + COMMA +
            KEY_CONSUMER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_CATEGORY_ID + DATA_TYPE_TEXT + COMMA +
            KEY_CATEGORY_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_1 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_2 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_3 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_4 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_1 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_2 + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_WHOLESALER_BEHAVIOUR_PATTERN = CT_IF_NOT_EXISTS + TABLE_WHOLESALER_BEHAVIOUR_PATTERN + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_DATE_TIME_IN + DATA_TYPE_TEXT + COMMA +
            KEY_CONSUMER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_CATEGORY_ID + DATA_TYPE_TEXT + COMMA +
            KEY_CATEGORY_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_1 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_2 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_3 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_4 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_1 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_2 + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_CONSUMER_ADDRESS = CT_IF_NOT_EXISTS + TABLE_CONSUMER_ADDRESS + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_CONSUMER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_ADDRESS_ID + DATA_TYPE_TEXT + COMMA +
            KEY_STREET_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_CITY + DATA_TYPE_TEXT + COMMA +
            KEY_PROVINCE + DATA_TYPE_TEXT + COMMA +
            KEY_ZIP + DATA_TYPE_INTEGER + COMMA +
            KEY_COUNTRY + DATA_TYPE_TEXT + COMMA +
            KEY_LONGITUDE + DATA_TYPE_TEXT + COMMA +
            KEY_LATITUDE + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_ADDED + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_UPDATED + DATA_TYPE_TEXT + COMMA +
            KEY_IS_DEFAULT + DATA_TYPE_INTEGER + COMMA +
            KEY_EXTRA_1 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_2 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_3 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_4 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_1 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_2 + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_ADMIN_BANK_DETAILS = CT_IF_NOT_EXISTS + TABLE_ADMIN_BANK_DETAILS + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_NETWORK_ID + DATA_TYPE_TEXT + COMMA +
            KEY_BANK_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_BANK_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_BANK_ACCOUNT_NO + DATA_TYPE_TEXT + COMMA +
            KEY_BANK_ACCOUNT_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_1 + DATA_TYPE_TEXT + COMMA +
            KEY_EXTRA_2 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_1 + DATA_TYPE_TEXT + COMMA +
            KEY_NOTES_2 + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_ORDERS_QUEUE = CT_IF_NOT_EXISTS +
            TABLE_ORDERS_QUEUE +
            GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_DATE_TIME_IN + DATA_TYPE_TEXT + COMMA +
            KEY_ORDER_TXN_ID + DATA_TYPE_TEXT + COMMA +
            KEY_ORDER_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_COMPLETED_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_ORDER_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_ID + DATA_TYPE_TEXT + COMMA +
            DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_MOBILE_NUMBER + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_EMAIL_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_DELIVERY_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_LONGITUDE + DATA_TYPE_TEXT + COMMA +
            KEY_LATITUDE + DATA_TYPE_TEXT + COMMA +
            KEY_ACCOUNT_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_ACCOUNT_ID + DATA_TYPE_TEXT + COMMA +
            KEY_ACCOUNT_USER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_TOTAL_ITEMS + DATA_TYPE_INTEGER + COMMA +
            KEY_TOTAL_ITEM_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_DELIVERY_CHARGE + DATA_TYPE_INTEGER + COMMA +
            KEY_TOTAL_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_ORDER_MEDIUM + DATA_TYPE_TEXT + COMMA +
            KEY_NO_OF_ROUTE + DATA_TYPE_INTEGER + COMMA +
            KEY_ROUTE_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_APPROVED_ORDER_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_PAYMENT_TXN_NO + DATA_TYPE_TEXT + COMMA +
            KEY_PAYMENT_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_CONFIRM_PAYMENT_BY + DATA_TYPE_TEXT + COMMA +
            KEY_CONFIRM_PAYMENT_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_ON_DELIVERY_BY + DATA_TYPE_TEXT + COMMA +
            KEY_ON_DELIVERY_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_IS_EXPIRED_ORDER + DATA_TYPE_INTEGER + COMMA +
            KEY_EXPIRED_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_IS_RE_OPEN_ORDER + DATA_TYPE_INTEGER + COMMA +
            KEY_RE_OPEN_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_COMPLETED_BY + DATA_TYPE_TEXT + COMMA +
            KEY_REMARKS + DATA_TYPE_TEXT + COMMA +
            KEY_LAST_UPDATE_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_XML_DETAILS + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_RATING + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_REMARKS + DATA_TYPE_TEXT + COMMA +
            KEY_IS_TAGGED_AS_COMPLETED_BY_CUSTOMER + DATA_TYPE_INTEGER + COMMA +
            KEY_IS_REGISTERED + DATA_TYPE_INTEGER + COMMA +
            KEY_IS_ASSIGNED + DATA_TYPE_INTEGER + COMMA +
            KEY_ASSIGNED_BY + DATA_TYPE_TEXT + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + COMMA +
            // // //
            KEY_TOTAL_ORDER_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_TOTAL_PACKAGE_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            // // //
            KEY_CAT_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_PACKAGE_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_PRICE + DATA_TYPE_INTEGER + COMMA +
            KEY_GROSS_PRICE + DATA_TYPE_INTEGER + COMMA +
            KEY_QUANTITY + DATA_TYPE_INTEGER + COMMA +
            KEY_QUANTITY_SERVED + DATA_TYPE_INTEGER + COMMA +
            KEY_ITEM_PIC_URL + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_PACK_SIZE + DATA_TYPE_TEXT + COMMA +
            KEY_PACKAGE_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_PAYMENTTYPE + DATA_TYPE_TEXT + COMMA +
            KEY_ADDED_DATE_TIME + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_ORDERS_HISTORY = CT_IF_NOT_EXISTS +
            TABLE_ORDERS_HISTORY +
            GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_DATE_TIME_IN + DATA_TYPE_TEXT + COMMA +
            KEY_ORDER_TXN_ID + DATA_TYPE_TEXT + COMMA +
            KEY_ORDER_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_COMPLETED_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_ORDER_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_ID + DATA_TYPE_TEXT + COMMA +
            DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_MOBILE_NUMBER + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_EMAIL_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_DELIVERY_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_LONGITUDE + DATA_TYPE_TEXT + COMMA +
            KEY_LATITUDE + DATA_TYPE_TEXT + COMMA +
            KEY_ACCOUNT_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_ACCOUNT_ID + DATA_TYPE_TEXT + COMMA +
            KEY_ACCOUNT_USER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_TOTAL_ITEMS + DATA_TYPE_INTEGER + COMMA +
            KEY_TOTAL_ITEM_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_DELIVERY_CHARGE + DATA_TYPE_INTEGER + COMMA +
            KEY_TOTAL_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_ORDER_MEDIUM + DATA_TYPE_TEXT + COMMA +
            KEY_NO_OF_ROUTE + DATA_TYPE_INTEGER + COMMA +
            KEY_ROUTE_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_APPROVED_ORDER_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_PAYMENT_TXN_NO + DATA_TYPE_TEXT + COMMA +
            KEY_PAYMENT_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_CONFIRM_PAYMENT_BY + DATA_TYPE_TEXT + COMMA +
            KEY_CONFIRM_PAYMENT_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_ON_DELIVERY_BY + DATA_TYPE_TEXT + COMMA +
            KEY_ON_DELIVERY_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_IS_EXPIRED_ORDER + DATA_TYPE_INTEGER + COMMA +
            KEY_EXPIRED_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_IS_RE_OPEN_ORDER + DATA_TYPE_INTEGER + COMMA +
            KEY_RE_OPEN_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_COMPLETED_BY + DATA_TYPE_TEXT + COMMA +
            KEY_REMARKS + DATA_TYPE_TEXT + COMMA +
            KEY_LAST_UPDATE_DATE_TIME + DATA_TYPE_TEXT + COMMA +
            KEY_XML_DETAILS + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_RATING + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_REMARKS + DATA_TYPE_TEXT + COMMA +
            KEY_IS_TAGGED_AS_COMPLETED_BY_CUSTOMER + DATA_TYPE_INTEGER + COMMA +
            KEY_IS_REGISTERED + DATA_TYPE_INTEGER + COMMA +
            KEY_IS_ASSIGNED + DATA_TYPE_INTEGER + COMMA +
            KEY_ASSIGNED_BY + DATA_TYPE_TEXT + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + COMMA +
            // // //
            KEY_TOTAL_ORDER_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_TOTAL_PACKAGE_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            // // //
            KEY_CAT_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_PACKAGE_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_PRICE + DATA_TYPE_INTEGER + COMMA +
            KEY_GROSS_PRICE + DATA_TYPE_INTEGER + COMMA +
            KEY_QUANTITY + DATA_TYPE_INTEGER + COMMA +
            KEY_QUANTITY_SERVED + DATA_TYPE_INTEGER + COMMA +
            KEY_ITEM_PIC_URL + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_PACK_SIZE + DATA_TYPE_TEXT + COMMA +
            KEY_PACKAGE_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_PAYMENTTYPE + DATA_TYPE_TEXT + COMMA +
            KEY_ADDED_DATE_TIME + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_BRANCHES = CT_IF_NOT_EXISTS + TABLE_BRANCHES + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_NETWORK_ID + DATA_TYPE_TEXT + COMMA +
            KEY_BRANCH_ID + DATA_TYPE_TEXT + COMMA +
            KEY_BRANCH_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_BRANCH_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_BRANCH_GROUP + DATA_TYPE_TEXT + COMMA +
            KEY_AUTHORISED_REPRESENTATIVE + DATA_TYPE_TEXT + COMMA +
            KEY_STREET_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_CITY + DATA_TYPE_TEXT + COMMA +
            KEY_PROVINCE + DATA_TYPE_TEXT + COMMA +
            KEY_ZIP + DATA_TYPE_INTEGER + COMMA +
            KEY_COUNTRY + DATA_TYPE_TEXT + COMMA +
            KEY_LONGITUDE + DATA_TYPE_TEXT + COMMA +
            KEY_LATITUDE + DATA_TYPE_TEXT + COMMA +
            KEY_AUTHORISED_EMAIL_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_AUTHORISED_TEL_NO + DATA_TYPE_TEXT + COMMA +
            KEY_AUTHORISED_COUNTRY_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_AUTHORISED_MOBILE_NO + DATA_TYPE_TEXT + COMMA +
            KEY_FAX + DATA_TYPE_TEXT + COMMA +
            KEY_CURRENT_ORDER_COUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_DATE_TIME_ADDED + DATA_TYPE_TEXT + COMMA +
            KEY_CALCULATED_DISTANCE + DATA_TYPE_INTEGER + COMMA +
            KEY_STOREOPEN + DATA_TYPE_TEXT + COMMA+
            KEY_STORECLOSE + DATA_TYPE_TEXT + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_MY_LIST = CT_IF_NOT_EXISTS + TABLE_MY_LIST +
            GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_CUSTOMER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_IMEI + DATA_TYPE_INTEGER + COMMA +
            KEY_CATEGORY + DATA_TYPE_TEXT + COMMA +
            KEY_CAT_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_PIC_URL + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_ADDED + DATA_TYPE_TEXT + COMMA +
            KEY_IS_REGISTERED + DATA_TYPE_INTEGER + COMMA +
            KEY_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_CODE + DATA_TYPE_TEXT + COMMA +
            KEY_ITEM_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_DESCRIPTION + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_WHOLESALER_PROFILE = CT_IF_NOT_EXISTS + TABLE_WHOLESALER_PROFILE + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_BRANCH_ID + DATA_TYPE_TEXT + COMMA +
            KEY_BRANCH_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_WHOLESALER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_USER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_USER_PASSWORD + DATA_TYPE_TEXT + COMMA +
            KEY_PROFILE_PIC_URL + DATA_TYPE_TEXT + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_ADDED + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_UPDATED + DATA_TYPE_TEXT + COMMA +
            KEY_LAST_LOGIN + DATA_TYPE_TEXT + COMMA +
            KEY_IS_ONLINE + DATA_TYPE_INTEGER + COMMA +
            KEY_LAST_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_FIRST_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_MIDDLE_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_COMPANY + DATA_TYPE_TEXT + COMMA +
            KEY_GENDER + DATA_TYPE_TEXT + COMMA +
            KEY_BIRTH_DATE + DATA_TYPE_TEXT + COMMA +
            KEY_MOBILE_NO + DATA_TYPE_TEXT + COMMA +
            KEY_EMAIL_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_IMEI + DATA_TYPE_TEXT + COMMA +
            KEY_ADDRESS + DATA_TYPE_TEXT + COMMA +
            KEY_CITY + DATA_TYPE_TEXT + COMMA +
            KEY_PROVINCE + DATA_TYPE_TEXT + COMMA +
            KEY_IS_CREDIT + DATA_TYPE_INTEGER + COMMA +
            KEY_CREDIT_LINE + DATA_TYPE_INTEGER + COMMA +
            KEY_PRICE_GROUP + DATA_TYPE_TEXT + COMMA +
            KEY_IS_PARTNER + DATA_TYPE_INTEGER + COMMA +
            KEY_PARTNER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_PARTNERED + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_SUPPLIER = CT_IF_NOT_EXISTS + TABLE_SUPPLIER +
            GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_SUPPLIER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_SUPPLIER_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_RANK + DATA_TYPE_INTEGER + COMMA +
            KEY_IS_SPONSOR + DATA_TYPE_INTEGER + COMMA +
            KEY_PHOTO_PATH + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_SUPPLIER_ITEMS = CT_IF_NOT_EXISTS + TABLE_SUPPLIER_ITEMS +
            GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_CATEGORY + DATA_TYPE_TEXT + COMMA +
            KEY_CAT_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_DESCRIPTION + DATA_TYPE_TEXT + COMMA +
            KEY_PHOTO_PATH + DATA_TYPE_TEXT + COMMA +
            KEY_IS_PROMO + DATA_TYPE_INTEGER + COMMA +
            KEY_SUPPLIER_ID + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_WALLET_RELOAD_QUEUE = CT_IF_NOT_EXISTS + TABLE_WALLET_RELOAD_QUEUE +
            GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_ID + DATA_TYPE_INTEGER + COMMA +
            KEY_PAYMENT_TXN_NO + DATA_TYPE_TEXT + COMMA +
            KEY_PARTNER_PAYMENT_TXN_NO + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_IN + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TIME_COMPLETED + DATA_TYPE_TEXT + COMMA +
            KEY_CONSUMER_TYPE + DATA_TYPE_TEXT + COMMA +
            KEY_CONSUMER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_MOBILE_NUMBER + DATA_TYPE_TEXT + COMMA +
            KEY_AMOUNT_PAID + DATA_TYPE_TEXT + COMMA +
            KEY_PAYMENT_DETAILS + DATA_TYPE_TEXT + COMMA +
            KEY_PAYMENT_OPTION + DATA_TYPE_TEXT + COMMA +
            KEY_PREACCOUNT_WALLET + DATA_TYPE_TEXT + COMMA +
            KEY_POSTACCOUNT_WALLET + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_REMARKS + DATA_TYPE_TEXT + COMMA +
            KEY_ACTED_BY + DATA_TYPE_TEXT + COMMA +
            KEY_REMARKS + DATA_TYPE_TEXT + COMMA +
            KEY_STATUS + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TO_MONTH + DATA_TYPE_TEXT + COMMA +
            KEY_DATE_TO_YEAR + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    //TABLE_PROMOS
    public static final String CREATE_TABLE_PROMOS = CT_IF_NOT_EXISTS + TABLE_PROMOS + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_PROMO_ID + DATA_TYPE_TEXT + COMMA +
            KEY_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_IS_PROMO + DATA_TYPE_INTEGER + COMMA +
            KEY_RANK + DATA_TYPE_INTEGER + COMMA +
            KEY_PHOTO_PATH + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_RECENT_SEARCHES = CT_IF_NOT_EXISTS + TABLE_RECENT_SEARCHES +
            GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_RECENT_SEARCH_KEY + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    //TABLE_ORDER_PAYMENTS
    public static final String CREATE_TABLE_ORDER_PAYMENTS = CT_IF_NOT_EXISTS + TABLE_ORDER_PAYMENTS + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_PAYMENT_TXN_NO + DATA_TYPE_TEXT + COMMA +
            KEY_ORDER_TXN_ID + DATA_TYPE_TEXT + COMMA +
            KEY_PAYMENT_OPTION + DATA_TYPE_TEXT + COMMA +
            KEY_TOTAL_AMOUNT + DATA_TYPE_INTEGER + COMMA +
            KEY_DELIVERY_CHARGE + DATA_TYPE_INTEGER + COMMA +
            KEY_PAYMENT_STATUS + DATA_TYPE_TEXT + COMMA +
            KEY_AMOUNT_PAID + DATA_TYPE_INTEGER + GENERIC_STATEMENT_ENDER;

    public static final String CREATE_TABLE_PROMOPTS = CT_IF_NOT_EXISTS + TABLE_PROMOPTS + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_CUSTOMER_ID + DATA_TYPE_TEXT + COMMA +
            KEY_DATE + DATA_TYPE_TEXT + COMMA +
            KEY_NAME + DATA_TYPE_TEXT + COMMA +
            KEY_PHOTO_PATH + DATA_TYPE_TEXT + COMMA +
            KEY_POINTS + DATA_TYPE_INTEGER + COMMA +
            KEY_PROMO_ID + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    //TABLE_NORIFICATION
    public static final String CREATE_TABLE_NOTIFICATION = CT_IF_NOT_EXISTS + TABLE_NOTIFICATION + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_DATE_TIME_IN + DATA_TYPE_TEXT + COMMA +
            KEY_NOTIFICATION_TNX_NO + DATA_TYPE_TEXT + COMMA +
            KEY_CHANNEL + DATA_TYPE_TEXT + COMMA +
            KEY_SUBJECT + DATA_TYPE_TEXT + COMMA +
            KEY_MESSAGE + DATA_TYPE_TEXT + COMMA +
            KEY_SENDER + DATA_TYPE_TEXT + COMMA +
            KEY_SENDER_LOGO + DATA_TYPE_TEXT + COMMA +
            KEY_PAYLOAD + DATA_TYPE_TEXT + COMMA +
            KEY_READ_STATUS + DATA_TYPE_TEXT + COMMA +
            KEY_CUSTOMER_ID + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    //TABLE_DELIVERY_CHARGE
    public static final String CREATE_TABLE_DELIVERY_CHARGE = CT_IF_NOT_EXISTS + TABLE_DELIVERY_CHARGE + GENERIC_ID + DATA_TYPE_INTEGER + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTOINCREMENT + COMMA +
            KEY_CHARGE_CLASS + DATA_TYPE_TEXT + COMMA +
            KEY_CURRENCY_ID + DATA_TYPE_TEXT + COMMA +
            KEY_VARIABLE_FEE + DATA_TYPE_INTEGER + COMMA +
            KEY_BASE_FEE + DATA_TYPE_INTEGER + COMMA +
            KEY_AMOUNT_FROM + DATA_TYPE_INTEGER + COMMA +
            KEY_AMOUNT_TO + DATA_TYPE_TEXT + GENERIC_STATEMENT_ENDER;

    private final Context mContext;
    private UltramegaShopDatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    public UltramegaShopUtilities(Context ctx) {
        mContext = ctx;
    }

    private UltramegaShopUtilities open() throws SQLException {
        if (mDbHelper == null) {
            mDbHelper = new UltramegaShopDatabaseHelper(mContext);
            if (mDb == null) {
                mDb = mDbHelper.getWritableDatabase();
            }
        }
        return this;
    }

    private void close() {
//        mDbHelper.close();
//        mDb.close();
    }

    public void truncateTable(String tableName) {
        open();
        mDb.execSQL("DELETE FROM " + tableName);
        close();
    }

    //table_ultramega_service_config
    public void insertServiceConfig(ShoppingModeConfig shoppingModeConfig) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONFIG_TYPE, shoppingModeConfig.getConfigType());
        contentValues.put(KEY_CONFIG_DESCRIPTION, shoppingModeConfig.getConfigDescription());
        contentValues.put(KEY_STATUS, shoppingModeConfig.getStatus());

        mDb.insert(TABLE_ULTRAMEGA_SERVICE_CONFIG, null, contentValues);
        close();
    }

    //ADMIN_BANK_DETAILS
    public void insertBankReference(BankReference bankReference) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, bankReference.getID());
        contentValues.put(KEY_NETWORK_ID, bankReference.getNetworkID());
        contentValues.put(KEY_BANK_CODE, bankReference.getBankCode());
        contentValues.put(KEY_BANK_NAME, bankReference.getBankName());
        contentValues.put(KEY_BANK_ACCOUNT_NO, bankReference.getBankAccountNo());
        contentValues.put(KEY_BANK_ACCOUNT_NAME, bankReference.getBankAccountName());
        contentValues.put(KEY_STATUS, bankReference.getStatus());
        contentValues.put(KEY_EXTRA_1, bankReference.getExtra1());
        contentValues.put(KEY_EXTRA_2, bankReference.getExtra2());
        contentValues.put(KEY_NOTES_1, bankReference.getNotes1());
        contentValues.put(KEY_NOTES_2, bankReference.getNotes2());

        mDb.insert(TABLE_ADMIN_BANK_DETAILS, null, contentValues);
        close();
    }

    //CONSUMER_ADDRESS
    public void insertConsumerAddress(ConsumerAddress consumerAddress) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, consumerAddress.getID());
        contentValues.put(KEY_CONSUMER_ID, consumerAddress.getConsumerID());
        contentValues.put(KEY_ADDRESS_ID, consumerAddress.getAddressID());
        contentValues.put(KEY_STREET_ADDRESS, consumerAddress.getStreetAddress());
        contentValues.put(KEY_CITY, consumerAddress.getCity());
        contentValues.put(KEY_PROVINCE, consumerAddress.getProvince());
        contentValues.put(KEY_ZIP, consumerAddress.getZip());
        contentValues.put(KEY_COUNTRY, consumerAddress.getCountry());
        contentValues.put(KEY_LONGITUDE, consumerAddress.getLongitude());
        contentValues.put(KEY_LATITUDE, consumerAddress.getLatitude());
        contentValues.put(KEY_DATE_TIME_ADDED, consumerAddress.getDateTimeAdded());
        contentValues.put(KEY_DATE_TIME_UPDATED, consumerAddress.getDateTimeUpdated());
        contentValues.put(KEY_IS_DEFAULT, consumerAddress.getIsDefault());
        contentValues.put(KEY_EXTRA_1, consumerAddress.getExtra1());
        contentValues.put(KEY_EXTRA_2, consumerAddress.getExtra2());
        contentValues.put(KEY_EXTRA_3, consumerAddress.getExtra3());
        contentValues.put(KEY_EXTRA_4, consumerAddress.getExtra4());
        contentValues.put(KEY_NOTES_1, consumerAddress.getNotes1());
        contentValues.put(KEY_NOTES_2, consumerAddress.getNotes2());

        mDb.insert(TABLE_CONSUMER_ADDRESS, null, contentValues);
        close();
    }

    //CONSUMER_BEHAVIOUR_PATTERN
    public void insertConsumerBehaviourPattern(ConsumerBehaviourPattern consumerPattern) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, consumerPattern.getID());
        contentValues.put(KEY_DATE_TIME_IN, consumerPattern.getDateTimeIN());
        contentValues.put(KEY_CONSUMER_ID, consumerPattern.getConsumerID());
        contentValues.put(KEY_CATEGORY_ID, consumerPattern.getCategoryID());
        contentValues.put(KEY_CATEGORY_NAME, consumerPattern.getCategoryName());
        contentValues.put(KEY_EXTRA_1, consumerPattern.getExtra1());
        contentValues.put(KEY_EXTRA_2, consumerPattern.getExtra2());
        contentValues.put(KEY_EXTRA_3, consumerPattern.getExtra3());
        contentValues.put(KEY_EXTRA_4, consumerPattern.getExtra4());
        contentValues.put(KEY_NOTES_1, consumerPattern.getNotes1());
        contentValues.put(KEY_NOTES_2, consumerPattern.getNotes2());
        mDb.insert(TABLE_CONSUMER_BEHAVIOUR_PATTERN, null, contentValues);
        close();
    }

    //TABLE_WHOLESALER_BEHAVIOUR_PATTERN
    public void insertWholesalerBehaviourPattern(ConsumerBehaviourPattern consumerPattern) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, consumerPattern.getID());
        contentValues.put(KEY_DATE_TIME_IN, consumerPattern.getDateTimeIN());
        contentValues.put(KEY_CONSUMER_ID, consumerPattern.getConsumerID());
        contentValues.put(KEY_CATEGORY_ID, consumerPattern.getCategoryID());
        contentValues.put(KEY_CATEGORY_NAME, consumerPattern.getCategoryName());
        contentValues.put(KEY_EXTRA_1, consumerPattern.getExtra1());
        contentValues.put(KEY_EXTRA_2, consumerPattern.getExtra2());
        contentValues.put(KEY_EXTRA_3, consumerPattern.getExtra3());
        contentValues.put(KEY_EXTRA_4, consumerPattern.getExtra4());
        contentValues.put(KEY_NOTES_1, consumerPattern.getNotes1());
        contentValues.put(KEY_NOTES_2, consumerPattern.getNotes2());
        mDb.insert(TABLE_WHOLESALER_BEHAVIOUR_PATTERN, null, contentValues);
        close();
    }

    //CONSUMER_PROFILE
    public void insertConsumerProfile(ConsumerProfile consumerProfile) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, consumerProfile.getID());
        contentValues.put(KEY_CONSUMER_ID, consumerProfile.getConsumerID());
        contentValues.put(KEY_FACEBOOK_ID, consumerProfile.getFacebookID());
        contentValues.put(KEY_USER_ID, consumerProfile.getUserID());
        contentValues.put(KEY_PASSWORD, consumerProfile.getPassword());
        contentValues.put(KEY_CONSUMER_TYPE, consumerProfile.getConsumerType());
        contentValues.put(KEY_FIRST_NAME, consumerProfile.getFirstName());
        contentValues.put(KEY_LAST_NAME, consumerProfile.getLastName());
        contentValues.put(KEY_BIRTH_DATE, consumerProfile.getBirthDate());
        contentValues.put(KEY_GENDER, consumerProfile.getGender());
        contentValues.put(KEY_OCCUPATION, consumerProfile.getOccupation());
        contentValues.put(KEY_INTEREST, consumerProfile.getInterest());
        contentValues.put(KEY_EMAIL_ADDRESS, consumerProfile.getEmailAddress());
        contentValues.put(KEY_IS_VERIFIED, consumerProfile.getIsVerified());
        contentValues.put(KEY_COUNTRY_CODE, consumerProfile.getCountryCode());
        contentValues.put(KEY_MOBILE_NO, consumerProfile.getMobileNo());
        contentValues.put(KEY_IMEI, consumerProfile.getIMEI());
        contentValues.put(KEY_PROFILE_PIC_URL, consumerProfile.getProfilePicURL());
        contentValues.put(KEY_DATE_TIME_REGISTERED, consumerProfile.getDateTimeRegistered());
        contentValues.put(KEY_DATE_TIME_UPDATED, consumerProfile.getDateTimeUpdated());
        contentValues.put(KEY_LAST_LOGIN, consumerProfile.getLastLogin());
        contentValues.put(KEY_STATUS, consumerProfile.getStatus());
        contentValues.put(KEY_REWARD_POINTS, consumerProfile.getRewardPoints());
        contentValues.put(KEY_EXTRA_1, consumerProfile.getExtra1());
        contentValues.put(KEY_EXTRA_2, consumerProfile.getExtra2());
        contentValues.put(KEY_EXTRA_3, consumerProfile.getExtra3());
        contentValues.put(KEY_EXTRA_4, consumerProfile.getExtra4());
        contentValues.put(KEY_NOTES_1, consumerProfile.getNotes1());
        contentValues.put(KEY_NOTES_2, consumerProfile.getNotes2());
        mDb.insert(TABLE_CONSUMER_PROFILE, null, contentValues);
        close();
    }

    public void updateProfPic(String imageLink, String userid) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_PROFILE_PIC_URL, imageLink);
        mDb.update(TABLE_CONSUMER_PROFILE, cv, KEY_USER_ID + "='" + userid + "'", null);
        close();
    }

    //CONSUMER_WALLET
    public void insertConsumerWallet(ConsumerWallet consumerWallet) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, consumerWallet.getID());
        contentValues.put(KEY_CONSUMER_ID, consumerWallet.getConsumerID());
        contentValues.put(KEY_CURRENCY_ID, consumerWallet.getCurrencyID());
        contentValues.put(KEY_TOTAL_WALLET, consumerWallet.getTotalWallet());
        contentValues.put(KEY_WALLET_TYPE, consumerWallet.getWalletType());
        contentValues.put(KEY_LAST_WALLET_RELOAD_DATE, consumerWallet.getLastWalletReloadDate());
        contentValues.put(KEY_LAST_WALLET_RELOAD_AMOUNT, consumerWallet.getLastWalletReloadAmount());
        contentValues.put(KEY_LAST_WALLET_RELOAD_PRE_WALLET, consumerWallet.getLastWalletReloadPreWallet());
        contentValues.put(KEY_LAST_WALLET_RELOAD_POST_WALLET, consumerWallet.getLastWalletReloadPostWallet());
        contentValues.put(KEY_LAST_ORDER_PAYMENT_DATE, consumerWallet.getLastOrderPaymentDate());
        contentValues.put(KEY_LAST_ORDER_PAYMENT_AMOUNT, consumerWallet.getLastOrderPaymentAmount());
        contentValues.put(KEY_LAST_ORDER_PAYMENT_PRE_WALLET, consumerWallet.getLastOrderPaymentPreWallet());
        contentValues.put(KEY_LAST_ORDER_PAYMENT_POST_WALLET, consumerWallet.getLastOrderPaymentPostWallet());
        contentValues.put(KEY_STATUS, consumerWallet.getStatus());
        contentValues.put(KEY_EXTRA_1, consumerWallet.getExtra1());
        contentValues.put(KEY_EXTRA_2, consumerWallet.getExtra2());
        contentValues.put(KEY_EXTRA_3, consumerWallet.getExtra3());
        contentValues.put(KEY_EXTRA_4, consumerWallet.getExtra4());
        contentValues.put(KEY_NOTES_1, consumerWallet.getNotes1());
        contentValues.put(KEY_NOTES_2, consumerWallet.getNotes2());

        mDb.insert(TABLE_CONSUMER_WALLET, null, contentValues);
        close();
    }

    //CONSUMER_CHARGE
    public void insertConsumerCharge(ConsumerCharge consumerCharge) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CHARGE_CLASS, consumerCharge.getChargeClass());
        contentValues.put(KEY_CURRENCY_ID, consumerCharge.getCurrencyID());
        contentValues.put(KEY_VARIABLE_FEE, consumerCharge.getVariableFee());
        contentValues.put(KEY_BASE_FEE, consumerCharge.getBaseFee());
        contentValues.put(KEY_AMOUNT_FROM, consumerCharge.getAmountFrom());
        contentValues.put(KEY_AMOUNT_TO, consumerCharge.getAmountTo());
        mDb.insert(TABLE_DELIVERY_CHARGE, null, contentValues);
        close();
    }

    public void deleteItemCartsQueue(String itemcode, String packagecode) {
        open();
        String strsql = "DELETE FROM " + TABLE_SHOPPING_CARTS_QUEUE + " WHERE " + KEY_ITEM_CODE + "=?" + " AND " + KEY_PACKAGE_CODE + "=?";
        mDb.execSQL(strsql, new String[]{itemcode, packagecode});
        close();
    }

    public void updateItemCartsQueue(int quantity, double totalamount, String itemcode, String packagecode) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_QUANTITY, quantity);
        contentValues.put(KEY_TOTAL_AMOUNT, totalamount);
        mDb.update(TABLE_SHOPPING_CARTS_QUEUE, contentValues, KEY_ITEM_CODE + "=?" + " AND " + KEY_PACKAGE_CODE + "=?", new String[]{itemcode, packagecode});
        close();
    }

    //SHOPPING_CARTS_QUEUE
    public void insertAllShoppingCartsQueue(ShoppingCartsQueue shoppingCartsQueue) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE_TIME_ADDED, shoppingCartsQueue.getDateTimeAdded());
        contentValues.put(KEY_CUSTOMER_TYPE, shoppingCartsQueue.getCustomerType());
        contentValues.put(KEY_CUSTOMER_ID, shoppingCartsQueue.getCustomerID());
        contentValues.put(KEY_IMEI, shoppingCartsQueue.getIMEI());
        contentValues.put(KEY_CAT_CLASS, shoppingCartsQueue.getCatClass());
        contentValues.put(KEY_ITEM_CODE, shoppingCartsQueue.getItemCode());
        contentValues.put(KEY_PACKAGE_CODE, shoppingCartsQueue.getPackageCode());
        contentValues.put(KEY_PRICE, shoppingCartsQueue.getPrice());
        contentValues.put(KEY_GROSS_PRICE, shoppingCartsQueue.getGrossPrice());
        contentValues.put(KEY_QUANTITY, shoppingCartsQueue.getQuantity());
        contentValues.put(KEY_MINIMUM_ORDER_QTY, shoppingCartsQueue.getMinimumOrderQTY());
        contentValues.put(KEY_INCREMENTAL_ORDER_QTY, shoppingCartsQueue.getIncrementalOrderQTY());
        contentValues.put(KEY_TOTAL_AMOUNT, shoppingCartsQueue.getTotalAmount());
        contentValues.put(KEY_ITEM_PIC_URL, shoppingCartsQueue.getItemPicUrl());
        contentValues.put(KEY_REMARKS, shoppingCartsQueue.getRemarks());
        contentValues.put(KEY_LAST_UPDATE_DATE_TIME, shoppingCartsQueue.getLastUpdateDateTime());
        contentValues.put(KEY_IS_REGISTERED, shoppingCartsQueue.getIsRegistered());
        contentValues.put(KEY_ORDER_TXN_ID, shoppingCartsQueue.getOrderTxnID());
        contentValues.put(KEY_ORDER_DATE_TIME, shoppingCartsQueue.getOrderDateTime());
        contentValues.put(KEY_STATUS, shoppingCartsQueue.getStatus());
        contentValues.put(KEY_PRICING_GROUP, shoppingCartsQueue.getPricingGroup());
        contentValues.put(KEY_ITEM_DESCRIPTION, shoppingCartsQueue.getItemDescription());
        contentValues.put(KEY_PACKAGE_DESCRIPTION, shoppingCartsQueue.getPackageDescription());
        contentValues.put(KEY_PACK_SIZE, shoppingCartsQueue.getPackSize());
        contentValues.put(KEY_IS_BULK, shoppingCartsQueue.getIsBulk());
        mDb.insert(TABLE_SHOPPING_CARTS_QUEUE, null, contentValues);
        close();
    }

    //ITEM_SKUS
    public void insertAllItemSKUS(ItemSKU itemSKU, String usertype) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM_CODE, itemSKU.getItemCode());
        contentValues.put(KEY_ITEM_DESCRIPTION, itemSKU.getItemDescription());
        contentValues.put(KEY_PACK_SIZE, itemSKU.getPackSize());
        contentValues.put(KEY_CATEGORY, itemSKU.getCategory());
        contentValues.put(KEY_CAT_CLASS, itemSKU.getCatClass());
        contentValues.put(KEY_SUPPLIER_ID, itemSKU.getSupplierID());
        contentValues.put(KEY_PACKAGE_CODE, itemSKU.getPackageCode());
        contentValues.put(KEY_PRICE, itemSKU.getPrice());
        contentValues.put(KEY_GROSS_PRICE, itemSKU.getGrossPrice());
        contentValues.put(KEY_IS_PROMO, itemSKU.getIsPromo());
        contentValues.put(KEY_PROMO_DETAILS, itemSKU.getPromoDetails());
        contentValues.put(KEY_MINIMUM_ORDER_QTY, itemSKU.getMinimumOrderQTY());
        contentValues.put(KEY_INCREMENTAL_ORDER_QTY, itemSKU.getIncrementalOrderQTY());
        contentValues.put(KEY_BARCODE, itemSKU.getBarcode());
        contentValues.put(KEY_PACKAGE_DESCRIPTION, itemSKU.getPackageDescription());
        contentValues.put(KEY_IS_BULK, itemSKU.getIsBulk());
        if (usertype.equals("CONSUMER")) {
            String itemcode = Normalizer.normalize(itemSKU.getItemCode(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = itemSKU.getItemCode() != null ? RetrofitBuild.S3_URL_RETAILER.concat((itemcode.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_ITEM_PIC_URL, pic);
        } else if (usertype.equals("WHOLESALER")) {
            String itemcode = Normalizer.normalize(itemSKU.getItemCode(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = itemSKU.getItemCode() != null ? RetrofitBuild.S3_URL_WHOLESALER.concat((itemcode.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_ITEM_PIC_URL, pic);
        }
        mDb.insert(TABLE_ITEM_SKUS, null, contentValues);
        close();
    }

    //CATEGORIES
    public void insertAllCategories(Category category) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CATEGORY, category.getCategory());
        contentValues.put(KEY_DESCRIPTION, category.getDescription());
        mDb.insert(TABLE_CATEGORIES, null, contentValues);
        close();
    }

    //CATEGORIES ITEMS
    public void insertAllCategoryItemsRegular(CategoryItems categoryItems, String usertype) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CATEGORY, categoryItems.getCategory());
        contentValues.put(KEY_CAT_CLASS, categoryItems.getCatClass());
        contentValues.put(KEY_IS_PROMO, categoryItems.getIsPromo());
        contentValues.put(KEY_DESCRIPTION, categoryItems.getDescription());

        if (usertype.equals("CONSUMER")) {
            String catclass = Normalizer.normalize(categoryItems.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = categoryItems.getCatClass() != null ? RetrofitBuild.S3_URL_RETAILER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_ITEM_GROUP_PIC_URL, pic == null ? "" : pic);
        } else if (usertype.equals("WHOLESALER")) {
            String catclass = Normalizer.normalize(categoryItems.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = categoryItems.getCatClass() != null ? RetrofitBuild.S3_URL_WHOLESALER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_ITEM_GROUP_PIC_URL, pic == null ? "" : pic);
        }
        mDb.insert(TABLE_CATEGORIES_ITEMS_REGULAR, null, contentValues);
        close();
    }

    //SUPPLIER ITEMS
    public void insertAllSupplierItems(SupplierItems info, String usertype) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CATEGORY, info.getCategory());
        contentValues.put(KEY_CAT_CLASS, info.getCatClass());
        contentValues.put(KEY_DESCRIPTION, info.getDescription());
        contentValues.put(KEY_IS_PROMO, info.getIsPromo());
        contentValues.put(KEY_SUPPLIER_ID, info.getSupplierID());

        if (usertype.equals("CONSUMER")) {
            String catclass = Normalizer.normalize(info.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = info.getCatClass() != null ? RetrofitBuild.S3_URL_RETAILER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_PHOTO_PATH, pic);
        } else if (usertype.equals("WHOLESALER")) {
            String catclass = Normalizer.normalize(info.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = info.getCatClass() != null ? RetrofitBuild.S3_URL_WHOLESALER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_PHOTO_PATH, pic);
        }
        mDb.insert(TABLE_SUPPLIER_ITEMS, null, contentValues);
        close();
    }

    //PROMOS ITEMS
    public void insertPromoItems(CategoryItems categoryItems, String usertype) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PROMO_ID, categoryItems.getPromoID());
        contentValues.put(KEY_CATEGORY, categoryItems.getCategory());
        contentValues.put(KEY_CAT_CLASS, categoryItems.getCatClass());
        contentValues.put(KEY_IS_PROMO, categoryItems.getIsPromo());
        contentValues.put(KEY_DESCRIPTION, categoryItems.getDescription());

        if (usertype.equals("CONSUMER")) {
            String catclass = Normalizer.normalize(categoryItems.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = categoryItems.getCatClass() != null ? RetrofitBuild.S3_URL_RETAILER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_ITEM_GROUP_PIC_URL, pic);
        } else if (usertype.equals("WHOLESALER")) {
            String catclass = Normalizer.normalize(categoryItems.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = categoryItems.getCatClass() != null ? RetrofitBuild.S3_URL_WHOLESALER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_ITEM_GROUP_PIC_URL, pic);
        }
        mDb.insert(TABLE_CATEGORIES_ITEMS_PROMOS, null, contentValues);
        close();
    }

    public void insertDailyFinds(CategoryItems categoryItems, String usertype) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CATEGORY, categoryItems.getCategory());
        contentValues.put(KEY_CAT_CLASS, categoryItems.getCatClass());
        contentValues.put(KEY_DESCRIPTION, categoryItems.getDescription());
        contentValues.put(KEY_IS_PROMO, categoryItems.getIsPromo());
        contentValues.put(KEY_DATE_TIME_IN, categoryItems.getDateTimeIN());

        if (usertype.equals("CONSUMER")) {
            if (categoryItems.getCatClass() != null) {
                String catclass = Normalizer.normalize(categoryItems.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
                String pic = categoryItems.getCatClass() != null ? RetrofitBuild.S3_URL_RETAILER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
                contentValues.put(KEY_ITEM_GROUP_PIC_URL, pic);
            }
        } else if (usertype.equals("WHOLESALER")) {
            if (categoryItems.getCatClass() != null) {
                String catclass = Normalizer.normalize(categoryItems.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
                String pic = categoryItems.getCatClass() != null ? RetrofitBuild.S3_URL_WHOLESALER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
                contentValues.put(KEY_ITEM_GROUP_PIC_URL, pic);
            }
        }
        mDb.insert(TABLE_CATEGORIES_ITEMS_DAILYFINDS, null, contentValues);
        // close();
    }

    //table_ultramega_service_config
    public ShoppingModeConfig getShoppingModeConfig(String configType) {
        ShoppingModeConfig shoppingModeConfig = null;
        open();
        String strsql = "SELECT * FROM " + TABLE_ULTRAMEGA_SERVICE_CONFIG + " WHERE " + KEY_CONFIG_TYPE + "=? LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{configType});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String ConfigType = cursor.getString(cursor.getColumnIndex(KEY_CONFIG_TYPE));
                String ConfigDescription = cursor.getString(cursor.getColumnIndex(KEY_CONFIG_DESCRIPTION));
                String Status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                shoppingModeConfig = new ShoppingModeConfig(ConfigType, ConfigDescription, Status);
            }
        }
        cursor.close();
        close();
        return shoppingModeConfig;
    }

    public ShoppingCartSummary getShoppingCartSummary(String usertype) {
        ShoppingCartSummary item = null;
        open();

        String strsqlconsumer = "SELECT SUM(" + KEY_QUANTITY + ") as '" + KEY_QUANTITY + "', SUM(" + KEY_TOTAL_AMOUNT + ") as '" + KEY_TOTAL_AMOUNT + "' FROM " + TABLE_SHOPPING_CARTS_QUEUE + " LIMIT 1";
        String strsqlwholesaler = "SELECT ( " +
                " SELECT SUM(" + KEY_QUANTITY + ") FROM " + TABLE_SHOPPING_CARTS_QUEUE + " WHERE isBulk = '1' LIMIT 1 ) as TotalBulk, " +
                " ( SELECT SUM(" + KEY_QUANTITY + ") FROM " + TABLE_SHOPPING_CARTS_QUEUE + " WHERE isBulk = '0' LIMIT 1 ) as TotalNonBulk,  " +
                "( SELECT SUM(" + KEY_TOTAL_AMOUNT + ") FROM " + TABLE_SHOPPING_CARTS_QUEUE + " LIMIT 1 ) as '" + KEY_TOTAL_AMOUNT + "' ";
        String strsql = usertype.equals("CONSUMER") ? strsqlconsumer : strsqlwholesaler;

        Cursor cursor = mDb.rawQuery(strsql, new String[]{});
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                if (usertype.equals("CONSUMER")) {

                    int Quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));
                    double TotalAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                    item = new ShoppingCartSummary(Quantity, TotalAmount);

                } else {

                    int totalBulk = cursor.getInt(cursor.getColumnIndex("TotalBulk"));
                    int totalNonBulk = cursor.getInt(cursor.getColumnIndex("TotalNonBulk"));
                    double TotalAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                    item = new ShoppingCartSummary(totalBulk, totalNonBulk, TotalAmount);

                }

            }

        } else {

            item = new ShoppingCartSummary(0, 0);

        }

        cursor.close();
        close();
        return item;
    }

    public List<ConsumerBehaviourPattern> getConsumerBehaviourPatterns() {
        List<ConsumerBehaviourPattern> listConsumerPatterns = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_CONSUMER_BEHAVIOUR_PATTERN, new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String DateTimeIN = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_IN));
                String ConsumerID = cursor.getString(cursor.getColumnIndex(KEY_CONSUMER_ID));
                String CategoryID = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID));
                String CategoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME));
                String Extra1 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_1));
                String Extra2 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_2));
                String Extra3 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_3));
                String Extra4 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_4));
                String Notes1 = cursor.getString(cursor.getColumnIndex(KEY_NOTES_1));
                String Notes2 = cursor.getString(cursor.getColumnIndex(KEY_NOTES_2));
                listConsumerPatterns.add(new ConsumerBehaviourPattern(ID, DateTimeIN, ConsumerID, CategoryID, CategoryName, Extra1, Extra2, Extra3, Extra4, Notes1, Notes2));
            }
        }
        cursor.close();
        close();
        return listConsumerPatterns;
    }

    //TABLE_WHOLESALER_BEHAVIOUR_PATTERN
    public List<ConsumerBehaviourPattern> getWholesalerBehaviourPatterns() {
        List<ConsumerBehaviourPattern> listConsumerPatterns = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_WHOLESALER_BEHAVIOUR_PATTERN, new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String DateTimeIN = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_IN));
                String ConsumerID = cursor.getString(cursor.getColumnIndex(KEY_CONSUMER_ID));
                String CategoryID = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID));
                String CategoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME));
                String Extra1 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_1));
                String Extra2 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_2));
                String Extra3 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_3));
                String Extra4 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_4));
                String Notes1 = cursor.getString(cursor.getColumnIndex(KEY_NOTES_1));
                String Notes2 = cursor.getString(cursor.getColumnIndex(KEY_NOTES_2));
                listConsumerPatterns.add(new ConsumerBehaviourPattern(ID, DateTimeIN, ConsumerID, CategoryID, CategoryName, Extra1, Extra2, Extra3, Extra4, Notes1, Notes2));
            }
        }
        cursor.close();
        close();
        return listConsumerPatterns;
    }

    public List<ShoppingCartsQueue> getOrderDetails(String catclass, String itemcode) {
        List<ShoppingCartsQueue> listShoppingCartsQueue = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + TABLE_SHOPPING_CARTS_QUEUE + " WHERE " + KEY_CAT_CLASS + "=?" + " AND " + KEY_ITEM_CODE + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{catclass, itemcode});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String dateTimeAdded = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_ADDED));
                String customerType = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_TYPE));
                String customerID = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ID));
                String IMEI = cursor.getString(cursor.getColumnIndex(KEY_IMEI));
                String catClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String itemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                String packageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                double grossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));
                int minimumOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_MINIMUM_ORDER_QTY));
                int incrementalOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_INCREMENTAL_ORDER_QTY));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                String itemPicUrl = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                String remarks = cursor.getString(cursor.getColumnIndex(KEY_REMARKS));
                String lastUpdateDateTime = cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATE_DATE_TIME));
                int isRegistered = cursor.getInt(cursor.getColumnIndex(KEY_IS_REGISTERED));
                String orderTxnID = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TXN_ID));
                String orderDateTime = cursor.getString(cursor.getColumnIndex(KEY_ORDER_DATE_TIME));
                String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                String pricingGroup = cursor.getString(cursor.getColumnIndex(KEY_PRICING_GROUP));
                String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                int isBulk = cursor.getInt(cursor.getColumnIndex(KEY_IS_BULK));
                listShoppingCartsQueue.add(new ShoppingCartsQueue(dateTimeAdded,
                        customerType,
                        customerID,
                        IMEI,
                        catClass,
                        itemCode,
                        packageCode,
                        price,
                        grossPrice,
                        quantity,
                        minimumOrderQTY,
                        incrementalOrderQTY,
                        totalAmount,
                        itemPicUrl,
                        remarks,
                        lastUpdateDateTime,
                        isRegistered,
                        orderTxnID,
                        orderDateTime,
                        status,
                        pricingGroup,
                        itemDescription,
                        packageDescription,
                        packSize,
                        isBulk));
            }
        }
        cursor.close();
        close();
        return listShoppingCartsQueue;
    }

    public List<ShoppingCartsQueue> getExistingItemInCartsQueue(String catclass, String itemcode, String packagecode) {
        List<ShoppingCartsQueue> listShoppingCartsQueue = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + TABLE_SHOPPING_CARTS_QUEUE + " WHERE " + KEY_CAT_CLASS + "=?" + " AND " + KEY_ITEM_CODE + "=?" + " AND " + KEY_PACKAGE_CODE + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{catclass, itemcode, packagecode});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String dateTimeAdded = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_ADDED));
                String customerType = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_TYPE));
                String customerID = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ID));
                String IMEI = cursor.getString(cursor.getColumnIndex(KEY_IMEI));
                String catClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String itemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                String packageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                double grossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));
                int minimumOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_MINIMUM_ORDER_QTY));
                int incrementalOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_INCREMENTAL_ORDER_QTY));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                String itemPicUrl = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                String remarks = cursor.getString(cursor.getColumnIndex(KEY_REMARKS));
                String lastUpdateDateTime = cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATE_DATE_TIME));
                int isRegistered = cursor.getInt(cursor.getColumnIndex(KEY_IS_REGISTERED));
                String orderTxnID = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TXN_ID));
                String orderDateTime = cursor.getString(cursor.getColumnIndex(KEY_ORDER_DATE_TIME));
                String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                String pricingGroup = cursor.getString(cursor.getColumnIndex(KEY_PRICING_GROUP));
                String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                int isBulk = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_BULK));
                listShoppingCartsQueue.add(new ShoppingCartsQueue(dateTimeAdded, customerType, customerID, IMEI, catClass, itemCode, packageCode, price, grossPrice, quantity, minimumOrderQTY, incrementalOrderQTY, totalAmount, itemPicUrl, remarks, lastUpdateDateTime, isRegistered, orderTxnID, orderDateTime, status, pricingGroup, itemDescription, packageDescription, packSize, isBulk));
            }
        }
        cursor.close();
        close();
        return listShoppingCartsQueue;
    }

    public List<ShoppingCartsQueue> getAllShoppingCartsQueue() {
        List<ShoppingCartsQueue> listShoppingCartsQueue = new ArrayList<>();

        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_SHOPPING_CARTS_QUEUE + " ORDER BY " + KEY_DATE_TIME_ADDED + " DESC ", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String dateTimeAdded = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_ADDED));
                String customerType = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_TYPE));
                String customerID = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ID));
                String IMEI = cursor.getString(cursor.getColumnIndex(KEY_IMEI));
                String catClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String itemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                String packageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                double grossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));
                int minimumOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_MINIMUM_ORDER_QTY));
                int incrementalOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_INCREMENTAL_ORDER_QTY));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                String itemPicUrl = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                String remarks = cursor.getString(cursor.getColumnIndex(KEY_REMARKS));
                String lastUpdateDateTime = cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATE_DATE_TIME));
                int isRegistered = cursor.getInt(cursor.getColumnIndex(KEY_IS_REGISTERED));
                String orderTxnID = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TXN_ID));
                String orderDateTime = cursor.getString(cursor.getColumnIndex(KEY_ORDER_DATE_TIME));
                String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                String pricingGroup = cursor.getString(cursor.getColumnIndex(KEY_PRICING_GROUP));
                String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                int isBulk = cursor.getInt(cursor.getColumnIndex(KEY_IS_BULK));
                listShoppingCartsQueue.add(new ShoppingCartsQueue(dateTimeAdded, customerType, customerID, IMEI, catClass, itemCode, packageCode, price, grossPrice, quantity, minimumOrderQTY, incrementalOrderQTY, totalAmount, itemPicUrl, remarks, lastUpdateDateTime, isRegistered, orderTxnID, orderDateTime, status, pricingGroup, itemDescription, packageDescription, packSize, isBulk));
            }
        }
        cursor.close();
        close();

        return listShoppingCartsQueue;
    }

    public boolean isItemSKUExist(String catclass) {
        boolean isSelected;
        open();
        String strsql = "SELECT CatClass FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{catclass});
        isSelected = cursor.getCount() > 0;
        cursor.close();
        close();
        return isSelected;
    }

    public void deleteSKU(String itemcode) {
        open();
        String strsql = "DELETE FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_ITEM_CODE + "=?";
        mDb.execSQL(strsql, new String[]{itemcode});
        close();
    }

    public List<ItemSKU> getAllItemSKU(String catclass, boolean ispromo) {
        List<ItemSKU> listItemSKU = new ArrayList<>();

        open();
        String strsql = "";
        if (ispromo) {
            strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?" + " AND " + KEY_IS_PROMO + "='1'" + " GROUP BY " + KEY_ITEM_CODE;
        } else {
            strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?" + " GROUP BY " + KEY_ITEM_CODE;
        }

        Cursor cursor = mDb.rawQuery(strsql, new String[]{catclass});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String itemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                String catClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String supplierID = cursor.getString(cursor.getColumnIndex(KEY_SUPPLIER_ID));
                String packageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                double grossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));
                String promoDetails = cursor.getString(cursor.getColumnIndex(KEY_PROMO_DETAILS));
                int minimumOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_MINIMUM_ORDER_QTY));
                int incrementalOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_INCREMENTAL_ORDER_QTY));
                String barcode = cursor.getString(cursor.getColumnIndex(KEY_BARCODE));
                String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                String itemPicURL = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                int isBulk = cursor.getInt(cursor.getColumnIndex(KEY_IS_BULK));
                listItemSKU.add(new ItemSKU(itemCode, itemDescription, packSize, category, catClass, supplierID, packageCode, price, grossPrice, isPromo, promoDetails, minimumOrderQTY, incrementalOrderQTY, barcode, packageDescription, itemPicURL, isBulk));

            }
        }
        cursor.close();
        close();

        return listItemSKU;
    }

    //LIMIT
    public List<ItemSKU> getAllItemSKU(String catclass, boolean ispromo, String limit) {
        List<ItemSKU> listItemSKU = new ArrayList<>();
        open();
        String strsql = "";
        if (ispromo) {
            strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?" + " AND " + KEY_IS_PROMO + "='1'" + " GROUP BY " + KEY_ITEM_CODE + " LIMIT " + limit + ",30 ";
        } else {
            strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?" + " GROUP BY " + KEY_ITEM_CODE + " LIMIT " + limit + ",30 ";
        }

        Cursor cursor = mDb.rawQuery(strsql, new String[]{catclass});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String itemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                String catClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String supplierID = cursor.getString(cursor.getColumnIndex(KEY_SUPPLIER_ID));
                String packageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                double grossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));
                String promoDetails = cursor.getString(cursor.getColumnIndex(KEY_PROMO_DETAILS));
                int minimumOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_MINIMUM_ORDER_QTY));
                int incrementalOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_INCREMENTAL_ORDER_QTY));
                String barcode = cursor.getString(cursor.getColumnIndex(KEY_BARCODE));
                String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                String itemPicURL = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                int isBulk = cursor.getInt(cursor.getColumnIndex(KEY_IS_BULK));
                listItemSKU.add(new ItemSKU(itemCode, itemDescription, packSize, category, catClass, supplierID, packageCode, price, grossPrice, isPromo, promoDetails, minimumOrderQTY, incrementalOrderQTY, barcode, packageDescription, itemPicURL, isBulk));

            }
        }
        cursor.close();
        close();
        return listItemSKU;
    }

    //FILTER GROUP
    public List<ItemSKU> getAllItemSKU(String catclass, String filtergroup, String filtervalue, boolean ispromo) {
        List<ItemSKU> listItemSKU = new ArrayList<>();

        open();
        String strsql = "";
        Cursor cursor = null;

        if (ispromo) {
            if(filtergroup.equals("CATEGORY")) {
                strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?"
                        + " AND "
                        + KEY_CATEGORY + "=?"
                        + " AND "
                        + KEY_IS_PROMO + "='1'"
                        + " GROUP BY " + KEY_ITEM_CODE;
                cursor = mDb.rawQuery(strsql, new String[]{catclass, filtervalue});
            } else if (filtergroup.equals("SUPPLIER")) {
                strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?"
                        + " AND "
                        + KEY_SUPPLIER_ID + "=?"
                        + " AND "
                        + KEY_IS_PROMO + "='1'"
                        + " GROUP BY " + KEY_ITEM_CODE;
                cursor = mDb.rawQuery(strsql, new String[]{catclass, filtervalue});
            } else {
                strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?"
                        + " AND "
                        + KEY_IS_PROMO + "='1'"
                        + " GROUP BY " + KEY_ITEM_CODE;
                cursor = mDb.rawQuery(strsql, new String[]{catclass});
            }
        } else {

            if(filtergroup.equals("CATEGORY")) {
                strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?"
                        + " AND "
                        + KEY_CATEGORY + "=?"
                        + " GROUP BY " + KEY_ITEM_CODE;
                cursor = mDb.rawQuery(strsql, new String[]{catclass, filtervalue});
            } else if (filtergroup.equals("SUPPLIER")) {
                strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?"
                        + " AND "
                        + KEY_SUPPLIER_ID + "=?"
                        + " GROUP BY " + KEY_ITEM_CODE;
                cursor = mDb.rawQuery(strsql, new String[]{catclass, filtervalue});
            } else {
                strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?" + " GROUP BY " + KEY_ITEM_CODE;
                cursor = mDb.rawQuery(strsql, new String[]{catclass});
            }

        }

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String itemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                String catClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String supplierID = cursor.getString(cursor.getColumnIndex(KEY_SUPPLIER_ID));
                String packageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                double grossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));
                String promoDetails = cursor.getString(cursor.getColumnIndex(KEY_PROMO_DETAILS));
                int minimumOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_MINIMUM_ORDER_QTY));
                int incrementalOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_INCREMENTAL_ORDER_QTY));
                String barcode = cursor.getString(cursor.getColumnIndex(KEY_BARCODE));
                String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                String itemPicURL = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                int isBulk = cursor.getInt(cursor.getColumnIndex(KEY_IS_BULK));
                listItemSKU.add(new ItemSKU(itemCode, itemDescription, packSize, category, catClass, supplierID, packageCode, price, grossPrice, isPromo, promoDetails, minimumOrderQTY, incrementalOrderQTY, barcode, packageDescription, itemPicURL, isBulk));

            }
        }
        cursor.close();
        close();

        return listItemSKU;
    }

    public int countSKU(String catclass, String itemcode, String isbulk) {
        int count = 0;

        if (catclass != null && itemcode != null) {

            open();

            String strsql = "SELECT COUNT(*) as TOTALCOUNT FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?" + " AND " + KEY_ITEM_CODE + "=?" + " AND " + KEY_IS_BULK + "=?" + " ORDER BY " + KEY_IS_BULK + " DESC ";
            Cursor cursor = mDb.rawQuery(strsql, new String[]{catclass, itemcode, isbulk});
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    count = cursor.getInt(cursor.getColumnIndex("TOTALCOUNT"));
                }
            }
            cursor.close();

            close();


        }


        return count;
    }

    public int checkisPromoCounter(String itemcode) {
        int count = 0;

        if (itemcode != null) {
            open();
            Cursor cursor = mDb.rawQuery("" +
                            "SELECT COUNT(*) as COUNTCHECKER FROM " + TABLE_ITEM_SKUS
                            + " WHERE " + KEY_IS_PROMO + "=1"
                            + " AND "
                            + KEY_ITEM_CODE + "=?"

                    , new String[]{itemcode});


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    count = cursor.getInt(cursor.getColumnIndex("COUNTCHECKER"));
                }
            }
            cursor.close();
//            close();
        }

        return count;
    }

    public int checkperPackage(String itemcode, String pack) {
        int count = 0;

        if (itemcode != null) {
            open();
            Cursor cursor = mDb.rawQuery("" +
                            "SELECT COUNT(*) as COUNTCHECKER FROM " + TABLE_ITEM_SKUS
                            + " WHERE " + KEY_IS_PROMO + "=1"
                            + " AND "
                            + KEY_ITEM_CODE + "=?"
                            + " AND "
                            + KEY_PACKAGE_CODE + "=?"

                    , new String[]{itemcode, pack});


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    count = cursor.getInt(cursor.getColumnIndex("COUNTCHECKER"));
                }
            }
            cursor.close();
            close();
        }
        return count;
    }

    public List<ItemSKU> getSkuPackagesByBulk(String catclass, String itemcode, String isbulk) {
        List<ItemSKU> listItemSKU = new ArrayList<>();
        if (catclass != null && itemcode != null) {
            open();
            String strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?" + " AND " + KEY_ITEM_CODE + "=?" + " AND " + KEY_IS_BULK + "=?" + " ORDER BY " + KEY_IS_BULK + " DESC ";
            Cursor cursor = mDb.rawQuery(strsql, new String[]{catclass, itemcode, isbulk});
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String itemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                    String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                    String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                    String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                    String catClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                    String supplierID = cursor.getString(cursor.getColumnIndex(KEY_SUPPLIER_ID));
                    String packageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                    double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                    double grossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                    int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));
                    String promoDetails = cursor.getString(cursor.getColumnIndex(KEY_PROMO_DETAILS));
                    int minimumOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_MINIMUM_ORDER_QTY));
                    int incrementalOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_INCREMENTAL_ORDER_QTY));
                    String barcode = cursor.getString(cursor.getColumnIndex(KEY_BARCODE));
                    String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                    String itemPicURL = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                    int isBulk = cursor.getInt(cursor.getColumnIndex(KEY_IS_BULK));
                    listItemSKU.add(new ItemSKU(itemCode, itemDescription, packSize, category, catClass, supplierID, packageCode, price, grossPrice, isPromo, promoDetails, minimumOrderQTY, incrementalOrderQTY, barcode, packageDescription, itemPicURL, isBulk));
                }
            }
            cursor.close();
            close();
        }
        return listItemSKU;
    }

    public List<ItemSKU> getSkuPackages(String catclass, String itemcode) {
        List<ItemSKU> listItemSKU = new ArrayList<>();
        if (catclass != null && itemcode != null) {
            open();
            String strsql = "SELECT * FROM " + TABLE_ITEM_SKUS + " WHERE " + KEY_CAT_CLASS + "=?" + " AND " + KEY_ITEM_CODE + "=?" + " ORDER BY " + KEY_IS_BULK + " DESC ";
            Cursor cursor = mDb.rawQuery(strsql, new String[]{catclass, itemcode});
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String itemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                    String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                    String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                    String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                    String catClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                    String supplierID = cursor.getString(cursor.getColumnIndex(KEY_SUPPLIER_ID));
                    String packageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                    double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                    double grossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                    int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));
                    String promoDetails = cursor.getString(cursor.getColumnIndex(KEY_PROMO_DETAILS));
                    int minimumOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_MINIMUM_ORDER_QTY));
                    int incrementalOrderQTY = cursor.getInt(cursor.getColumnIndex(KEY_INCREMENTAL_ORDER_QTY));
                    String barcode = cursor.getString(cursor.getColumnIndex(KEY_BARCODE));
                    String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                    String itemPicURL = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                    int isBulk = cursor.getInt(cursor.getColumnIndex(KEY_IS_BULK));
                    listItemSKU.add(new ItemSKU(itemCode, itemDescription, packSize, category, catClass, supplierID, packageCode, price, grossPrice, isPromo, promoDetails, minimumOrderQTY, incrementalOrderQTY, barcode, packageDescription, itemPicURL, isBulk));
                    Log.d("itemhttp", "DESC:" + itemDescription);
                }

            }

            cursor.close();
            close();
        }
        return listItemSKU;
    }

    public List<CategoryItems> getCategoryItems(String category) {
        List<CategoryItems> listCategoryItems = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + TABLE_CATEGORIES_ITEMS_REGULAR + " WHERE " + KEY_CATEGORY + "=?";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{category});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String Category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                String CatClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String Description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                String ItemGroupPicURL = cursor.getString(cursor.getColumnIndex(KEY_ITEM_GROUP_PIC_URL));
                int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));
                listCategoryItems.add(new CategoryItems(Category, CatClass, Description, ItemGroupPicURL, isPromo));
            }
        }
        cursor.close();
        close();
        return listCategoryItems;
    }

    public List<CategoryItems> getPromoItems(String promoid) {
        List<CategoryItems> listCategoryItems = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + TABLE_CATEGORIES_ITEMS_PROMOS + " WHERE " + KEY_PROMO_ID + "=?" + " AND " + KEY_IS_PROMO + "='1'";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{promoid});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String Category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                String CatClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String Description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                String ItemGroupPicURL = cursor.getString(cursor.getColumnIndex(KEY_ITEM_GROUP_PIC_URL));
                int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));
                listCategoryItems.add(new CategoryItems(Category, CatClass, Description, ItemGroupPicURL, isPromo));
            }
        }
        cursor.close();
        close();
        return listCategoryItems;
    }

    public List<CategoryItems> getDailyFinds() {

        List<CategoryItems> listCategoryItems = new ArrayList<>();

        String strsql = "SELECT * FROM " + TABLE_CATEGORIES_ITEMS_DAILYFINDS + " ORDER BY " + KEY_DATE_TIME_IN + " DESC ";
        open();
        Cursor cursor = mDb.rawQuery(strsql, new String[]{});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String Category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                String CatClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String Description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                String DateTimeIN = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_IN));
                int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));
                String ItemGroupPicURL = cursor.getString(cursor.getColumnIndex(KEY_ITEM_GROUP_PIC_URL));
                listCategoryItems.add(new CategoryItems(Category, CatClass, Description, DateTimeIN, ItemGroupPicURL, isPromo));
            }
        }
        cursor.close();
        close();

        //close();
        return listCategoryItems;
    }

    //GET CONSUMER_TOTAL_WALLET
    public double getConsumerTotalWallet() {
        double totalwallet = 0;
        open();
        Cursor cursor = mDb.rawQuery("SELECT TotalWallet FROM " + TABLE_CONSUMER_WALLET + " LIMIT 1", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                totalwallet = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_WALLET));
            }
        }
        cursor.close();
        close();
        return totalwallet;
    }

    //GET CONSUMER_DELIVERY_CHARGE
    public List<ConsumerCharge> getDeliveryCharges() {
        List<ConsumerCharge> listcharges = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_DELIVERY_CHARGE, new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String chargeClass = cursor.getString(cursor.getColumnIndex(KEY_CHARGE_CLASS));
                String currencyID = cursor.getString(cursor.getColumnIndex(KEY_CURRENCY_ID));
                double variableFee = cursor.getDouble(cursor.getColumnIndex(KEY_VARIABLE_FEE));
                double baseFee = cursor.getDouble(cursor.getColumnIndex(KEY_BASE_FEE));
                double amountFrom = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT_FROM));
                double amountTo = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT_TO));
                listcharges.add(new ConsumerCharge(chargeClass, currencyID, variableFee, baseFee, amountFrom, amountTo));
            }
        }
        cursor.close();
        close();
        return listcharges;
    }

    public String getConsumerID() {
        String consumerid = null;
        open();
        Cursor cursor = mDb.rawQuery("SELECT ConsumerID FROM " + TABLE_CONSUMER_PROFILE + " LIMIT 1", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                consumerid = cursor.getString(cursor.getColumnIndex(KEY_CONSUMER_ID));
            }
        }
        cursor.close();
        close();
        return consumerid;
    }

    public ConsumerProfile getConsumerProfile() {
        ConsumerProfile item = null;
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_CONSUMER_PROFILE + " LIMIT 1", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String consumerID = cursor.getString(cursor.getColumnIndex(KEY_CONSUMER_ID));
                String facebookID = cursor.getString(cursor.getColumnIndex(KEY_FACEBOOK_ID));
                String userID = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
                String password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
                String consumerType = cursor.getString(cursor.getColumnIndex(KEY_CONSUMER_TYPE));
                String firstName = cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME));
                String birthDate = cursor.getString(cursor.getColumnIndex(KEY_BIRTH_DATE));
                String gender = cursor.getString(cursor.getColumnIndex(KEY_GENDER));
                String occupation = cursor.getString(cursor.getColumnIndex(KEY_OCCUPATION));
                String interest = cursor.getString(cursor.getColumnIndex(KEY_INTEREST));
                String emailAddress = cursor.getString(cursor.getColumnIndex(KEY_EMAIL_ADDRESS));
                int isVerified = cursor.getInt(cursor.getColumnIndex(KEY_IS_VERIFIED));
                String countryCode = cursor.getString(cursor.getColumnIndex(KEY_COUNTRY_CODE));
                String mobileNo = cursor.getString(cursor.getColumnIndex(KEY_MOBILE_NO));
                String IMEI = cursor.getString(cursor.getColumnIndex(KEY_IMEI));
                String profilePicURL = cursor.getString(cursor.getColumnIndex(KEY_PROFILE_PIC_URL));
                String dateTimeRegistered = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_REGISTERED));
                String dateTimeUpdated = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_UPDATED));
                String lastLogin = cursor.getString(cursor.getColumnIndex(KEY_LAST_LOGIN));
                String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                double rewardPoints = cursor.getDouble(cursor.getColumnIndex(KEY_REWARD_POINTS));
                String extra1 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_1));
                String extra2 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_2));
                String extra3 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_3));
                String extra4 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_4));
                String notes1 = cursor.getString(cursor.getColumnIndex(KEY_NOTES_1));
                String notes2 = cursor.getString(cursor.getColumnIndex(KEY_NOTES_2));
                item = new ConsumerProfile(ID, consumerID, facebookID, userID, password, consumerType, firstName, lastName, birthDate, gender, occupation, interest, emailAddress, isVerified, countryCode, mobileNo, IMEI, profilePicURL, dateTimeRegistered, dateTimeUpdated, lastLogin, status, rewardPoints, extra1, extra2, extra3, extra4, notes1, notes2);
            }
        } else {
            item = new ConsumerProfile(0, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null, null, null, null, null, 0, null, null, null, null, null, null);
        }
        cursor.close();
        close();
        return item;
    }

    public ConsumerProfile getConsumerAddressProfile(String ConsumerID) {
        ConsumerProfile item = null;
        open();
        String strsql = "SELECT FirstName,LastName,MobileNo FROM " + TABLE_CONSUMER_PROFILE + " WHERE " + KEY_CONSUMER_ID + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{ConsumerID});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String FirstName = cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME));
                String LastName = cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME));
                String MobileNo = cursor.getString(cursor.getColumnIndex(KEY_MOBILE_NO));
                item = new ConsumerProfile(FirstName, LastName, MobileNo);
            }
        } else {
            item = new ConsumerProfile(null, null, null);
        }
        cursor.close();
        close();
        return item;
    }

    public List<ConsumerAddress> getConsumerAddress() {
        List<ConsumerAddress> listAddress = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_CONSUMER_ADDRESS, new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String ConsumerID = cursor.getString(cursor.getColumnIndex(KEY_CONSUMER_ID));
                String AddressID = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_ID));
                String StreetAddress = cursor.getString(cursor.getColumnIndex(KEY_STREET_ADDRESS));
                String City = cursor.getString(cursor.getColumnIndex(KEY_CITY));
                String Province = cursor.getString(cursor.getColumnIndex(KEY_PROVINCE));
                int Zip = cursor.getInt(cursor.getColumnIndex(KEY_ZIP));
                String Country = cursor.getString(cursor.getColumnIndex(KEY_COUNTRY));
                String Longitude = cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE));
                String Latitude = cursor.getString(cursor.getColumnIndex(KEY_LATITUDE));
                String DateTimeAdded = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_ADDED));
                String DateTimeUpdated = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_UPDATED));
                int isDefault = cursor.getInt(cursor.getColumnIndex(KEY_IS_DEFAULT));
                String Extra1 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_1));
                String Extra2 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_2));
                String Extra3 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_3));
                String Extra4 = cursor.getString(cursor.getColumnIndex(KEY_EXTRA_4));
                String Notes1 = cursor.getString(cursor.getColumnIndex(KEY_NOTES_1));
                String Notes2 = cursor.getString(cursor.getColumnIndex(KEY_NOTES_2));
                listAddress.add(new ConsumerAddress(ID, ConsumerID, AddressID, StreetAddress, City, Province, Zip, Country, Longitude, Latitude, DateTimeAdded, DateTimeUpdated, isDefault, Extra1, Extra2, Extra3, Extra4, Notes1, Notes2));
            }
        }
        cursor.close();
        close();

        return listAddress;
    }

    public List<BankNameReference> getBankNameReferences() {
        List<BankNameReference> listbankreference = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT BankName,BankCode FROM " + TABLE_ADMIN_BANK_DETAILS + " GROUP BY " + KEY_BANK_CODE, new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String BankCode = cursor.getString(cursor.getColumnIndex(KEY_BANK_CODE));
                String BankName = cursor.getString(cursor.getColumnIndex(KEY_BANK_NAME));
                listbankreference.add(new BankNameReference(BankName, BankCode));
            }
        }
        cursor.close();
        close();
        return listbankreference;
    }

    public List<AccountNumberReference> getAccountNumberReferences(String BankCode) {
        List<AccountNumberReference> listaccountnumber = new ArrayList<>();
        open();
        String strsql = "SELECT BankAccountNo,BankAccountName FROM " + TABLE_ADMIN_BANK_DETAILS + " WHERE " + KEY_BANK_CODE + "=?";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{BankCode});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String BankAccountName = cursor.getString(cursor.getColumnIndex(KEY_BANK_ACCOUNT_NAME));
                String BankAccountNumber = cursor.getString(cursor.getColumnIndex(KEY_BANK_ACCOUNT_NO));
                listaccountnumber.add(new AccountNumberReference(BankAccountName, BankAccountNumber));
            }
        }
        cursor.close();
        close();
        return listaccountnumber;
    }

    public void insertOrdersHistory(OrdersQueue order, String usertype) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_ID, order.getID());
            contentValues.put(KEY_DATE_TIME_IN, order.getDateTimeIN());
            contentValues.put(KEY_ORDER_TXN_ID, order.getOrderTxnID());
            contentValues.put(KEY_ORDER_DATE_TIME, order.getOrderDateTime());
            contentValues.put(KEY_COMPLETED_DATE_TIME, order.getCompletedDateTime());
            contentValues.put(KEY_ORDER_TYPE, order.getOrderType());
            contentValues.put(KEY_CUSTOMER_TYPE, order.getCustomerType());
            contentValues.put(KEY_CUSTOMER_ID, order.getCustomerID());
            contentValues.put(KEY_CUSTOMER_NAME, order.getCustomerName());
            contentValues.put(KEY_CUSTOMER_MOBILE_NUMBER, order.getCustomerMobileNumber());
            contentValues.put(KEY_CUSTOMER_EMAIL_ADDRESS, order.getCustomerEmailAddress());
            contentValues.put(KEY_CUSTOMER_DELIVERY_ADDRESS, order.getCustomerDeliveryAddress());
            contentValues.put(KEY_LONGITUDE, order.getLongitude());
            contentValues.put(KEY_LATITUDE, order.getLatitude());
            contentValues.put(KEY_ACCOUNT_TYPE, order.getAccountType());
            contentValues.put(KEY_ACCOUNT_ID, order.getAccountID());
            contentValues.put(KEY_ACCOUNT_USER_ID, order.getAccountUserID());
            contentValues.put(KEY_TOTAL_ITEMS, order.getTotalItems());
            contentValues.put(KEY_TOTAL_ITEM_AMOUNT, order.getTotalItemAmount());
            contentValues.put(KEY_DELIVERY_CHARGE, order.getDeliveryCharge());
            contentValues.put(KEY_TOTAL_AMOUNT, order.getTotalAmount());
            contentValues.put(KEY_ORDER_MEDIUM, order.getOrderMedium());
            contentValues.put(KEY_NO_OF_ROUTE, order.getNoOfRoute());
            contentValues.put(KEY_ROUTE_DATE_TIME, order.getRouteDateTime());
            contentValues.put(KEY_APPROVED_ORDER_DATE_TIME, order.getApprovedOrderDateTime());
            contentValues.put(KEY_PAYMENT_TXN_NO, order.getPaymentTxnNo());
            contentValues.put(KEY_PAYMENT_DATE_TIME, order.getPaymentDateTime());
            contentValues.put(KEY_CONFIRM_PAYMENT_BY, order.getConfirmPaymentBy());
            contentValues.put(KEY_CONFIRM_PAYMENT_DATE_TIME, order.getConfirmPaymentDateTime());
            contentValues.put(KEY_ON_DELIVERY_BY, order.getOnDeliveryBy());
            contentValues.put(KEY_ON_DELIVERY_DATE_TIME, order.getOnDeliveryDateTime());
            contentValues.put(KEY_IS_EXPIRED_ORDER, order.getIsExpiredOrder());
            contentValues.put(KEY_EXPIRED_DATE_TIME, order.getExpiredDateTime());
            contentValues.put(KEY_IS_RE_OPEN_ORDER, order.getIsReOpenOrder());
            contentValues.put(KEY_RE_OPEN_DATE_TIME, order.getReOpenDateTime());
            contentValues.put(KEY_COMPLETED_BY, order.getCompletedBy());
            contentValues.put(KEY_REMARKS, order.getRemarks());
            contentValues.put(KEY_LAST_UPDATE_DATE_TIME, order.getLastUpdateDateTime());
            contentValues.put(KEY_XML_DETAILS, order.getXMLDetails());
            contentValues.put(KEY_CUSTOMER_RATING, order.getCustomerRating());
            contentValues.put(KEY_CUSTOMER_REMARKS, order.getCustomerRemarks());
            contentValues.put(KEY_IS_TAGGED_AS_COMPLETED_BY_CUSTOMER, order.getIsTaggedAsCompletedByConsumer());
            contentValues.put(KEY_IS_REGISTERED, order.getIsRegistered());
            contentValues.put(KEY_IS_ASSIGNED, order.getIsAssigned());
            contentValues.put(KEY_ASSIGNED_BY, order.getAssignedBy());
            contentValues.put(KEY_STATUS, order.getStatus());
            // // // //
            contentValues.put(KEY_TOTAL_ORDER_AMOUNT, order.getTotalOrderAmount());
            contentValues.put(KEY_TOTAL_PACKAGE_AMOUNT, order.getTotalPackageAmount());
            // // // //
            contentValues.put(KEY_CAT_CLASS, order.getCatClass());
            contentValues.put(KEY_ITEM_CODE, order.getItemCode());
            contentValues.put(KEY_PACKAGE_CODE, order.getPackageCode());
            contentValues.put(KEY_PRICE, order.getPrice());
            contentValues.put(KEY_GROSS_PRICE, order.getGrossPrice());
            contentValues.put(KEY_QUANTITY, order.getQuantity());
            contentValues.put(KEY_QUANTITY_SERVED, order.getQuantityServed());
            if (usertype.equals("CONSUMER")) {
                String itemcode = Normalizer.normalize(order.getItemCode(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
                contentValues.put(KEY_ITEM_PIC_URL, order.getItemPicUrl() == null ? RetrofitBuild.S3_URL_RETAILER.concat((itemcode.replaceAll("\\s+", "") + ".png").toLowerCase()) : order.getItemPicUrl());
            } else if (usertype.equals("WHOLESALER")) {
                String itemcode = Normalizer.normalize(order.getItemCode(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
                contentValues.put(KEY_ITEM_PIC_URL, order.getItemPicUrl() == null ? RetrofitBuild.S3_URL_WHOLESALER.concat((itemcode.replaceAll("\\s+", "") + ".png").toLowerCase()) : order.getItemPicUrl());
            }
            contentValues.put(KEY_ADDED_DATE_TIME, order.getAddedDateTime());
            contentValues.put(KEY_ITEM_DESCRIPTION, order.getItemDescription());
            contentValues.put(KEY_PACK_SIZE, order.getPackSize());
            contentValues.put(KEY_PACKAGE_DESCRIPTION, order.getPackageDescription());
            contentValues.put(KEY_PAYMENTTYPE,order.getPaymentType());
            open();
            mDb.insert(TABLE_ORDERS_HISTORY, null, contentValues);
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertOrdersQueue(OrdersQueue order, String usertype) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, order.getID());
        contentValues.put(KEY_DATE_TIME_IN, order.getDateTimeIN());
        contentValues.put(KEY_ORDER_TXN_ID, order.getOrderTxnID());
        contentValues.put(KEY_ORDER_DATE_TIME, order.getOrderDateTime());
        contentValues.put(KEY_COMPLETED_DATE_TIME, order.getCompletedDateTime());
        contentValues.put(KEY_ORDER_TYPE, order.getOrderType());
        contentValues.put(KEY_CUSTOMER_TYPE, order.getCustomerType());
        contentValues.put(KEY_CUSTOMER_ID, order.getCustomerID());
        contentValues.put(KEY_CUSTOMER_NAME, order.getCustomerName());
        contentValues.put(KEY_CUSTOMER_MOBILE_NUMBER, order.getCustomerMobileNumber());
        contentValues.put(KEY_CUSTOMER_EMAIL_ADDRESS, order.getCustomerEmailAddress());
        contentValues.put(KEY_CUSTOMER_DELIVERY_ADDRESS, order.getCustomerDeliveryAddress());
        contentValues.put(KEY_LONGITUDE, order.getLongitude());
        contentValues.put(KEY_LATITUDE, order.getLatitude());
        contentValues.put(KEY_ACCOUNT_TYPE, order.getAccountType());
        contentValues.put(KEY_ACCOUNT_ID, order.getAccountID());
        contentValues.put(KEY_ACCOUNT_USER_ID, order.getAccountUserID());
        contentValues.put(KEY_TOTAL_ITEMS, order.getTotalItems());
        contentValues.put(KEY_TOTAL_ITEM_AMOUNT, order.getTotalItemAmount());
        contentValues.put(KEY_DELIVERY_CHARGE, order.getDeliveryCharge());
        contentValues.put(KEY_TOTAL_AMOUNT, order.getTotalAmount());
        contentValues.put(KEY_ORDER_MEDIUM, order.getOrderMedium());
        contentValues.put(KEY_NO_OF_ROUTE, order.getNoOfRoute());
        contentValues.put(KEY_ROUTE_DATE_TIME, order.getRouteDateTime());
        contentValues.put(KEY_APPROVED_ORDER_DATE_TIME, order.getApprovedOrderDateTime());
        contentValues.put(KEY_PAYMENT_TXN_NO, order.getPaymentTxnNo());
        contentValues.put(KEY_PAYMENT_DATE_TIME, order.getPaymentDateTime());
        contentValues.put(KEY_CONFIRM_PAYMENT_BY, order.getConfirmPaymentBy());
        contentValues.put(KEY_CONFIRM_PAYMENT_DATE_TIME, order.getConfirmPaymentDateTime());
        contentValues.put(KEY_ON_DELIVERY_BY, order.getOnDeliveryBy());
        contentValues.put(KEY_ON_DELIVERY_DATE_TIME, order.getOnDeliveryDateTime());
        contentValues.put(KEY_IS_EXPIRED_ORDER, order.getIsExpiredOrder());
        contentValues.put(KEY_EXPIRED_DATE_TIME, order.getExpiredDateTime());
        contentValues.put(KEY_IS_RE_OPEN_ORDER, order.getIsReOpenOrder());
        contentValues.put(KEY_RE_OPEN_DATE_TIME, order.getReOpenDateTime());
        contentValues.put(KEY_COMPLETED_BY, order.getCompletedBy());
        contentValues.put(KEY_REMARKS, order.getRemarks());
        contentValues.put(KEY_LAST_UPDATE_DATE_TIME, order.getLastUpdateDateTime());
        contentValues.put(KEY_XML_DETAILS, order.getXMLDetails());
        contentValues.put(KEY_CUSTOMER_RATING, order.getCustomerRating());
        contentValues.put(KEY_CUSTOMER_REMARKS, order.getCustomerRemarks());
        contentValues.put(KEY_IS_TAGGED_AS_COMPLETED_BY_CUSTOMER, order.getIsTaggedAsCompletedByConsumer());
        contentValues.put(KEY_IS_REGISTERED, order.getIsRegistered());
        contentValues.put(KEY_IS_ASSIGNED, order.getIsAssigned());
        contentValues.put(KEY_ASSIGNED_BY, order.getAssignedBy());
        contentValues.put(KEY_STATUS, order.getStatus());
        // // // //
        contentValues.put(KEY_TOTAL_ORDER_AMOUNT, order.getTotalOrderAmount());
        contentValues.put(KEY_TOTAL_PACKAGE_AMOUNT, order.getTotalPackageAmount());
        // // // //
        contentValues.put(KEY_CAT_CLASS, order.getCatClass());
        contentValues.put(KEY_ITEM_CODE, order.getItemCode());
        contentValues.put(KEY_PACKAGE_CODE, order.getPackageCode());
        contentValues.put(KEY_PRICE, order.getPrice());
        contentValues.put(KEY_GROSS_PRICE, order.getGrossPrice());
        contentValues.put(KEY_QUANTITY, order.getQuantity());
        contentValues.put(KEY_QUANTITY_SERVED, order.getQuantityServed());
        if (usertype.equals("CONSUMER")) {
            String itemcode = Normalizer.normalize(order.getItemCode(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            contentValues.put(KEY_ITEM_PIC_URL, order.getItemPicUrl() == null ? RetrofitBuild.S3_URL_RETAILER.concat((itemcode.replaceAll("\\s+", "") + ".png").toLowerCase()) : order.getItemPicUrl());
        } else if (usertype.equals("WHOLESALER")) {
            String itemcode = Normalizer.normalize(order.getItemCode(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            contentValues.put(KEY_ITEM_PIC_URL, order.getItemPicUrl() == null ? RetrofitBuild.S3_URL_WHOLESALER.concat((itemcode.replaceAll("\\s+", "") + ".png").toLowerCase()) : order.getItemPicUrl());
        }
        contentValues.put(KEY_ADDED_DATE_TIME, order.getAddedDateTime());
        contentValues.put(KEY_ITEM_DESCRIPTION, order.getItemDescription());
        contentValues.put(KEY_PACK_SIZE, order.getPackSize());
        contentValues.put(KEY_PACKAGE_DESCRIPTION, order.getPackageDescription());
        contentValues.put(KEY_PAYMENTTYPE,order.getPaymentType());
        open();
        mDb.insert(TABLE_ORDERS_QUEUE, null, contentValues);
        close();
    }

    //TABLE_ORDERS_QUEUE
    public List<OrdersQueue> getOrders(String orderType) {
        List<OrdersQueue> listOrder = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + (orderType.equals("PENDING") ? TABLE_ORDERS_QUEUE : TABLE_ORDERS_HISTORY) + " GROUP BY " + KEY_ORDER_TXN_ID + " ORDER BY " + KEY_COMPLETED_DATE_TIME + " DESC ", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String DateTimeIN = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_IN));
                String OrderTxnID = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TXN_ID));
                String OrderDateTime = cursor.getString(cursor.getColumnIndex(KEY_ORDER_DATE_TIME));
                String CompleteDateTime = cursor.getString(cursor.getColumnIndex(KEY_COMPLETED_DATE_TIME));
                String OrderType = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TYPE));
                String CustomerType = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TYPE));
                String CustomerID = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ID));
                String CustomerName = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME));
                String CustomerMobileNumber = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_MOBILE_NUMBER));
                String CustomerEmailAddress = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_EMAIL_ADDRESS));
                String CustomerDeliveryAddress = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_DELIVERY_ADDRESS));
                String Longitude = cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE));
                String Latitude = cursor.getString(cursor.getColumnIndex(KEY_LATITUDE));
                String AccountType = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_TYPE));
                String AccountID = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_ID));
                String AccountUserID = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_USER_ID));
                int TotalItems = cursor.getInt(cursor.getColumnIndex(KEY_TOTAL_ITEMS));
                double TotalItemAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_ITEM_AMOUNT));
                double DeliveryCharge = cursor.getDouble(cursor.getColumnIndex(KEY_DELIVERY_CHARGE));
                double TotalAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                String OrderMedium = cursor.getString(cursor.getColumnIndex(KEY_ORDER_MEDIUM));
                int NoOfRoute = cursor.getInt(cursor.getColumnIndex(KEY_NO_OF_ROUTE));
                String RouteDateTime = cursor.getString(cursor.getColumnIndex(KEY_ROUTE_DATE_TIME));
                String ApprovedOrderDateTime = cursor.getString(cursor.getColumnIndex(KEY_APPROVED_ORDER_DATE_TIME));
                String PaymentTxnNo = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_TXN_NO));
                String PaymentDateTime = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_DATE_TIME));
                String ConfirmPaymentBy = cursor.getString(cursor.getColumnIndex(KEY_CONFIRM_PAYMENT_BY));
                String ConfirmPaymentDateTime = cursor.getString(cursor.getColumnIndex(KEY_CONFIRM_PAYMENT_DATE_TIME));
                String OnDeliveryBy = cursor.getString(cursor.getColumnIndex(KEY_ON_DELIVERY_BY));
                String OnDeliveryDateTime = cursor.getString(cursor.getColumnIndex(KEY_ON_DELIVERY_DATE_TIME));
                int isExpiredOrder = cursor.getInt(cursor.getColumnIndex(KEY_IS_EXPIRED_ORDER));
                String ExpiredDateTime = cursor.getString(cursor.getColumnIndex(KEY_EXPIRED_DATE_TIME));
                int isReOpenOrder = cursor.getInt(cursor.getColumnIndex(KEY_IS_RE_OPEN_ORDER));
                String ReOpenDateTime = cursor.getString(cursor.getColumnIndex(KEY_RE_OPEN_DATE_TIME));
                String CompletedBy = cursor.getString(cursor.getColumnIndex(KEY_COMPLETED_BY));
                String Remarks = cursor.getString(cursor.getColumnIndex(KEY_REMARKS));
                String LastUpdateDateTime = cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATE_DATE_TIME));
                String XMLDetails = cursor.getString(cursor.getColumnIndex(KEY_XML_DETAILS));
                String CustomerRating = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_RATING));
                String CustomerRemarks = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_REMARKS));
                int isTaggedAsCompletedByConsumer = cursor.getInt(cursor.getColumnIndex(KEY_IS_TAGGED_AS_COMPLETED_BY_CUSTOMER));
                int isRegistered = cursor.getInt(cursor.getColumnIndex(KEY_IS_REGISTERED));
                int isAssigned = cursor.getInt(cursor.getColumnIndex(KEY_IS_ASSIGNED));
                String AssignedBy = cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_BY));
                String Status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                // // // //
                String CatClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String ItemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                String PackageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                double Price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                double GrossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                int Quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));
                int QuantityServed = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY_SERVED));
                String ItemPicUrl = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                String AddedDateTime = cursor.getString(cursor.getColumnIndex(KEY_ADDED_DATE_TIME));
                // // // //
                double TotalOrderAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_ORDER_AMOUNT));
                double TotalPackageAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_PACKAGE_AMOUNT));
                String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                String paymenttype = cursor.getString(cursor.getColumnIndex(KEY_PAYMENTTYPE));

                listOrder.add(new OrdersQueue(ID, DateTimeIN, OrderTxnID, OrderDateTime, CompleteDateTime, OrderType, CustomerType, CustomerID, CustomerName, CustomerMobileNumber,
                        CustomerEmailAddress, CustomerDeliveryAddress, Longitude, Latitude, AccountType, AccountID, AccountUserID, TotalItems,
                        TotalItemAmount, DeliveryCharge, TotalAmount, OrderMedium, NoOfRoute, RouteDateTime, ApprovedOrderDateTime, PaymentTxnNo, PaymentDateTime,
                        ConfirmPaymentBy, ConfirmPaymentDateTime, OnDeliveryBy, OnDeliveryDateTime, isExpiredOrder, ExpiredDateTime, isReOpenOrder,
                        ReOpenDateTime, CompletedBy, Remarks, LastUpdateDateTime, XMLDetails, CustomerRating, CustomerRemarks, isTaggedAsCompletedByConsumer,
                        isRegistered, isAssigned, AssignedBy, Status, CatClass, ItemCode, PackageCode, Price, GrossPrice,
                        Quantity, QuantityServed, ItemPicUrl, AddedDateTime, TotalOrderAmount, TotalPackageAmount, itemDescription, packSize, packageDescription,paymenttype));
            }
        }
        cursor.close();
        close();
        return listOrder;
    }

    public List<String> getOrderImages(String orderType, String orderTxnID) {
        List<String> listimages = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + (orderType.equals("PENDING") ? TABLE_ORDERS_QUEUE : TABLE_ORDERS_HISTORY) + " WHERE " + KEY_ORDER_TXN_ID + "=?" + " ORDER BY " + KEY_ORDER_DATE_TIME + " DESC ";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{orderTxnID});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String ItemPicUrl = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                listimages.add(ItemPicUrl);
            }
        }
        cursor.close();
        close();
        return listimages;
    }

    public List<OrdersQueue> getOrderDetailsForTxnId(String orderType, String orderTxnID) {
        String status = orderType.replaceAll("\\s+", "").toUpperCase();
        List<OrdersQueue> listOrder = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + (status.equals("PENDING") ? TABLE_ORDERS_QUEUE : TABLE_ORDERS_HISTORY) + " WHERE " + KEY_ORDER_TXN_ID + "=?";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{orderTxnID});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String DateTimeIN = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_IN));
                String OrderTxnID = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TXN_ID));
                String OrderDateTime = cursor.getString(cursor.getColumnIndex(KEY_ORDER_DATE_TIME));
                String CompleteDateTime = cursor.getString(cursor.getColumnIndex(KEY_COMPLETED_DATE_TIME));
                String OrderType = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TYPE));
                String CustomerType = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TYPE));
                String CustomerID = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ID));
                String CustomerName = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME));
                String CustomerMobileNumber = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_MOBILE_NUMBER));
                String CustomerEmailAddress = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_EMAIL_ADDRESS));
                String CustomerDeliveryAddress = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_DELIVERY_ADDRESS));
                String Longitude = cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE));
                String Latitude = cursor.getString(cursor.getColumnIndex(KEY_LATITUDE));
                String AccountType = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_TYPE));
                String AccountID = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_ID));
                String AccountUserID = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_USER_ID));
                int TotalItems = cursor.getInt(cursor.getColumnIndex(KEY_TOTAL_ITEMS));
                double TotalItemAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_ITEM_AMOUNT));
                double DeliveryCharge = cursor.getDouble(cursor.getColumnIndex(KEY_DELIVERY_CHARGE));
                double TotalAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                String OrderMedium = cursor.getString(cursor.getColumnIndex(KEY_ORDER_MEDIUM));
                int NoOfRoute = cursor.getInt(cursor.getColumnIndex(KEY_NO_OF_ROUTE));
                String RouteDateTime = cursor.getString(cursor.getColumnIndex(KEY_ROUTE_DATE_TIME));
                String ApprovedOrderDateTime = cursor.getString(cursor.getColumnIndex(KEY_APPROVED_ORDER_DATE_TIME));
                String PaymentTxnNo = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_TXN_NO));
                String PaymentDateTime = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_DATE_TIME));
                String ConfirmPaymentBy = cursor.getString(cursor.getColumnIndex(KEY_CONFIRM_PAYMENT_BY));
                String ConfirmPaymentDateTime = cursor.getString(cursor.getColumnIndex(KEY_CONFIRM_PAYMENT_DATE_TIME));
                String OnDeliveryBy = cursor.getString(cursor.getColumnIndex(KEY_ON_DELIVERY_BY));
                String OnDeliveryDateTime = cursor.getString(cursor.getColumnIndex(KEY_ON_DELIVERY_DATE_TIME));
                int isExpiredOrder = cursor.getInt(cursor.getColumnIndex(KEY_IS_EXPIRED_ORDER));
                String ExpiredDateTime = cursor.getString(cursor.getColumnIndex(KEY_EXPIRED_DATE_TIME));
                int isReOpenOrder = cursor.getInt(cursor.getColumnIndex(KEY_IS_RE_OPEN_ORDER));
                String ReOpenDateTime = cursor.getString(cursor.getColumnIndex(KEY_RE_OPEN_DATE_TIME));
                String CompletedBy = cursor.getString(cursor.getColumnIndex(KEY_COMPLETED_BY));
                String Remarks = cursor.getString(cursor.getColumnIndex(KEY_REMARKS));
                String LastUpdateDateTime = cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATE_DATE_TIME));
                String XMLDetails = cursor.getString(cursor.getColumnIndex(KEY_XML_DETAILS));
                String CustomerRating = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_RATING));
                String CustomerRemarks = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_REMARKS));
                int isTaggedAsCompletedByConsumer = cursor.getInt(cursor.getColumnIndex(KEY_IS_TAGGED_AS_COMPLETED_BY_CUSTOMER));
                int isRegistered = cursor.getInt(cursor.getColumnIndex(KEY_IS_REGISTERED));
                int isAssigned = cursor.getInt(cursor.getColumnIndex(KEY_IS_ASSIGNED));
                String AssignedBy = cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_BY));
                String Status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                // // // //
                String CatClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String ItemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                String PackageCode = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CODE));
                double Price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                double GrossPrice = cursor.getDouble(cursor.getColumnIndex(KEY_GROSS_PRICE));
                int Quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));
                int QuantityServed = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY_SERVED));
                String ItemPicUrl = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                String AddedDateTime = cursor.getString(cursor.getColumnIndex(KEY_ADDED_DATE_TIME));
                // // // //
                double TotalOrderAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_ORDER_AMOUNT));
                double TotalPackageAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_PACKAGE_AMOUNT));
                String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                String packSize = cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE));
                String packageDescription = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION));
                String paymenttype = cursor.getString(cursor.getColumnIndex(KEY_PAYMENTTYPE));
                listOrder.add(new OrdersQueue(ID, DateTimeIN, OrderTxnID, OrderDateTime, CompleteDateTime, OrderType, CustomerType, CustomerID, CustomerName, CustomerMobileNumber,
                        CustomerEmailAddress, CustomerDeliveryAddress, Longitude, Latitude, AccountType, AccountID, AccountUserID, TotalItems,
                        TotalItemAmount, DeliveryCharge, TotalAmount, OrderMedium, NoOfRoute, RouteDateTime, ApprovedOrderDateTime, PaymentTxnNo, PaymentDateTime,
                        ConfirmPaymentBy, ConfirmPaymentDateTime, OnDeliveryBy, OnDeliveryDateTime, isExpiredOrder, ExpiredDateTime, isReOpenOrder,
                        ReOpenDateTime, CompletedBy, Remarks, LastUpdateDateTime, XMLDetails, CustomerRating, CustomerRemarks, isTaggedAsCompletedByConsumer,
                        isRegistered, isAssigned, AssignedBy, Status, CatClass, ItemCode, PackageCode, Price, GrossPrice,
                        Quantity, QuantityServed, ItemPicUrl, AddedDateTime, TotalOrderAmount, TotalPackageAmount, itemDescription, packSize, packageDescription,paymenttype));
            }
        }
        cursor.close();
        close();
        return listOrder;
    }

    public boolean isCategoriesTableEmpty() {
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_CATEGORIES, null);

        return cursor.getCount() <= 0;
    }

    public boolean isCategoryExist(String Category) {
        boolean isSelected;
        open();
        String strsql = "SELECT Category FROM " + TABLE_CATEGORIES_ITEMS_REGULAR + " WHERE " + KEY_CATEGORY + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{Category});
        isSelected = cursor.getCount() > 0;
        cursor.close();
        close();
        return isSelected;
    }

    public List<Category> getCategories() {
        List<Category> listCategory = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_CATEGORIES + " ORDER BY " + KEY_DESCRIPTION + " ASC ", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String Category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                String Description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                listCategory.add(new Category(Category, Description));
            }
        }
        cursor.close();
        close();

        return listCategory;
    }

    public void insertAllBranches(Branches branches, double distance) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NETWORK_ID, branches.getNetworkID());
        contentValues.put(KEY_BRANCH_ID, branches.getBranchID());
        contentValues.put(KEY_BRANCH_NAME, branches.getBranchName());
        contentValues.put(KEY_BRANCH_TYPE, branches.getBranchType());
        contentValues.put(KEY_BRANCH_GROUP, branches.getBranchGroup());
        contentValues.put(KEY_AUTHORISED_REPRESENTATIVE, branches.getAuthorisedRepresentative());
        contentValues.put(KEY_STREET_ADDRESS, branches.getStreetAddress());
        contentValues.put(KEY_CITY, branches.getCity());
        contentValues.put(KEY_PROVINCE, branches.getProvince());
        contentValues.put(KEY_ZIP, branches.getZip());
        contentValues.put(KEY_COUNTRY, branches.getCountry());
        contentValues.put(KEY_LONGITUDE, branches.getLongitude());
        contentValues.put(KEY_LATITUDE, branches.getLatitude());
        contentValues.put(KEY_AUTHORISED_EMAIL_ADDRESS, branches.getAuthorisedEmailAddress());
        contentValues.put(KEY_AUTHORISED_TEL_NO, branches.getAuthorisedTelNo());
        contentValues.put(KEY_AUTHORISED_COUNTRY_CODE, branches.getAuthorisedCountryCode());
        contentValues.put(KEY_AUTHORISED_MOBILE_NO, branches.getAuthorisedMobileNo());
        contentValues.put(KEY_FAX, branches.getFax());
        contentValues.put(KEY_CURRENT_ORDER_COUNT, branches.getCurrentOrderCount());
        contentValues.put(KEY_DATE_TIME_ADDED, branches.getDateTimeAdded());
        contentValues.put(KEY_STATUS, branches.getStatus());
        contentValues.put(KEY_CALCULATED_DISTANCE, distance);
        contentValues.put(KEY_STOREOPEN,branches.getStoreHoursStart());
        contentValues.put(KEY_STORECLOSE,branches.getStoreHoursEnd());
        mDb.insert(TABLE_BRANCHES, null, contentValues);
        close();
    }

    public List<Branches> getBranches() {
        List<Branches> listBranches = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_BRANCHES + " ORDER BY " + KEY_CALCULATED_DISTANCE + " ASC ", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String NetworkID = cursor.getString(cursor.getColumnIndex(KEY_NETWORK_ID));
                String BranchID = cursor.getString(cursor.getColumnIndex(KEY_BRANCH_ID));
                String BranchName = cursor.getString(cursor.getColumnIndex(KEY_BRANCH_NAME));
                String BranchType = cursor.getString(cursor.getColumnIndex(KEY_BRANCH_TYPE));
                String BranchGroup = cursor.getString(cursor.getColumnIndex(KEY_BRANCH_GROUP));
                String AuthorisedRepresentative = cursor.getString(cursor.getColumnIndex(KEY_AUTHORISED_REPRESENTATIVE));
                String StreetAddress = cursor.getString(cursor.getColumnIndex(KEY_STREET_ADDRESS));
                String City = cursor.getString(cursor.getColumnIndex(KEY_CITY));
                String Province = cursor.getString(cursor.getColumnIndex(KEY_PROVINCE));
                int Zip = cursor.getInt(cursor.getColumnIndex(KEY_ZIP));
                String Country = cursor.getString(cursor.getColumnIndex(KEY_COUNTRY));
                String Longitude = cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE));
                String Latitude = cursor.getString(cursor.getColumnIndex(KEY_LATITUDE));
                String AuthorisedEmailAddress = cursor.getString(cursor.getColumnIndex(KEY_AUTHORISED_EMAIL_ADDRESS));
                String AuthorisedTelNo = cursor.getString(cursor.getColumnIndex(KEY_AUTHORISED_TEL_NO));
                String AuthorisedCountryCode = cursor.getString(cursor.getColumnIndex(KEY_AUTHORISED_COUNTRY_CODE));
                String AuthorisedMobileNo = cursor.getString(cursor.getColumnIndex(KEY_AUTHORISED_MOBILE_NO));
                String Fax = cursor.getString(cursor.getColumnIndex(KEY_FAX));
                int CurrentOrderCount = cursor.getInt(cursor.getColumnIndex(KEY_CURRENT_ORDER_COUNT));
                String DateTimeAdded = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_ADDED));
                String Status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                double CalculatedDistance = cursor.getDouble(cursor.getColumnIndex(KEY_CALCULATED_DISTANCE));
                String storeopen = cursor.getString(cursor.getColumnIndex(KEY_STOREOPEN));
                String storeclose = cursor.getString(cursor.getColumnIndex(KEY_STORECLOSE));
                listBranches.add(new Branches(NetworkID, BranchID, BranchName, BranchType, BranchGroup, AuthorisedRepresentative, StreetAddress, City, Province, Zip, Country, Longitude, Latitude, AuthorisedEmailAddress, AuthorisedTelNo, AuthorisedCountryCode, AuthorisedMobileNo, Fax, CurrentOrderCount, DateTimeAdded, Status, CalculatedDistance,storeopen,storeclose));
            }
        }
        cursor.close();
        close();

        return listBranches;
    }

    public void insertCustomerMyList(MyList myList, String usertype) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CUSTOMER_ID, myList.getCustomerID());
        contentValues.put(KEY_CUSTOMER_TYPE, myList.getCustomerType());
        contentValues.put(KEY_IMEI, myList.getIMEI());
        contentValues.put(KEY_CATEGORY, myList.getCategory());
        contentValues.put(KEY_CAT_CLASS, myList.getCatClass());
        if (usertype.equals("CONSUMER")) {
            String catclass = Normalizer.normalize(myList.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            contentValues.put(KEY_ITEM_PIC_URL, RetrofitBuild.S3_URL_RETAILER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()));
        } else if (usertype.equals("WHOLESALER")) {
            String catclass = Normalizer.normalize(myList.getCatClass(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            contentValues.put(KEY_ITEM_PIC_URL, RetrofitBuild.S3_URL_WHOLESALER.concat((catclass.replaceAll("\\s+", "") + ".png").toLowerCase()));
        }
        contentValues.put(KEY_DATE_TIME_ADDED, myList.getDateTimeAdded());
        contentValues.put(KEY_IS_REGISTERED, myList.getIsRegistered());
        contentValues.put(KEY_DESCRIPTION, myList.getDescription());
        contentValues.put(KEY_ITEM_CODE, myList.getItemCode());
        contentValues.put(KEY_ITEM_DESCRIPTION, myList.getItemDescription());
        contentValues.put(KEY_TYPE, myList.getType());
        mDb.insert(TABLE_MY_LIST, null, contentValues);
        close();
    }

    public List<MyList> getCustomerMyList() {
        List<MyList> myList = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_MY_LIST, new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String CustomerID = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ID));
                String CustomerType = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_TYPE));
                String IMEI = cursor.getString(cursor.getColumnIndex(KEY_IMEI));
                String Category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                String CatClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String ItemPicUrl = cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC_URL));
                String DateTimeAdded = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_ADDED));
                int isRegistered = cursor.getInt(cursor.getColumnIndex(KEY_IS_REGISTERED));
                String Description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                String itemCode = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));
                String itemDescription = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                String type = cursor.getString(cursor.getColumnIndex(KEY_TYPE));

                myList.add(new MyList(CustomerID, CustomerType, IMEI, Category, CatClass,
                        ItemPicUrl, DateTimeAdded, isRegistered, Description, itemCode, itemDescription, type));
            }
        }
        cursor.close();
        close();

        return myList;
    }

    //insert details wholesaler profile
    public void insertWholesalerProfile(WholesalerProfile profile) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_BRANCH_ID, profile.getBranchID());
        contentValues.put(KEY_BRANCH_NAME, profile.getBranchName());
        contentValues.put(KEY_WHOLESALER_ID, profile.getWholesalerID());
        contentValues.put(KEY_USER_ID, profile.getUserID());
        contentValues.put(KEY_USER_PASSWORD, profile.getUserPassword());
        contentValues.put(KEY_PROFILE_PIC_URL, profile.getProfilePicURL());
        contentValues.put(KEY_STATUS, profile.getStatus());
        contentValues.put(KEY_DATE_TIME_ADDED, profile.getDateTimeAdded());
        contentValues.put(KEY_DATE_TIME_UPDATED, profile.getDateTimeUpdated());
        contentValues.put(KEY_IS_ONLINE, profile.getIsOnline());
        contentValues.put(KEY_LAST_NAME, profile.getLastName());
        contentValues.put(KEY_FIRST_NAME, profile.getFirstName());
        contentValues.put(KEY_MIDDLE_NAME, profile.getMiddleName());
        contentValues.put(KEY_COMPANY, profile.getCompany());
        contentValues.put(KEY_GENDER, profile.getGender());
        contentValues.put(KEY_BIRTH_DATE, profile.getBirthdate());
        contentValues.put(KEY_MOBILE_NO, profile.getMobileNo());
        contentValues.put(KEY_EMAIL_ADDRESS, profile.getEmailAddress());
        contentValues.put(KEY_IMEI, profile.getIMEI());
        contentValues.put(KEY_ADDRESS, profile.getAddress());
        contentValues.put(KEY_CITY, profile.getCity());
        contentValues.put(KEY_PROVINCE, profile.getProvince());
        contentValues.put(KEY_IS_CREDIT, profile.getIsCredit());
        contentValues.put(KEY_CREDIT_LINE, profile.getCreditLine());
        contentValues.put(KEY_PRICE_GROUP, profile.getPriceGroup());
        contentValues.put(KEY_IS_PARTNER, profile.getIsPartner());
        contentValues.put(KEY_PARTNER_ID, profile.getPartnerID());
        contentValues.put(KEY_DATE_TIME_PARTNERED, profile.getDateTimePartnered());
        mDb.insert(TABLE_WHOLESALER_PROFILE, null, contentValues);
        close();
    }

    public WholesalerProfile getWholesalerProfile() {
        WholesalerProfile item = null;
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_WHOLESALER_PROFILE + " LIMIT 1", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String branchID = cursor.getString(cursor.getColumnIndex(KEY_BRANCH_ID));
                String branchName = cursor.getString(cursor.getColumnIndex(KEY_BRANCH_NAME));
                String wholesalerID = cursor.getString(cursor.getColumnIndex(KEY_WHOLESALER_ID));
                String userID = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
                String userPassword = cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD));
                String profilePicURL = cursor.getString(cursor.getColumnIndex(KEY_PROFILE_PIC_URL));
                String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                String dateTimeAdded = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_ADDED));
                String dateTimeUpdated = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_UPDATED));
                String lastLogin = cursor.getString(cursor.getColumnIndex(KEY_LAST_LOGIN));
                int isOnline = cursor.getInt(cursor.getColumnIndex(KEY_IS_ONLINE));
                String lastName = cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME));
                String firstName = cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME));
                String middleName = cursor.getString(cursor.getColumnIndex(KEY_MIDDLE_NAME));
                String company = cursor.getString(cursor.getColumnIndex(KEY_COMPANY));
                String gender = cursor.getString(cursor.getColumnIndex(KEY_GENDER));
                String birthdate = cursor.getString(cursor.getColumnIndex(KEY_BIRTH_DATE));
                String mobileNo = cursor.getString(cursor.getColumnIndex(KEY_MOBILE_NO));
                String emailAddress = cursor.getString(cursor.getColumnIndex(KEY_EMAIL_ADDRESS));
                String IMEI = cursor.getString(cursor.getColumnIndex(KEY_IMEI));
                String address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
                String city = cursor.getString(cursor.getColumnIndex(KEY_CITY));
                String province = cursor.getString(cursor.getColumnIndex(KEY_PROVINCE));
                int isCredit = cursor.getInt(cursor.getColumnIndex(KEY_IS_CREDIT));
                double creditLine = cursor.getDouble(cursor.getColumnIndex(KEY_CREDIT_LINE));
                String priceGroup = cursor.getString(cursor.getColumnIndex(KEY_PRICE_GROUP));
                int isPartner = cursor.getInt(cursor.getColumnIndex(KEY_IS_PARTNER));
                String partnerID = cursor.getString(cursor.getColumnIndex(KEY_PARTNER_ID));
                String dateTimePartnered = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_PARTNERED));
                item = new WholesalerProfile(branchID, branchName, wholesalerID, userID, userPassword, profilePicURL, status, dateTimeAdded, dateTimeUpdated, lastLogin, isOnline, lastName, firstName, middleName, company, gender, birthdate, mobileNo, emailAddress, IMEI, address, city, province, isCredit, creditLine, priceGroup, isPartner, partnerID, dateTimePartnered);
            }
        } else {
            item = new WholesalerProfile(null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0, null, 0, null, null);
        }
        cursor.close();
        close();
        return item;
    }

    public void updateWholesalerProfPic(String imageLink, String userid) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_PROFILE_PIC_URL, imageLink);
        mDb.update(TABLE_WHOLESALER_PROFILE, cv, KEY_USER_ID + "='" + userid + "'", null);
        close();
    }

    public void deleteItemList(String catclass) {
        open();
        String strsql = "DELETE FROM " + TABLE_MY_LIST + " WHERE " + KEY_CAT_CLASS + "=?";
        mDb.execSQL(strsql, new String[]{catclass});
        close();
    }

    public String getWholesalerID() {
        String wholesalerid = null;
        open();
        Cursor cursor = mDb.rawQuery("SELECT " + KEY_WHOLESALER_ID + " FROM " + TABLE_WHOLESALER_PROFILE + " LIMIT 1", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                wholesalerid = cursor.getString(cursor.getColumnIndex(KEY_WHOLESALER_ID));
            }
        }
        cursor.close();
        close();
        return wholesalerid;
    }

    //insert all suppliers
    public void insertSuppliers(Supplier info) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, info.getID());
        contentValues.put(KEY_SUPPLIER_ID, info.getSupplierID());
        contentValues.put(KEY_SUPPLIER_NAME, info.getSupplierName());
        contentValues.put(KEY_RANK, info.getRank());
        contentValues.put(KEY_IS_SPONSOR, info.getIsSponsor());
        contentValues.put(KEY_PHOTO_PATH, info.getPhotoPath());
        mDb.insert(TABLE_SUPPLIER, null, contentValues);
        close();
    }

    public List<Supplier> getSuppliers() {
        List<Supplier> supplierist = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_SUPPLIER, new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String supplierID = cursor.getString(cursor.getColumnIndex(KEY_SUPPLIER_ID));
                String supplierName = cursor.getString(cursor.getColumnIndex(KEY_SUPPLIER_NAME));
                int Rank = cursor.getInt(cursor.getColumnIndex(KEY_RANK));
                int isSponsor = cursor.getInt(cursor.getColumnIndex(KEY_IS_SPONSOR));
                String PhotoPath = cursor.getString(cursor.getColumnIndex(KEY_PHOTO_PATH));
                supplierist.add(new Supplier(ID, supplierID, supplierName,
                        Rank, isSponsor, PhotoPath));
            }
        }
        cursor.close();
        close();

        return supplierist;
    }

    public List<SupplierItems> getSupplierItems(String supplierid) {
        List<SupplierItems> supplieritems = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + TABLE_SUPPLIER_ITEMS + " WHERE " + KEY_SUPPLIER_ID + "=?";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{supplierid});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String CatClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                String Category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                String Description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                String SupplierID = cursor.getString(cursor.getColumnIndex(KEY_SUPPLIER_ID));
                String PhotoURL = cursor.getString(cursor.getColumnIndex(KEY_PHOTO_PATH));
                int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));

                supplieritems.add(new SupplierItems(Category, CatClass, SupplierID, Description, PhotoURL, isPromo));
            }
        }
        cursor.close();
        close();

        return supplieritems;
    }

    public boolean isSupplierIdExist(String SupplierID) {
        boolean isSelected;
        open();
        String strsql = "SELECT SupplierID FROM " + TABLE_SUPPLIER_ITEMS + " WHERE " + KEY_SUPPLIER_ID + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{SupplierID});
        isSelected = cursor.getCount() > 0;
        cursor.close();
        close();
        return isSelected;
    }

    public void insertWalletReload(WalletReload walletReload) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, walletReload.getID());
        contentValues.put(KEY_PAYMENT_TXN_NO, walletReload.getPaymentTxnNo());
        contentValues.put(KEY_PARTNER_PAYMENT_TXN_NO, walletReload.getPartnerPaymentTxnNo());
        contentValues.put(KEY_DATE_TIME_IN, walletReload.getDateTimeIN());
        contentValues.put(KEY_DATE_TIME_COMPLETED, walletReload.getDateTimeCompleted());
        contentValues.put(KEY_CONSUMER_TYPE, walletReload.getConsumerType());
        contentValues.put(KEY_CUSTOMER_NAME, walletReload.getConsumerName());
        contentValues.put(KEY_CUSTOMER_MOBILE_NUMBER, walletReload.getConsumerMobileNumber());
        contentValues.put(KEY_AMOUNT_PAID, walletReload.getAmountPaid());
        contentValues.put(KEY_PAYMENT_OPTION, walletReload.getPaymentOption());
        contentValues.put(KEY_PAYMENT_DETAILS, walletReload.getPaymentDetails());
        contentValues.put(KEY_PREACCOUNT_WALLET, walletReload.getPreAccountWallet());
        contentValues.put(KEY_POSTACCOUNT_WALLET, walletReload.getPostAccountWallet());
        contentValues.put(KEY_CUSTOMER_REMARKS, walletReload.getCustomerRemarks());
        contentValues.put(KEY_ACTED_BY, walletReload.getActedBy());
        contentValues.put(KEY_REMARKS, walletReload.getRemarks());
        contentValues.put(KEY_STATUS, walletReload.getStatus());

        mDb.insert(TABLE_WALLET_RELOAD_QUEUE, null, contentValues);
        close();
    }

    public List<WalletReload> getWalletReload() {
        List<WalletReload> walletReload = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_WALLET_RELOAD_QUEUE + " ORDER BY " + KEY_DATE_TIME_IN + " DESC ", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String paymentTxnNo = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_TXN_NO));
                String partnerPaymentTxnNo = cursor.getString(cursor.getColumnIndex(KEY_PARTNER_PAYMENT_TXN_NO));
                String dateTimeIN = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_IN));
                String dateTimeCompleted = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_COMPLETED));
                String consumerID = cursor.getString(cursor.getColumnIndex(KEY_CONSUMER_ID));
                String consumerName = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME));
                String customerMobileNumber = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_MOBILE_NUMBER));
                String amountPaid = cursor.getString(cursor.getColumnIndex(KEY_AMOUNT_PAID));
                String paymentOption = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_OPTION));
                String paymentDetails = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_DETAILS));
                String preAccountWallet = cursor.getString(cursor.getColumnIndex(KEY_PREACCOUNT_WALLET));
                String postAccountWallet = cursor.getString(cursor.getColumnIndex(KEY_POSTACCOUNT_WALLET));
                String customerRemarks = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_REMARKS));
                String actedBy = cursor.getString(cursor.getColumnIndex(KEY_ACTED_BY));
                String remarks = cursor.getString(cursor.getColumnIndex(KEY_REMARKS));
                String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));

                walletReload.add(new WalletReload(ID, paymentTxnNo, partnerPaymentTxnNo, dateTimeIN, dateTimeCompleted, consumerID, consumerName, customerMobileNumber, amountPaid, paymentOption, paymentDetails,
                        preAccountWallet, postAccountWallet, customerRemarks, actedBy, remarks, status));
            }
        }
        cursor.close();
        close();

        return walletReload;
    }
    //THIS IS FOR YOUR EXPANDABLE LIST HEADER

    public ArrayList<WalletReload> getWalletHeader() {
        ArrayList<WalletReload> walletReload = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_WALLET_RELOAD_QUEUE + " GROUP BY " + KEY_DATE_TIME_IN + " ORDER BY " + KEY_DATE_TIME_IN + " DESC", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String DateTimeIN = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_IN));
                String amountPaid = cursor.getString(cursor.getColumnIndex(KEY_AMOUNT_PAID));
                String paymentOption = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_OPTION));
                String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));

                walletReload.add(new WalletReload(0, "", "", DateTimeIN, "", "", "", "", amountPaid, paymentOption, "",
                        "", "", "", "", "", status));
            }
        }
        cursor.close();
        close();
        return walletReload;
    }

    public ArrayList<WalletReload> getWalletChild() {
        ArrayList<WalletReload> walletReload = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + TABLE_WALLET_RELOAD_QUEUE + " WHERE " + KEY_DATE_TIME_IN + " = ? ORDER BY " + KEY_DATE_TIME_IN + " DESC";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String paymentTxnNo = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_TXN_NO));
                String DateTimeIN = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_IN));
                String paymentDetails = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_DETAILS));
                String preAccountWallet = cursor.getString(cursor.getColumnIndex(KEY_PREACCOUNT_WALLET));
                String postAccountWallet = cursor.getString(cursor.getColumnIndex(KEY_POSTACCOUNT_WALLET));

                walletReload.add(new WalletReload(0, paymentTxnNo, DateTimeIN, "", "", "", "", "", "", "", paymentDetails,
                        preAccountWallet, postAccountWallet, "", "", "", ""));
            }
        }
        cursor.close();
        close();
        return walletReload;
    }


    //PROMOS
    public void insertPromos(Promos promos, String usertype) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PROMO_ID, promos.getPromoID());
        contentValues.put(KEY_NAME, promos.getName());
        contentValues.put(KEY_IS_PROMO, promos.getIsPromo());
        contentValues.put(KEY_RANK, promos.getRank());

        if (usertype.equals("CONSUMER")) {
            String promoid = Normalizer.normalize(promos.getPromoID(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = promos.getPromoID() != null ? RetrofitBuild.S3_URL_RETAILER.concat((promoid.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_PHOTO_PATH, pic);
        } else if (usertype.equals("WHOLESALER")) {
            String promoid = Normalizer.normalize(promos.getPromoID(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            String pic = promos.getPromoID() != null ? RetrofitBuild.S3_URL_WHOLESALER.concat((promoid.replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
            contentValues.put(KEY_PHOTO_PATH, pic);
        }

        //contentValues.put(KEY_PHOTO_PATH, promos.getPhotoPath());

        mDb.insert(TABLE_PROMOS, null, contentValues);
        close();
    }

    public List<Promos> getPromos() {
        List<Promos> promosList = new ArrayList<>();
        open();

        String strsql = "SELECT * FROM " + TABLE_PROMOS;

        Cursor cursor = mDb.rawQuery(strsql, new String[]{});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String PromoID = cursor.getString(cursor.getColumnIndex(KEY_PROMO_ID));
                String Name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                int isPromo = cursor.getInt(cursor.getColumnIndex(KEY_IS_PROMO));
                String PhotoPath = cursor.getString(cursor.getColumnIndex(KEY_PHOTO_PATH));
                int rank = cursor.getInt(cursor.getColumnIndex(KEY_RANK));
                promosList.add(new Promos(PromoID, Name, PhotoPath, isPromo, rank));
            }
        }
        cursor.close();
        close();
        return promosList;
    }

    public boolean isPromoExist(String promoid) {
        boolean isSelected;
        open();
        String strsql = "SELECT PromoID FROM " + TABLE_CATEGORIES_ITEMS_PROMOS + " WHERE " + KEY_PROMO_ID + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{promoid});
        isSelected = cursor.getCount() > 0;
        cursor.close();
        close();
        return isSelected;
    }

    //RECENT_SEARCHES
    public void insertRecentSearches(String searchKey) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_RECENT_SEARCH_KEY, searchKey);
        mDb.insert(TABLE_RECENT_SEARCHES, null, contentValues);
        close();
    }

    public List<String> getRecentSearches() {

        List<String> recentSearches = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT t.* FROM (SELECT * FROM " + TABLE_RECENT_SEARCHES + " GROUP BY " + KEY_RECENT_SEARCH_KEY + " ORDER BY _id DESC) t " + " LIMIT 5", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String searchKey = cursor.getString(cursor.getColumnIndex(KEY_RECENT_SEARCH_KEY));
                recentSearches.add(searchKey);
            }
        }
        cursor.close();
        close();
        return recentSearches;
    }

    //Adapter
    public List<CategoryItems> getSuggestiveForKeyWord(String keyword, String searchType) {
        List<CategoryItems> listCategoryItems = new ArrayList<>();
        open();

        if (keyword.contains("'")) {
            keyword = keyword.replace("'", "''");

        }

        keyword = CommonFunctions.replaceEscapeData(keyword);

        switch (searchType) {
            case "PROMOS":
                Cursor cursor = mDb.rawQuery(
                        " SELECT * FROM ("
                                + " SELECT Name AS ResultChecker, PhotoPath AS ResultPhoto FROM " + TABLE_PROMOS
                                + " UNION "
                                + " SELECT ItemDescription AS ResultChecker, ItemPicUrl AS ResultPhoto FROM " + TABLE_ITEM_SKUS
                                + " UNION "
                                + " SELECT Description AS ResultChecker, ItemGroupPicUrl AS ResultPhoto FROM " + TABLE_CATEGORIES_ITEMS_PROMOS

                                + ") AS a "
                                + " WHERE a.ResultChecker LIKE '%" + keyword + "%'"
                                + " GROUP BY a.ResultChecker "
                                + " ORDER BY a.ResultChecker "
                                + " LIMIT 30 "

                        , new String[]{});

                if (!keyword.equals("")) {
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            String Description = cursor.getString(cursor.getColumnIndex("ResultChecker"));
                            String ItemGroupPicURL = cursor.getString(cursor.getColumnIndex("ResultPhoto"));

                            listCategoryItems.add(new CategoryItems(Description, ItemGroupPicURL));

                        }
                    }
                }

                cursor.close();
                break;

            case "CATEGORIES":
                Cursor cursor2 = mDb.rawQuery(
                        " SELECT * FROM ("
                                + " SELECT ItemDescription AS ResultChecker, ItemPicUrl AS ResultPhoto FROM " + TABLE_ITEM_SKUS
                                + " UNION "
                                + " SELECT Description AS ResultChecker, Null FROM " + TABLE_CATEGORIES
                                + " UNION "
                                + " SELECT Description AS ResultChecker, ItemGroupPicUrl AS ResultPhoto FROM " + TABLE_CATEGORIES_ITEMS_REGULAR

                                + ") AS a "
                                + " WHERE a.ResultChecker LIKE '%" + keyword + "%'"
                                + " GROUP BY a.ResultChecker "
                                + " ORDER BY a.ResultChecker "
                                + " LIMIT 30 "

                        , new String[]{});

                if (!keyword.equals("")) {
                    if (cursor2.getCount() > 0) {
                        while (cursor2.moveToNext()) {
                            String Description = cursor2.getString(cursor2.getColumnIndex("ResultChecker"));
                            String ItemGroupPicURL = cursor2.getString(cursor2.getColumnIndex("ResultPhoto"));


                            listCategoryItems.add(new CategoryItems(Description, ItemGroupPicURL));

                        }
                    }
                }

                cursor2.close();
                break;

            case "SUPPLIERS":
                Cursor cursor3 = mDb.rawQuery(
                        " SELECT * FROM ("
                                + " SELECT ItemDescription AS ResultChecker, ItemPicUrl AS ResultPhoto FROM " + TABLE_ITEM_SKUS
                                + " UNION "
                                + " SELECT SupplierName AS ResultChecker, Null FROM " + TABLE_SUPPLIER
                                + " UNION "
                                + " SELECT Description AS ResultChecker, PhotoPath AS ResultPhoto FROM " + TABLE_SUPPLIER_ITEMS

                                + ") AS a "
                                + " WHERE a.ResultChecker LIKE '%" + keyword + "%'"
                                + " GROUP BY a.ResultChecker "
                                + " ORDER BY a.ResultChecker "
                                + " LIMIT 30 "

                        , new String[]{});

                if (!keyword.equals("")) {
                    if (cursor3.getCount() > 0) {
                        while (cursor3.moveToNext()) {
                            String Description = cursor3.getString(cursor3.getColumnIndex("ResultChecker"));
                            String ItemGroupPicURL = cursor3.getString(cursor3.getColumnIndex("ResultPhoto"));

                            listCategoryItems.add(new CategoryItems(Description, ItemGroupPicURL));

                        }
                    }
                }

                cursor3.close();
                break;

            default:
                Cursor cursor4 = mDb.rawQuery(
                        " SELECT * FROM ("
                                + " SELECT Description AS ResultChecker, ItemGroupPicUrl AS ResultPhoto FROM " + TABLE_CATEGORIES_ITEMS_DAILYFINDS
                                + " UNION "
                                + " SELECT Name AS ResultChecker, PhotoPath AS ResultPhoto FROM " + TABLE_PROMOS
                                + " UNION "
                                + " SELECT Description AS ResultChecker, Null FROM " + TABLE_CATEGORIES
                                + " UNION "
                                + " SELECT SupplierName AS ResultChecker, Null FROM " + TABLE_SUPPLIER
                                + " UNION "
                                + " SELECT ItemDescription AS ResultChecker, ItemPicUrl AS ResultPhoto FROM " + TABLE_ITEM_SKUS
                                + " UNION "
                                + " SELECT Description AS ResultChecker, ItemGroupPicUrl AS ResultPhoto FROM " + TABLE_CATEGORIES_ITEMS_REGULAR
                                + " UNION "
                                + " SELECT Description AS ResultChecker, ItemGroupPicUrl AS ResultPhoto FROM " + TABLE_CATEGORIES_ITEMS_PROMOS
                                + " UNION "
                                + " SELECT Description AS ResultChecker, PhotoPath AS ResultPhoto FROM " + TABLE_SUPPLIER_ITEMS

                                + ") AS a "
                                + " WHERE a.ResultChecker LIKE '%" + keyword + "%'"
                                + " GROUP BY a.ResultChecker "
                                + " ORDER BY a.ResultChecker "
                                + " LIMIT 30 "

                        , new String[]{});

                if (!keyword.equals("")) {
                    if (cursor4.getCount() > 0) {
                        while (cursor4.moveToNext()) {
                            String Description = cursor4.getString(cursor4.getColumnIndex("ResultChecker"));
                            String ItemGroupPicURL = cursor4.getString(cursor4.getColumnIndex("ResultPhoto"));

                            listCategoryItems.add(new CategoryItems(Description, ItemGroupPicURL));

                        }
                    }
                }

                cursor4.close();
                break;
        }


        close();

        return listCategoryItems;
    }

    //Set Suggestion
    public List<String> setSuggestionForKeyWord(String keyword) {
        List<String> suggestiveSearches = new ArrayList<>();
        open();

        Cursor cursor = mDb.rawQuery(
                " SELECT * FROM ( "
                        + " SELECT Description AS ResultChecker FROM " + TABLE_CATEGORIES_ITEMS_DAILYFINDS
                        + " UNION "
                        + " SELECT Name AS ResultChecker FROM " + TABLE_PROMOS
                        + " UNION "
                        + " SELECT Description AS ResultChecker FROM " + TABLE_CATEGORIES
                        + " UNION "
                        + " SELECT SupplierName AS ResultChecker FROM " + TABLE_SUPPLIER
                        + " ) AS a "

                        + " WHERE a.ResultChecker LIKE '" + keyword + "%'"
                        + " ORDER BY a.ResultChecker "

                , new String[]{});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String CatClass = cursor.getString(cursor.getColumnIndex(KEY_CAT_CLASS));
                suggestiveSearches.add(CatClass);
            }
        }
        cursor.close();
        close();
        return suggestiveSearches;
    }

    //TABLE_ORDER_PAYMENTS
    public void insertOrderPayments(OrderPayments payments) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ORDER_TXN_ID, payments.getOrderTxnID());
        contentValues.put(KEY_PAYMENT_TXN_NO, payments.getPaymentTxnNo());
        contentValues.put(KEY_PAYMENT_OPTION, payments.getPaymentOption());
        contentValues.put(KEY_TOTAL_AMOUNT, payments.getTotalAmount());
        contentValues.put(KEY_AMOUNT_PAID, payments.getAmountPaid());
        contentValues.put(KEY_DELIVERY_CHARGE, payments.getDeliveryCharge());
        contentValues.put(KEY_PAYMENT_STATUS, payments.getPaymentStatus());
        mDb.insert(TABLE_ORDER_PAYMENTS, null, contentValues);
        close();
    }

    public OrderPayments getOrderPayments(String ordertxnid) {
        OrderPayments item = null;
        open();
        String strsql = "SELECT * FROM " + TABLE_ORDER_PAYMENTS + " WHERE " + KEY_ORDER_TXN_ID + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{ordertxnid});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String paymentTxnNo = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_TXN_NO));
                String orderTxnID = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TXN_ID));
                String paymentOption = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_OPTION));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                double amountPaid = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT_PAID));
                double deliveryCharge = cursor.getDouble(cursor.getColumnIndex(KEY_DELIVERY_CHARGE));
                String paymentStatus = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_STATUS));

                item = new OrderPayments(paymentTxnNo, orderTxnID, paymentOption, totalAmount, amountPaid, deliveryCharge, paymentStatus);
            }
        } else {
            item = new OrderPayments(null, null, null, 0, 0, 0, null);
        }
        cursor.close();
        close();
        return item;
    }

    public List<OrderPayments> getOrderPaymentsList(String ordertxnid) {
        List<OrderPayments> getOrderPayments = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + TABLE_ORDER_PAYMENTS + " WHERE " + KEY_ORDER_TXN_ID + "=?" + " GROUP BY " + KEY_PAYMENT_TXN_NO;
        Cursor cursor = mDb.rawQuery(strsql, new String[]{ordertxnid});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String paymentTxnNo = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_TXN_NO));
                String orderTxnID = cursor.getString(cursor.getColumnIndex(KEY_ORDER_TXN_ID));
                String paymentOption = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_OPTION));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));
                double amountPaid = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT_PAID));
                double deliveryCharge = cursor.getDouble(cursor.getColumnIndex(KEY_DELIVERY_CHARGE));
                String paymentStatus = cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_STATUS));

                getOrderPayments.add(new OrderPayments(paymentTxnNo, orderTxnID, paymentOption, totalAmount, amountPaid, deliveryCharge, paymentStatus));
            }
        }
        cursor.close();
        close();
        return getOrderPayments;
    }

    public double getOrderPaymentsAmountPaid(String ordertxnid) {
        double amountPaid = 0;

        open();

        String strsql = "SELECT SUM(" + KEY_AMOUNT_PAID + ") as TotalAmountPaid FROM " + TABLE_ORDER_PAYMENTS + " WHERE " + KEY_ORDER_TXN_ID + "=?";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{ordertxnid});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                amountPaid = cursor.getDouble(cursor.getColumnIndex("TotalAmountPaid"));
            }
        }
        cursor.close();
        close();

        return amountPaid;
    }

    public boolean isOrderPaymentExist(String ordertxnid) {
        boolean isSelected;
        open();
        String strsql = "SELECT PaymentTxnNo FROM " + TABLE_ORDER_PAYMENTS + " WHERE " + KEY_ORDER_TXN_ID + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{ordertxnid});
        isSelected = cursor.getCount() > 0;
        cursor.close();
        close();
        return isSelected;
    }

    //PROMOPTS
    public void insertPromoPts(PromoPts promoPts, String usertype) {
        open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CUSTOMER_ID, promoPts.getCustomerID());
        contentValues.put(KEY_DATE, promoPts.getDate());
        contentValues.put(KEY_NAME, promoPts.getName());
        if (usertype.equals("CONSUMER")) {
            String promoid = Normalizer.normalize(promoPts.getPromoID(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            contentValues.put(KEY_PHOTO_PATH, RetrofitBuild.S3_URL_RETAILER.concat((promoid.replaceAll("\\s+", "") + ".png").toLowerCase()));
        } else if (usertype.equals("WHOLESALER")) {
            String promoid = Normalizer.normalize(promoPts.getPromoID(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9_-]", "");
            contentValues.put(KEY_PHOTO_PATH, RetrofitBuild.S3_URL_WHOLESALER.concat((promoid.replaceAll("\\s+", "") + ".png").toLowerCase()));
        }
        contentValues.put(KEY_POINTS, promoPts.getPoints());
        contentValues.put(KEY_PROMO_ID, promoPts.getPromoID());

        mDb.insert(TABLE_PROMOPTS, null, contentValues);
        close();
    }

    public List<PromoPts> getPromoPts(String customerid) {
        List<PromoPts> promosList = new ArrayList<>();
        open();
        String strsql = "SELECT * FROM " + TABLE_PROMOPTS + " WHERE " + KEY_CUSTOMER_ID + "=?" + " ORDER BY Date DESC";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{customerid});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String customerID = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ID));
                String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String photoPath = cursor.getString(cursor.getColumnIndex(KEY_PHOTO_PATH));
                double points = cursor.getDouble(cursor.getColumnIndex(KEY_POINTS));
                String promoID = cursor.getString(cursor.getColumnIndex(KEY_PROMO_ID));

                promosList.add(new PromoPts(customerID, date, name, photoPath, points, promoID));
            }
        }
        cursor.close();
        close();
        return promosList;
    }


    public double getTotalPoints(String customerid) {
        double totalPoints = 0;
        open();
        String strsql = "SELECT SUM(Points) as Points FROM " + TABLE_PROMOPTS + " WHERE " + KEY_CUSTOMER_ID + "=?" + " LIMIT 1";
        Cursor cursor = mDb.rawQuery(strsql, new String[]{customerid});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                totalPoints = cursor.getDouble(cursor.getColumnIndex(KEY_POINTS));
            }
        }

        cursor.close();
        close();
        return totalPoints;
    }

    //Notification
    public void insertNotification(Notification notification) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE_TIME_IN, notification.getDateTimeIN());
        contentValues.put(KEY_NOTIFICATION_TNX_NO, notification.getTxnNo());
        contentValues.put(KEY_CHANNEL, notification.getChannel());
        contentValues.put(KEY_SUBJECT, notification.getSubject());
        contentValues.put(KEY_MESSAGE, notification.getMessage());
        contentValues.put(KEY_SENDER, notification.getSender());
        contentValues.put(KEY_SENDER_LOGO, notification.getSenderLogo());
        contentValues.put(KEY_PAYLOAD, notification.getPayLoad());
        contentValues.put(KEY_READ_STATUS, notification.getReadStatus());
        contentValues.put(KEY_CUSTOMER_ID, notification.getCustomerID());
        mDb.insert(TABLE_NOTIFICATION, null, contentValues);
        close();
    }

    public List<Notification> getNotificationList() {
        List<Notification> notificationList = new ArrayList<>();
        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_NOTIFICATION + " ORDER BY DateTimeIN DESC", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String DateTimeIN = cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME_IN));
                String TxnNo = cursor.getString(cursor.getColumnIndex(KEY_NOTIFICATION_TNX_NO));
                String Channel = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL));
                String Subject = cursor.getString(cursor.getColumnIndex(KEY_SUBJECT));
                String Message = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE));
                String Sender = cursor.getString(cursor.getColumnIndex(KEY_SENDER));
                String SenderLogo = cursor.getString(cursor.getColumnIndex(KEY_SENDER_LOGO));
                String PayLoad = cursor.getString(cursor.getColumnIndex(KEY_PAYLOAD));
                String ReadStatus = cursor.getString(cursor.getColumnIndex(KEY_READ_STATUS));
                String CustomerID = cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ID));
                notificationList.add(new Notification(DateTimeIN, TxnNo, Channel, Subject, Message, Sender, SenderLogo, PayLoad, ReadStatus, CustomerID));
            }
        }
        cursor.close();
        close();
        return notificationList;
    }

    public int getUnreadNotificationCount() {
        int count = 0;
        open();
        Cursor cursor = mDb.rawQuery("SELECT ( SELECT COUNT(*) FROM " + TABLE_NOTIFICATION + " WHERE ReadStatus = '0' ) as Count ", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("Count"));
            }
        }
        cursor.close();
        close();
        return count;
    }

    public void updateNotificationStatus(Notification notification) {
        open();
        String strsql = "UPDATE " + TABLE_NOTIFICATION + " SET " + KEY_READ_STATUS + "=?" + " WHERE " + KEY_NOTIFICATION_TNX_NO + "=?";
        mDb.execSQL(strsql, new String[]{"1", notification.getTxnNo()});
        close();
    }

    public void deleteOrderPayment(String ordertxnid) {
        open();

        String strsql = "DELETE FROM " + TABLE_ORDER_PAYMENTS + " WHERE " + KEY_ORDER_TXN_ID + "=?";
        mDb.execSQL(strsql, new String[]{ordertxnid});

        close();
    }

}
