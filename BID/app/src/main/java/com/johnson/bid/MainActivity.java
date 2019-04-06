package com.johnson.bid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.johnson.bid.centre.auction.AuctionFragment;
import com.johnson.bid.util.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private MainContract.Presenter mPresenter;
    private MainMvpController mMainMvpController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void openLoginUi() {
        mMainMvpController.createLoginView();
    }

    @Override
    public void openCenterUi() {
        mMainMvpController.findOrCreateCenterView();
    }

    @Override
    public void openTradeUi() {
        mMainMvpController.findOrCreateTradeView();
    }

    @Override
    public void openChatUi() {
        mMainMvpController.findOrCreateChatView();
    }

    @Override
    public void openSettingsUi() {
        mMainMvpController.findOrCreateSettingsView();
    }

    @Override
    public void openPostUi() {
        mMainMvpController.createPostView();
    }

    @Override
    public AuctionFragment findEnglishAuctionView() {
        return mMainMvpController.findOrCreateEnglishAuctionView();
    }

    @Override
    public AuctionFragment findSealedAuctionView() {
        return mMainMvpController.findOrCreateSealedAuctionView();
    }

    @Override
    public void setToolbarTitleUi(String title) {
        mToolbarTitle.setText(title);
    }

    @Override
    public void hideToolbarUi() {
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbarUi() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomNavigationUi() {
        mBottomNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showBottomNavigationUi() {
        mBottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UserManager.getInstance().getFbCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        mMainMvpController = MainMvpController.create(this);

        setToolbar();
        setBottomNavigation();
        mToolbar.setVisibility(View.VISIBLE);
        mBottomNavigation.setVisibility(View.VISIBLE);

        mPresenter.openCenter();

//        if (UserManager.getInstance().isLoggedIn()) {
//            mPresenter.openCenter();
//        } else {
//            mPresenter.openLogin();
//        }



    }

    private void setToolbar() {
        // Retrieve the AppCompact Toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        // Set the padding to match the Status Bar height
        mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        mToolbarTitle = mToolbar.findViewById(R.id.text_toolbar_title);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setBottomNavigation() {

        mBottomNavigation = findViewById(R.id.bottom_navigation_main);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setItemIconTintList(null); //去除BottomNavigation上面icon的顏色

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = menuItem -> {

        switch (menuItem.getItemId()) {
            case R.id.navigation_center:
                mPresenter.updateToolbar("拍賣中心");
                mPresenter.openCenter();
                return true;
            case R.id.navigation_trade:
                mPresenter.updateToolbar("我的交易");
                mPresenter.openTrade();
                return true;
            case R.id.navigation_message:
                mPresenter.updateToolbar("聊聊");
                mPresenter.openChat();
                return true;
            case R.id.navigation_settings:
                mPresenter.updateToolbar("設定");
                mPresenter.openSettings();
                return true;
            default:
        }
        return false;
    };

}
