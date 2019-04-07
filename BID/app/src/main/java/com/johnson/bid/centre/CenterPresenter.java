package com.johnson.bid.centre;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.johnson.bid.Bid;
import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.centre.auction.AuctionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class CenterPresenter implements CenterContract.Presenter {

    private final CenterContract.View mCenterView;
    private MainActivity mMainActivity;

    private Button mCancelBtn;
    private Button mOpenGalleryBtn;

    public CenterPresenter(@NonNull CenterContract.View centerView, MainActivity mainActivity) {
        mCenterView = checkNotNull(centerView, "centerView cannot be null!");
        mMainActivity = mainActivity;
    }

    @Override
    public void start() {

    }

    @Override
    public AuctionFragment findEnglishAuction() {
        return null;
    }

    @Override
    public AuctionFragment findSealedAuction() {
        return null;
    }

    @Override
    public void openGallery() {

    }

    @Override
    public void openCamera() {

    }

}
