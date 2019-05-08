package com.johnson.bid.auction.auctionitem;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.Bid;
import com.johnson.bid.R;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.data.User;
import com.johnson.bid.util.Constants;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuctionItemPresenter implements AuctionItemContract.Presenter {

    private AuctionItemContract.View mAuctionView;
    private ArrayList<Product> mProductList;

    public AuctionItemPresenter(@NonNull AuctionItemContract.View auctionView) {
        mAuctionView = checkNotNull(auctionView, "centerView cannot be null!");
    }

    @Override
    public void start() {}

    @Override
    public void openBidding(String auctionType, Product product) {}

    @Override
    public void hideToolbarAndBottomNavigation() {}

    @Override
    public void loadEnglishData() {

        mProductList = new ArrayList<>();

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_products))
                .whereEqualTo(Bid.getAppContext().getString(R.string.firebase_field_auction_type), Bid.getAppContext().getString(R.string.firebase_auction_type_English))
                .whereEqualTo(Bid.getAppContext().getString(R.string.firebase_field_auction_condition), Bid.getAppContext().getString(R.string.firebase_auction_condition_bidding))
                .orderBy(Bid.getAppContext().getString(R.string.firebase_field_start_time), Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mProductList.add(document.toObject(Product.class));
                        }

                        setEnglishData(mProductList);
                    } else {
                        Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void setEnglishData(ArrayList<Product> productList) {
        mAuctionView.showAuctionUi(productList);
    }

    @Override
    public void loadSealedData() {

        mProductList = new ArrayList<>();

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_products))
                .whereEqualTo(Bid.getAppContext().getString(R.string.firebase_field_auction_type), Bid.getAppContext().getString(R.string.firebase_auction_type_sealed))
                .whereEqualTo(Bid.getAppContext().getString(R.string.firebase_field_auction_condition), Bid.getAppContext().getString(R.string.firebase_auction_condition_bidding))
                .orderBy(Bid.getAppContext().getString(R.string.firebase_field_start_time), Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mProductList.add(document.toObject(Product.class));
                        }

                        setSealedData(mProductList);
                    } else {
                        Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void setSealedData(ArrayList<Product> productList) {
        mAuctionView.showAuctionUi(productList);
    }

    @Override
    public void loadMySoldData() {}

    @Override
    public void loadNobodyBidData() {}

    @Override
    public void loadMySellingData() {}

    @Override
    public void loadMyBiddingData() {}

    @Override
    public void loadMyBoughtData() {}

    @Override
    public void loadNobodyBidBadgeData() {}

    @Override
    public void loadSoldBadgeData() {}

    @Override
    public void loadBoughtBadgeData() {}

    @Override
    public void updateTradeBadge() {}

    @Override
    public void openSelling(String from, Product product) {}

    @Override
    public void removeSellingProductId(long productId, String from) {
        UserManager.getInstance().removeSellingProductId(productId);
        UserManager.getInstance().setHasUserDataChange(true);
    }

    @Override
    public void addNobodyBidProductId(long productId, String from) {
        UserManager.getInstance().addNobodyBidProductId(productId);
        UserManager.getInstance().setHasUserDataChange(true);
    }

    @Override
    public void addSoldProductId(long productId, String from) {
        UserManager.getInstance().addSoldProductId(productId);
        UserManager.getInstance().setHasUserDataChange(true);
    }

    @Override
    public void removeBiddingProductId(long productId, String from) {
        UserManager.getInstance().removeBiddingProductId(productId);
        UserManager.getInstance().setHasUserDataChange(true);
    }

    @Override
    public void addBoughtProductId(long productId, String from) {
        UserManager.getInstance().addBoughtProductId(productId);
        UserManager.getInstance().setHasUserDataChange(true);
    }

    @Override
    public void increaseUnreadNobodyBid(String from) {
        UserManager.getInstance().increaseUnreadNobodyBid();
        UserManager.getInstance().setHasUserDataChange(true);
    }

    @Override
    public void increaseUnreadSold(String from) {
        UserManager.getInstance().increaseUnreadSold();
        UserManager.getInstance().setHasUserDataChange(true);
    }

    @Override
    public void increaseUnreadBought(String from) {
        UserManager.getInstance().increaseUnreadBought();
        UserManager.getInstance().setHasUserDataChange(true);
    }

    @Override
    public void productResult(Product product, String from) {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_products))
                .document(String.valueOf(product.getProductId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        Product latestProduct = document.toObject(Product.class);

                        if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId() ||
                                UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {

                            updateProductAuctionCondition(latestProduct);
                            if (UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()
                                    && latestProduct.getHighestUserId() == -1) {

                                mAuctionView.showSellingFailUi(latestProduct, from);
                            } else {
                                if (UserManager.getInstance().getUser().getId() == latestProduct.getSellerId()) {
                                    if (latestProduct.getReservePrice() > latestProduct.getCurrentPrice()) {

                                        mAuctionView.showSellingFailUi(latestProduct, from);
                                    } else {

                                        mAuctionView.showSoldSuccessUi(latestProduct, from);
                                    }
                                } else if (UserManager.getInstance().getUser().getId() == latestProduct.getHighestUserId()) {
                                    if (latestProduct.getReservePrice() < latestProduct.getCurrentPrice()) {

                                        mAuctionView.showBoughtSuccessUi(latestProduct, from);
                                    } else {

                                        mAuctionView.showMyBiddingData();
                                    }
                                }
                            }

                            uploadUser();
                            mAuctionView.showTradeBadge();
                        }

                    } else {
                        Log.d(Constants.TAG, "Error getting documents ", task.getException());
                    }
                });
    }

    private void updateProductAuctionCondition(Product latestProduct) {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_products))
                .document(String.valueOf(latestProduct.getProductId()))
                .update(Bid.getAppContext().getString(R.string.firebase_field_auction_condition), Bid.getAppContext().getString(R.string.firebase_auction_condition_finish))
                .addOnSuccessListener(aVoid -> Log.d(Constants.TAG, "Product condition (finish) successfully updated!"))
                .addOnFailureListener(e -> Log.w(Constants.TAG, "Product condition (finish) Error updating document", e));
    }

    private void uploadUser() {

        UserManager.getInstance().updateUser2Firebase();
        UserManager.getInstance().setHasUserDataChange(false);
    }

    @Override
    public void createChatRoom(Product product, String from) {

        ChatRoom chatRoom = new ChatRoom();

        if (UserManager.getInstance().getUser().getId() == product.getSellerId()) {

            UserManager.getInstance().setChatList(product.getHighestUserId());
            UserManager.getInstance().setHasUserDataChange(true);

            sellerCreateRoom(product,chatRoom);
        } else {

            UserManager.getInstance().setChatList(product.getSellerId());
            UserManager.getInstance().setHasUserDataChange(true);

            buyerCreateRoom(product,chatRoom);
        }
    }

    private void sellerCreateRoom(Product product, ChatRoom chatRoom) {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_users))
                .document(String.valueOf(product.getHighestUserId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();

                        chatRoom.setBuyerImage(document.toObject(User.class).getImage());
                        chatRoom.setSellerImage(UserManager.getInstance().getUser().getImage());
                        setChatRoom(chatRoom, product);
                    } else {
                        Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void buyerCreateRoom(Product product, ChatRoom chatRoom) {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_users))
                .document(String.valueOf(product.getSellerId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        chatRoom.setSellerImage(document.toObject(User.class).getImage());
                        chatRoom.setBuyerImage(UserManager.getInstance().getUser().getImage());
                        setChatRoom(chatRoom, product);
                    } else {
                        Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void setChatRoom(ChatRoom chatRoom, Product product) {

        chatRoom.setChatRoomId(product.getProductId());
        chatRoom.setBuyerId(product.getHighestUserId());
        chatRoom.setSellerId(product.getSellerId());
        chatRoom.setBuyerName(product.getBuyerName());
        chatRoom.setSellerName(product.getSellerName());
        setOwnerList(chatRoom, product);

        uploadChatRoom(chatRoom);
    }

    private void uploadChatRoom(ChatRoom chatRoom) {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_chatrooms))
                .document(String.valueOf(chatRoom.getChatRoomId()))
                .set(chatRoom)
                .addOnSuccessListener(documentReference -> Log.d(Constants.TAG, "DocumentSnapshot added"))
                .addOnFailureListener(e -> Log.w(Constants.TAG, "Error adding document", e));
    }

    private void setOwnerList(ChatRoom chatRoom, Product product) {

        ArrayList<Long> ownerList = chatRoom.getOwnerList();
        ownerList.add(product.getHighestUserId());
        ownerList.add(product.getSellerId());
    }
}
