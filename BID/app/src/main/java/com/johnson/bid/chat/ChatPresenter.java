package com.johnson.bid.chat;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.johnson.bid.data.ChatBrief;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.data.User;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Long.parseLong;

public class ChatPresenter implements ChatContract.Presenter {

    private final ChatContract.View mChatView;
    private ArrayList<ChatBrief> mChatBriefArrayList;
    private ArrayList<ChatRoom> mChatRoomArrayList;
    private ChatBrief mChatBrief;

    public ChatPresenter(@NonNull ChatContract.View chatView) {
        mChatView = checkNotNull(chatView, "chatView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void openChatContent(ChatRoom chatRoom) {

    }

    @Override
    public void hideBottomNavigation() {

    }

    @Override
    public void loadChatData() {

        mChatRoomArrayList = new ArrayList<>();
        findAllChatRoom();

    }

    @Override
    public void setChatData(ArrayList<ChatRoom> chatRoomArrayList) {
        mChatView.showChatUI(chatRoomArrayList);
    }

    private void findAllChatRoom() {

        Firebase.getInstance().getFirestore().collection("chatrooms")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            mChatRoomArrayList.add(document.toObject(ChatRoom.class));
                        }

                        setChatData(mChatRoomArrayList);
                    } else {
                        Log.d("Johnsi", "Error getting documents: ", task.getException());
                    }
                });

    }
}
