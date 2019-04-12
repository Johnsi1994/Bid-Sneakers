package com.johnson.bid.centre.auction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.SEALED;

public class AuctionFragment extends Fragment implements AuctionContract.View {

    private AuctionContract.Presenter mPresenter;
    private AuctionAdapter mAuctionAdapter;

    private String mAuctionType;

    public AuctionFragment() {}

    public static AuctionFragment newInstance() {
        return new AuctionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuctionAdapter = new AuctionAdapter(mPresenter, mAuctionType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_auction, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_auction);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAuctionAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (mAuctionType) {
            case ENGLISH:
                mPresenter.loadEnglishData();
                break;
            case SEALED:
                mPresenter.loadSealedData();
                break;
            default:
        }
    }

    @Override
    public void setPresenter(AuctionContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void setAuctionType(@MainMvpController.AuctionType String auctionType) {
        mAuctionType = auctionType;
    }

    @Override
    public void showAuctionUi(ArrayList<Product> productList) {
        mAuctionAdapter.updateData(productList);
    }
}
