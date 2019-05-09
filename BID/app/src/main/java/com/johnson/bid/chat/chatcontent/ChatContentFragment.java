package com.johnson.bid.chat.chatcontent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.johnson.bid.R;
import com.johnson.bid.data.ChatContent;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.johnson.bid.MainMvpController.CHAT;

public class ChatContentFragment extends Fragment implements ChatContentContract.View {

    private ChatContentContract.Presenter mPresenter;
    private ChatContentAdapter mChatContentAdapter;
    private EditText mEdtTxtMessage;
    private Button mBtnSend;
    private RecyclerView mRecyclerView;
    private String mFrom;

    public ChatContentFragment() {}

    public static ChatContentFragment newInstance() {
        return new ChatContentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChatContentAdapter = new ChatContentAdapter(mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat_content, container, false);

        mRecyclerView = root.findViewById(R.id.recycler_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mChatContentAdapter);

        mEdtTxtMessage = root.findViewById(R.id.edit_send_message);
        mBtnSend = root.findViewById(R.id.button_send_message);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.setChatListener();
        mPresenter.loadChatContentData();

        mBtnSend.setOnClickListener(v -> {

            if (!"".equals(mEdtTxtMessage.getText().toString())) {

                mPresenter.sendMessage(setChatContent());
                mEdtTxtMessage.setText("");
            }
        });
    }

    private ChatContent setChatContent() {
        ChatContent chatContent = new ChatContent();

        long time = System.currentTimeMillis();
        chatContent.setTime(time);
        chatContent.setAuthorId(UserManager.getInstance().getUser().getId());
        chatContent.setAuthorImage(UserManager.getInstance().getUser().getImage());
        chatContent.setMessage(mEdtTxtMessage.getText().toString());

        return chatContent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mFrom.equals(CHAT)) {
            mPresenter.showBottomNavigation();
            mPresenter.updateToolbar(getString(R.string.toolbar_title_chat));
        } else {
            mPresenter.hideToolbar();
            mPresenter.updateToolbar(getString(R.string.toolbar_title_chat));
        }

    }

    @Override
    public void setPresenter(ChatContentContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showChatContentUi(ArrayList<ChatContent> chatContentArrayList) {

        if (mChatContentAdapter != null) {
            mChatContentAdapter.updateData(chatContentArrayList);
            mRecyclerView.scrollToPosition(mChatContentAdapter.getItemCount() - 1);
        }

    }

    public void setFrom(String from) {
        mFrom = from;
    }
}
