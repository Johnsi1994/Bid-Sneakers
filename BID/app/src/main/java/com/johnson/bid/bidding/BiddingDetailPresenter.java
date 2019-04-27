package com.johnson.bid.bidding;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;

import static com.google.common.base.Preconditions.checkNotNull;

public class BiddingDetailPresenter implements BiddingDetailContract.Presenter {

    private final BiddingDetailContract.View mBiddingView;
    private Product mProduct;

    public BiddingDetailPresenter(@NonNull BiddingDetailContract.View biddingView) {
        mBiddingView = checkNotNull(biddingView, "biddingView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void openCenter() {

    }

    @Override
    public void showToolbarAndBottomNavigation() {

    }

    @Override
    public void setProductData(Product product) {
        mProduct = product;
        loadProductData();
    }

    @Override
    public void loadProductData() {
        mBiddingView.showBiddingUi(mProduct);
    }

    @Override
    public void openBidDialog(String from, Product product) {

    }

    @Override
    public void updateCenterData() {

    }

    @Override
    public void loadBiddingFreshData() {

        Firebase.getInstance().getFirestore().collection("products")
                .document(String.valueOf(mProduct.getProductId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        mProduct = document.toObject(Product.class);
                        loadProductData();

                    } else {
                        Log.d("Johnsi", "Error getting documents: ", task.getException());
                    }
                });

    }

}
