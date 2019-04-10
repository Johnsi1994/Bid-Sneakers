package com.johnson.bid.trade;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import static com.johnson.bid.MainMvpController.BIDDING;
import static com.johnson.bid.MainMvpController.BOUGHT;
import static com.johnson.bid.MainMvpController.SELLING;
import static com.johnson.bid.MainMvpController.SOLD;

public class TradeAdapter extends FragmentPagerAdapter {

    private TradeContract.Presenter mPresenter;
    private String[] mTrading = new String[]{BIDDING, SELLING, BOUGHT, SOLD};

    public TradeAdapter(FragmentManager fm, TradeContract.Presenter presenter) {
        super(fm);
        mPresenter = presenter;
    }

    @Override
    public Fragment getItem(int i) {

        Log.d("Johnsi", "IN getItem : i = " + i);

        switch (mTrading[i]) {
            case BIDDING:
                Log.d("Johnsi", "getItem : BIDDING");
                return mPresenter.findBidding();
            case SELLING:
                Log.d("Johnsi", "getItem : SELLING");
                return mPresenter.findSelling();
            case BOUGHT:
                Log.d("Johnsi", "getItem : BOUGHT");
                return mPresenter.findBought();
            case SOLD:
            default:
                Log.d("Johnsi", "getItem : SOLD");
                return mPresenter.findSold();
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
