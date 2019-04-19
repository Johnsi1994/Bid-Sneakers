package com.johnson.bid.auction.auctionitem;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;

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

        Firebase.getFirestore().collection("products")
                .whereEqualTo("auctionType", "一般拍賣")
                .whereEqualTo("auctionCondition", "bidding")
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

        Firebase.getFirestore().collection("products")
                .whereEqualTo("auctionType", "封閉拍賣")
                .whereEqualTo("auctionCondition", "bidding")
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

}
