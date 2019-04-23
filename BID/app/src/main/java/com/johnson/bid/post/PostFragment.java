package com.johnson.bid.post;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.bid.MainActivity;
import com.johnson.bid.R;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class PostFragment extends Fragment implements PostContract.View {

    private PostContract.Presenter mPresenter;

    private PostAdapter mPostAdapter;

    public PostFragment() {}

    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostAdapter = new PostAdapter(mPresenter, (MainActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_container, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mPostAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.loadPostPics();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.showBottomNavigation();
        mPresenter.updateToolbar("拍賣中心");

    }

    @Override
    public void setPresenter(PostContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }



    @Override
    public void showPostUi(ArrayList<String> imagePath) {
        if (mPostAdapter != null) {
            mPostAdapter.updateData(imagePath);
        }

    }
}
