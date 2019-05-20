package com.johnson.bid.eyeson;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class EyesOnFragment extends Fragment implements EyesOnContract.View {

    private EyesOnContract.Presenter mPresenter;
    private EyesOnAdapter mEyesOnAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public EyesOnFragment() {}

    public static EyesOnFragment newInstance() {
        return new EyesOnFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEyesOnAdapter = new EyesOnAdapter(mPresenter, (MainActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_swipe_container, container, false);

        mSwipeRefreshLayout = root.findViewById(R.id.swipe_layout);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_container_swipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mEyesOnAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.loadEyesOnData();

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            mPresenter.loadEyesOnData();
        });
    }

    @Override
    public void setPresenter(EyesOnContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mEyesOnAdapter.cancelAllTimers();
    }

    @Override
    public void showEyesOnUi(ArrayList<Product> products) {
        if (mEyesOnAdapter != null) {
            mEyesOnAdapter.updateData(products);
        }
    }
}
