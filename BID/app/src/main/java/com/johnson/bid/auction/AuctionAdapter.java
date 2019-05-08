package com.johnson.bid.auction;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.johnson.bid.Bid;
import com.johnson.bid.R;

import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.SEALED;

public class AuctionAdapter extends FragmentPagerAdapter {

    private AuctionContract.Presenter mPresenter;
    private String[] mAuctionTypes = new String[]{ENGLISH, SEALED};
    private String[] mTitle = new String[]{Bid.getAppContext().getString(R.string.tablayout_title_English_auction),
            Bid.getAppContext().getString(R.string.tablayout_title_sealed_auction)};

    public AuctionAdapter(FragmentManager fm, AuctionContract.Presenter presenter) {
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
        return mTitle[position];
    }
}
