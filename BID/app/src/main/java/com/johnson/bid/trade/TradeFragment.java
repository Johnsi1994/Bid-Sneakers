package com.johnson.bid.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.Bid;
import com.johnson.bid.R;
import com.rahimlis.badgedtablayout.BadgedTabLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class TradeFragment extends Fragment implements TradeContract.View {

    private TradeContract.Presenter mPresenter;
    private TradeAdapter mTradeAdapter;

    private BadgedTabLayout mTabLayout;
    private ViewPager mViewPager;

    public TradeFragment() {}

    public static TradeFragment newInstance() {
        return new TradeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTradeAdapter = new TradeAdapter(getChildFragmentManager(), mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trade, container, false);

        mTabLayout = root.findViewById(R.id.tab_trade);
        mViewPager = root.findViewById(R.id.viewpager_trade);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mViewPager.setAdapter(mTradeAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mTabLayout.setupWithViewPager(mViewPager);

        mPresenter.loadBoughtBadgeData();
        mPresenter.loadSoldBadgeData();
        mPresenter.loadNobodyBidBadgeData();

    }

    @Override
    public void setPresenter(TradeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void showBoughtBadgeUI(int unreadBought) {

        if (unreadBought > 0) {
            mTabLayout.setBadgeText(2, String.valueOf(unreadBought));
        } else {
            mTabLayout.setBadgeText(2, null);
        }

    }

    @Override
    public void showSoldBadgeUI(int unreadBought) {

        if (unreadBought > 0) {
            mTabLayout.setBadgeText(3,String.valueOf(unreadBought));
        } else {
            mTabLayout.setBadgeText(3, null);
        }

    }

    @Override
    public void showNobodyBidBadgeUI(int unreadBought) {

        if (unreadBought > 0) {
            mTabLayout.setBadgeText(4,String.valueOf(unreadBought));
        } else {
            mTabLayout.setBadgeText(4, null);
        }

    }
}
