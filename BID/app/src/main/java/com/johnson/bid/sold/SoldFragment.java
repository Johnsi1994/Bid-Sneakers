package com.johnson.bid.sold;

import android.support.v4.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class SoldFragment extends Fragment implements SoldContract.View {

    private SoldContract.Presenter mPresenter;

    public SoldFragment() {}

    public static SoldFragment newInstance() {
        return new SoldFragment();
    }

    @Override
    public void setPresenter(SoldContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
