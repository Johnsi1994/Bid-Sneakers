package com.johnson.bid.util;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.johnson.bid.data.Product;
import com.johnson.bid.data.User;

import java.util.ArrayList;

public class Firebase {

    private ArrayList<Product> mProductList;

    private static class FirebaseHolder {
        private static final Firebase INSTANCE = new Firebase();
    }

    private Firebase() {
    }

    public static Firebase getInstance() {
        return FirebaseHolder.INSTANCE;
    }


    private  FirebaseFirestore db = FirebaseFirestore.getInstance();

    private  StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public  FirebaseFirestore getFirestore() {
        return db;
    }

    public  StorageReference getStorage() {
        return mStorageRef;
    }

    public void getAllBiddingProductsFromFirebase(UserManager.LoadCallback loadCallback) {

        mProductList = new ArrayList<>();

        db.collection("products")
                .whereEqualTo("auctionCondition", "bidding")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mProductList.add(document.toObject(Product.class));
                        }

                        setAllBiddingProducts(mProductList);
                        loadCallback.onSuccess();
                    } else {
                        Log.d("Johnsi", "Error getting documents: ", task.getException());
                    }
                });

    }

    private void setAllBiddingProducts(ArrayList<Product> productList) {
        mProductList = productList;
    }

    public ArrayList<Product> getAllBiddingProducts() {
        return mProductList;
    }


}
