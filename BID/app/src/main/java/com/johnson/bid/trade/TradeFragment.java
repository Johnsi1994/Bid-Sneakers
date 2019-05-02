package com.johnson.bid.trade;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
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

import com.etiya.etiyabadgetablib.EtiyaBadgeTab;
import com.johnson.bid.Bid;
import com.johnson.bid.R;
import com.rahimlis.badgedtablayout.BadgedTabLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class TradeFragment extends Fragment implements TradeContract.View {

    private TradeContract.Presenter mPresenter;
    private TradeAdapter mTradeAdapter;

    private EtiyaBadgeTab etiyaBadgeTab;;
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

        etiyaBadgeTab = root.findViewById(R.id.tab_trade);
        mViewPager = root.findViewById(R.id.viewpager_trade);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(etiyaBadgeTab));
        mViewPager.setAdapter(mTradeAdapter);
        mViewPager.setOffscreenPageLimit(0);
        etiyaBadgeTab.setupWithViewPager(mViewPager);

        etiyaBadgeTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.gray_BBBBBB));
        etiyaBadgeTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        etiyaBadgeTab.setTabGravity(TabLayout.GRAVITY_CENTER);

        etiyaBadgeTab.selectEtiyaBadgeTab(0)
                .tabTitle("競標中")
                .tabTitleColor(R.color.translucent_80)
                .tabBadge(false)
                .createEtiyaBadgeTab();

        etiyaBadgeTab.selectEtiyaBadgeTab(1)
                .tabTitle("出售中")
                .tabTitleColor(R.color.translucent_80)
                .tabBadge(false)
                .createEtiyaBadgeTab();

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

//        if (unreadBought > 0) {
//            mTabLayout.setBadgeText(2, String.valueOf(unreadBought));
//        } else {
//            mTabLayout.setBadgeText(2, null);
//        }

        if (unreadBought > 0) {
            etiyaBadgeTab.selectEtiyaBadgeTab(2)
                    .tabTitle("已得標")
                    .tabTitleColor(R.color.translucent_80)
                    .tabBadge(true)
                    .tabBadgeCount(unreadBought)
                    .tabBadgeCountMore(true)
                    .tabBadgeBgColor(R.color.translucent_80)
                    .tabBadgeTextColor(R.color.white)
                    .tabBadgeCornerRadius(36)
                    .createEtiyaBadgeTab();
        } else {
            etiyaBadgeTab.selectEtiyaBadgeTab(2)
                    .tabTitle("已得標")
                    .tabTitleColor(R.color.translucent_80)
                    .tabBadge(false)
                    .createEtiyaBadgeTab();
        }



    }

    @Override
    public void showSoldBadgeUI(int unreadBought) {

        if (unreadBought > 0) {
            etiyaBadgeTab.selectEtiyaBadgeTab(3)
                    .tabTitle("已出售")
                    .tabTitleColor(R.color.translucent_80)
                    .tabBadge(true)
                    .tabBadgeCount(unreadBought)
                    .tabBadgeCountMore(true)
                    .tabBadgeBgColor(R.color.translucent_80)
                    .tabBadgeTextColor(R.color.white)
                    .tabBadgeCornerRadius(36)
                    .createEtiyaBadgeTab();
        } else {
            etiyaBadgeTab.selectEtiyaBadgeTab(3)
                    .tabTitle("已出售")
                    .tabTitleColor(R.color.translucent_80)
                    .tabBadge(false)
                    .createEtiyaBadgeTab();
        }
    }

    @Override
    public void showNobodyBidBadgeUI(int unreadBought) {

        if (unreadBought > 0) {

            etiyaBadgeTab.selectEtiyaBadgeTab(4)
                    .tabTitle("流標")
                    .tabTitleColor(R.color.translucent_80)
                    .tabBadge(true)
                    .tabBadgeCount(unreadBought)
                    .tabBadgeCountMore(true)
                    .tabBadgeBgColor(R.color.translucent_80)
                    .tabBadgeTextColor(R.color.white)
                    .tabBadgeCornerRadius(36)
                    .createEtiyaBadgeTab();
        } else {
            etiyaBadgeTab.selectEtiyaBadgeTab(4)
                    .tabTitle("流標")
                    .tabTitleColor(R.color.translucent_80)
                    .tabBadge(false)
                    .createEtiyaBadgeTab();
        }
    }
}
