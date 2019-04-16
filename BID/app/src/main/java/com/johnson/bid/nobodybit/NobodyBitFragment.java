package com.johnson.bid.nobodybit;

import android.support.v4.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class NobodyBitFragment extends Fragment implements NobodyBitContract.View {

    private NobodyBitContract.Presenter mPresenter;

    public NobodyBitFragment() {}

    public static NobodyBitFragment newInstance() {
        return new NobodyBitFragment();
    }

    @Override
    public void setPresenter(NobodyBitContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
