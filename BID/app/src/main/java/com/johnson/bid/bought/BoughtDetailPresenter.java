package com.johnson.bid.bought;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.Bid;
import com.johnson.bid.R;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Constants;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.johnson.bid.MainMvpController.BOUGHTDETAIL;

public class BoughtDetailPresenter implements BoughtDetailContract.Presenter{

    private BoughtDetailContract.View mBoughtView;
    private Product mProduct;
    private boolean isConnecting = false;

    public BoughtDetailPresenter(@NonNull BoughtDetailContract.View boughtView) {
        mBoughtView = checkNotNull(boughtView, "boughtView cannot be null!");
    }

    @Override
    public void start() {}

    @Override
    public void showToolbarAndBottomNavigation() {}

    @Override
    public void loadBoughtDetailData() {

        mBoughtView.showBoughtDetailUi(mProduct);
    }

    @Override
    public void setBoughtDetailData(Product product) {
        mProduct = product;
    }

    @Override
    public void openChatContent(ChatRoom chatRoom, String from) {}

    @Override
    public void showToolbar() {}

    @Override
    public void updateToolbar(String name) {}

    @Override
    public void chatWithSeller() {

        if (!isConnecting) {
            isConnecting = true;
            findChatRoomStep1();
        }
    }

    private void findChatRoomStep1() {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_chatrooms))
                .whereEqualTo(Bid.getAppContext().getString(R.string.firebase_field_seller_id), UserManager.getInstance().getUser().getId())
                .whereEqualTo(Bid.getAppContext().getString(R.string.firebase_field_buyer_id), mProduct.getSellerId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mBoughtView.openChat(document);
                                isConnecting = false;
                            }
                        } else {
                            findChatRoomStep2();
                        }
                    } else {
                        Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void findChatRoomStep2() {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_chatrooms))
                .whereEqualTo(Bid.getAppContext().getString(R.string.firebase_field_buyer_id), UserManager.getInstance().getUser().getId())
                .whereEqualTo(Bid.getAppContext().getString(R.string.firebase_field_seller_id), mProduct.getSellerId())
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task1.getResult()) {
                            mBoughtView.openChat(document);
                            isConnecting = false;
                        }
                    }
                });
    }
}
