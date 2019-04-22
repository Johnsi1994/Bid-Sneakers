package com.johnson.bid.eyeson;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class EyesOnPresenter implements EyesOnContract.Presenter {

    private EyesOnContract.View mEyesOnView;
    private ArrayList<Product> mProductsList;
    ArrayList<Long> mEyesOnList;

    public EyesOnPresenter(@NonNull EyesOnContract.View eyesOnView) {
        mEyesOnView = checkNotNull(eyesOnView, "eyesOnView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void showBottomNavigation() {

    }

    @Override
    public void updateToolbar(String title) {

    }

    @Override
    public void openBidding(String from, Product product) {

    }

    @Override
    public void hideToolbarAndBottomNavigation() {

    }

    @Override
    public void loadEyesOnData() {

        mEyesOnList =  UserManager.getInstance().getUser().getEyesOn();
        mProductsList = new ArrayList<>();

        if (mEyesOnList.size() > 0) {

            loadDataFromFireBase(0);
        } else {

            setMyEyesOnData(mProductsList);
        }
    }

    @Override
    public void setMyEyesOnData(ArrayList<Product> products) {
        mEyesOnView.showEyesOnUi(products);
    }

    private void loadDataFromFireBase(int i) {

        int j = i + 1;
        Firebase.getFirestore().collection("products")
                .document(String.valueOf(mEyesOnList.get(i)))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        mProductsList.add(document.toObject(Product.class));

                        if (j < mEyesOnList.size()) {

                            loadDataFromFireBase(j);
                        } else {

                            setMyEyesOnData(mProductsList);
                        }
                    } else {
                        Log.d("Johnsi", "Error getting documents: ", task.getException());
                    }
                });
    }
}
