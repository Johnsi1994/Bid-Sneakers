package com.johnson.bid.centre.auction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuctionFragment extends Fragment implements AuctionContract.View {

    private AuctionContract.Presenter mPresenter;

    private String mAuctionType;

    public AuctionFragment() {}

    public static AuctionFragment newInstance() {
        return new AuctionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_auction, container, false);

        return root;
    }

    @Override
    public void setPresenter(AuctionContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void setAuctionType(@MainMvpController.AuctionType String auctionType) {
        mAuctionType = auctionType;
    }
}
