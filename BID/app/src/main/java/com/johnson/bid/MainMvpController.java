package com.johnson.bid;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.johnson.bid.centre.CenterFragment;
import com.johnson.bid.centre.CenterPresenter;
import com.johnson.bid.util.ActivityUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainMvpController {

    private final FragmentActivity mActivity;
    private MainPresenter mMainPresenter;

    private CenterPresenter mCenterPresenter;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
          CENTER
    })

    public @interface FragmentType {}
    static final String CENTER    = "CENTER";


    private MainMvpController(@NonNull FragmentActivity activity) {
        mActivity = activity;
    }

    static MainMvpController create(@NonNull FragmentActivity activity) {
        checkNotNull(activity);
        MainMvpController mainMvpController = new MainMvpController(activity);
        mainMvpController.createMainPresenter();
        return mainMvpController;
    }

    private MainPresenter createMainPresenter() {
        mMainPresenter = new MainPresenter((MainActivity) mActivity);

        return mMainPresenter;
    }

    void findOrCreateCenterView() {

        CenterFragment centerFragment = findOrCreateCenterFragment();
        if (centerFragment == null) {
            mCenterPresenter = new CenterPresenter(centerFragment);
            mMainPresenter.setCenterPresenter(mCenterPresenter);
            centerFragment.setPresenter(mMainPresenter);
        }
    }

    @NonNull
    private CenterFragment findOrCreateCenterFragment() {

        CenterFragment centerFragment =
                (CenterFragment) getFragmentManager().findFragmentByTag(CENTER);
        if (centerFragment == null) {
            // Create the fragment
            centerFragment = CenterFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), centerFragment, CENTER);

        return centerFragment;
    }

    private FragmentManager getFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
}
