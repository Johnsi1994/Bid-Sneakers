package com.johnson.bid.centre;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.R;

public class CenterFragment extends Fragment implements CenterContract.View {

    private CenterContract.Presenter mPresenter;

    public CenterFragment() {}

    public static CenterFragment newInstance() {
        return new CenterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_center, container, false);
        return root;
    }

    @Override
    public void setPresenter(CenterContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
