package com.johnson.bid.login;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;

    public LoginPresenter(@NonNull LoginContract.View loginView) {
        mLoginView = checkNotNull(loginView, "chatView cannot be null!");
    }

    @Override
    public void start() {}

    @Override
    public void onLoginSuccess() {}
}
