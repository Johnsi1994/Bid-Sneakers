package com.johnson.bid.bought;

import android.support.v4.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class BoughtFragment extends Fragment implements BoughtContract.View {

    BoughtContract.Presenter mPresenter;

    public BoughtFragment() {}

    public static BoughtFragment newInstance() {
        return new BoughtFragment();
    }

    @Override
    public void setPresenter(BoughtContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
