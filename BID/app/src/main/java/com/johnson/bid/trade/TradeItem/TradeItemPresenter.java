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
        mProductsList = new ArrayList<>();

        if (mProductIdList.size() > 0) {
            loadDataFromFireBase(0, "BIDDING");
        } else {
            setMyBiddingData(mProductsList);
        }
    }

    @Override
    public void setMyBiddingData(ArrayList<Product> productsList) {
        mTradeItemView.showTradeUi(productsList);
    }

    @Override
    public void loadMySellingData() {
        mProductIdList = UserManager.getInstance().getUser().getMySellingProductsId();
        mProductsList = new ArrayList<>();


        if (mProductIdList.size() > 0) {
            loadDataFromFireBase(0, "SELLING");
        } else {
            setMySellingData(mProductsList);
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

    @Override
    public void loadNobodyBidData() {
        mProductIdList = UserManager.getInstance().getUser().getNobodyBitProductsId();
        mProductsList = new ArrayList<>();

        if (mProductIdList.size() > 0) {
            loadDataFromFireBase(0, "NOBODYBIT");
        }
    }

    @Override
    public void setNobodyBidData(ArrayList<Product> productsList) {
        mTradeItemView.showTradeUi(productsList);
    }

    @Override
    public void hideToolbarAndBottomNavigation() {

    }

    @Override
    public void openBidding(String type, Product product) {

    }

    @Override
    public void openSelling(String auctionType, Product product) {

    }

    @Override
    public void openBoughtDetail(Product product) {

    }

    @Override
    public void openSoldDetail(Product product) {

    }

    @Override
    public void openNobodyBidDetail(Product product) {

    }

    @Override
    public void setBuyerHasRead(boolean hasRead, Product product) {

        Firebase.getFirestore().collection("products")
                .document(String.valueOf(product.getProductId()))
                .update("buyerHasRead", hasRead)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Product hasRead successfully added!");
                    loadMyBoughtData();
                })
                .addOnFailureListener(e -> Log.w("Johnsi", "Product hasRead Error updating document", e));

    }

    @Override
    public void setSellerHasRead(boolean hasRead, Product product, int from) {

        Firebase.getFirestore().collection("products")
                .document(String.valueOf(product.getProductId()))
                .update("sellerHasRead", hasRead)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Product hasRead successfully added!");

                    if (from == 1) {
                        loadMySoldData();
                    } else {
                        Log.d("JOHNSITESTING", "Load Nobody Bid Data !!!");
                        loadNobodyBidData();
                    }

                })
                .addOnFailureListener(e -> Log.w("Johnsi", "Product hasRead Error updating document", e));
    }

    @Override
    public void minusNobodyBidBadgeCount(LoadCallback loadCallback) {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("unreadNobodyBid", UserManager.getInstance().getUser().getUnreadNobodyBid() - 1)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Nobody Bid Badge Count successfully update!");
                    loadCallback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w("Johnsi", "Nobody Bid Badge Count Error updating document", e);
                    loadCallback.onFail(e.getMessage());
                });

    }

    @Override
    public void minusSoldBadgeCount(LoadCallback loadCallback) {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("unreadSold", UserManager.getInstance().getUser().getUnreadSold() - 1)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Sold Badge Count successfully update!");
                    loadCallback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w("Johnsi", "Sold Badge Count Error updating document", e);
                    loadCallback.onFail(e.getMessage());
                });
    }

    @Override
    public void minusBoughtBadgeCount(LoadCallback loadCallback) {

        Firebase.getFirestore().collection("users")
                .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                .update("unreadBought", UserManager.getInstance().getUser().getUnreadBought() - 1)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Johnsi", "Bought Badge Count successfully update!");
                    loadCallback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w("Johnsi", "Bought Badge Count Error updating document", e);
                    loadCallback.onFail(e.getMessage());
                });
    }

    @Override
    public void loadNobodyBidBadgeData() {

    }

    @Override
    public void updateTradeBadge() {

    }

    @Override
    public void loadSoldBadgeData() {

    }

    @Override
    public void loadBoughtBadgeData() {

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
                                    Log.d("Johnsi", "Start set bidding data");
                                    setMyBiddingData(mProductsList);
                                    break;
                                case "SELLING":
                                    setMySellingData(mProductsList);
                                    break;
                                case "BOUGHT":
                                    setMyBoughtData(mProductsList);
                                    break;
                                case "SOLD":
                                    setMySoldData(mProductsList);
                                    break;
                                case "NOBODYBIT":
                                    setNobodyBidData(mProductsList);
                                    break;
                                default:
                            }
                        }
                    } else {
                        Log.d("Johnsi", "Error getting documents: ", task.getException());
                    }
                });
    }

    public interface LoadCallback {

        void onSuccess();

        void onFail(String errorMessage);
    }
}
