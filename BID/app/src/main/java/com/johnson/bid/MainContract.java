package com.johnson.bid;

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void openCenterUi();
    }

    interface Presenter extends BasePresenter {
        void openCenter();
    }
}
