package com.johnson.bid.bought;

import android.support.v4.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class BoughtDetailFragment extends Fragment implements BoughtDetailContract.View {

    BoughtDetailContract.Presenter mPresenter;

    public BoughtDetailFragment() {}

    public static BoughtDetailFragment newInstance() {
        return new BoughtDetailFragment();
    }

    @Override
    public void setPresenter(BoughtDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
