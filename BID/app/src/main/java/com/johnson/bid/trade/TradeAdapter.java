package com.johnson.bid.trade;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.johnson.bid.MainMvpController.MYBIDDING;
import static com.johnson.bid.MainMvpController.MYBOUGHT;
import static com.johnson.bid.MainMvpController.MYSELLING;
import static com.johnson.bid.MainMvpController.MYSOLD;

public class TradeAdapter extends FragmentPagerAdapter {

    private TradeContract.Presenter mPresenter;
    private String[] mTrading = new String[]{MYBIDDING, MYSELLING, MYBOUGHT, MYSOLD};

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
            default:
                return mPresenter.findMySold();
        }
    }

    @Override
    public int getCount() {
        return mTrading.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTrading[position];
    }

}
