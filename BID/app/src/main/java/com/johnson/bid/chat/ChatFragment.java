package com.johnson.bid.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.R;
import com.johnson.bid.data.ChatRoom;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatFragment extends Fragment implements ChatContract.View {

    private ChatContract.Presenter mPresenter;
    private ChatAdapter mChatAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ChatFragment() {}

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChatAdapter = new ChatAdapter(mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_swipe_container, container, false);

        mSwipeRefreshLayout = root.findViewById(R.id.swipe_layout);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_container_swipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mChatAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.loadChatData();

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            mPresenter.loadChatData();
        });
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showChatUI(ArrayList<ChatRoom> chatRoomArrayList) {
        mChatAdapter.updateData(chatRoomArrayList);
    }
}
