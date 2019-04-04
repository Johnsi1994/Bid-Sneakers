package com.johnson.bid.centre;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class CenterFragment extends Fragment implements CenterContract.View {

    private CenterContract.Presenter mPresenter;
    private CenterAdapter mCenterAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public CenterFragment() {}

    public static CenterFragment newInstance() {
        return new CenterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCenterAdapter = new CenterAdapter(getChildFragmentManager(), mPresenter);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_center, container, false);

        mTabLayout = root.findViewById(R.id.tab_center);
        mViewPager = root.findViewById(R.id.viewpager_center);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager.setAdapter(mCenterAdapter);
        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void setPresenter(CenterContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
