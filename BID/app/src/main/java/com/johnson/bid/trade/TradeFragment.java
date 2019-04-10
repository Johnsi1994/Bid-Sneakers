package com.johnson.bid.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class TradeFragment extends Fragment implements TradeContract.View {

    private TradeContract.Presenter mPresenter;
    private TradeAdapter mTradeAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mSwitchTradeTypeButton;
    private String mTradeType = "Trading";
    private Boolean isTrading = true;

    public TradeFragment() {}

    public static TradeFragment newInstance() {
        return new TradeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTradeAdapter = new TradeAdapter(getChildFragmentManager(), mPresenter, mTradeType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trade, container, false);

        mTabLayout = root.findViewById(R.id.tab_trade);
        mViewPager = root.findViewById(R.id.viewpager_trade);

        mSwitchTradeTypeButton = root.findViewById(R.id.fab_trade);

        mSwitchTradeTypeButton.setOnClickListener( v -> {
            if (isTrading) {
                isTrading = false;
                mSwitchTradeTypeButton.setImageResource(R.drawable.ic_trading);
                mTradeType = "Traded";
                mTradeAdapter.setTradeType(mTradeType);
            } else {
                isTrading = true;
                mSwitchTradeTypeButton.setImageResource(R.drawable.ic_traded);
                mTradeType = "Trading";
                mTradeAdapter.setTradeType(mTradeType);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager.setAdapter(mTradeAdapter);
        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void setPresenter(TradeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
