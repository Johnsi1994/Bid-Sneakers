package com.johnson.bid.selling;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class SellingFragment extends Fragment implements SellingContract.View {

    private SellingContract.Presenter mPresenter;
    private String mAuctionType;
    private SellingAdapter mSellingAdapter;

    public SellingFragment() {}

    public static SellingFragment newInstance() {
        return new SellingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSellingAdapter = new SellingAdapter(mPresenter, mAuctionType, (MainActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_container, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mSellingAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.loadSellingDetailData();
    }

    @Override
    public void setPresenter(SellingContract.Presenter presenter) {
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

        mSellingAdapter.updateData(product);
    }
}
