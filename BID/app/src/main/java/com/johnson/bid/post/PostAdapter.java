package com.johnson.bid.post;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.util.CustomDateTimePicker;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter {

    private PostContract.Presenter mPresenter;
    private MainActivity mMainActivity;

    public PostAdapter(PostContract.Presenter presenter, MainActivity mainActivity) {
        mPresenter = presenter;
        mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_post_details, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements OnDateSetListener {

        private ConstraintLayout mTimePickerLayout;
        private TextView mExpireTimeText;
        private TimePickerDialog mDialogMonthDayHourMinute;
        
        @SuppressLint("SimpleDateFormat")
        private SimpleDateFormat sf = new SimpleDateFormat("MM月 dd日 HH時 mm分");
        long oneWeek = 7 * 1000 * 60 * 60 * 24L;

        public ViewHolder(View itemView) {
            super(itemView);

            mExpireTimeText = itemView.findViewById(R.id.text_expire_time);
            mTimePickerLayout = itemView.findViewById(R.id.layout_time_picker);

            mDialogMonthDayHourMinute = new TimePickerDialog.Builder()
                    .setCallBack(this)
                    .setCancelStringId("取消")
                    .setSureStringId("確定")
                    .setTitleStringId("結標時間")
                    .setMonthText("月")
                    .setDayText("日")
                    .setHourText("時")
                    .setMinuteText("分")
                    .setCyclic(false)
                    .setMinMillseconds(System.currentTimeMillis())
                    .setMaxMillseconds(System.currentTimeMillis() + oneWeek)
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(mMainActivity.getResources().getColor(R.color.colorPrimary))
                    .setWheelItemTextSize(12)
                    .build();

            mTimePickerLayout.setOnClickListener( v -> mDialogMonthDayHourMinute.show(mMainActivity.getSupportFragmentManager(), "month_day_hour_minute"));

        }

        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
            String text = getDateToString(millseconds);
            mExpireTimeText.setText(text);
        }

        public String getDateToString(long time) {
            Date d = new Date(time);
            return sf.format(d);
        }
    }
}
