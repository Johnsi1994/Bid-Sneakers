package com.johnson.bid.bought;

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

import static com.johnson.bid.MainMvpController.BOUGHTDETAIL;
import static com.johnson.bid.MainMvpController.SOLDDETAIL;


public class BoughtDetailAdapter extends RecyclerView.Adapter {

    private BoughtDetailContract.Presenter mPresenter;
    private LinearSnapHelper mLinearSnapHelper;
    private MainActivity mMainActivity;
    private Product mProduct;
    private boolean isOpening = false;

    public BoughtDetailAdapter(BoughtDetailContract.Presenter presenter, MainActivity mainActivity) {
        mPresenter = presenter;
        mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BoughtDetailViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_bought_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                Bid.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        BoughtDetailGalleryAdapter boughtDetailGalleryAdapter = new BoughtDetailGalleryAdapter(mPresenter, mProduct.getImages());

        if (mLinearSnapHelper == null) {
            mLinearSnapHelper = new LinearSnapHelper();
            mLinearSnapHelper.attachToRecyclerView(((BoughtDetailViewHolder) holder).getGalleryRecycler());
        }
        ((BoughtDetailViewHolder) holder).getGalleryRecycler().setAdapter(boughtDetailGalleryAdapter);
        ((BoughtDetailViewHolder) holder).getGalleryRecycler().setLayoutManager(layoutManager);

        bindBoughtDetailViewHolder((BoughtDetailViewHolder) holder, mProduct);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class BoughtDetailViewHolder extends RecyclerView.ViewHolder {

        private Button mBackBtn;
        private RecyclerView mGalleryRecycler;
        private TextView mTitleText;
        private TextView mIntroText;
        private TextView mConditionText;
        private TextView mAuctionTypeText;
        private TextView mExpiredText;
        private TextView mPriceText;
        private TextView mSellerText;
        private Button mConnectSellerBtn;

        public BoughtDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            mBackBtn = itemView.findViewById(R.id.button_bought_back);
            mGalleryRecycler = itemView.findViewById(R.id.recycler_bought_gallery);
            mTitleText = itemView.findViewById(R.id.text_bought_title);
            mIntroText = itemView.findViewById(R.id.text_bought_intro);
            mConditionText = itemView.findViewById(R.id.text_bought_condition);
            mAuctionTypeText = itemView.findViewById(R.id.text_bought_auction_type);
            mExpiredText = itemView.findViewById(R.id.text_bought_expired);
            mPriceText = itemView.findViewById(R.id.text_bought_price);
            mSellerText = itemView.findViewById(R.id.text_bought_seller);
            mConnectSellerBtn = itemView.findViewById(R.id.button_bought_connect_seller);

            mBackBtn.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mConnectSellerBtn.setOnClickListener(v -> {

                if (!isOpening) {
                    isOpening = true;
                    Firebase.getInstance().getFirestore().collection("chatrooms")
                            .whereEqualTo("sellerId", UserManager.getInstance().getUser().getId())
                            .whereEqualTo("buyerId", mProduct.getSellerId())
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() > 0) {
                                        Log.d("chattest", "Size 1 : " + task.getResult().size());
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            openChat(document);
                                            isOpening = false;
                                        }
                                    } else {

                                        Firebase.getInstance().getFirestore().collection("chatrooms")
                                                .whereEqualTo("buyerId", UserManager.getInstance().getUser().getId())
                                                .whereEqualTo("sellerId", mProduct.getSellerId())
                                                .get()
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        Log.d("chattest", "Size 2 : " + task1.getResult().size());
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
            });
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

        private TextView getSellerText() {
            return mSellerText;
        }

    }

    private void bindBoughtDetailViewHolder(BoughtDetailViewHolder holder, Product product) {

        holder.getTitleText().setText(product.getTitle());
        holder.getIntroText().setText(product.getIntroduction());
        holder.getConditionText().setText(product.getCondition());
        holder.getPriceText().setText(String.valueOf(product.getCurrentPrice()));
        holder.getAuctionTypeText().setText(product.getAuctionType());
        holder.getExpiredText().setText(getDateToString(product.getExpired()));
        holder.getSellerText().setText(product.getSellerName());

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

        mPresenter.openChatContent(document.toObject(ChatRoom.class), BOUGHTDETAIL);
        mPresenter.showToolbar();
        mPresenter.updateToolbar(document.toObject(ChatRoom.class).getSellerName());

    }
}
