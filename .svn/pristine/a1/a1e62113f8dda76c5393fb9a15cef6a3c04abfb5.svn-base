package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.fullscreenimage.FullScreenActivity;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ItemSKU;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.AddUdateShoppingCartResponse;
import com.ultramega.shop.responses.FetchShoppingCartsQueueResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectVariationActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    private String imei = "";
    private String usertype = "";
    private String authcode = "";
    private String sessionid = "";
    private String customerid = "";
    private String itemcode = "";
    private String itemid = "";
    private String itempicurl = "";
    private int quantity = 1;
    private String userid = "";
    private String packageid = "";
    private double ordered_total_amount = 0;
    private double prod_item_amount = 0;
    private int minimumOrderQuantity;
    private int incrementalOrderQuantity;

    private UltramegaShopUtilities mdb;
    private ItemSKU itemSKU;
    private List<ItemSKU> mPackageData;
    private ItemSKU selectedItemSKU = null;

    //    private TextView packagedescription;
    private TextView item_amount;
    private EditText prod_qty;
    private TextView item_name;
    private TextView item_pack_size;

    private Button btnaddedtocart;
    private Button button_confirm;
    private Button btn_decrease;
    private Button btn_increase;

    private MaterialEditText edtpackages;

    private List<String> listpackages;

    private ImageView skuimage;
    private ImageView imgPackageItem;

    private ImageView item_promo_variation;

    private TextView txvPromoDescription;
    private LinearLayout promoDescriptionLayout;

    private boolean ismylist = false;

    private int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_up_dialog, R.anim.slide_out_down);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_variation);

//        try {
        //==================
        //initialize data
        //==================
        mdb = new UltramegaShopUtilities(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());

        //========================================================
        //GET THE OBJECT DATA PASSED FROM THE LIST
        //========================================================
        itemSKU = getIntent().getParcelableExtra("item");

        Log.d("antonhttp", "SCANNED ITEM: " + new Gson().toJson(itemSKU));

        ismylist = getIntent().getBooleanExtra("ismylist", false);

        itemid = itemSKU.getCatClass();
        itemcode = itemSKU.getItemCode();
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            ConsumerProfile consumerProfile = mdb.getConsumerProfile();
            customerid = consumerProfile.getConsumerID();
            userid = consumerProfile.getUserID();
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();
            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
        }

        //========================================================
        //GET ALL SKU PACKAGES AND STORE IN ARRAYLIST
        //========================================================
        mPackageData = new ArrayList<>();
        mPackageData = mdb.getSkuPackages(itemSKU.getCatClass(), itemSKU.getItemCode());

        if (mPackageData.size() > 0) {
            //========================================================
            //GET THE FIRST SKU PACKAGE BY DEFAULT
            //========================================================
            if(usertype.equals("CONSUMER")){
                for(ItemSKU itemSKUs: mPackageData){
                    if(itemSKUs.getPackageDescription().equals("Piece")){
                        selectedItemSKU = itemSKU;
                        selectedPosition = 1;
                        break;
                    }else if(itemSKUs.getPackageDescription().equals("Case")){
                        selectedItemSKU = itemSKU;
                        selectedPosition = 0;
                    }
                }
            }else{
                selectedItemSKU = mPackageData.get(0);
            }
            packageid = selectedItemSKU.getPackageCode();
            itempicurl = selectedItemSKU.getItemPicURL();
            minimumOrderQuantity = selectedItemSKU.getMinimumOrderQTY() == 0 ? 1 : selectedItemSKU.getMinimumOrderQTY();
            quantity = minimumOrderQuantity;
            incrementalOrderQuantity = selectedItemSKU.getIncrementalOrderQTY() == 0 ? 1 : selectedItemSKU.getIncrementalOrderQTY();
            prod_item_amount = minimumOrderQuantity * selectedItemSKU.getPrice();

            Log.d("okhttp","selectedItemSKU: "+new Gson().toJson(selectedItemSKU));

            //=====================
            //SETUP DATA FOR DIALOG
            //=====================
            listpackages = new ArrayList<>();
            for (ItemSKU itemSKU : mPackageData) {
                listpackages.add(itemSKU.getPackageDescription());
            }
            //SETUP PRODUCT DESCRIPTION
            setUpProductDescription();
            init();
            setupDataDisplay(mPackageData.get(selectedPosition));
        }

//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void init() {
        findViewById(R.id.packageOptionLayout).setOnClickListener(this);
        imgPackageItem = findViewById(R.id.imgPackageItem);
        edtpackages = findViewById(R.id.edtpackages);
        edtpackages.setText(selectedItemSKU.getPackageDescription());
        edtpackages.setOnClickListener(this);
        findViewById(R.id.variation_layout).setOnTouchListener(this);
        skuimage = findViewById(R.id.skuimage);
        skuimage.setOnClickListener(this);
        item_pack_size = findViewById(R.id.item_pack_size);

        if (selectedItemSKU.getPackSize().equals(".") || selectedItemSKU.getPackSize() == null) {
            item_pack_size.setVisibility(View.GONE);
        } else {
            item_pack_size.setVisibility(View.VISIBLE);
            item_pack_size.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", selectedItemSKU.getPackSize()));
        }

        item_name = findViewById(R.id.item_name);
        item_name.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", selectedItemSKU.getItemDescription()));

        TextView item_qty_label = findViewById(R.id.item_qty_label);
        item_qty_label.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "QUANTITY"));
        TextView item_price_label = findViewById(R.id.item_price_label);
        item_price_label.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "TOTAL PRICE"));
        btn_decrease = findViewById(R.id.btn_variation_decrease);
        btn_decrease.setOnClickListener(this);
        btn_increase = findViewById(R.id.btn_variation_increase);
        btn_increase.setOnClickListener(this);
        button_confirm = findViewById(R.id.btnConfirm);
        button_confirm.setOnClickListener(this);
        button_confirm.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "ADD TO CART"));
        btnaddedtocart = findViewById(R.id.btnaddedtocart);
        btnaddedtocart.setOnClickListener(this);
        btnaddedtocart.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "ADDED TO CART"));
        item_amount = findViewById(R.id.item_amount);
        String itemamountvalue = "₱".concat(CommonFunctions.currencyFormatter(String.valueOf(minimumOrderQuantity * selectedItemSKU.getPrice())));
        item_amount.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", itemamountvalue));

        prod_qty = findViewById(R.id.my_variation_quantity);
        prod_qty.setText(String.valueOf(minimumOrderQuantity));

        item_promo_variation = findViewById(R.id.item_promo_variation);

        promoDescriptionLayout = findViewById(R.id.promoDescriptionLayout);
        txvPromoDescription = findViewById(R.id.txvPromoDescription);
        txvPromoDescription.setMovementMethod(new ScrollingMovementMethod());

        //======================
        //SETUP PROMO DESCRIPTION
        //======================
        setUpPromoDescription(selectedItemSKU.getIsPromo());

//        String photoimg = itemSKU.getItemPicURL() == null ? "." : itemSKU.getItemPicURL();
        String photoimg;
        if(itemSKU.getItemPicURL() == null){
            ItemSKU sku = mPackageData.get(selectedPosition);
            photoimg = sku.getItemPicURL();
        }else{
            photoimg = itemSKU.getItemPicURL() == null ? "." : itemSKU.getItemPicURL();
        }
        Log.d("antonhttp", "SCANNED PHOTO: " + photoimg);

        Glide.with(getViewContext())
                .load((photoimg == null ? "." : photoimg))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_um_default_products)
                        .error(R.drawable.ic_um_default_products)
                        .fitCenter())
                .into(skuimage);

        Glide.with(getViewContext())
                .load(photoimg)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_um_default_products)
                        .error(R.drawable.ic_um_default_products)
                        .fitCenter())
                .into(imgPackageItem);


        prod_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.length() < 7) {

                    //update total price
                    int qty = Integer.parseInt(s.toString());
                    prod_qty.removeTextChangedListener(this);
                    changeTotalPrice(qty);
                    setUpItemSavings(selectedItemSKU, qty);
                    prod_qty.addTextChangedListener(this);

                    if (qty >= minimumOrderQuantity && qty % incrementalOrderQuantity == 0) {
                        button_confirm.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_F83832));
                        button_confirm.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "ADD TO CART"));
                        ((EditText) findViewById(R.id.my_variation_quantity)).setTextColor(ContextCompat.getColor(getViewContext(), R.color.color_979797));
                        disableAllButtons(false);
                    } else {
                        button_confirm.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_A9A9A9));
                        button_confirm.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "INVALID QUANTITY"));
                        ((EditText) findViewById(R.id.my_variation_quantity)).setTextColor(ContextCompat.getColor(getViewContext(), R.color.color_F83832));
                        disableButtons(true);
                    }

                } else {
                    button_confirm.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_A9A9A9));
                    button_confirm.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "INVALID QUANTITY"));
                    item_amount.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", CommonFunctions.currencyFormatter("0").concat(" Php")));
                    changeTotalPrice(0);
                    disableButtons(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() < 7) {
                    //
                    int qty = Integer.parseInt(s.toString());
                    prod_qty.removeTextChangedListener(this);
                    prod_qty.setText(String.valueOf(qty));
                    prod_qty.setSelection(prod_qty.getText().length());
                    prod_qty.addTextChangedListener(this);

                }
            }
        });

        //======================
        //SET UP ITEM SAVINGS
        //======================
        setUpItemSavings(selectedItemSKU, 1);

    }

    private void setUpPromoDescription(int isPromo) {

        if (isPromo == 1 && selectedItemSKU.getPromoDetails() != null) {

            if (selectedItemSKU.getPromoDetails().length() > 0) {

                promoDescriptionLayout.setVisibility(View.VISIBLE);
                txvPromoDescription.setVisibility(View.VISIBLE);
                txvPromoDescription.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", selectedItemSKU.getPromoDetails()));

            } else {

                promoDescriptionLayout.setVisibility(View.GONE);
                txvPromoDescription.setVisibility(View.GONE);

            }

        } else {

            promoDescriptionLayout.setVisibility(View.GONE);
            txvPromoDescription.setVisibility(View.GONE);

        }

        int countcheck = mdb.checkperPackage(selectedItemSKU.getItemCode(), selectedItemSKU.getPackageCode());

        if (countcheck > 0) {

            item_promo_variation.setVisibility(View.VISIBLE);

            Glide.with(getViewContext())
                    .load(ContextCompat.getDrawable(getViewContext(), R.drawable.promo_icon_red_box))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.drawable.promo_icon_red_box)
                            .error(R.drawable.promo_icon_red_box)
                            .override(512, 512)
                            .fitCenter())
                    .into(item_promo_variation);

        } else {

            item_promo_variation.setVisibility(View.GONE);

        }

    }

    private void setUpItemSavings(ItemSKU selectedItemSKU, int qty) {
        if (selectedItemSKU.getPrice() == selectedItemSKU.getGrossPrice()) {

            findViewById(R.id.savingsLayout).setVisibility(View.GONE);

        } else {

            findViewById(R.id.savingsLayout).setVisibility(View.VISIBLE);
            String saveAmount = CommonFunctions.currencyFormatter(String.valueOf((selectedItemSKU.getGrossPrice() - selectedItemSKU.getPrice()) * qty)).concat(" Php");
            String savings = "Save " + saveAmount;
            ((TextView) findViewById(R.id.txvSavings)).setText(savings);

        }
    }

    private void shakeQuantityAnimation() {
        final Animation shake = AnimationUtils.loadAnimation(getViewContext(), R.anim.shake);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.quantityBoxLayout).startAnimation(shake);
            }
        }, 670);
    }

    private void disableAllButtons(boolean isEnabled) {
        findViewById(R.id.btn_variation_increase).setEnabled(!isEnabled);
        findViewById(R.id.btn_variation_decrease).setEnabled(!isEnabled);
        findViewById(R.id.btnConfirm).setEnabled(!isEnabled);
    }

    private void disableButtons(boolean isEnabled) {
        //findViewById(R.id.btn_variation_increase).setEnabled(!isEnabled);
        findViewById(R.id.btn_variation_decrease).setEnabled(!isEnabled);
        findViewById(R.id.btnConfirm).setEnabled(!isEnabled);
    }

    private void openPackagesDialog() {

        MaterialDialog.Builder dialog = new MaterialDialog.Builder(getViewContext());
        dialog.title("Package Option");
        dialog.items(listpackages);
        dialog.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                Log.d("okhttp","SELECTED: "+ text.toString());
                edtpackages.setText(text.toString());
                setupDataDisplay(mPackageData.get(position));
                Log.d("okhttp","DATA SELECTED: "+ new Gson().toJson(mPackageData.get(position)));
            }
        });
        dialog.show();
    }

    public void setupDataDisplay(ItemSKU item) {
        selectedItemSKU = null;
        selectedItemSKU = item;

        //======================
        //SETUP PROMO DESCRIPTION
        //======================
        setUpPromoDescription(selectedItemSKU.getIsPromo());

        //======================
        //SET UP ITEM SAVINGS
        //======================
        setUpItemSavings(selectedItemSKU, 1);

        minimumOrderQuantity = item.getMinimumOrderQTY() == 0 ? 1 : item.getMinimumOrderQTY();
        incrementalOrderQuantity = item.getIncrementalOrderQTY() == 0 ? 1 : item.getIncrementalOrderQTY();

        quantity = minimumOrderQuantity;
        itempicurl = item.getItemPicURL();

        Log.d("okhttp","ITEM PIC : "+itempicurl);

        packageid = item.getPackageCode();
        prod_item_amount = minimumOrderQuantity * item.getPrice();

        prod_qty.setText(String.valueOf(minimumOrderQuantity));

        item_name.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", item.getItemDescription()));

        if (item.getPackSize().equals(".") || item.getPackSize() == null) {
            item_pack_size.setVisibility(View.GONE);
        } else {
            item_pack_size.setVisibility(View.VISIBLE);
            item_pack_size.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", item.getPackSize()));
        }

        item_amount.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "₱".concat(CommonFunctions.currencyFormatter(String.valueOf(minimumOrderQuantity * item.getPrice())))));

        //SETUP PRODUCT DESCRIPTION
        setUpProductDescription();
    }

    public void setUpProductDescription() {
        ((TextView) findViewById(R.id.txvMinimumOrder)).setText(String.format("Minimum order is %d.", minimumOrderQuantity));
        ((TextView) findViewById(R.id.txvIncrementalOrder)).setText(String.format("You're only allowed to increase your order by %d.", incrementalOrderQuantity));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context, ItemSKU item, boolean isMyList) {
        Intent intent = new Intent(context, SelectVariationActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("ismylist", isMyList);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnaddedtocart: {
                finish();
                break;
            }
            case R.id.btnConfirm: {
                if (customerid == null) {

                    ordered_total_amount = quantity * selectedItemSKU.getPrice();
                    List<ShoppingCartsQueue> existingitems = mdb.getExistingItemInCartsQueue(itemid, itemcode, packageid);

                    if (existingitems.size() > 0) {
                        quantity = quantity + existingitems.get(0).getQuantity();
                        ordered_total_amount = ordered_total_amount + existingitems.get(0).getTotalAmount();
                        mdb.deleteItemCartsQueue(selectedItemSKU.getItemCode(), selectedItemSKU.getPackageCode());
                    }

                    //=======================
                    //INSERT ITEM TO CART
                    //=======================
                    insertItemToCart();

                    button_confirm.setVisibility(View.GONE);
                    btnaddedtocart.setVisibility(View.VISIBLE);

                    //=======================
                    //refresh badge count icon
                    //=======================
                    ViewShopItemActivity shopItem = ViewShopItemActivity.getInstance();
                    shopItem.setUpBadge(true);

                    //=======================
                    //ANIMATE IMAGE
                    //=======================
                    setUpImageAnimation();

                    btn_decrease.setEnabled(false);
                    btn_increase.setEnabled(false);
                    prod_qty.setEnabled(false);
                } else {

                    //api session
                    createSession(callsession);

                }
                break;
            }
            case R.id.btn_variation_decrease: {

                int qty = Integer.valueOf(prod_qty.getText().toString());

                if (qty > minimumOrderQuantity) {

                    int newVal = qty - incrementalOrderQuantity;
                    changeTotalPrice(newVal);
                    setUpItemSavings(selectedItemSKU, newVal);
                }

                prod_qty.clearFocus();

                break;
            }
            case R.id.btn_variation_increase: {

                if (prod_qty.getText().toString().length() > 0 && prod_qty.getText().toString().length() < 10) {

                    if (Integer.parseInt(prod_qty.getText().toString()) % incrementalOrderQuantity == 0) {
                        int qty = Integer.parseInt(prod_qty.getText().toString());
                        int newVal = qty + incrementalOrderQuantity;
                        changeTotalPrice(newVal);
                        setUpItemSavings(selectedItemSKU, newVal);
                    }

                } else {

                    changeTotalPrice(incrementalOrderQuantity);
                    setUpItemSavings(selectedItemSKU, incrementalOrderQuantity);

                }

                prod_qty.clearFocus();

                break;
            }
            case R.id.edtpackages: {
                openPackagesDialog();
                prod_qty.clearFocus();
                break;
            }
            case R.id.packageOptionLayout: {
                //hide keyboard
                hideSoftKeyboard();
                prod_qty.clearFocus();
                break;
            }
            case R.id.skuimage: {
                FullScreenActivity.start(getViewContext(), itempicurl);
                break;
            }
        }
    }

    private void insertItemToCart() {
        mdb.insertAllShoppingCartsQueue(new ShoppingCartsQueue(String.valueOf(new Date()),
                usertype,
                customerid,
                imei,
                selectedItemSKU.getCatClass(),
                selectedItemSKU.getItemCode(),
                selectedItemSKU.getPackageCode(),
                selectedItemSKU.getPrice(),
                selectedItemSKU.getGrossPrice(),
                quantity,
                minimumOrderQuantity,
                incrementalOrderQuantity,
                ordered_total_amount,
                itempicurl,
                ".",
                String.valueOf(new Date()),
                0,
                ".",
                ".",
                "PENDING",
                ".",
                selectedItemSKU.getItemDescription(),
                selectedItemSKU.getPackageDescription(),
                selectedItemSKU.getPackSize(),
                selectedItemSKU.getIsBulk()));
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                    List<ShoppingCartsQueue> existingitems;
                    existingitems = mdb.getExistingItemInCartsQueue(itemid, itemcode, packageid);
                    if (existingitems.size() > 0) {
                        quantity = quantity + existingitems.get(0).getQuantity();
                    }
                    addItemToCart(callCartSession);
                } else {
                    //hideProgressDialog();
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                //hideProgressDialog();
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            //hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void addItemToCart(Callback<AddUdateShoppingCartResponse> addUpdateCallback) {
        Call<AddUdateShoppingCartResponse> call = RetrofitBuild.addUpdateShoppingCartService(getViewContext())
                .addUpdateShoppingCartCall(imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        itemcode,
                        quantity,
                        userid,
                        packageid,
                        (itempicurl == null ? "." : itempicurl));
        call.enqueue(addUpdateCallback);
    }

    private final Callback<AddUdateShoppingCartResponse> callCartSession = new Callback<AddUdateShoppingCartResponse>() {

        @Override
        public void onResponse(Call<AddUdateShoppingCartResponse> call, Response<AddUdateShoppingCartResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response.body().getStatus(), response.body().getMessage());
            } else {
                //hideProgressDialog();
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<AddUdateShoppingCartResponse> call, Throwable t) {
            //hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void evaluateResponse(String statuscode, String message) {
        switch (statuscode) {
            case "000": {

                refreshShoppingCartsQueue(fetchShoppingSession);

                break;
            }
            case "004": {

                forceLogoutDialog("Invalid User");

                break;
            }
            default: {
                //hideProgressDialog();
                openErrorDialog(message);
                break;
            }
        }
    }

    private final Callback<FetchShoppingCartsQueueResponse> fetchShoppingSession = new Callback<FetchShoppingCartsQueueResponse>() {

        @Override
        public void onResponse(Call<FetchShoppingCartsQueueResponse> call, Response<FetchShoppingCartsQueueResponse> response) {
            //hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                List<ShoppingCartsQueue> shoppingqueue = response.body().getShoppingCartsQueues();
                for (ShoppingCartsQueue queue : shoppingqueue) {
                    mdb.insertAllShoppingCartsQueue(queue);
                }

                button_confirm.setVisibility(View.GONE);
                btnaddedtocart.setVisibility(View.VISIBLE);

                if (!ismylist) {
                    //refresh badge count icon
                    ViewShopItemActivity shopItem = ViewShopItemActivity.getInstance();
                    shopItem.setUpBadge(true);

                    setUpImageAnimation();

                } else {

                }

                btn_decrease.setEnabled(false);
                btn_increase.setEnabled(false);
                prod_qty.setEnabled(false);
            }
        }

        @Override
        public void onFailure(Call<FetchShoppingCartsQueueResponse> call, Throwable t) {
            //hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void setUpImageAnimation() {
        imgPackageItem.setVisibility(View.VISIBLE);
        Animation move = AnimationUtils.loadAnimation(getViewContext(), R.anim.move);
        move.setInterpolator((new AccelerateDecelerateInterpolator()));
        move.setFillAfter(true);
        imgPackageItem.startAnimation(move);
    }

    private void refreshShoppingCartsQueue(Callback<FetchShoppingCartsQueueResponse> refreshShoppingCartsCallback) {
        authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);
        Call<FetchShoppingCartsQueueResponse> callshoppingqueue = RetrofitBuild.fetchShoppingCartsQueueService(getViewContext())
                .fetchShoppingCartsQueueCall(imei,
                        authcode,
                        "0",
                        sessionid,
                        userid,
                        customerid,
                        getCurrentUserType(getViewContext()));
        callshoppingqueue.enqueue(refreshShoppingCartsCallback);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.variation_layout: {
                finish();
                break;
            }
            case R.id.buttons: {
                return false;
            }
        }
        return false;
    }

    private void changeTotalPrice(int newVal) {
        quantity = newVal;
        prod_qty.setText(String.valueOf(newVal));
        double mAmount = newVal * selectedItemSKU.getPrice();
        item_amount.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", CommonFunctions.currencyFormatter(String.valueOf(mAmount)).concat(" Php")));
    }
}
