package com.johnson.bid.auction.auctionitem;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.data.User;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.ImageManager;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

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
                        .inflate(R.layout.item_product_most, viewGroup, false));
            } else {
                return new SealedAuctionViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_product_sealed, viewGroup, false));
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

        private CardView mLayoutEnglishAuction;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPlaceBidTimes;
        CountDownTimer countDownTimer;

        private EnglishAuctionViewHolder(View itemView) {
            super(itemView);

            mLayoutEnglishAuction = itemView.findViewById(R.id.layout_product_most_brief);
            mImageMain = itemView.findViewById(R.id.image_product_most);
            mTextTitle = itemView.findViewById(R.id.text_product_most_title);
            mTextTime = itemView.findViewById(R.id.text_product_most_remaining_time);
            mTextPrice = itemView.findViewById(R.id.text_product_most_price);
            mTextPlaceBidTimes = itemView.findViewById(R.id.text_product_most_place_bid_times);
        }

        private CardView getLayoutEnglishAuction() {
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

        private TextView getTextPlaceBidTimes() {
            return mTextPlaceBidTimes;
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

                Firebase.getInstance().getFirestore().collection("products")
                        .document(String.valueOf(product.getProductId()))
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                Product latestProduct = document.toObject(Product.class);

                                if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId() ||
                                        UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {
                                    updateProductAuctionCondition(latestProduct);

                                    if (latestProduct.getHighestUserId() == -1
                                            && latestProduct.getSellerId() == UserManager.getInstance().getUser().getId()) {

                                        mPresenter.removeSellingProductId(latestProduct.getProductId(), ENGLISH);
                                        mPresenter.addNobodyBidProductId(latestProduct.getProductId(), ENGLISH);
                                        mPresenter.increaseUnreadNobodyBid(ENGLISH);

                                        mPresenter.loadMySellingData();
                                        mPresenter.loadNobodyBidData();
                                        mPresenter.loadNobodyBidBadgeData();


                                    } else {

                                        if (UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {

                                            mPresenter.removeSellingProductId(latestProduct.getProductId(), ENGLISH);
                                            mPresenter.addSoldProductId(latestProduct.getProductId(), ENGLISH);
                                            mPresenter.increaseUnreadSold(ENGLISH);

                                            mPresenter.loadMySellingData();
                                            mPresenter.loadMySoldData();
                                            mPresenter.loadSoldBadgeData();

                                        } else if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId()) {

                                            mPresenter.removeBiddingProductId(latestProduct.getProductId(), ENGLISH);
                                            mPresenter.addBoughtProductId(latestProduct.getProductId(), ENGLISH);
                                            mPresenter.increaseUnreadBought(ENGLISH);

                                            mPresenter.loadMyBiddingData();
                                            mPresenter.loadMyBoughtData();
                                            mPresenter.loadBoughtBadgeData();

                                        }

                                        if (UserManager.getInstance().isHasUserDataChange()) {

                                            UserManager.getInstance().updateUser2Firebase();
                                            UserManager.getInstance().setHasUserDataChange(false);
                                        }
//                                        createChatRoom(product);
                                    }

                                    mPresenter.updateTradeBadge();
                                }

                            } else {
                                Log.d("Johnsi", "Error getting documents: ", task.getException());
                            }
                        });
            }
        }.start();

        countDownMap.put(holder.getTextTime().hashCode(), holder.countDownTimer);

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextPlaceBidTimes().setText(String.valueOf(product.getPlaceBidTimes()));

    }

    private class SealedAuctionViewHolder extends RecyclerView.ViewHolder {

        private CardView mLayoutSealedAuction;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        CountDownTimer countDownTimer;

        private SealedAuctionViewHolder(View itemView) {
            super(itemView);

            mLayoutSealedAuction = itemView.findViewById(R.id.layout_product_sealed_brief);
            mImageMain = itemView.findViewById(R.id.image_product_sealed);
            mTextTitle = itemView.findViewById(R.id.text_product_sealed_title);
            mTextTime = itemView.findViewById(R.id.text_product_sealed_remaining_time);

        }

        private CardView getLayoutSealedAuction() {
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

                Firebase.getInstance().getFirestore().collection("products")
                        .document(String.valueOf(product.getProductId()))
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                Product latestProduct = document.toObject(Product.class);

                                if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId() ||
                                        UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {
                                    updateProductAuctionCondition(latestProduct);

                                    if (latestProduct.getHighestUserId() == -1
                                            && latestProduct.getSellerId() == UserManager.getInstance().getUser().getId()) {

                                        mPresenter.removeSellingProductId(latestProduct.getProductId(), SEALED);
                                        mPresenter.addNobodyBidProductId(latestProduct.getProductId(), SEALED);
                                        mPresenter.increaseUnreadNobodyBid(SEALED);

                                        mPresenter.loadMySellingData();
                                        mPresenter.loadNobodyBidData();
                                        mPresenter.loadNobodyBidBadgeData();

                                    } else {

                                        if (UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {

                                            mPresenter.removeSellingProductId(latestProduct.getProductId(), SEALED);
                                            mPresenter.addSoldProductId(latestProduct.getProductId(), SEALED);
                                            mPresenter.increaseUnreadSold(SEALED);

                                            mPresenter.loadMySellingData();
                                            mPresenter.loadMySoldData();
                                            mPresenter.loadSoldBadgeData();

                                        } else if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId()) {

                                            mPresenter.removeBiddingProductId(latestProduct.getProductId(), SEALED);
                                            mPresenter.addBoughtProductId(latestProduct.getProductId(), SEALED);
                                            mPresenter.increaseUnreadBought(SEALED);

                                            mPresenter.loadMyBiddingData();
                                            mPresenter.loadMyBoughtData();
                                            mPresenter.loadBoughtBadgeData();

                                        }

                                        if (UserManager.getInstance().isHasUserDataChange()) {

                                            UserManager.getInstance().updateUser2Firebase();
                                            UserManager.getInstance().setHasUserDataChange(false);
                                        }
//                                        createChatRoom(product);
                                    }

                                    mPresenter.updateTradeBadge();
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

        String time = days + " 天 " + hours + " 時 " + minutes + " 分 " + seconds + " 秒";
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

    private void updateProductAuctionCondition(Product latestProduct) {

        Firebase.getInstance().getFirestore().collection("products")
                .document(String.valueOf(latestProduct.getProductId()))
                .update("auctionCondition", "finish")
                .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Product condition (finish) successfully updated!"))
                .addOnFailureListener(e -> Log.w("Johnsi", "Product condition (finish) Error updating document", e));

    }

    private void createChatRoom(Product product) {

        ChatRoom chatRoom = new ChatRoom();

        if (UserManager.getInstance().getUser().getId() == product.getSellerId()) {
            Firebase.getInstance().getFirestore().collection("users")
                    .document(String.valueOf(product.getHighestUserId()))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();

//                            if (document.toObject(User.class).getImage() == null) {
//                                Log.d("documentCheck", "getImage is null ");
//                            } else {
//                                Log.d("documentCheck", "getImage not null ");
//                            }

                            chatRoom.setBuyerImage(document.toObject(User.class).getImage());
                            chatRoom.setSellerImage(UserManager.getInstance().getUser().getImage());
                            chatRoom.setChatRoomId(product.getProductId());
                            chatRoom.setBuyerId(product.getHighestUserId());
                            chatRoom.setSellerId(product.getSellerId());

                            uploadChatRoom(chatRoom);

                        } else {
                            Log.d("Johnsi", "Error getting documents: ", task.getException());
                        }
                    });

        } else {
            Firebase.getInstance().getFirestore().collection("users")
                    .document(String.valueOf(product.getSellerId()))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            chatRoom.setSellerImage(document.toObject(User.class).getImage());
                            chatRoom.setBuyerImage(UserManager.getInstance().getUser().getImage());
                            chatRoom.setChatRoomId(product.getProductId());
                            chatRoom.setBuyerId(product.getHighestUserId());
                            chatRoom.setSellerId(product.getSellerId());

                            uploadChatRoom(chatRoom);

                        } else {
                            Log.d("Johnsi", "Error getting documents: ", task.getException());
                        }
                    });

        }

    }

    private void uploadChatRoom(ChatRoom chatRoom) {

        Firebase.getInstance().getFirestore().collection("chatrooms")
                .document(String.valueOf(chatRoom.getChatRoomId()))
                .set(chatRoom)
                .addOnSuccessListener(documentReference -> Log.d("Johnsi", "DocumentSnapshot added"))
                .addOnFailureListener(e -> Log.w("Johnsi", "Error adding document", e));

    }
}
