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
    private FloatingActionButton mFabPost;
    private FloatingActionButton mFabSearch;
    private FloatingActionButton mFabEyesOn;
    private FloatingActionButton mFabMenu;

    private boolean isFabOpen = false;

    public AuctionFragment() {}

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

        mFabMenu = root.findViewById(R.id.fab_menu);
        mFabPost = root.findViewById(R.id.fab_post);
        mFabSearch = root.findViewById(R.id.fab_search);
        mFabEyesOn = root.findViewById(R.id.fab_eyes_on);

        mFabMenu.setOnClickListener(v -> {
            if (isFabOpen) {
                closeFABMenu();
            } else {
                showFABMenu();
            }
        });

        mFabPost.setOnClickListener(v -> {
            closeFABMenu();
            mPresenter.openGalleryDialog(AUCTION);
        });


        mFabEyesOn.setOnClickListener(v -> {
            closeFABMenu();
            mPresenter.openEyesOn(getString(R.string.toolbar_title_eyes_on));
        });

        mFabSearch.setOnClickListener(v -> {
            closeFABMenu();
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
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void setPresenter(AuctionContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private void showFABMenu() {
        isFabOpen = true;
        mFabPost.animate().translationY(-getResources().getDimension(R.dimen.standard_70));
        mFabSearch.animate().translationY(-getResources().getDimension(R.dimen.standard_140));
        mFabEyesOn.animate().translationY(-getResources().getDimension(R.dimen.standard_210));
        mFabMenu.animate().rotationBy(180);
    }

    private void closeFABMenu() {
        isFabOpen = false;
        mFabPost.animate().translationY(0);
        mFabSearch.animate().translationY(0);
        mFabEyesOn.animate().translationY(0);
        mFabMenu.animate().rotationBy(180);
    }
}
