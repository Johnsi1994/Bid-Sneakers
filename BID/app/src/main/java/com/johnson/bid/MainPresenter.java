package com.johnson.bid;

import com.johnson.bid.bidding.BiddingContract;
import com.johnson.bid.bidding.BiddingPresenter;
import com.johnson.bid.bought.BoughtDetailContract;
import com.johnson.bid.bought.BoughtDetailPresenter;
import com.johnson.bid.centre.auction.AuctionContract;
import com.johnson.bid.centre.auction.AuctionPresenter;
import com.johnson.bid.chat.ChatContract;
import com.johnson.bid.chat.ChatPresenter;
import com.johnson.bid.centre.CenterContract;
import com.johnson.bid.centre.CenterPresenter;
import com.johnson.bid.centre.auction.AuctionFragment;
import com.johnson.bid.data.Product;
import com.johnson.bid.dialog.MessageDialog;
import com.johnson.bid.login.LoginContract;
import com.johnson.bid.login.LoginPresenter;
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


public class MainPresenter implements MainContract.Presenter, CenterContract.Presenter, TradeContract.Presenter,
        ChatContract.Presenter, SettingsContract.Presenter , AuctionContract.Presenter, LoginContract.Presenter,
        PostContract.Presenter, TradeItemContract.Presenter, BiddingContract.Presenter, SellingDetailContract.Presenter,
        BoughtDetailContract.Presenter, SoldDetailContract.Presenter {

    private MainContract.View mMainView;

    private LoginPresenter mLoginPresenter;
    private CenterPresenter mCenterPresenter;
    private TradePresenter mTradePresenter;
    private ChatPresenter mChatPresenter;
    private SettingsPresenter mSettingsPresenter;
    private PostPresenter mPostPresenter;
    private BiddingPresenter mBiddingPresenter;
    private SellingDetailPresenter mSellingDetailPresenter;
    private BoughtDetailPresenter mBoughtDetailPresenter;
    private SoldDetailPresenter mSoldDetailPresenter;

    private AuctionPresenter mEnglishAuctionPresenter;
    private AuctionPresenter mSealedAuctionPresenter;

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
        mEnglishAuctionPresenter = checkNotNull(englishAuctionPresenter);
    }

    void setSealedAuctionPresenter(AuctionPresenter sealedAuctionPresenter) {
        mSealedAuctionPresenter = checkNotNull(sealedAuctionPresenter);
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

    void setBiddingPresenter(BiddingPresenter biddingPresenter) {
        mBiddingPresenter = checkNotNull(biddingPresenter);
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
        mEnglishAuctionPresenter.loadEnglishData();
        mSealedAuctionPresenter.loadSealedData();
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
        mEnglishAuctionPresenter.loadEnglishData();
    }

    @Override
    public void setEnglishData(ArrayList<Product> productList) {
        mEnglishAuctionPresenter.setEnglishData(productList);
    }

    @Override
    public void loadSealedData() {
        mSealedAuctionPresenter.loadSealedData();
    }

    @Override
    public void setSealedData(ArrayList<Product> productList) {
        mSealedAuctionPresenter.setSealedData(productList);
    }

    @Override
    public void showToolbarAndBottomNavigation() {
        mMainView.showToolbarUi();
        mMainView.showBottomNavigationUi();
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
        mBiddingPresenter.setProductData(product);
    }

    @Override
    public void loadProductData() {
        mBiddingPresenter.loadProductData();
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
    public void loadMyBiddingData() {
        mMyBiddingPresenter.loadMyBiddingData();
    }

    @Override
    public void setMyBiddingData(ArrayList<Product> productsList) {
        mMyBiddingPresenter.setMyBiddingData(productsList);
    }

    @Override
    public void loadMySellingData() {
        mMySellingPresenter.loadMySellingData();
    }

    @Override
    public void setMySellingData(ArrayList<Product> productsList) {
        mMySellingPresenter.setMySellingData(productsList);
    }

    @Override
    public void loadMyBoughtData() {
        mMyBoughtPresenter.loadMyBoughtData();
    }

    @Override
    public void setMyBoughtData(ArrayList<Product> productsList) {
        mMyBoughtPresenter.setMyBoughtData(productsList);
    }

    @Override
    public void loadMySoldData() {
        mMySoldPresenter.loadMySoldData();
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
