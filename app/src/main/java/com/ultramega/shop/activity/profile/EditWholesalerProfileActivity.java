package com.ultramega.shop.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.adapter.InterestRecyclerAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Category;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;
import com.ultramega.shop.responses.FetchShopCategoriesResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditWholesalerProfileActivity extends BaseActivity {
    private UltramegaShopUtilities mdb;
    private RecyclerView recyclerView;
    private InterestRecyclerAdapter interestAdapter;

    private NestedScrollView nested_scroll;
    private boolean isfirstload = true;
    private boolean isLoading = false;
    private boolean isloadmore = true;
    private boolean isbottomscroll = false;

    private List<ConsumerBehaviourPattern> mPatterns;
    private List<Category> mCategories;
    private int limit;
    private String imei = "";
    private String usertype = "";
    private String sessionid = "";
    private String pricegroup = "";

    private LinearLayout mSmoothProgressBar;
    private TextView nointerestlabel;
    private TextView nointerestsublabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wholesaler_profile);
        setUpToolBar();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        menu.findItem(R.id.edit_profile).setVisible(false);
        menu.findItem(R.id.edit_profile_done).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile_done:
                //getSession();
                insertPatterns();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {

        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Edit Profile"));

        mdb = new UltramegaShopUtilities(getViewContext());
        mCategories = new ArrayList<>();
        mPatterns = new ArrayList<>();

        mCategories = mdb.getCategories();
        mPatterns = mdb.getWholesalerBehaviourPatterns();
        limit = getLimit(mCategories.size(), 30);
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

        nointerestlabel = (TextView) findViewById(R.id.nointerestlabel);
        nointerestlabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Tell us what you're into."));
        nointerestsublabel = (TextView) findViewById(R.id.nointerestsublabel);
        nointerestsublabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Tap once on the interest you like, or tap again the ones you don't."));
        mSmoothProgressBar = (LinearLayout) findViewById(R.id.mSmoothProgressBar);
        nested_scroll = (NestedScrollView) findViewById(R.id.nested_scroll);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        interestAdapter = new InterestRecyclerAdapter(getViewContext(), mdb.getCategories(), mPatterns, usertype);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(interestAdapter);

        if (mdb.getCategories().size() == 0) {
            getSession();
        } else {
            updateList(mdb.getCategories());
        }

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
            isLoading = true;

            createSession(callsession);
        } else {
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {

            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    getAllCategories();
                } else {
                    isLoading = false;
                    mSmoothProgressBar.setVisibility(View.GONE);
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                isLoading = false;
                mSmoothProgressBar.setVisibility(View.GONE);
                openErrorDialog("Something went wrong with the server. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            openErrorDialog("Something went wrong with the server. Please try again.");
        }
    };

    private void getAllCategories() {
        Call<FetchShopCategoriesResponse> callcategories = RetrofitBuild.fetchShopCategoriesService(getViewContext())
                .fetchShopCategoriesCall(
                        imei,
                        usertype,
                        CommonFunctions.getSha1Hex(imei + sessionid),
                        String.valueOf(limit),
                        sessionid,
                        pricegroup);
        callcategories.enqueue(getAllCategoriesCallback);
    }

    private Callback<FetchShopCategoriesResponse> getAllCategoriesCallback = new Callback<FetchShopCategoriesResponse>() {
        @Override
        public void onResponse(Call<FetchShopCategoriesResponse> call, Response<FetchShopCategoriesResponse> response) {
            ResponseBody errBody = response.errorBody();

            mSmoothProgressBar.setVisibility(View.GONE);
            isfirstload = false;
            isLoading = false;

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

                    updateList(mdb.getCategories());

                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                openErrorDialog("Something went wrong with the server. Please try again.");
            }

        }

        @Override
        public void onFailure(Call<FetchShopCategoriesResponse> call, Throwable t) {
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            openErrorDialog("Something went wrong with the server. Please try again.");
        }
    };

    private void updateList(List<Category> data) {
        if (interestAdapter != null) {
            if (mdb != null) {
                if (!isLoading) {
                    interestAdapter.setCategoriesData(data);
                }
            }
        }
    }

    private void insertPatterns() {
        //update behaviour patterns
        if (interestAdapter != null) {
            interestAdapter.insertCategories();
        }

        finish();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EditWholesalerProfileActivity.class);
        context.startActivity(intent);
    }
}
