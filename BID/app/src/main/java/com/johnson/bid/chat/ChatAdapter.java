package com.johnson.bid.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.Bid;
import com.johnson.bid.R;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.util.ImageManager;
import com.johnson.bid.util.ProfileAvatarOutlineProvider;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.johnson.bid.MainMvpController.CHAT;

public class ChatAdapter extends RecyclerView.Adapter {

    private ChatContract.Presenter mPresenter;
    private ArrayList<ChatRoom> mChatRoomArrayList = new ArrayList<>();

    public ChatAdapter(ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChatAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_chat, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        bindViewHolder((ViewHolder) holder, mChatRoomArrayList.get(i));
    }

    @Override
    public int getItemCount() {

        if (mChatRoomArrayList == null) {
            return 0;
        } else {
            return mChatRoomArrayList.size();
        }

    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private ImageView mImageChatProfile;
        private TextView mTextName;
        private TextView mTextContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.layout_chat_brief);
            mImageChatProfile = itemView.findViewById(R.id.image_chat_profile);
            mImageChatProfile.setOutlineProvider(new ProfileAvatarOutlineProvider());
            mTextName = itemView.findViewById(R.id.text_chat_name);
            mTextContent = itemView.findViewById(R.id.text_chat_content);

        }

        private ImageView getImageChatProfile() {
            return mImageChatProfile;
        }

        private TextView getTextName() {
            return mTextName;
        }

        private TextView getTextContent() {
            return mTextContent;
        }

        private CardView getCardView() {
            return mCardView;
        }

    }

    private void bindViewHolder(ViewHolder holder, ChatRoom chatRoom) {

        ImageManager.getInstance().setImageByUrl(holder.getImageChatProfile(),
                (UserManager.getInstance().getUser().getId() == chatRoom.getSellerId()) ? chatRoom.getBuyerImage() : chatRoom.getSellerImage());

        holder.getTextName().setText(
                (UserManager.getInstance().getUser().getId() == chatRoom.getSellerId()) ? chatRoom.getBuyerName() : chatRoom.getSellerName());

        holder.getTextContent().setText((chatRoom.getChatContentArrayList().size() > 0) ?
                chatRoom.getChatContentArrayList().get((chatRoom.getChatContentArrayList().size() - 1)).getMessage() : Bid.getAppContext().getString(R.string.placeholder_start_chatting));

        holder.getCardView().setOnClickListener(v -> {

            mPresenter.openChatContent(chatRoom, CHAT);
            mPresenter.hideBottomNavigation();

            if (UserManager.getInstance().getUser().getId() == chatRoom.getSellerId()) {
                mPresenter.updateToolbar(chatRoom.getBuyerName());
            } else {
                mPresenter.updateToolbar(chatRoom.getSellerName());
            }
        });
    }

    public void updateData(ArrayList<ChatRoom> chatRoomArrayList) {
        mChatRoomArrayList = chatRoomArrayList;
        notifyDataSetChanged();
    }
}
