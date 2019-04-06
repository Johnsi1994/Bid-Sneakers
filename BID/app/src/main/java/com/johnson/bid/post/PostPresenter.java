package com.johnson.bid.post;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.johnson.bid.MainActivity;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class PostPresenter implements PostContract.Presenter{

    private PostContract.View mPostView;

    public PostPresenter(@NonNull PostContract.View postView) {
        mPostView = checkNotNull(postView, "postView cannot be null!");
    }

    @Override
    public void start() {

    }

}
