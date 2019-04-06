package com.johnson.bid.post;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
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

        private EditText mProductTitle;
        private EditText mProductIntro;
        private Spinner mProductCondition;
        private EditText mStartingPrice;
        private EditText mReservePrice;
        private Spinner mAuctionType;
        private Spinner mIncrease;
        private ConstraintLayout mTimePickerLayout;
        private TextView mExpireTimeText;
        private TimePickerDialog mDialogMonthDayHourMinute;

        @SuppressLint("SimpleDateFormat")
        private SimpleDateFormat sf = new SimpleDateFormat("MM月 dd日 HH時 mm分");
        long oneHour = 1000 * 60 * 60 * 1L;
        long oneWeek = 7 * 1000 * 60 * 60 * 24L;

        public ViewHolder(View itemView) {
            super(itemView);

            mProductTitle = itemView.findViewById(R.id.edit_product_title);
            mProductIntro = itemView.findViewById(R.id.edit_product_intro);
            mProductCondition = itemView.findViewById(R.id.spinner_product_condition);
            mStartingPrice = itemView.findViewById(R.id.edit_starting_price);
            mReservePrice = itemView.findViewById(R.id.edit_reserve_price);
            mAuctionType = itemView.findViewById(R.id.spinner_auction_type);
            mIncrease = itemView.findViewById(R.id.spinner_increase);
            mTimePickerLayout = itemView.findViewById(R.id.layout_time_picker);
            mExpireTimeText = itemView.findViewById(R.id.text_expire_time);


//            mPresenter.setProductTitle(mProductTitle.getText().toString());
//            mPresenter.setProductIntro(mProductIntro.getText().toString());
//            mProductCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if (position == 0) {
//                        mPresenter.setProductCondition("BrandNew");
//                    } else {
//                        mPresenter.setProductCondition("Used");
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//            mPresenter.setStartingPrice(Integer.parseInt(mStartingPrice.getText().toString()));
//            mPresenter.setReservePrice(Integer.parseInt(mReservePrice.getText().toString()));
//            mAuctionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if (position == 0) {
//                        mPresenter.setAuctionType("English");
//                    } else {
//                        mPresenter.setAuctionType("Sealed");
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//            mIncrease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    switch (position) {
//                        case 0 :
//                            mPresenter.setIncrease(1);
//                            break;
//                        case 1 :
//                            mPresenter.setIncrease(10);
//                            break;
//                        case 2 :
//                            mPresenter.setIncrease(100);
//                            break;
//                        case 3 :
//                            mPresenter.setIncrease(1000);
//                            break;
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//
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
                    .setMinMillseconds(System.currentTimeMillis() + oneHour)
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
            mExpireTimeText.setVisibility(View.VISIBLE);
            mPresenter.setStartingTime(millseconds);
        }

        public String getDateToString(long time) {
            Date d = new Date(time);
            return sf.format(d);
        }
    }
}
