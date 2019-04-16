package com.johnson.bid.nobodybit;

import android.support.v4.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class NobodyBitDetailFragment extends Fragment implements NobodyBitDetailContract.View {

    private NobodyBitDetailContract.Presenter mPresenter;

    public NobodyBitDetailFragment() {}

    public static NobodyBitDetailFragment newInstance() {
        return new NobodyBitDetailFragment();
    }

    @Override
    public void setPresenter(NobodyBitDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
