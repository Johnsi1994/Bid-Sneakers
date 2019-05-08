package com.johnson.bid.bought;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;

public interface BoughtDetailContract {

    interface View extends BaseView<Presenter> {
        void showBoughtDetailUi(Product product);

        void openChat(QueryDocumentSnapshot document);
    }

    interface Presenter extends BasePresenter {

        void showToolbarAndBottomNavigation();

        void loadBoughtDetailData();

        void setBoughtDetailData(Product product);

        void openChatContent(ChatRoom chatRoom, String from);

        void showToolbar();

        void updateToolbar(String name);

        void chatWithSeller();

    }

}
