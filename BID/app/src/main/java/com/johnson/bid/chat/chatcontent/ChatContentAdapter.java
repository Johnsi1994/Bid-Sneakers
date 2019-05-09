package com.johnson.bid.chat.chatcontent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.Bid;
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

        return (mChatContentArrayList == null) ? 0 : mChatContentArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (mChatContentArrayList.get(position).getAuthorId() == UserManager.getInstance().getUser().getId()) ? TYPE_ME : TYPE_HE;
    }

    private class MyContentViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMyProfile;
        private TextView mTextContent;
        private TextView mTextTime;


        public MyContentViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageMyProfile = itemView.findViewById(R.id.image_profile_me);
            mImageMyProfile.setOutlineProvider(new ProfileAvatarOutlineProvider());
            mTextContent = itemView.findViewById(R.id.text_content_me);
            mTextTime = itemView.findViewById(R.id.text_time_me);

        }

        private ImageView getChatProfile() {
            return mImageMyProfile;
        }

        private TextView getTextTime() {
            return mTextTime;
        }

        private TextView getTextContent() {
            return mTextContent;
        }

    }

    private void bindMyContentViewHolder(MyContentViewHolder holder, ChatContent chatContent) {

        ImageManager.getInstance().setImageByUrl(holder.getChatProfile(), chatContent.getAuthorImage());
        holder.getTextTime().setText(getDateToString(chatContent.getTime()));
        holder.getTextContent().setText(chatContent.getMessage());

    }

    private class HisContentViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageProfile;
        private TextView mTextContent;
        private TextView mTextTime;


        public HisContentViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageProfile = itemView.findViewById(R.id.image_profile_not_me);
            mImageProfile.setOutlineProvider(new ProfileAvatarOutlineProvider());
            mTextContent = itemView.findViewById(R.id.text_content_not_me);
            mTextTime = itemView.findViewById(R.id.text_time_not_me);

        }

        private ImageView getChatProfile() {
            return mImageProfile;
        }

        private TextView getTextTime() {
            return mTextTime;
        }

        private TextView getTextContent() {
            return mTextContent;
        }

    }

    private void bindHisContentViewHolder(HisContentViewHolder holder, ChatContent chatContent) {

        ImageManager.getInstance().setImageByUrl(holder.getChatProfile(), chatContent.getAuthorImage());
        holder.getTextTime().setText(getDateToString(chatContent.getTime()));
        holder.getTextContent().setText(chatContent.getMessage());

    }

    private String getDateToString(long millSeconds) {
        Date d = new Date(millSeconds);
        SimpleDateFormat sf = new SimpleDateFormat(Bid.getAppContext().getString(R.string.simple_date_format_EHm));
        return sf.format(d);
    }

    public void updateData(ArrayList<ChatContent> chatContentArrayList) {
        mChatContentArrayList = chatContentArrayList;
        notifyDataSetChanged();
    }
}
