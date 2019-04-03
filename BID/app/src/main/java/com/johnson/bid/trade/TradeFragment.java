package com.johnson.bid.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class TradeFragment extends Fragment implements TradeContract.View {

    private TradeContract.Presenter mPresenter;

    public TradeFragment() {}

    public static TradeFragment newInstance() {
        return new TradeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trade, container, false);
        return root;
    }

    @Override
    public void setPresenter(TradeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
