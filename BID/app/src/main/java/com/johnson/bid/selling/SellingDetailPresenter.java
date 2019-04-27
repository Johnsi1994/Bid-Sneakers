package com.johnson.bid.selling;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;

import static com.google.common.base.Preconditions.checkNotNull;

public class SellingDetailPresenter implements SellingDetailContract.Presenter {

    private SellingDetailContract.View mSellingView;
    private Product mProduct;

    public SellingDetailPresenter(@NonNull SellingDetailContract.View sellingView) {
        mSellingView = checkNotNull(sellingView, "sellingView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void showToolbarAndBottomNavigation() {

    }

    @Override
    public void setSellingDetailData(Product product) {
        mProduct = product;
    }

    @Override
    public void loadSellingDetailData() {
        mSellingView.showSellingDetailUi(mProduct);
    }

    @Override
    public void openDeleteProductDialog(Product product) {

    }

    @Override
    public void loadSellingFreshData() {

        Firebase.getInstance().getFirestore().collection("products")
                .document(String.valueOf(mProduct.getProductId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        mProduct = document.toObject(Product.class);
                        loadSellingDetailData();

                    } else {
                        Log.d("Johnsi", "Error getting documents: ", task.getException());
                    }
                });

    }
}
