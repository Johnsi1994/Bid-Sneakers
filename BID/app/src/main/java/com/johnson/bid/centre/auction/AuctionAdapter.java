package com.johnson.bid.centre.auction;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.ImageManager;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.SEALED;

public class AuctionAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_PRODUCT = 0x01;

    private AuctionContract.Presenter mPresenter;
    private String mAuctionType;
    private ArrayList<Product> mProductList;
    private SparseArray<CountDownTimer> countDownMap;


    public AuctionAdapter(AuctionContract.Presenter presenter, @MainMvpController.AuctionType String auctionType) {
        mPresenter = presenter;
        mAuctionType = auctionType;
        countDownMap = new SparseArray<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PRODUCT) {
            if (mAuctionType.equals(ENGLISH)) {
                return new EnglishAuctionViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_bidding_english, viewGroup, false));
            } else {
                return new SealedAuctionViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_bidding_sealed, viewGroup, false));
            }
        } else {
            return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_all_loading, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (mProductList != null) {
            if (viewHolder instanceof EnglishAuctionViewHolder) {
                bindEnglishAuctionViewHolder((EnglishAuctionViewHolder) viewHolder, mProductList.get(i));
            } else {
                bindSealedAuctionViewHolder((SealedAuctionViewHolder) viewHolder, mProductList.get(i));
            }
        }

    }

    @Override
    public int getItemCount() {
        if (mProductList == null) {
            return 0;
        } else {
            return mProductList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position < mProductList.size()) ? TYPE_PRODUCT : TYPE_LOADING;
    }

    private class EnglishAuctionViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayoutEnglishAuction;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPeople;
        CountDownTimer countDownTimer;

        private EnglishAuctionViewHolder(View itemView) {
            super(itemView);

            mLayoutEnglishAuction = itemView.findViewById(R.id.layout_e_auction);
            mImageMain = itemView.findViewById(R.id.image_product_e_auction);
            mTextTitle = itemView.findViewById(R.id.text_product_title_e_auction);
            mTextTime = itemView.findViewById(R.id.text_last_time_e_auction);
            mTextPrice = itemView.findViewById(R.id.text_price_e_auction);
            mTextPeople = itemView.findViewById(R.id.text_participant_num_e_auction);
        }

        private ConstraintLayout getLayoutEnglishAuction() {
            return mLayoutEnglishAuction;
        }

        private ImageView getImageMain() {
            return mImageMain;
        }

        private TextView getTextTitle() {
            return mTextTitle;
        }

        private TextView getTextTime() {
            return mTextTime;
        }

        private TextView getTextPrice() {
            return mTextPrice;
        }

        private TextView getTextPeople() {
            return mTextPeople;
        }
    }

    private void bindEnglishAuctionViewHolder(EnglishAuctionViewHolder holder, final Product product) {

        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
            mPresenter.openBidding(ENGLISH, product);
            mPresenter.hideToolbarAndBottomNavigation();
        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        long lastTime = product.getExpired() - System.currentTimeMillis();
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        holder.countDownTimer = new CountDownTimer(lastTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                holder.getTextTime().setText(getDateToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {

                holder.getTextTime().setText("競標結束");

                Firebase.getFirestore().collection("products")
                        .document(String.valueOf(product.getProductId()))
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();

                                if (document.toObject(Product.class).getHighestUserId() == -1 ) {

                                    Firebase.getFirestore().collection("users")
                                            .document(String.valueOf(document.toObject(Product.class).getSellerId()))
                                            .update("nobodyBitProductsId", FieldValue.arrayUnion(product.getProductId()))
                                            .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Nobody Bit Products Id successfully added!"))
                                            .addOnFailureListener(e -> Log.w("Johnsi", "Nobody Bit Products Id Products Id Error updating document", e));

                                } else {

                                    Firebase.getFirestore().collection("users")
                                            .document(String.valueOf(document.toObject(Product.class).getHighestUserId()))
                                            .update("myBiddingProductsId", FieldValue.arrayRemove(product.getProductId()))
                                            .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Bidding Products Id successfully removed!"))
                                            .addOnFailureListener(e -> Log.w("Johnsi", "Bidding Products Id Error updating document", e));

                                    Firebase.getFirestore().collection("users")
                                            .document(String.valueOf(document.toObject(Product.class).getHighestUserId()))
                                            .update("myBoughtProductsId", FieldValue.arrayUnion(product.getProductId()))
                                            .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Bought Products Id successfully added!"))
                                            .addOnFailureListener(e -> Log.w("Johnsi", "Bought Products Id Error updating document", e));

                                    Firebase.getFirestore().collection("users")
                                            .document(String.valueOf(document.toObject(Product.class).getSellerId()))
                                            .update("mySoldProductsId", FieldValue.arrayUnion(product.getProductId()))
                                            .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Sold Products Id successfully added!"))
                                            .addOnFailureListener(e -> Log.w("Johnsi", "Sold Products Id Error updating document", e));

                                }

                                Firebase.getFirestore().collection("users")
                                        .document(String.valueOf(document.toObject(Product.class).getSellerId()))
                                        .update("mySellingProductsId", FieldValue.arrayRemove(product.getProductId()))
                                        .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Selling Products Id successfully removed!"))
                                        .addOnFailureListener(e -> Log.w("Johnsi", "Selling Products Id Error updating document", e));


                            } else {
                                Log.d("Johnsi", "Error getting documents: ", task.getException());
                            }
                        });

                Firebase.getFirestore().collection("products")
                        .document(String.valueOf(product.getProductId()))
                        .update("auctionCondition", "finish")
                        .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Product condition (finish) successfully updated!"))
                        .addOnFailureListener(e -> Log.w("Johnsi", "Product condition (finish) Error updating document", e));

            }
        }.start();

        countDownMap.put(holder.getTextTime().hashCode(), holder.countDownTimer);

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextPeople().setText(String.valueOf(product.getParticipantsNumber()));

    }

    private class SealedAuctionViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayoutSealedAuction;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        CountDownTimer countDownTimer;

        private SealedAuctionViewHolder(View itemView) {
            super(itemView);

            mLayoutSealedAuction = itemView.findViewById(R.id.layout_s_auction);
            mImageMain = itemView.findViewById(R.id.image_product_s_auction);
            mTextTitle = itemView.findViewById(R.id.text_product_title_s_auction);
            mTextTime = itemView.findViewById(R.id.text_last_time_s_auction);

        }

        private ConstraintLayout getLayoutSealedAuction() {
            return mLayoutSealedAuction;
        }

        private ImageView getImageMain() {
            return mImageMain;
        }

        private TextView getTextTitle() {
            return mTextTitle;
        }

        private TextView getTextTime() {
            return mTextTime;
        }
    }

    private void bindSealedAuctionViewHolder(SealedAuctionViewHolder holder, Product product) {

        holder.getLayoutSealedAuction().setOnClickListener(v -> {
            mPresenter.openBidding(SEALED, product);
            mPresenter.hideToolbarAndBottomNavigation();
        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        long lastTime = product.getExpired() - System.currentTimeMillis();
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        holder.countDownTimer = new CountDownTimer(lastTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                holder.getTextTime().setText(getDateToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {

                holder.getTextTime().setText("競標結束");

                Firebase.getFirestore().collection("products")
                        .document(String.valueOf(product.getProductId()))
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();

                                if (document.toObject(Product.class).getHighestUserId() == -1 ) {

                                    Firebase.getFirestore().collection("users")
                                            .document(String.valueOf(document.toObject(Product.class).getSellerId()))
                                            .update("nobodyBitProductsId", FieldValue.arrayUnion(product.getProductId()))
                                            .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Nobody Bit Products Id successfully added!"))
                                            .addOnFailureListener(e -> Log.w("Johnsi", "Nobody Bit Products Id Products Id Error updating document", e));

                                } else {

                                    Firebase.getFirestore().collection("users")
                                            .document(String.valueOf(document.toObject(Product.class).getHighestUserId()))
                                            .update("myBiddingProductsId", FieldValue.arrayRemove(product.getProductId()))
                                            .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Bidding Products Id successfully removed!"))
                                            .addOnFailureListener(e -> Log.w("Johnsi", "Bidding Products Id Error updating document", e));

                                    Firebase.getFirestore().collection("users")
                                            .document(String.valueOf(document.toObject(Product.class).getHighestUserId()))
                                            .update("myBoughtProductsId", FieldValue.arrayUnion(product.getProductId()))
                                            .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Bought Products Id successfully added!"))
                                            .addOnFailureListener(e -> Log.w("Johnsi", "Bought Products Id Error updating document", e));

                                    Firebase.getFirestore().collection("users")
                                            .document(String.valueOf(document.toObject(Product.class).getSellerId()))
                                            .update("mySoldProductsId", FieldValue.arrayUnion(product.getProductId()))
                                            .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Sold Products Id successfully added!"))
                                            .addOnFailureListener(e -> Log.w("Johnsi", "Sold Products Id Error updating document", e));

                                }

                                Firebase.getFirestore().collection("users")
                                        .document(String.valueOf(document.toObject(Product.class).getSellerId()))
                                        .update("mySellingProductsId", FieldValue.arrayRemove(product.getProductId()))
                                        .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Selling Products Id successfully removed!"))
                                        .addOnFailureListener(e -> Log.w("Johnsi", "Selling Products Id Error updating document", e));

                            } else {
                                Log.d("Johnsi", "Error getting documents: ", task.getException());
                            }
                        });

                Firebase.getFirestore().collection("products")
                        .document(String.valueOf(product.getProductId()))
                        .update("auctionCondition", "finish")
                        .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Product condition (finish) successfully updated!"))
                        .addOnFailureListener(e -> Log.w("Johnsi", "Product condition (finish) Error updating document", e));

            }
        }.start();

        countDownMap.put(holder.getTextTime().hashCode(), holder.countDownTimer);
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }


    public void updateData(ArrayList<Product> productList) {
        mProductList = productList;
        notifyDataSetChanged();
    }

    private String getDateToString(long millSeconds) {

        long days = millSeconds / (1000 * 60 * 60 * 24);
        long hours = (millSeconds - days * (1000 * 60 * 60 * 24)) / (1000* 60 * 60);
        long minutes = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000* 60 * 60)) / (1000 * 60);
        long seconds = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000* 60 * 60) - minutes * (1000 * 60)) / 1000;

        String time = days + "天 " + hours + "時 " + minutes + "分 " + seconds + "秒";
        return time;
    }

    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }

        for (int i = 0,length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

}
