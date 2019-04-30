package com.johnson.bid.auction.auctionitem;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firestore.v1.StructuredQuery;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuctionItemPresenter implements AuctionItemContract.Presenter {

    private AuctionItemContract.View mAuctionView;
    private ArrayList<Product> mProductList;
    private ArrayList<String> mImages;
    private Product mProduct;

    public AuctionItemPresenter(@NonNull AuctionItemContract.View auctionView) {
        mAuctionView = checkNotNull(auctionView, "centerView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void openBidding(String auctionType, Product product) {

    }

    @Override
    public void hideToolbarAndBottomNavigation() {

    }

    @Override
    public void loadEnglishData() {

        mProductList = new ArrayList<>();

        Firebase.getInstance().getFirestore().collection("products")
                .whereEqualTo("auctionType", "一般拍賣")
                .whereEqualTo("auctionCondition", "bidding")
                .orderBy("startTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mProductList.add(document.toObject(Product.class));
                        }

                        setEnglishData(mProductList);
                    } else {
                        Log.d("Johnsi", "Error getting documents: ", task.getException());
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

        Firebase.getInstance().getFirestore().collection("products")
                .whereEqualTo("auctionType", "封閉拍賣")
                .whereEqualTo("auctionCondition", "bidding")
                .orderBy("startTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mProductList.add(document.toObject(Product.class));
                        }

                        setSealedData(mProductList);
                    } else {
                        Log.d("Johnsi", "Error getting documents: ", task.getException());
                    }
                });

    }

    @Override
    public void setSealedData(ArrayList<Product> productList) {
        mAuctionView.showAuctionUi(productList);
    }

    @Override
    public void loadMySoldData() {

    }

    @Override
    public void loadNobodyBidData() {

    }

    @Override
    public void loadMySellingData() {

    }

    @Override
    public void loadMyBiddingData() {

    }

    @Override
    public void loadMyBoughtData() {

    }

    @Override
    public void loadNobodyBidBadgeData() {

    }

    @Override
    public void loadSoldBadgeData() {

    }

    @Override
    public void loadBoughtBadgeData() {

    }

    @Override
    public void updateTradeBadge() {

    }

    @Override
    public void openSelling(String from, Product product) {

    }

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

}
