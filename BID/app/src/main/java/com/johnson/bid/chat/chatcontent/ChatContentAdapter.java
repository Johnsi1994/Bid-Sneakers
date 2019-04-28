package com.johnson.bid.chat.chatcontent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.R;
import com.johnson.bid.data.ChatContent;
import com.johnson.bid.util.ImageManager;
import com.johnson.bid.util.ProfileAvatarOutlineProvider;
import com.johnson.bid.util.UserManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatContentAdapter extends RecyclerView.Adapter {

    private ChatContentContract.Presenter mPresenter;
    private ArrayList<ChatContent> mChatContentArrayList;
    private static final int TYPE_ME     = 0x01;
    private static final int TYPE_HE      = 0x02;

    public ChatContentAdapter(ChatContentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_ME) {
            return new MyContentViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_chat_content_me, viewGroup, false));
        } else {
            return new HisContentViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_chat_content_not_me, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof MyContentViewHolder) {
            bindMyContentViewHolder((MyContentViewHolder) holder, mChatContentArrayList.get(i));
        } else {
            bindHisContentViewHolder((HisContentViewHolder) holder, mChatContentArrayList.get(i));
        }
    }

    @Override
    public int getItemCount() {

        if (mChatContentArrayList == null) {
            return 0;
        } else {
            return mChatContentArrayList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (mChatContentArrayList.get(position).getAuthorId() == UserManager.getInstance().getUser().getId()) {
            return TYPE_ME;
        }  else {
            return TYPE_HE;
        }
    }

    private class MyContentViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMyProfile;
        private TextView mContent;
        private TextView mTime;


        public MyContentViewHolder(@NonNull View itemView) {
            super(itemView);

            mMyProfile = itemView.findViewById(R.id.image_profile_me);
            mMyProfile.setOutlineProvider(new ProfileAvatarOutlineProvider());
            mContent = itemView.findViewById(R.id.text_content_me);
            mTime = itemView.findViewById(R.id.text_time_me);

        }

        private ImageView getChatProfile() {
            return mMyProfile;
        }

        private TextView getTime() {
            return mTime;
        }

        private TextView getContent() {
            return mContent;
        }

    }

    private void bindMyContentViewHolder(MyContentViewHolder holder, ChatContent chatContent) {

        ImageManager.getInstance().setImageByUrl(holder.getChatProfile(), chatContent.getAuthorImage());
        holder.getTime().setText(getDateToString(chatContent.getTime()));
        holder.getContent().setText(chatContent.getMessage());

    }

    private class HisContentViewHolder extends RecyclerView.ViewHolder {

        private ImageView mProfile;
        private TextView mContent;
        private TextView mTime;


        public HisContentViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfile = itemView.findViewById(R.id.image_profile_not_me);
            mProfile.setOutlineProvider(new ProfileAvatarOutlineProvider());
            mContent = itemView.findViewById(R.id.text_content_not_me);
            mTime = itemView.findViewById(R.id.text_time_not_me);

        }

        private ImageView getChatProfile() {
            return mProfile;
        }

        private TextView getTime() {
            return mTime;
        }

        private TextView getContent() {
            return mContent;
        }

    }

    private void bindHisContentViewHolder(HisContentViewHolder holder, ChatContent chatContent) {

        ImageManager.getInstance().setImageByUrl(holder.getChatProfile(), chatContent.getAuthorImage());
        holder.getTime().setText(getDateToString(chatContent.getTime()));
        holder.getContent().setText(chatContent.getMessage());

    }

    private String getDateToString(long millSeconds) {
        Date d = new Date(millSeconds);
        SimpleDateFormat sf = new SimpleDateFormat("E  HH : mm");
        return sf.format(d);
    }

    public void updateData(ArrayList<ChatContent> chatContentArrayList) {
        mChatContentArrayList = chatContentArrayList;
        notifyDataSetChanged();
    }
}
