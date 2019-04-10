package com.johnson.bid.bidding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class BiddingFragment extends Fragment implements BiddingContract.View {

    private BiddingContract.Presenter mPresenter;
    private BiddingAdapter mBiddingAdapter;

    public BiddingFragment() {
    }

    public static BiddingFragment newInstance() {
        return new BiddingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBiddingAdapter = new BiddingAdapter(mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_bidding, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mBiddingAdapter);

        return root;
    }

    @Override
    public void setPresenter(BiddingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
