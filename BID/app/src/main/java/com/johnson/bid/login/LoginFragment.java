package com.johnson.bid.login;

import android.os.Bundle;
import com.johnson.bid.util.UserManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.johnson.bid.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    private Button mLoginBtn;
    private boolean mIsLoading = false;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login_bid, container, false);

        mLoginBtn = root.findViewById(R.id.button_login);
        mLoginBtn.setOnClickListener(v -> {
            if (!isLoading()) {
                setLoading(true);
                UserManager.getInstance().loginBidByFacebook(getActivity(), new UserManager.LoadCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("logintest", "onSuccess : In Login Fragment");
                        mPresenter.onLoginSuccess();
                        setLoading(false);
                    }

                    @Override
                    public void onFail(String errorMessage) {

                        Log.d("logintest", "onFail : In Login Fragment");
                        setLoading(false);
                    }
                });
            }
        });

        return root;
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
    }
}
