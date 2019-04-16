package com.johnson.bid.sold;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.bought.BoughtDetailAdapter;
import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class SoldDetailFragment extends Fragment implements SoldDetailContract.View {

    private SoldDetailContract.Presenter mPresenter;
    private SoldDetailAdapter mSoldDetailAdapter;
    private Product mProduct;

    public SoldDetailFragment() {}

    public static SoldDetailFragment newInstance() {
        return new SoldDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSoldDetailAdapter = new SoldDetailAdapter(mPresenter, (MainActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_container, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mSoldDetailAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.loadSoldDetailData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.showToolbarAndBottomNavigation();
    }

    @Override
    public void setPresenter(SoldDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showSoldDetailUi(Product product) {
        mSoldDetailAdapter.updateData(product);
    }
}
