package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.adapter.InterestRecyclerAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Category;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;
import com.ultramega.shop.pojo.InterestPost;
import com.ultramega.shop.responses.FetchShopCategoriesResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.SaveInterestToBehaviourResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterestActivity extends BaseActivity implements View.OnClickListener {

    //    private ImageView imageView;
    private TextView txv_next;
    private InterestRecyclerAdapter mAdapter;

    private String imei = "";
    private String usertype = "";
    private String sessionid = "";
    private String authcode = "";
    private String otherinterest = "";
    private String pricegroup = "";
    private int limit;

    private UltramegaShopUtilities mdb;

    private NestedScrollView nested_scroll_view;
    private boolean isfirstload = true;
    private boolean isLoading = false;
    private boolean isloadmore = true;
    private boolean isbottomscroll = false;
    private LinearLayout mSmoothProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        this.setFinishOnTouchOutside(false);
        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Interest"));

        TextView interestLabel = findViewById(R.id.interestLabel);
        interestLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Pick a few categories"));
        txv_next = findViewById(R.id.txv_next);
        txv_next.setOnClickListener(this);
        nested_scroll_view = findViewById(R.id.nested_scroll_view);
        mSmoothProgressBar = findViewById(R.id.mSmoothProgressBar);

        //initliaze empty data
        mdb = new UltramegaShopUtilities(getViewContext());
        int mSize = mdb.getCategories().size();
        limit = getLimit(mSize, 30);

        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            pricegroup = ".";
        } else if (usertype.equals("WHOLESALER")) {
            if (mdb.getWholesalerID() != null) {
                pricegroup = mdb.getWholesalerProfile().getPriceGroup();
            } else {
                pricegroup = ".";
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view_interest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new InterestRecyclerAdapter(getViewContext(), usertype);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(mAdapter);

        if (mdb.getCategories().size() == 0) {
            getSession();
        } else {
            updateList();
        }

        nested_scroll_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    isbottomscroll = true;
                    if (isloadmore) {
                        if (!isfirstload) {
                            limit = limit + 30;
                        }
                        getSession();
                    }
                }
            }
        });
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            //api session
            isLoading = true;
            createSession(callcategoriessession);
        } else {
            openErrorDialog(getString(R.string.generic_internet_error_message));
        }
    }

    private final Callback<GetSessionResponse> callcategoriessession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    session = response.body().getSession();
                    getAllCategories();
                } else {
                    isLoading = false;
                    openErrorDialog(response.body().getMessage());
                    mSmoothProgressBar.setVisibility(View.GONE);
                }
            } else {
                //open error dialog message
                isLoading = false;
                openErrorDialog("Something went wrong with the server. Please try again.");
                mSmoothProgressBar.setVisibility(View.GONE);
            }

        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            //open error dialog message
            isLoading = false;
            openErrorDialog("Something went wrong with the server. Please try again.");
            mSmoothProgressBar.setVisibility(View.GONE);
        }
    };

    private void getAllCategories() {
        Call<FetchShopCategoriesResponse> callcategories = RetrofitBuild.fetchShopCategoriesService(getViewContext())
                .fetchShopCategoriesCall(
                        imei,
                        usertype,
                        CommonFunctions.getSha1Hex(imei + session),
                        String.valueOf(limit),
                        session,
                        pricegroup);
        callcategories.enqueue(getAllCategoriesCallback);
    }

    private Callback<FetchShopCategoriesResponse> getAllCategoriesCallback = new Callback<FetchShopCategoriesResponse>() {
        @Override
        public void onResponse(Call<FetchShopCategoriesResponse> call, Response<FetchShopCategoriesResponse> response) {
            ResponseBody errBody = response.errorBody();
            mSmoothProgressBar.setVisibility(View.GONE);
            isLoading = false;
            isfirstload = false;
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getCategories().size() > 0;
                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);
                    }
                    List<Category> categories = response.body().getCategories();
                    for (Category category : categories) {
                        mdb.insertAllCategories(category);
                    }
                    updateList();
                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                //open error dialog message
                openErrorDialog("Something went wrong with the server. Please try again.");
            }

        }

        @Override
        public void onFailure(Call<FetchShopCategoriesResponse> call, Throwable t) {
            //open error dialog message
            isLoading = false;
            openErrorDialog("Something went wrong with the server. Please try again.");
            mSmoothProgressBar.setVisibility(View.GONE);
        }
    };

    private void updateList() {
        if (mAdapter != null) {
            if (mdb != null) {
                if (!isLoading) {
                    mAdapter.setCategoriesData(mdb.getCategories());
                }
            }
        }
    }

    public static void start(Context context) {
        Intent i = new Intent(context, InterestActivity.class);
        context.startActivity(i);
    }

    public void changeSkipNext(int value) {
        if (value > 0) {
            txv_next.setText(getString(R.string.menu_next));
        } else {
            txv_next.setText(getString(R.string.menu_skip));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txv_next: {

                if (txv_next.getText().toString().equals(getString(R.string.menu_skip))) {
                    Bundle args = new Bundle();
                    args.putBoolean("isShow", false);
                    MainActivity.start(getViewContext(), 0, args);
                } else if (txv_next.getText().toString().equals(getString(R.string.menu_next))) {
                    mAdapter.clear();
                    mAdapter.insertCategories();

                    if (usertype.equals("CONSUMER")) {
                        if (mdb.getConsumerBehaviourPatterns().size() > 0) {
                            createSession(callsession);
                        } else {
                            finish();
                        }
                    } else if (usertype.equals("WHOLESALER")) {
                        if (mdb.getWholesalerBehaviourPatterns().size() > 0) {
                            createSession(callsession);
                        } else {
                            finish();
                        }
                    }

                }

                break;
            }
        }
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);

                    List<InterestPost> listinterest = new ArrayList<>();
                    List<ConsumerBehaviourPattern> listPattern = mdb.getConsumerBehaviourPatterns();
                    for (int i = 0; i < listPattern.size(); i++) {
                        listinterest.add(new InterestPost(listPattern.get(i).getCategoryID(), listPattern.get(i).getCategoryName()));
                    }
                    otherinterest = new Gson().toJson(listinterest);

                    saveInterestToBehaviour(saveInterestToBehaviourSession);
                } else {
                    hideProgressDialog();
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                hideProgressDialog();
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void saveInterestToBehaviour(Callback<SaveInterestToBehaviourResponse> saveInterestToBehaviourCallback) {
        Call<SaveInterestToBehaviourResponse> saveinterest = RetrofitBuild.saveInterestToBehaviourService(getViewContext())
                .saveInterestToBehaviourCall(imei,
                        usertype,
                        sessionid,
                        authcode,
                        otherinterest);
        saveinterest.enqueue(saveInterestToBehaviourCallback);
    }

    private final Callback<SaveInterestToBehaviourResponse> saveInterestToBehaviourSession = new Callback<SaveInterestToBehaviourResponse>() {

        @Override
        public void onResponse(Call<SaveInterestToBehaviourResponse> call, Response<SaveInterestToBehaviourResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response);
            }
        }

        @Override
        public void onFailure(Call<SaveInterestToBehaviourResponse> call, Throwable t) {
            openErrorDialog("Error in connection. Please contact support.");
        }
    };

    private void evaluateResponse(Response<SaveInterestToBehaviourResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {
                mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_DAILYFINDS);
                finish();
                break;
            }
            default: {
                openErrorDialog(response.body().getMessage());
                break;
            }
        }
    }

}
