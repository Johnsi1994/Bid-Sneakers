package com.johnson.bid;

import android.util.Log;

import com.johnson.bid.centre.auction.AuctionContract;
import com.johnson.bid.centre.auction.AuctionPresenter;
import com.johnson.bid.chat.ChatContract;
import com.johnson.bid.chat.ChatPresenter;
import com.johnson.bid.centre.CenterContract;
import com.johnson.bid.centre.CenterPresenter;
import com.johnson.bid.centre.auction.AuctionFragment;
import com.johnson.bid.login.LoginContract;
import com.johnson.bid.login.LoginPresenter;
import com.johnson.bid.post.PostContract;
import com.johnson.bid.post.PostPresenter;
import com.johnson.bid.settings.SettingsContract;
import com.johnson.bid.settings.SettingsPresenter;
import com.johnson.bid.trade.TradeContract;
import com.johnson.bid.trade.TradePresenter;

import static com.google.common.base.Preconditions.checkNotNull;


public class MainPresenter implements MainContract.Presenter, CenterContract.Presenter, TradeContract.Presenter,
        ChatContract.Presenter, SettingsContract.Presenter , AuctionContract.Presenter, LoginContract.Presenter,
        PostContract.Presenter {

    private MainContract.View mMainView;

    private LoginPresenter mLoginPresenter;
    private CenterPresenter mCenterPresenter;
    private TradePresenter mTradePresenter;
    private ChatPresenter mChatPresenter;
    private SettingsPresenter mSettingsPresenter;
    private PostPresenter mPostPresenter;

    private AuctionPresenter mEmglishAuctionPresenter;
    private AuctionPresenter mSealedAuctionPresenter;

    public MainPresenter(MainContract.View mainView) {
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);
    }

    void setLoginPresenter(LoginPresenter loginPresenter) {
        mLoginPresenter = checkNotNull(loginPresenter);
    }

    void setPostPresenter(PostPresenter postPresenter) {
        mPostPresenter = checkNotNull(postPresenter);
    }

    void setCenterPresenter(CenterPresenter centerPresenter) {
        mCenterPresenter = checkNotNull(centerPresenter);
    }

    void setTradePresenter(TradePresenter tradePresenter) {
        mTradePresenter = checkNotNull(tradePresenter);
    }

    void setChatPresenter(ChatPresenter chatPresenter) {
        mChatPresenter = checkNotNull(chatPresenter);
    }

    void setSettingsPresenter(SettingsPresenter settingsPresenter) {
        mSettingsPresenter = checkNotNull(settingsPresenter);
    }

    void setEnglishAuctionPresenter(AuctionPresenter englishAuctionPresenter) {
        mEmglishAuctionPresenter = checkNotNull(englishAuctionPresenter);
    }

    void setSealedAuctionPresenter(AuctionPresenter sealedAuctionPresenter) {
        mSealedAuctionPresenter = checkNotNull(sealedAuctionPresenter);
    }

    @Override
    public void openLogin() {
        mMainView.openLoginUi();
    }

    @Override
    public void openCenter() {
        mMainView.openCenterUi();
    }

    @Override
    public void openTrade() {
        mMainView.openTradeUi();
    }

    @Override
    public void openChat() {
        mMainView.openChatUi();
    }

    @Override
    public void openSettings() {
        mMainView.openSettingsUi();
    }

    @Override
    public void updateToolbar(String title) {
        mMainView.setToolbarTitleUi(title);
    }

    @Override
    public void hideToolbarAndBottomNavigation() {
        mMainView.hideToolbarUi();
        mMainView.hideBottomNavigationUi();
    }

    @Override
    public void showToolbarAndBottomNavigation() {
        mMainView.showToolbarUi();
        mMainView.showBottomNavigationUi();
    }

    @Override
    public void hideBottomNavigation() {
        mMainView.hideBottomNavigationUi();
    }

    @Override
    public void showBottomNavigation() {
        mMainView.showBottomNavigationUi();
    }

    @Override
    public void start() {
    }

    @Override
    public AuctionFragment findEnglishAuction() {
        return mMainView.findEnglishAuctionView();
    }

    @Override
    public AuctionFragment findSealedAuction() {
        return mMainView.findSealedAuctionView();
    }

    @Override
    public void openPost(String title) {
        mMainView.openPostUi();
        updateToolbar(title);
        hideBottomNavigation();
    }

    @Override
    public void openGallery() {
        mMainView.openGallery();
    }

    @Override
    public void openCamera() {
        mMainView.openCamera();
    }

    @Override
    public void onLoginSuccess() {
        mMainView.openCenterUi();
        showToolbarAndBottomNavigation();
    }

    @Override
    public void setProductTitle(String productTitle) {
        mPostPresenter.setProductTitle(productTitle);
    }

    @Override
    public void setProductIntro(String productIntro) {
        mPostPresenter.setProductIntro(productIntro);
    }

    @Override
    public void setStartingPrice(int startingPrice) {
        mPostPresenter.setStartingPrice(startingPrice);
    }

    @Override
    public void setReservePrice(int reservePrice) {
        mPostPresenter.setReservePrice(reservePrice);
    }

    @Override
    public void setProductCondition(String condition) {
        mPostPresenter.setProductCondition(condition);
    }

    @Override
    public void setAuctionType(String auctionType) {
        mPostPresenter.setAuctionType(auctionType);
    }

    @Override
    public void setIncrease(int increase) {
        mPostPresenter.setIncrease(increase);
    }

    @Override
    public void setExpireTime(long expireTime) {
        mPostPresenter.setExpireTime(expireTime);
    }

    @Override
    public void setProductId(long productId) {
        mPostPresenter.setProductId(productId);
    }

    @Override
    public void setStartingTime(long startingTime) {
        mPostPresenter.setStartingTime(startingTime);
    }
}
