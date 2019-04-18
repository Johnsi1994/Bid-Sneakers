package com.johnson.bid;

import android.util.Log;

import com.johnson.bid.bidding.BiddingDetailContract;
import com.johnson.bid.bidding.BiddingDetailPresenter;
import com.johnson.bid.bought.BoughtDetailContract;
import com.johnson.bid.bought.BoughtDetailPresenter;
import com.johnson.bid.auction.auctionitem.AuctionItemContract;
import com.johnson.bid.auction.auctionitem.AuctionItemPresenter;
import com.johnson.bid.chat.ChatContract;
import com.johnson.bid.chat.ChatPresenter;
import com.johnson.bid.auction.AuctionContract;
import com.johnson.bid.auction.AuctionPresenter;
import com.johnson.bid.auction.auctionitem.AuctionItemFragment;
import com.johnson.bid.data.Product;
import com.johnson.bid.dialog.MessageDialog;
import com.johnson.bid.login.LoginContract;
import com.johnson.bid.login.LoginPresenter;
import com.johnson.bid.nobodybit.NobodyBidDetailContract;
import com.johnson.bid.nobodybit.NobodyBidDetailPresenter;
import com.johnson.bid.post.PostContract;
import com.johnson.bid.post.PostPresenter;
import com.johnson.bid.selling.SellingDetailContract;
import com.johnson.bid.selling.SellingDetailPresenter;
import com.johnson.bid.settings.SettingsContract;
import com.johnson.bid.settings.SettingsPresenter;
import com.johnson.bid.sold.SoldDetailContract;
import com.johnson.bid.sold.SoldDetailPresenter;
import com.johnson.bid.trade.TradeContract;
import com.johnson.bid.trade.TradeItem.TradeItemContract;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;
import com.johnson.bid.trade.TradeItem.TradeItemPresenter;
import com.johnson.bid.trade.TradePresenter;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;


public class MainPresenter implements MainContract.Presenter, AuctionContract.Presenter, TradeContract.Presenter,
        ChatContract.Presenter, SettingsContract.Presenter, AuctionItemContract.Presenter, LoginContract.Presenter,
        PostContract.Presenter, TradeItemContract.Presenter, BiddingDetailContract.Presenter, SellingDetailContract.Presenter,
        BoughtDetailContract.Presenter, SoldDetailContract.Presenter, NobodyBidDetailContract.Presenter {

    private MainContract.View mMainView;

    private LoginPresenter mLoginPresenter;
    private AuctionPresenter mAuctionPresenter;
    private TradePresenter mTradePresenter;
    private ChatPresenter mChatPresenter;
    private SettingsPresenter mSettingsPresenter;
    private PostPresenter mPostPresenter;
    private BiddingDetailPresenter mBiddingDetailPresenter;
    private SellingDetailPresenter mSellingDetailPresenter;
    private BoughtDetailPresenter mBoughtDetailPresenter;
    private SoldDetailPresenter mSoldDetailPresenter;
    private NobodyBidDetailPresenter mNobodyBidDetailPresenter;

    private AuctionItemPresenter mEnglishAuctionItemPresenter;
    private AuctionItemPresenter mSealedAuctionItemPresenter;

    private TradeItemPresenter mMyBiddingPresenter;
    private TradeItemPresenter mMySellingPresenter;
    private TradeItemPresenter mMyBoughtPresenter;
    private TradeItemPresenter mMySoldPresenter;
    private TradeItemPresenter mNobodyBidPresenter;

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

    void setAuctionPresenter(AuctionPresenter auctionPresenter) {
        mAuctionPresenter = checkNotNull(auctionPresenter);
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

    void setEnglishAuctionItemPresenter(AuctionItemPresenter englishAuctionItemPresenter) {
        mEnglishAuctionItemPresenter = checkNotNull(englishAuctionItemPresenter);
    }

    void setSealedAuctionItemPresenter(AuctionItemPresenter sealedAuctionItemPresenter) {
        mSealedAuctionItemPresenter = checkNotNull(sealedAuctionItemPresenter);
    }

    void setMyBiddingPresenter(TradeItemPresenter myBiddingPresenter) {
        mMyBiddingPresenter = checkNotNull(myBiddingPresenter);
    }

    void setMySellingPresenter(TradeItemPresenter mySellingPresenter) {
        mMySellingPresenter = checkNotNull(mySellingPresenter);
    }

    void setMyBoughtPresenter(TradeItemPresenter myBoughtPresenter) {
        mMyBoughtPresenter = checkNotNull(myBoughtPresenter);
    }

    void setMySoldPresenter(TradeItemPresenter mySoldPresenter) {
        mMySoldPresenter = checkNotNull(mySoldPresenter);
    }

    void setNobodyBitPresenter(TradeItemPresenter nobodyBitPresenter) {
        mNobodyBidPresenter = checkNotNull(nobodyBitPresenter);
    }

    void setBiddingDetailPresenter(BiddingDetailPresenter biddingDetailPresenter) {
        mBiddingDetailPresenter = checkNotNull(biddingDetailPresenter);
    }

    void setSellingDetailPresenter(SellingDetailPresenter sellingDetailPresenter) {
        mSellingDetailPresenter = checkNotNull(sellingDetailPresenter);
    }

    void setBoughtDetailPresenter(BoughtDetailPresenter boughtDetailPresenter) {
        mBoughtDetailPresenter = checkNotNull(boughtDetailPresenter);
    }

    void setSoldDetailPresenter(SoldDetailPresenter soldDetailPresenter) {
        mSoldDetailPresenter = checkNotNull(soldDetailPresenter);
    }

    void setNobodyBidDetailPresenter(NobodyBidDetailPresenter nobodyBidDetailPresenter) {
        mNobodyBidDetailPresenter = checkNotNull(nobodyBidDetailPresenter);
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
    public void updateCenterData() {
        mEnglishAuctionItemPresenter.loadEnglishData();
        mSealedAuctionItemPresenter.loadSealedData();
    }

    @Override
    public void setRead(boolean isRead) {
        mPostPresenter.setRead(isRead);
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
    public void loadEnglishData() {
        mEnglishAuctionItemPresenter.loadEnglishData();
    }

    @Override
    public void setEnglishData(ArrayList<Product> productList) {
        mEnglishAuctionItemPresenter.setEnglishData(productList);
    }

    @Override
    public void loadSealedData() {
        mSealedAuctionItemPresenter.loadSealedData();
    }

    @Override
    public void setSealedData(ArrayList<Product> productList) {
        mSealedAuctionItemPresenter.setSealedData(productList);
    }

    @Override
    public void showToolbarAndBottomNavigation() {
        mMainView.showToolbarUi();
        mMainView.showBottomNavigationUi();
    }

    @Override
    public void setNobodyBitDetailData(Product product) {
        mNobodyBidDetailPresenter.setNobodyBitDetailData(product);
    }

    @Override
    public void loadNobodyBidDetailData() {
        mNobodyBidDetailPresenter.loadNobodyBidDetailData();
    }

    @Override
    public void loadBoughtDetailData() {
        mBoughtDetailPresenter.loadBoughtDetailData();
    }

    @Override
    public void setBoughtDetailData(Product product) {
        mBoughtDetailPresenter.setBoughtDetailData(product);
    }

    @Override
    public void setSellingDetailData(Product product) {
        mSellingDetailPresenter.setSellingDetailData(product);
    }

    @Override
    public void loadSellingDetailData() {
        mSellingDetailPresenter.loadSellingDetailData();
    }

    @Override
    public void openDeleteProductDialog(Product product) {
        mMainView.openDeleteProductDialog(product);
    }

    @Override
    public void setProductData(Product product) {
        mBiddingDetailPresenter.setProductData(product);
    }

    @Override
    public void loadProductData() {
        mBiddingDetailPresenter.loadProductData();
    }

    @Override
    public void openBidDialog(String from, Product product) {
        mMainView.openBidDialog(from, product);
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
    public AuctionItemFragment findEnglishAuction() {
        return mMainView.findEnglishAuctionView();
    }

    @Override
    public AuctionItemFragment findSealedAuction() {
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
    public Product getProduct() {
        return mPostPresenter.getProduct();
    }

    @Override
    public void setProductTitle(String productTitle) {
        mPostPresenter.setProductTitle(productTitle);
    }

    @Override
    public void setAuctionCondition(String auctionCondition) {
        mPostPresenter.setAuctionCondition(auctionCondition);
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
    public void setPostProductId2User(long productId) {
        mPostPresenter.setPostProductId2User(productId);
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
    public void setSellerId(long sellerId) {
        mPostPresenter.setSellerId(sellerId);
    }

    @Override
    public void setParticipantsNumber(int participantsNumber) {
        mPostPresenter.setParticipantsNumber(participantsNumber);
    }

    @Override
    public void setCurrentPrice(int currentPrice) {
        mPostPresenter.setCurrentPrice(currentPrice);
    }

    @Override
    public TradeItemFragment findMyBidding() {
        return mMainView.findMyBiddingView();
    }

    @Override
    public TradeItemFragment findMySelling() {
        return mMainView.findMySellingView();

    }

    @Override
    public TradeItemFragment findMyBought() {
        return mMainView.findMyBoughtView();

    }

    @Override
    public TradeItemFragment findMySold() {
        return mMainView.findMySoldView();

    }

    @Override
    public TradeItemFragment findNobodyBid() {
        return mMainView.findNobodyBidView();
    }


    @Override
    public void openBidding(String auctionType, Product product) {
        mMainView.findBiddingView(auctionType, product);
    }

    @Override
    public void openSelling(String auctionType, Product product) {
        mMainView.findSellingView(auctionType, product);
    }

    @Override
    public void openBoughtDetail(Product product) {
        mMainView.findBoughtDetailView(product);
    }

    @Override
    public void openSoldDetail(Product product) {
        mMainView.findSoldDetailView(product);
    }

    @Override
    public void openNobodyBidDetail(Product product) {
        mMainView.findNobodyBidDetailView(product);
    }

    @Override
    public void loadMyBiddingData() {

        if (mMyBiddingPresenter != null) {
            Log.d("Johnsi", "Start load my bidding data");
            mMyBiddingPresenter.loadMyBiddingData();
        }

    }

    @Override
    public void setMyBiddingData(ArrayList<Product> productsList) {
        mMyBiddingPresenter.setMyBiddingData(productsList);
    }

    @Override
    public void loadMySellingData() {

        if (mMySellingPresenter != null) {
            mMySellingPresenter.loadMySellingData();
        }
    }

    @Override
    public void setMySellingData(ArrayList<Product> productsList) {
        mMySellingPresenter.setMySellingData(productsList);
    }

    @Override
    public void loadMyBoughtData() {

        if (mMyBoughtPresenter != null) {
            mMyBoughtPresenter.loadMyBoughtData();
        }

    }

    @Override
    public void setMyBoughtData(ArrayList<Product> productsList) {
        mMyBoughtPresenter.setMyBoughtData(productsList);
    }

    @Override
    public void loadMySoldData() {

        if (mMySoldPresenter != null) {
            mMySoldPresenter.loadMySoldData();
        }

    }

    @Override
    public void setMySoldData(ArrayList<Product> productsList) {
        mMySoldPresenter.setMySoldData(productsList);
    }

    @Override
    public void loadNobodyBidData() {
        mNobodyBidPresenter.loadNobodyBidData();
    }

    @Override
    public void setNobodyBidData(ArrayList<Product> productsList) {
        mNobodyBidPresenter.setNobodyBidData(productsList);
    }


    @Override
    public void setSoldDetailData(Product product) {
        mSoldDetailPresenter.setSoldDetailData(product);
    }

    @Override
    public void loadSoldDetailData() {
        mSoldDetailPresenter.loadSoldDetailData();
    }
}
