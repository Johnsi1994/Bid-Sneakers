package com.johnson.bid.auction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.R;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.johnson.bid.MainMvpController.AUCTION;

public class AuctionFragment extends Fragment implements AuctionContract.View {

    private AuctionContract.Presenter mPresenter;
    private AuctionAdapter mAuctionAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mPostFloatingActionButton;
    private FloatingActionButton mSearchFloatingActionButton;
    private FloatingActionButton mEyesOnFloatingActionButton;
    private FloatingActionButton mMenuFloatingActionButton;

    private boolean isFABOpen = false;

    public AuctionFragment() {
    }

    public static AuctionFragment newInstance() {
        return new AuctionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuctionAdapter = new AuctionAdapter(getChildFragmentManager(), mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_auction, container, false);

        mTabLayout = root.findViewById(R.id.tab_center);
        mViewPager = root.findViewById(R.id.viewpager_center);

        mMenuFloatingActionButton = root.findViewById(R.id.fab_menu);
        mPostFloatingActionButton = root.findViewById(R.id.fab_post);
        mSearchFloatingActionButton = root.findViewById(R.id.fab_search);
        mEyesOnFloatingActionButton = root.findViewById(R.id.fab_eyes_on);

        mMenuFloatingActionButton.setOnClickListener(v -> {
            if (isFABOpen) {
                mMenuFloatingActionButton.setImageResource(R.drawable.ic_up_arrow);
                closeFABMenu();
            } else {
                mMenuFloatingActionButton.setImageResource(R.drawable.ic_close_arrow);
                showFABMenu();
            }
        });

        mPostFloatingActionButton.setOnClickListener(v -> {
            closeFABMenu();
            mMenuFloatingActionButton.setImageResource(R.drawable.ic_up_arrow);
            mPresenter.openGalleryDialog(AUCTION);
        });


        mEyesOnFloatingActionButton.setOnClickListener(v -> {
            closeFABMenu();
            mMenuFloatingActionButton.setImageResource(R.drawable.ic_up_arrow);
            mPresenter.openEyesOn("關注");
        });

        mSearchFloatingActionButton.setOnClickListener(v -> {
            closeFABMenu();
            mMenuFloatingActionButton.setImageResource(R.drawable.ic_up_arrow);
            mPresenter.openSearchDialog();
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager.setAdapter(mAuctionAdapter);
        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mViewPager.setOffscreenPageLimit(0);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void setPresenter(AuctionContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private void showFABMenu() {
        isFABOpen = true;
        mPostFloatingActionButton.animate().translationY(-getResources().getDimension(R.dimen.standard_70));
        mSearchFloatingActionButton.animate().translationY(-getResources().getDimension(R.dimen.standard_140));
        mEyesOnFloatingActionButton.animate().translationY(-getResources().getDimension(R.dimen.standard_210));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        mPostFloatingActionButton.animate().translationY(0);
        mSearchFloatingActionButton.animate().translationY(0);
        mEyesOnFloatingActionButton.animate().translationY(0);
    }

}
