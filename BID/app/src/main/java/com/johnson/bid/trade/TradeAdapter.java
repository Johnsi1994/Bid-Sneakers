package com.johnson.bid.trade;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.johnson.bid.MainMvpController.MYBIDDING;
import static com.johnson.bid.MainMvpController.MYBOUGHT;
import static com.johnson.bid.MainMvpController.MYSELLING;
import static com.johnson.bid.MainMvpController.MYSOLD;
import static com.johnson.bid.MainMvpController.NOBODYBID;

public class TradeAdapter extends FragmentPagerAdapter {

    private TradeContract.Presenter mPresenter;
    private String[] mTrading = new String[]{MYBIDDING, MYSELLING, MYBOUGHT, MYSOLD, NOBODYBID};
    private String[] mTitle = new String[]{"競標中", "出售中", "已得標", "已售出", "流標"};

    public TradeAdapter(FragmentManager fm, TradeContract.Presenter presenter) {
        super(fm);
        mPresenter = presenter;
    }

    @Override
    public Fragment getItem(int i) {

        switch (mTrading[i]) {
            case MYBIDDING:
                return mPresenter.findMyBidding();
            case MYSELLING:
                return mPresenter.findMySelling();
            case MYBOUGHT:
                return mPresenter.findMyBought();
            case MYSOLD:
                return mPresenter.findMySold();
            case NOBODYBID:
            default:
                return mPresenter.findNobodyBid();
        }

    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }

}
