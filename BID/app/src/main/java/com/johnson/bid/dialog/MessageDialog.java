package com.johnson.bid.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.Bid;
import com.johnson.bid.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MessageDialog extends AppCompatDialogFragment {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            LOGIN_SUCCESS, LOGIN_FAIL, POST_SUCCESS
    })
    public @interface MessageType {}
    public static final int LOGIN_SUCCESS   = 0x11;
    public static final int LOGIN_FAIL      = 0x12;
    public static final int POST_SUCCESS   = 0x13;

    private int mIconRes;
    private String mMessage;

    public MessageDialog() {}

    public void setMessage(@MessageType int messageType) {

        switch (messageType) {
            case POST_SUCCESS:
                mIconRes = R.drawable.ic_success;
                mMessage = Bid.getAppContext().getString(R.string.post_success);
                break;
            default:
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.MessageDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_message, container, false);
        view.setOnClickListener(v -> dismiss());

        ((ImageView) view.findViewById(R.id.image_message_icon)).setImageResource(mIconRes);
        ((TextView) view.findViewById(R.id.text_message_content)).setText(mMessage);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler().postDelayed(this::dismiss, 2000);
    }
}
