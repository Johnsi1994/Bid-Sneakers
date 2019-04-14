package com.johnson.bid.trade.TradeItem;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class TradeItemPresenter implements TradeItemContract.Presenter {

    private TradeItemContract.View mTradeItemView;
    private ArrayList<Long> mProductIdList = new ArrayList<>();
    private ArrayList<Product> mProductsList;

    public TradeItemPresenter(@NonNull TradeItemContract.View tradeItemView) {
        mTradeItemView = checkNotNull(tradeItemView, "tradeItemView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void loadMyBiddingData() {
        mProductIdList = UserManager.getInstance().getUser().getMyBiddingProductsId();
        Log.d("Johnsi", "loadMyBiddingData size : " + mProductIdList.size());
        mProductsList = new ArrayList<>();

        if (mProductIdList.size() > 0) {
            loadDataFromFireBase(0, "BIDDING");
        }
    }

    @Override
    public void setMyBiddingData(ArrayList<Product> productsList) {
        mTradeItemView.showTradeUi(productsList);
    }

    @Override
    public void loadMySellingData() {
        mProductIdList = UserManager.getInstance().getUser().getMySellingProductsId();
        Log.d("Johnsi", "loadMySellingData size : " + mProductIdList.size());
        mProductsList = new ArrayList<>();

        if (mProductIdList.size() > 0) {
            loadDataFromFireBase(0, "SELLING");
        }
    }

    @Override
    public void setMySellingData(ArrayList<Product> productsList) {
        mTradeItemView.showTradeUi(productsList);
    }

    @Override
    public void loadMyBoughtData() {
        mProductIdList = UserManager.getInstance().getUser().getMyBoughtProductsId();
        mProductsList = new ArrayList<>();

        if (mProductIdList.size() > 0) {
            loadDataFromFireBase(0, "BOUGHT");
        }

    }

    @Override
    public void setMyBoughtData(ArrayList<Product> productsList) {
        mTradeItemView.showTradeUi(productsList);
    }

    @Override
    public void loadMySoldData() {
        mProductIdList = UserManager.getInstance().getUser().getMySoldProductsId();
        mProductsList = new ArrayList<>();

        if (mProductIdList.size() > 0) {
            loadDataFromFireBase(0, "SOLD");
        }
    }

    @Override
    public void setMySoldData(ArrayList<Product> productsList) {
        mTradeItemView.showTradeUi(productsList);
    }

    private void loadDataFromFireBase(int i, String type) {

        int j = i + 1;
        Firebase.getFirestore().collection("products")
                .document(String.valueOf(mProductIdList.get(i)))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        mProductsList.add(document.toObject(Product.class));

                        if (j < mProductIdList.size()) {
                            loadDataFromFireBase(j, type);
                        } else {
                            switch (type) {
                                case "BIDDING":
                                    setMyBiddingData(mProductsList);
                                    break;
                                case "SELLING":
                                    setMyBiddingData(mProductsList);
                                    break;
                                case "BOUGHT":
                                    setMyBiddingData(mProductsList);
                                    break;
                                case "SOLD":
                                    setMyBiddingData(mProductsList);
                                    break;
                                default:
                            }
                        }
                    } else {
                        Log.d("Johnsi", "Error getting documents: ", task.getException());
                    }
                });
    }
}
