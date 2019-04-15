package com.johnson.bid.centre.auction;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuctionPresenter implements AuctionContract.Presenter {

    private AuctionContract.View mAuctionView;
    private ArrayList<Product> mProductList;
    private ArrayList<String> mImages;
    private Product mProduct;

    public AuctionPresenter(@NonNull AuctionContract.View auctionView) {
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
                .whereEqualTo("auctionType", "English")
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
                .whereEqualTo("auctionType", "Sealed")
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

}
