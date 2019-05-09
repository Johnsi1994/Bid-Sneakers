package com.johnson.bid.sold;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;

public interface SoldDetailContract {

    interface View extends BaseView<Presenter> {

        void showSoldDetailUi(Product product);

        void openChat(QueryDocumentSnapshot document);
    }

    interface Presenter extends BasePresenter {

        void setSoldDetailData(Product product);

        void loadSoldDetailData();

        void showToolbarAndBottomNavigation();

        void openChatContent(ChatRoom chatRoom, String from);

        void hideBottomNavigation();

        void updateToolbar(String name);

        void showToolbar();

        void chatWithBuyer();

    }

}
