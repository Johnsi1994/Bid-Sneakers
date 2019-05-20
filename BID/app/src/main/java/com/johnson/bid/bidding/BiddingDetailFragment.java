package com.johnson.bid.bidding;

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
import com.johnson.bid.util.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class BiddingDetailFragment extends Fragment implements BiddingDetailContract.View {

    private BiddingDetailContract.Presenter mPresenter;
    private BiddingDetailAdapter mBiddingDetailAdapter;
    private String mAuctionType;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public BiddingDetailFragment() {
    }

    public static BiddingDetailFragment newInstance() {
        return new BiddingDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBiddingDetailAdapter = new BiddingDetailAdapter(mPresenter, (MainActivity) getActivity(), mAuctionType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_swipe_container, container, false);

        mSwipeRefreshLayout = root.findViewById(R.id.swipe_layout);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_container_swipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mBiddingDetailAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.loadProductData();

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            mPresenter.loadBiddingFreshData();
        });
    }

    @Override
    public void setPresenter(BiddingDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.showToolbarAndBottomNavigation();
        mPresenter.updateAuctionData();

        if (UserManager.getInstance().isHasUserDataChange()) {
            UserManager.getInstance().updateUser2Firebase();
        }
    }

    public void setAuctionType(String type) {
        mAuctionType = type;
    }

    @Override
    public void showBiddingUi(Product product) {

        if (mBiddingDetailAdapter == null) {
            mBiddingDetailAdapter = new BiddingDetailAdapter(mPresenter, (MainActivity) getActivity(), mAuctionType);
        }
        mBiddingDetailAdapter.updateData(product);
    }
}
