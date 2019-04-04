package com.johnson.bid;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.johnson.bid.centre.auction.AuctionFragment;
import com.johnson.bid.centre.auction.AuctionPresenter;
import com.johnson.bid.chat.ChatFragment;
import com.johnson.bid.chat.ChatPresenter;
import com.johnson.bid.centre.CenterFragment;
import com.johnson.bid.centre.CenterPresenter;
import com.johnson.bid.settings.SettingsFragment;
import com.johnson.bid.settings.SettingsPresenter;
import com.johnson.bid.trade.TradeFragment;
import com.johnson.bid.trade.TradePresenter;
import com.johnson.bid.util.ActivityUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public class MainMvpController {

    private final FragmentActivity mActivity;
    private MainPresenter mMainPresenter;
    private CenterPresenter mCenterPresenter;
    private TradePresenter mTradePresenter;
    private ChatPresenter mChatPresenter;
    private SettingsPresenter mSettingsPresenter;

    private AuctionPresenter mEnglishAuctionPresenter;
    private AuctionPresenter mSealedAuctionPresenter;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            CENTER, TRADE, CHAT, SETTINGS
    })

    public @interface FragmentType {
    }

    static final String CENTER = "CENTER";
    static final String TRADE = "TRADE";
    static final String CHAT = "CHAT";
    static final String SETTINGS = "SETTINGS";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ENGLISH, SEALED
    })
    public @interface AuctionType {
    }

    public static final String ENGLISH = "ENGLISH";
    public static final String SEALED = "SEALED";


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

    void findOrCreateCenterView() {

        CenterFragment centerFragment = findOrCreateCenterFragment();

        if (mCenterPresenter == null) {
            mCenterPresenter = new CenterPresenter(centerFragment);
            mMainPresenter.setCenterPresenter(mCenterPresenter);
            centerFragment.setPresenter(mMainPresenter);
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

    AuctionFragment findOrCreateEnglishAuctionView() {

        AuctionFragment fragment = findOrCreateAuctionFragment(ENGLISH);

        mEnglishAuctionPresenter = new AuctionPresenter(fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setAuctionType(ENGLISH);
        mMainPresenter.setEnglishAuctionPresenter(mEnglishAuctionPresenter);

        return fragment;
    }

    AuctionFragment findOrCreateSealedAuctionView() {

        AuctionFragment fragment = findOrCreateAuctionFragment(SEALED);

        mSealedAuctionPresenter = new AuctionPresenter(fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setAuctionType(SEALED);
        mMainPresenter.setSealedAuctionPresenter(mSealedAuctionPresenter);

        return fragment;
    }

    @NonNull
    private CenterFragment findOrCreateCenterFragment() {

        CenterFragment centerFragment =
                (CenterFragment) getFragmentManager().findFragmentByTag(CENTER);
        if (centerFragment == null) {
            // Create the fragment
            centerFragment = CenterFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), centerFragment, CENTER);

        return centerFragment;
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
    private AuctionFragment findOrCreateAuctionFragment(@AuctionType String auctionType) {

        AuctionFragment fragment =
                (AuctionFragment) ((getFragmentManager().findFragmentByTag(CENTER)))
                        .getChildFragmentManager().findFragmentByTag(auctionType);
        if (fragment == null) {
            // Create the fragment
            fragment = AuctionFragment.newInstance();
        }

        return fragment;
    }

    private FragmentManager getFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
}
