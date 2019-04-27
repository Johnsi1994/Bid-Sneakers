package com.johnson.bid.chat;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.ChatBrief;
import com.johnson.bid.data.ChatRoom;

import java.util.ArrayList;

public interface ChatContract {

    interface View extends BaseView<Presenter> {

        void showChatUI(ArrayList<ChatRoom> chatRoomArrayList);

    }

    interface Presenter extends BasePresenter {

        void openChatContent(ChatRoom chatRoom);

        void hideBottomNavigation();

        void loadChatData();

        void setChatData(ArrayList<ChatRoom> chatRoomArrayList);

    }

}
