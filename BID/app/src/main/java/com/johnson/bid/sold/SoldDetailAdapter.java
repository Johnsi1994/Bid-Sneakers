package com.johnson.bid.sold;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.Bid;
import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.johnson.bid.MainMvpController.SOLDDETAIL;

public class SoldDetailAdapter extends RecyclerView.Adapter {

    private SoldDetailContract.Presenter mPresenter;
    private LinearSnapHelper mLinearSnapHelper;
    private MainActivity mMainActivity;
    private Product mProduct;
    private boolean isOpening = false;

    public SoldDetailAdapter(SoldDetailContract.Presenter presenter, MainActivity mainActivity) {
        mPresenter = presenter;
        mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SoldDetailViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_sold_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                Bid.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        SoldDetailGalleryAdapter soldDetailGalleryAdapter = new SoldDetailGalleryAdapter(mPresenter, mProduct.getImages());

        if (mLinearSnapHelper == null) {
            mLinearSnapHelper = new LinearSnapHelper();
            mLinearSnapHelper.attachToRecyclerView(((SoldDetailViewHolder) holder).getGalleryRecycler());
        }
        ((SoldDetailViewHolder) holder).getGalleryRecycler().setAdapter(soldDetailGalleryAdapter);
        ((SoldDetailViewHolder) holder).getGalleryRecycler().setLayoutManager(layoutManager);

        bindSoldDetailViewHolder((SoldDetailViewHolder) holder, mProduct);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class SoldDetailViewHolder extends RecyclerView.ViewHolder {

        private Button mBackBtn;
        private RecyclerView mGalleryRecycler;
        private TextView mTitleText;
        private TextView mIntroText;
        private TextView mConditionText;
        private TextView mAuctionTypeText;
        private TextView mExpiredText;
        private TextView mPriceText;
        private TextView mBuyerText;
        private Button mConnectBuyerBtn;

        public SoldDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            mBackBtn = itemView.findViewById(R.id.button_sold_back);
            mGalleryRecycler = itemView.findViewById(R.id.recycler_sold_gallery);
            mTitleText = itemView.findViewById(R.id.text_sold_title);
            mIntroText = itemView.findViewById(R.id.text_sold_intro);
            mConditionText = itemView.findViewById(R.id.text_sold_condition);
            mAuctionTypeText = itemView.findViewById(R.id.text_sold_auction_type);
            mExpiredText = itemView.findViewById(R.id.text_sold_expired);
            mPriceText = itemView.findViewById(R.id.text_sold_price);
            mBuyerText = itemView.findViewById(R.id.text_sold_buyer);
            mConnectBuyerBtn = itemView.findViewById(R.id.button_sold_connect_buyer);

            mBackBtn.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mConnectBuyerBtn.setOnClickListener(v -> {
                        if (!isOpening) {
                            isOpening = true;
                            Firebase.getInstance().getFirestore().collection("chatrooms")
                                    .whereEqualTo("sellerId", UserManager.getInstance().getUser().getId())
                                    .whereEqualTo("buyerId", mProduct.getHighestUserId())
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().size() > 0) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    openChat(document);
                                                    isOpening = false;
                                                }
                                            } else {

                                                Firebase.getInstance().getFirestore().collection("chatrooms")
                                                        .whereEqualTo("buyerId", UserManager.getInstance().getUser().getId())
                                                        .whereEqualTo("sellerId", mProduct.getHighestUserId())
                                                        .get()
                                                        .addOnCompleteListener(task1 -> {
                                                            if (task1.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task1.getResult()) {
                                                                    openChat(document);
                                                                    isOpening = false;
                                                                }
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.d("Johnsi", "Error getting documents: ", task.getException());
                                        }
                                    });
                        }
                    }
            );
        }

        private RecyclerView getGalleryRecycler() {
            return mGalleryRecycler;
        }

        private TextView getTitleText() {
            return mTitleText;
        }

        private TextView getIntroText() {
            return mIntroText;
        }

        private TextView getConditionText() {
            return mConditionText;
        }

        private TextView getAuctionTypeText() {
            return mAuctionTypeText;
        }

        private TextView getExpiredText() {
            return mExpiredText;
        }

        private TextView getPriceText() {
            return mPriceText;
        }

        private TextView getBuyerText() {
            return mBuyerText;
        }

    }

    private void bindSoldDetailViewHolder(SoldDetailViewHolder holder, Product product) {

        holder.getTitleText().setText(product.getTitle());
        holder.getIntroText().setText(product.getIntroduction());
        holder.getConditionText().setText(product.getCondition());
        holder.getPriceText().setText(String.valueOf(product.getCurrentPrice()));
        holder.getAuctionTypeText().setText(product.getAuctionType());
        holder.getExpiredText().setText(getDateToString(product.getExpired()));
        holder.getBuyerText().setText(product.getBuyerName());

    }

    private String getDateToString(long millSeconds) {
        Date d = new Date(millSeconds);
        SimpleDateFormat sf = new SimpleDateFormat("MM 月 dd 日 HH 時 mm 分");
        return sf.format(d);
    }

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }

    private void openChat(QueryDocumentSnapshot document) {

        mPresenter.openChatContent(document.toObject(ChatRoom.class), SOLDDETAIL);
        mPresenter.showToolbar();
        mPresenter.updateToolbar(document.toObject(ChatRoom.class).getBuyerName());

    }
}
