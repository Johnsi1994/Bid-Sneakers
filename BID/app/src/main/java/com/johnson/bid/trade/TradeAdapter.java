package com.johnson.bid.trade;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import static com.johnson.bid.MainMvpController.BIDDING;
import static com.johnson.bid.MainMvpController.BOUGHT;
import static com.johnson.bid.MainMvpController.SELLING;
import static com.johnson.bid.MainMvpController.SOLD;

public class TradeAdapter extends FragmentPagerAdapter {

    private TradeContract.Presenter mPresenter;
    private String[] mTrading = new String[]{BIDDING, SELLING};
    private String[] mTraded = new String[]{BOUGHT, SOLD};
    private String mTradeType;

    public TradeAdapter(FragmentManager fm, TradeContract.Presenter presenter, String tradeType) {
        super(fm);

        mPresenter = presenter;
        mTradeType = tradeType;
    }

    @Override
    public Fragment getItem(int i) {

        Log.d("Johnsi", "getItem : I AM HERE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        if (mTradeType.equals("Trading")) {
            switch (mTrading[i]) {
                case BIDDING:
                    Log.d("Johnsi", "getItem : BIDDINGGGGGGGGGGGGGGG");
                    return mPresenter.findBidding();
                case SELLING:
                default:
                    Log.d("Johnsi", "getItem : SELLINGGGGGGGGGGGGGGG");
                    return mPresenter.findSelling();
            }
        } else {
            switch (mTraded[i]) {
                case BOUGHT:
                    Log.d("Johnsi", "getItem : BOUGHTTTTTTTTTTTTTTTT");
                    return mPresenter.findBought();
                case SOLD:
                default:
                    Log.d("Johnsi", "getItem : SOLDDDDDDDDDDDDDDDDDD");
                    return mPresenter.findSold();
            }
        }
    }

    @Override
    public int getCount() {
        return (mTradeType.equals("Trading")) ? mTrading.length : mTraded.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (mTradeType.equals("Trading")) ? mTrading[position] : mTraded[position];
    }
    

    public void setTradeType(String tradeType) {
        mTradeType = tradeType;
        notifyDataSetChanged();
    }
}
