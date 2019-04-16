package com.johnson.bid.selling;

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

public class SellingDetailFragment extends Fragment implements SellingDetailContract.View {

    private SellingDetailContract.Presenter mPresenter;
    private String mAuctionType;
    private SellingDetailAdapter mSellingDetailAdapter;

    public SellingDetailFragment() {}

    public static SellingDetailFragment newInstance() {
        return new SellingDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSellingDetailAdapter = new SellingDetailAdapter(mPresenter, mAuctionType, (MainActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_container, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mSellingDetailAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.loadSellingDetailData();
    }

    @Override
    public void setPresenter(SellingDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.showToolbarAndBottomNavigation();
    }

    public void setAuctionType(String type) {
        mAuctionType = type;
    }

    @Override
    public void showSellingDetailUi(Product product) {

        mSellingDetailAdapter.updateData(product);
    }
}
