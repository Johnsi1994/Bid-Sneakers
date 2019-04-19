package com.johnson.bid.auction.auctionitem;

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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;
import com.johnson.bid.data.User;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.ImageManager;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.SEALED;

public class AuctionItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_PRODUCT = 0x01;

    private AuctionItemContract.Presenter mPresenter;
    private String mAuctionType;
    private ArrayList<Product> mProductList;
    private SparseArray<CountDownTimer> countDownMap;


    public AuctionItemAdapter(AuctionItemContract.Presenter presenter, @MainMvpController.AuctionType String auctionType) {
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

            if (UserManager.getInstance().getUser().getId() == product.getSellerId()) {

                mPresenter.openSelling(ENGLISH, product);
            } else {

                mPresenter.openBidding(ENGLISH, product);
            }

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
                                Product latestProduct = document.toObject(Product.class);

                                if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId() ||
                                        UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {
                                    updateProductAuctionCondition(latestProduct);

                                    UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
                                        @Override
                                        public void onSuccess() {

                                            if (latestProduct.getHighestUserId() == -1
                                                    && latestProduct.getSellerId() == UserManager.getInstance().getUser().getId()) {

                                                //3rd parameter : 0 Nobody Bid / 1 somebody won the auction
                                                removeSellingProductsIdOnFirebase(latestProduct, 0);

                                            } else {

                                                if (UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {

                                                    removeSellingProductsIdOnFirebase(latestProduct, 1);

                                                } else if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId()) {

                                                    removeBiddingProductsIdOnFirebase(latestProduct);

                                                }
                                            }
                                        }

                                        @Override
                                        public void onFail(String errorMessage) {

                                        }
                                    });
                                }
                            } else {
                                Log.d("Johnsi", "Error getting documents: ", task.getException());
                            }
                        });
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

            if (UserManager.getInstance().getUser().getId() == product.getSellerId()) {

                mPresenter.openSelling(SEALED, product);
            } else {

                mPresenter.openBidding(SEALED, product);
            }

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
                                Product latestProduct = document.toObject(Product.class);

                                if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId() ||
                                        UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {
                                    updateProductAuctionCondition(latestProduct);

                                    UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
                                        @Override
                                        public void onSuccess() {

                                            if (latestProduct.getHighestUserId() == -1
                                                    && latestProduct.getSellerId() == UserManager.getInstance().getUser().getId()) {

                                                //3rd parameter : 0 Nobody Bid / 1 somebody won the auction
                                                removeSellingProductsIdOnFirebase(latestProduct, 0);

                                            } else {

                                                if (UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {

                                                    removeSellingProductsIdOnFirebase(latestProduct, 1);

                                                } else if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId()) {

                                                    removeBiddingProductsIdOnFirebase(latestProduct);

                                                }
                                            }
                                        }

                                        @Override
                                        public void onFail(String errorMessage) {

                                        }
                                    });
                                }
                            } else {
                                Log.d("Johnsi", "Error getting documents: ", task.getException());
                            }
                        });
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
        long hours = (millSeconds - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;

        String time = days + "天 " + hours + "時 " + minutes + "分 " + seconds + "秒";
        return time;
    }

    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }

        for (int i = 0, length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }


    private void removeSellingProductsIdOnFirebase(Product product, int i) {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("mySellingProductsId", FieldValue.arrayRemove(product.getProductId()))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Selling Products Id successfully removed!");

                    if (i == 0) {

                        addNobodyBitProductsIdOnFirebase(product);
                    } else {

                        addSoldProductsIdOnFirebase(product);
                    }

                })
                .addOnFailureListener(e -> {
                    Log.w("Johnsi", "Selling Products Id Error updating document", e);
                });

    }

    private void addNobodyBitProductsIdOnFirebase(Product product) {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("nobodyBitProductsId", FieldValue.arrayUnion(product.getProductId()))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Nobody Bit Products Id successfully added!");
                    updateUnreadNobodyBidOnFirebase();
                })
                .addOnFailureListener(e -> Log.w("Johnsi", "Nobody Bit Products Id Products Id Error updating document", e));

    }

    private void updateUnreadNobodyBidOnFirebase() {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("unreadNobodyBid", UserManager.getInstance().getUser().getUnreadNobodyBid() + 1)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Unread Nobody Bit Count successfully added!");

                    UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
                        @Override
                        public void onSuccess() {

                            mPresenter.loadMySellingData();
                            mPresenter.loadNobodyBidData();
                            mPresenter.loadNobodyBidBadgeData();
                            mPresenter.updateTradeBadge();

                        }

                        @Override
                        public void onFail(String errorMessage) {

                        }
                    });

                })
                .addOnFailureListener(e -> Log.w("Johnsi", "Unread Nobody Bit Count Error updating document", e));

    }

    private void addSoldProductsIdOnFirebase(Product product) {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("mySoldProductsId", FieldValue.arrayUnion(product.getProductId()))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Sold Products Id successfully added!");
                    updateUnreadSoldOnFirebase();
                })
                .addOnFailureListener(e -> Log.w("Johnsi", "Sold Products Id Error updating document", e));

    }


    private void updateUnreadSoldOnFirebase() {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("unreadSold", UserManager.getInstance().getUser().getUnreadSold() + 1)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Unread Sold Count successfully added!");

                    UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
                        @Override
                        public void onSuccess() {

                            mPresenter.loadMySellingData();
                            mPresenter.loadMySoldData();
                            mPresenter.loadSoldBadgeData();
                            mPresenter.updateTradeBadge();

                        }

                        @Override
                        public void onFail(String errorMessage) {

                        }
                    });

                })
                .addOnFailureListener(e -> Log.w("Johnsi", "Unread Sold Count Error updating document", e));

    }

    private void removeBiddingProductsIdOnFirebase(Product product) {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("myBiddingProductsId", FieldValue.arrayRemove(product.getProductId()))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Bidding Products Id successfully removed!");
                    addBoughtProductsIdOnFirebase(product);
                })
                .addOnFailureListener(e -> Log.w("Johnsi", "Bidding Products Id Error updating document", e));

    }

    private void addBoughtProductsIdOnFirebase(Product product) {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("myBoughtProductsId", FieldValue.arrayUnion(product.getProductId()))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Bought Products Id successfully added!");
                    updateUnreadBoughtOnFirebase();
                })
                .addOnFailureListener(e -> Log.w("Johnsi", "Bought Products Id Error updating document", e));

    }


    private void updateUnreadBoughtOnFirebase() {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("unreadBought", UserManager.getInstance().getUser().getUnreadBought() + 1)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Unread Bought Count successfully added!");

                    UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
                        @Override
                        public void onSuccess() {

                            mPresenter.loadMyBiddingData();
                            mPresenter.loadMyBoughtData();
                            mPresenter.loadBoughtBadgeData();
                            mPresenter.updateTradeBadge();

                        }

                        @Override
                        public void onFail(String errorMessage) {

                        }
                    });

                })
                .addOnFailureListener(e -> Log.w("Johnsi", "Unread Bought Count Error updating document", e));

    }

    private void updateProductAuctionCondition(Product latestProduct) {

        Firebase.getFirestore().collection("products")
                .document(String.valueOf(latestProduct.getProductId()))
                .update("auctionCondition", "finish")
                .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Product condition (finish) successfully updated!"))
                .addOnFailureListener(e -> Log.w("Johnsi", "Product condition (finish) Error updating document", e));

    }

}
