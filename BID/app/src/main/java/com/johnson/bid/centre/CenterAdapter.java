package com.johnson.bid.centre;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.SEALED;

public class CenterAdapter extends FragmentPagerAdapter {

    private CenterContract.Presenter mPresenter;
    private String[] mAuctionTypes = new String[]{ENGLISH, SEALED};

    public CenterAdapter(FragmentManager fm, CenterContract.Presenter presenter) {
        super(fm);

        mPresenter = presenter;
    }

    @Override
    public Fragment getItem(int i) {

        switch (mAuctionTypes[i]) {
            case ENGLISH:
                return mPresenter.findEnglishAuction();
            case SEALED:
            default:
                return mPresenter.findSealedAuction();
        }
    }

    @Override
    public int getCount() {
        return mAuctionTypes.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mAuctionTypes[position];
    }
}
