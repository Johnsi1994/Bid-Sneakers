package com.johnson.bid.chat;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.Bid;
import com.johnson.bid.R;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.util.Constants;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatPresenter implements ChatContract.Presenter {

    private final ChatContract.View mChatView;
    private ArrayList<ChatRoom> mChatRoomArrayList;

    public ChatPresenter(@NonNull ChatContract.View chatView) {
        mChatView = checkNotNull(chatView, "chatView cannot be null!");
    }

    @Override
    public void start() {}

    @Override
    public void openChatContent(ChatRoom chatRoom, String from) {}

    @Override
    public void hideBottomNavigation() {}

    @Override
    public void loadChatData() {

        mChatRoomArrayList = new ArrayList<>();
        findAllChatRoom();
    }

    @Override
    public void setChatData(ArrayList<ChatRoom> chatRoomArrayList) {
        mChatView.showChatUI(chatRoomArrayList);
    }

    @Override
    public void updateToolbar(String name) {}

    private void findAllChatRoom() {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_chatrooms))
                .whereArrayContains(Bid.getAppContext().getString(R.string.firebase_field_owner_list), UserManager.getInstance().getUser().getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mChatRoomArrayList.add(document.toObject(ChatRoom.class));
                        }

                        setChatData(mChatRoomArrayList);
                    } else {
                        Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
