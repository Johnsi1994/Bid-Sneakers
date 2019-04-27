package com.johnson.bid.chat.chatcontent;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.ChatContent;
import com.johnson.bid.data.ChatRoom;

import java.util.ArrayList;

public class ChatContentContract {

    interface View extends BaseView<Presenter> {

        void showChatContentUi(ArrayList<ChatContent> mChatContentArrayList);

    }

    public interface Presenter extends BasePresenter {

        void showBottomNavigation();

        void sendMessage(ChatContent chatContent);

        void setChatRoomData(ChatRoom chatRoom);

        void loadChatContentData();

        void setChatListener();

    }

}
