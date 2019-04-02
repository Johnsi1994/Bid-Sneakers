package com.johnson.bid;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mMainView;

    @Override
    public void openCenter() {
        mMainView.openCenterUi();
    }

    @Override
    public void start() {}
}
