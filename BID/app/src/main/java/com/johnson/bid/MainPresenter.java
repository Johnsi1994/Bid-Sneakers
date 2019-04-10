package com.johnson.bid;

import android.graphics.Bitmap;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.johnson.bid.centre.auction.AuctionContract;
import com.johnson.bid.centre.auction.AuctionPresenter;
import com.johnson.bid.chat.ChatContract;
import com.johnson.bid.chat.ChatPresenter;
import com.johnson.bid.centre.CenterContract;
import com.johnson.bid.centre.CenterPresenter;
import com.johnson.bid.centre.auction.AuctionFragment;
import com.johnson.bid.dialog.MessageDialog;
import com.johnson.bid.login.LoginContract;
import com.johnson.bid.login.LoginPresenter;
import com.johnson.bid.post.PostContract;
import com.johnson.bid.post.PostPresenter;
import com.johnson.bid.settings.SettingsContract;
import com.johnson.bid.settings.SettingsPresenter;
import com.johnson.bid.trade.TradeContract;
import com.johnson.bid.trade.TradeItem.TradeItemContract;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;
import com.johnson.bid.trade.TradeItem.TradeItemPresenter;
import com.johnson.bid.trade.TradePresenter;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;


public class MainPresenter implements MainContract.Presenter, CenterContract.Presenter, TradeContract.Presenter,
        ChatContract.Presenter, SettingsContract.Presenter , AuctionContract.Presenter, LoginContract.Presenter,
        PostContract.Presenter, TradeItemContract.Presenter {

    private MainContract.View mMainView;

    private LoginPresenter mLoginPresenter;
    private CenterPresenter mCenterPresenter;
    private TradePresenter mTradePresenter;
    private ChatPresenter mChatPresenter;
    private SettingsPresenter mSettingsPresenter;
    private PostPresenter mPostPresenter;

    private AuctionPresenter mEmglishAuctionPresenter;
    private AuctionPresenter mSealedAuctionPresenter;

    private TradeItemPresenter mBiddingPresenter;
    private TradeItemPresenter mSellingPresenter;
    private TradeItemPresenter mBoughtPresenter;
    private TradeItemPresenter mSoldPresenter;

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

    void setBiddingPresenter(TradeItemPresenter biddingPresenter) {
        mBiddingPresenter = checkNotNull(biddingPresenter);
    }

    void setSellingPresenter(TradeItemPresenter sellingPresenter) {
        mSellingPresenter = checkNotNull(sellingPresenter);
    }

    void setBoughtPresenter(TradeItemPresenter boughtPresenter) {
        mBoughtPresenter = checkNotNull(boughtPresenter);
    }

    void setSoldPresenter(TradeItemPresenter soldPresenter) {
        mSoldPresenter = checkNotNull(soldPresenter);
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
    public void setPostPics(ArrayList<String> imagePath) {
        mPostPresenter.setPostPics(imagePath);
    }

    @Override
    public void loadPostPics() {
        mPostPresenter.loadPostPics();
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
    public void openPost(String title, ArrayList<String> imagePath) {
        mMainView.openPostUi(imagePath);
        updateToolbar(title);
        hideBottomNavigation();
    }

    @Override
    public void openGalleryDialog(String from) {
        mMainView.openGalleryDialog(from);
    }

    @Override
    public void showPostSuccessDialog() {
        mMainView.showMessageDialogUi(MessageDialog.POST_SUCCESS);

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

    @Override
    public void setImages(ArrayList<String> Url) {
        mPostPresenter.setImages(Url);
    }

    @Override
    public TradeItemFragment findBidding() {
        return mMainView.findBiddingView();
    }

    @Override
    public TradeItemFragment findSelling() {
        return mMainView.findSellingView();

    }

    @Override
    public TradeItemFragment findBought() {
        return mMainView.findBoughtView();

    }

    @Override
    public TradeItemFragment findSold() {
        return mMainView.findSoldView();

    }
}
