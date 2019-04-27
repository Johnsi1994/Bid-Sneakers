package com.johnson.bid.trade.TradeItem;

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

import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.johnson.bid.MainMvpController.MYBIDDING;
import static com.johnson.bid.MainMvpController.MYBOUGHT;
import static com.johnson.bid.MainMvpController.MYSELLING;
import static com.johnson.bid.MainMvpController.MYSOLD;
import static com.johnson.bid.MainMvpController.NOBODYBID;

public class TradeItemFragment extends Fragment implements TradeItemContract.View {

    private TradeItemContract.Presenter mPresenter;
    private TradeItemAdapter mTradeItemAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String mTradeType;

    public TradeItemFragment() {}

    public static TradeItemFragment newInstance() {
        return new TradeItemFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTradeItemAdapter = new TradeItemAdapter(mPresenter, mTradeType);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_swipe_container, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipe_layout);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mTradeItemAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            loadData();
        });
    }

    private void loadData() {

        switch (mTradeType) {
            case MYBIDDING:
                mPresenter.loadMyBiddingData();
                break;
            case MYSELLING:
                mPresenter.loadMySellingData();
                break;
            case MYBOUGHT:
                mPresenter.loadMyBoughtData();
                break;
            case MYSOLD:
                mPresenter.loadMySoldData();
                break;
            case NOBODYBID:
                mPresenter.loadNobodyBidData();
                break;
            default:
        }

    }

    @Override
    public void setPresenter(TradeItemContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void setTradeType(@MainMvpController.TradeType String tradeType) {
        mTradeType = tradeType;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mTradeItemAdapter.cancelAllTimers();
    }

    @Override
    public void showTradeUi(ArrayList<Product> productsList) {
        mTradeItemAdapter.updateData(productsList);
    }
}
