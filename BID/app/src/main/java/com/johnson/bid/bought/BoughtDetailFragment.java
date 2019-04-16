package com.johnson.bid.bought;

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
import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class BoughtDetailFragment extends Fragment implements BoughtDetailContract.View {

    private BoughtDetailContract.Presenter mPresenter;
    private BoughtDetailAdapter mBoughtDetailAdapter;
    private Product mProduct;

    public BoughtDetailFragment() {}

    public static BoughtDetailFragment newInstance() {
        return new BoughtDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBoughtDetailAdapter = new BoughtDetailAdapter(mPresenter, (MainActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_container, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mBoughtDetailAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.loadBoughtDetailData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.showToolbarAndBottomNavigation();
    }

    @Override
    public void setPresenter(BoughtDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showBoughtDetailUi(Product product) {
        mBoughtDetailAdapter.updateData(product);
    }
}
