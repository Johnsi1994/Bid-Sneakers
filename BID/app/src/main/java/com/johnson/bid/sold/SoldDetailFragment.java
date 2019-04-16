package com.johnson.bid.sold;

import android.support.v4.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class SoldDetailFragment extends Fragment implements SoldDetailContract.View {

    private SoldDetailContract.Presenter mPresenter;

    public SoldDetailFragment() {}

    public static SoldDetailFragment newInstance() {
        return new SoldDetailFragment();
    }

    @Override
    public void setPresenter(SoldDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
