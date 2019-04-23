package com.johnson.bid;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.johnson.bid.auction.AuctionFragment;
import com.johnson.bid.auction.AuctionPresenter;
import com.johnson.bid.auction.auctionitem.AuctionItemFragment;
import com.johnson.bid.auction.auctionitem.AuctionItemPresenter;
import com.johnson.bid.bidding.BiddingDetailFragment;
import com.johnson.bid.bidding.BiddingDetailPresenter;
import com.johnson.bid.bought.BoughtDetailFragment;
import com.johnson.bid.bought.BoughtDetailPresenter;
import com.johnson.bid.chat.ChatFragment;
import com.johnson.bid.chat.ChatPresenter;
import com.johnson.bid.data.Product;
import com.johnson.bid.eyeson.EyesOnFragment;
import com.johnson.bid.eyeson.EyesOnPresenter;
import com.johnson.bid.login.LoginFragment;
import com.johnson.bid.login.LoginPresenter;
import com.johnson.bid.nobodybit.NobodyBidDetailFragment;
import com.johnson.bid.nobodybit.NobodyBidDetailPresenter;
import com.johnson.bid.post.PostFragment;
import com.johnson.bid.post.PostPresenter;
import com.johnson.bid.search.SearchFragment;
import com.johnson.bid.search.SearchPresenter;
import com.johnson.bid.selling.SellingDetailFragment;
import com.johnson.bid.selling.SellingDetailPresenter;
import com.johnson.bid.settings.SettingsFragment;
import com.johnson.bid.settings.SettingsPresenter;
import com.johnson.bid.sold.SoldDetailFragment;
import com.johnson.bid.sold.SoldDetailPresenter;
import com.johnson.bid.trade.TradeFragment;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;
import com.johnson.bid.trade.TradeItem.TradeItemPresenter;
import com.johnson.bid.trade.TradePresenter;
import com.johnson.bid.util.ActivityUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainMvpController {

    private final FragmentActivity mActivity;
    private MainPresenter mMainPresenter;
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
    private EyesOnPresenter mEyesOnPresenter;
    private SearchPresenter mSearchPresenter;

    private AuctionItemPresenter mEnglishAuctionItemPresenter;
    private AuctionItemPresenter mSealedAuctionItemPresenter;

    private TradeItemPresenter mMyBiddingPresenter;
    private TradeItemPresenter mMySellingPresenter;
    private TradeItemPresenter mMyBoughtPresenter;
    private TradeItemPresenter mMySoldPresenter;
    private TradeItemPresenter mNobodyBidPresenter;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            LOGIN, CENTER, TRADE, CHAT, SETTINGS, POST, BIDDING, SELLING, BOUGHTDETAIL, SOLDDETAIL, NOBODYBIDDETAIL,
            EYESON, SEARCH
    })

    public @interface FragmentType {
    }

    static final String LOGIN = "LOGIN";
    static final String CENTER = "CENTER";
    static final String TRADE = "TRADE";
    static final String CHAT = "CHAT";
    static final String SETTINGS = "SETTINGS";
    static final String POST = "POST";
    static final String BIDDING = "BIDDING";
    static final String SELLING = "SELLING";
    static final String BOUGHTDETAIL = "BOUGHTDETAIL";
    static final String SOLDDETAIL = "SOLDDETAIL";
    static final String NOBODYBIDDETAIL = "NOBODYBIDDETAIL";
    static final String EYESON = "EYESON";
    static final String SEARCH = "SEARCH";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ENGLISH, SEALED
    })
    public @interface AuctionType {
    }

    public static final String ENGLISH = "ENGLISH";
    public static final String SEALED = "SEALED";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            MYBIDDING, MYSELLING, MYBOUGHT, MYSOLD, NOBODYBID
    })
    public @interface TradeType {
    }

    public static final String MYBIDDING = "MYBIDDING";
    public static final String MYSELLING = "MYSELLING";
    public static final String MYBOUGHT = "MYBOUGHT";
    public static final String MYSOLD = "MYSOLD";
    public static final String NOBODYBID = "NOBODYBID";


    private MainMvpController(@NonNull FragmentActivity activity) {
        mActivity = activity;
    }

    static MainMvpController create(@NonNull FragmentActivity activity) {
        checkNotNull(activity);
        MainMvpController mainMvpController = new MainMvpController(activity);
        mainMvpController.createMainPresenter();
        return mainMvpController;
    }

    private MainPresenter createMainPresenter() {
        mMainPresenter = new MainPresenter((MainActivity) mActivity);

        return mMainPresenter;
    }

    void createLoginView() {

        LoginFragment loginFragment = createLoginFragment();

        mLoginPresenter = new LoginPresenter(loginFragment);
        mMainPresenter.setLoginPresenter(mLoginPresenter);
        loginFragment.setPresenter(mMainPresenter);
    }

    void findOrCreateCenterView() {

        AuctionFragment auctionFragment = findOrCreateCenterFragment();

        if (mAuctionPresenter == null) {
            mAuctionPresenter = new AuctionPresenter(auctionFragment, (MainActivity) mActivity);
            mMainPresenter.setAuctionPresenter(mAuctionPresenter);
            auctionFragment.setPresenter(mMainPresenter);
        }
    }

    void findOrCreateTradeView() {

        TradeFragment tradeFragment = findOrCreateTradeFragment();

        if (mTradePresenter == null) {
            mTradePresenter = new TradePresenter(tradeFragment);
            mMainPresenter.setTradePresenter(mTradePresenter);
            tradeFragment.setPresenter(mMainPresenter);
        }
    }

    void findOrCreateChatView() {

        ChatFragment chatFragment = findOrCreateChatFragment();

        if (mChatPresenter == null) {
            mChatPresenter = new ChatPresenter(chatFragment);
            mMainPresenter.setChatPresenter(mChatPresenter);
            chatFragment.setPresenter(mMainPresenter);
        }
    }

    void findOrCreateSettingsView() {

        SettingsFragment settingsFragment = findOrCreateSettingsFragment();

        if (mSettingsPresenter == null) {
            mSettingsPresenter = new SettingsPresenter(settingsFragment);
            mMainPresenter.setSettingsPresenter(mSettingsPresenter);
            settingsFragment.setPresenter(mMainPresenter);
        }
    }

    void createEyesOnView() {

        EyesOnFragment eyesOnFragment = findOrCreateEyesOnFragment();

        if (mEyesOnPresenter == null) {
            mEyesOnPresenter = new EyesOnPresenter(eyesOnFragment);
            mMainPresenter.setEyesOnPresenter(mEyesOnPresenter);
            eyesOnFragment.setPresenter(mMainPresenter);
        }

    }


    void createPostView(ArrayList<String> imagePath) {

        PostFragment postFragment = createPostFragment();

        mPostPresenter = new PostPresenter(postFragment);
        mMainPresenter.setPostPresenter(mPostPresenter);
        postFragment.setPresenter(mMainPresenter);
        mPostPresenter.setPostPics(imagePath);
    }

    void createSearchUi(String keyword) {

        SearchFragment searchFragment = createSearchFragment();

        mSearchPresenter = new SearchPresenter(searchFragment);
        mMainPresenter.setSearchPresenter(mSearchPresenter);
        searchFragment.setPresenter(mMainPresenter);
        mSearchPresenter.setKeyword(keyword);

    }

    void setPostPics(ArrayList<String> imagePath) {
        mPostPresenter.setPostPics(imagePath);
    }

    void setAfterBidData(Product product) {
        mBiddingDetailPresenter.setProductData(product);
    }

    AuctionItemFragment findOrCreateEnglishAuctionView() {

        AuctionItemFragment fragment = findOrCreateAuctionFragment(ENGLISH);

        mEnglishAuctionItemPresenter = new AuctionItemPresenter(fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setAuctionType(ENGLISH);
        mMainPresenter.setEnglishAuctionItemPresenter(mEnglishAuctionItemPresenter);

        return fragment;
    }

    AuctionItemFragment findOrCreateSealedAuctionView() {

        AuctionItemFragment fragment = findOrCreateAuctionFragment(SEALED);

        mSealedAuctionItemPresenter = new AuctionItemPresenter(fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setAuctionType(SEALED);
        mMainPresenter.setSealedAuctionItemPresenter(mSealedAuctionItemPresenter);

        return fragment;
    }

    TradeItemFragment findOrCreateMyBiddingView() {

        TradeItemFragment fragment = findOrCreateTradeItemFragment(MYBIDDING);

        mMyBiddingPresenter = new TradeItemPresenter(fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setTradeType(MYBIDDING);
        mMainPresenter.setMyBiddingPresenter(mMyBiddingPresenter);

        return fragment;
    }

    TradeItemFragment findOrCreateMySellingView() {

        TradeItemFragment fragment = findOrCreateTradeItemFragment(MYSELLING);

        mMySellingPresenter = new TradeItemPresenter(fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setTradeType(MYSELLING);
        mMainPresenter.setMySellingPresenter(mMySellingPresenter);

        return fragment;
    }

    TradeItemFragment findOrCreateMyBoughtView() {

        TradeItemFragment fragment = findOrCreateTradeItemFragment(MYBOUGHT);

        mMyBoughtPresenter = new TradeItemPresenter(fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setTradeType(MYBOUGHT);
        mMainPresenter.setMyBoughtPresenter(mMyBoughtPresenter);

        return fragment;
    }

    TradeItemFragment findOrCreateMySoldView() {

        TradeItemFragment fragment = findOrCreateTradeItemFragment(MYSOLD);

        mMySoldPresenter = new TradeItemPresenter(fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setTradeType(MYSOLD);
        mMainPresenter.setMySoldPresenter(mMySoldPresenter);

        return fragment;
    }

    TradeItemFragment findOrCreateNobodyBidView() {

        TradeItemFragment fragment = findOrCreateTradeItemFragment(NOBODYBID);

        mNobodyBidPresenter = new TradeItemPresenter(fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setTradeType(NOBODYBID);
        mMainPresenter.setNobodyBitPresenter(mNobodyBidPresenter);

        return fragment;
    }

    void createBiddingView(String auctionType, Product product) {

        BiddingDetailFragment biddingDetailFragment = createBiddingFragment();

        mBiddingDetailPresenter = new BiddingDetailPresenter(biddingDetailFragment);
        mBiddingDetailPresenter.setProductData(product);
        mMainPresenter.setBiddingDetailPresenter(mBiddingDetailPresenter);
        biddingDetailFragment.setPresenter(mMainPresenter);
        biddingDetailFragment.setAuctionType(auctionType);
    }

    void createSellingView(String auctionType, Product product) {

        SellingDetailFragment sellingDetailFragment = createSellingFragment();

        mSellingDetailPresenter = new SellingDetailPresenter(sellingDetailFragment);
        mSellingDetailPresenter.setSellingDetailData(product);
        mMainPresenter.setSellingDetailPresenter(mSellingDetailPresenter);
        sellingDetailFragment.setPresenter(mMainPresenter);
        sellingDetailFragment.setAuctionType(auctionType);
    }

    void createBoughtDetailView(Product product) {

        BoughtDetailFragment boughtDetailFragment = createBoughtDetailFragment();

        mBoughtDetailPresenter = new BoughtDetailPresenter(boughtDetailFragment);
        mBoughtDetailPresenter.setBoughtDetailData(product);
        mMainPresenter.setBoughtDetailPresenter(mBoughtDetailPresenter);
        boughtDetailFragment.setPresenter(mMainPresenter);
    }

    void createSoldDetailView(Product product) {

        SoldDetailFragment soldDetailFragment = createSoldDetailFragment();

        mSoldDetailPresenter = new SoldDetailPresenter(soldDetailFragment);
        mSoldDetailPresenter.setSoldDetailData(product);
        mMainPresenter.setSoldDetailPresenter(mSoldDetailPresenter);
        soldDetailFragment.setPresenter(mMainPresenter);
    }

    void createNobodyBidDetailView(Product product) {

        NobodyBidDetailFragment nobodyBidDetailFragment = createNobodyBidDetailFragment();

        mNobodyBidDetailPresenter = new NobodyBidDetailPresenter(nobodyBidDetailFragment);
        mNobodyBidDetailPresenter.setNobodyBitDetailData(product);
        mMainPresenter.setNobodyBidDetailPresenter(mNobodyBidDetailPresenter);
        nobodyBidDetailFragment.setPresenter(mMainPresenter);
    }

    @NonNull
    private LoginFragment createLoginFragment() {

        LoginFragment loginFragment = LoginFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), loginFragment, LOGIN);

        return loginFragment;
    }

    @NonNull
    private PostFragment createPostFragment() {

        PostFragment postFragment = PostFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), postFragment, POST);

        return postFragment;
    }

    @NonNull
    private SearchFragment createSearchFragment() {

        SearchFragment searchFragment = SearchFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), searchFragment, SEARCH);

        return searchFragment;
    }

    @NonNull
    private EyesOnFragment findOrCreateEyesOnFragment() {

        EyesOnFragment eyesOnFragment =
                (EyesOnFragment) getFragmentManager().findFragmentByTag(EYESON);

        if (eyesOnFragment == null) {
            eyesOnFragment = EyesOnFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), eyesOnFragment, EYESON);

        return eyesOnFragment;
    }

    @NonNull
    private BiddingDetailFragment createBiddingFragment() {

        BiddingDetailFragment biddingDetailFragment = BiddingDetailFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), biddingDetailFragment, BIDDING);

        return biddingDetailFragment;
    }

    @NonNull
    private SellingDetailFragment createSellingFragment() {

        SellingDetailFragment sellingDetailFragment = SellingDetailFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), sellingDetailFragment, SELLING);

        return sellingDetailFragment;
    }

    @NonNull
    private BoughtDetailFragment createBoughtDetailFragment() {

        BoughtDetailFragment boughtDetailFragment = BoughtDetailFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), boughtDetailFragment, BOUGHTDETAIL);

        return boughtDetailFragment;
    }

    @NonNull
    private SoldDetailFragment createSoldDetailFragment() {

        SoldDetailFragment soldDetailFragment = SoldDetailFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), soldDetailFragment, SOLDDETAIL);

        return soldDetailFragment;
    }

    @NonNull
    private NobodyBidDetailFragment createNobodyBidDetailFragment() {

        NobodyBidDetailFragment nobodyBidDetailFragment = NobodyBidDetailFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), nobodyBidDetailFragment, NOBODYBIDDETAIL);

        return nobodyBidDetailFragment;
    }

    @NonNull
    private AuctionFragment findOrCreateCenterFragment() {

        AuctionFragment auctionFragment =
                (AuctionFragment) getFragmentManager().findFragmentByTag(CENTER);
        if (auctionFragment == null) {
            // Create the fragment
            auctionFragment = AuctionFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), auctionFragment, CENTER);

        return auctionFragment;
    }

    @NonNull
    private TradeFragment findOrCreateTradeFragment() {

        TradeFragment tradeFragment =
                (TradeFragment) getFragmentManager().findFragmentByTag(TRADE);
        if (tradeFragment == null) {
            // Create the fragment
            tradeFragment = TradeFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), tradeFragment, TRADE);

        return tradeFragment;
    }

    @NonNull
    private ChatFragment findOrCreateChatFragment() {

        ChatFragment chatFragment =
                (ChatFragment) getFragmentManager().findFragmentByTag(CHAT);
        if (chatFragment == null) {
            // Create the fragment
            chatFragment = ChatFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), chatFragment, CHAT);

        return chatFragment;
    }

    @NonNull
    private SettingsFragment findOrCreateSettingsFragment() {

        SettingsFragment settingsFragment =
                (SettingsFragment) getFragmentManager().findFragmentByTag(SETTINGS);
        if (settingsFragment == null) {
            // Create the fragment
            settingsFragment = SettingsFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), settingsFragment, SETTINGS);

        return settingsFragment;
    }

    @NonNull
    private AuctionItemFragment findOrCreateAuctionFragment(@AuctionType String auctionType) {

        AuctionItemFragment fragment =
                (AuctionItemFragment) ((getFragmentManager().findFragmentByTag(CENTER)))
                        .getChildFragmentManager().findFragmentByTag(auctionType);
        if (fragment == null) {
            // Create the fragment
            fragment = AuctionItemFragment.newInstance();
        }

        return fragment;
    }

    @NonNull
    private TradeItemFragment findOrCreateTradeItemFragment(@TradeType String tradeType) {

        TradeItemFragment fragment =
                (TradeItemFragment) ((getFragmentManager().findFragmentByTag(TRADE)))
                        .getChildFragmentManager().findFragmentByTag(tradeType);
        if (fragment == null) {
            // Create the fragment
            fragment = TradeItemFragment.newInstance();
        }

        return fragment;
    }

    private FragmentManager getFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
}
