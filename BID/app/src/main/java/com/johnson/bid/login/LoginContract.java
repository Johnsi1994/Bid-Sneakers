package com.johnson.bid.login;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void onLoginSuccess();

    }
}
