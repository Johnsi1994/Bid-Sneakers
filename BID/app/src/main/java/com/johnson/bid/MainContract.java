package com.johnson.bid;

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void openCenterUi();

        void setToolbarTitleUi(String title);
    }

    interface Presenter extends BasePresenter {
        void openCenter();

        void updateToolbar(String title);
    }
}
