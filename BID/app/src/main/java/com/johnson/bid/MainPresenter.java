package com.johnson.bid;

import com.johnson.bid.Chat.ChatContract;
import com.johnson.bid.Chat.ChatPresenter;
import com.johnson.bid.centre.CenterContract;
import com.johnson.bid.centre.CenterPresenter;
import com.johnson.bid.settings.SettingsContract;
import com.johnson.bid.settings.SettingsPresenter;
import com.johnson.bid.trade.TradeContract;
import com.johnson.bid.trade.TradePresenter;

import static com.google.common.base.Preconditions.checkNotNull;


public class MainPresenter implements MainContract.Presenter, CenterContract.Presenter, TradeContract.Presenter,
        ChatContract.Presenter, SettingsContract.Presenter {

    private MainContract.View mMainView;

    private CenterPresenter mCenterPresenter;
    private TradePresenter mTradePresenter;
    private ChatPresenter mChatPresenter;
    private SettingsPresenter mSettingsPresenter;

    public MainPresenter(MainContract.View mainView) {
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);
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
    public void start() {
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
}
