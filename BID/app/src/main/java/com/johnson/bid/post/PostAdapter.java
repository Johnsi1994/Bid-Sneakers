package com.johnson.bid.post;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.StorageReference;
import com.johnson.bid.Bid;
import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter {

    private PostContract.Presenter mPresenter;
    private MainActivity mMainActivity;

    private ArrayList<String> mImagePath;
    private LinearSnapHelper mLinearSnapHelper;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                Bid.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        PostPicsGalleryAdapter postPicsGalleryAdapter = new PostPicsGalleryAdapter(mImagePath, mPresenter);
//        if (mLinearSnapHelper == null) {
//            mLinearSnapHelper = new LinearSnapHelper();
//            mLinearSnapHelper.attachToRecyclerView(((ViewHolder) viewHolder).getPostPicGallery());
//        }
//        ((ViewHolder) viewHolder).getPostPicGallery().scrollToPosition(mImagePath.size());
        ((ViewHolder) viewHolder).getPostPicGallery().setAdapter(postPicsGalleryAdapter);
        ((ViewHolder) viewHolder).getPostPicGallery().setLayoutManager(layoutManager);


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements OnDateSetListener {

        private RecyclerView mPostPicGallery;
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
        private Button mPostBtn;
        private TextView mIncreaseTitle;

        private ArrayList<String> mUrls = new ArrayList<>();
        private long mTime = -1;

        @SuppressLint("SimpleDateFormat")
        private SimpleDateFormat sf = new SimpleDateFormat("MM月 dd日 HH時 mm分");
        long oneHour = 1000 * 60 * 60 * 1L;
        long oneWeek = 7 * 1000 * 60 * 60 * 24L;

        public ViewHolder(View itemView) {
            super(itemView);

            mPostPicGallery = itemView.findViewById(R.id.recycler_post_pics);
            mProductTitle = itemView.findViewById(R.id.edit_product_title);
            mProductIntro = itemView.findViewById(R.id.edit_product_intro);
            mProductCondition = itemView.findViewById(R.id.spinner_product_condition);
            mStartingPrice = itemView.findViewById(R.id.edit_starting_price);
            mReservePrice = itemView.findViewById(R.id.edit_reserve_price);
            mAuctionType = itemView.findViewById(R.id.spinner_auction_type);
            mIncreaseTitle = itemView.findViewById(R.id.text_increase);
            mIncrease = itemView.findViewById(R.id.spinner_increase);
            mTimePickerLayout = itemView.findViewById(R.id.layout_time_picker);
            mExpireTimeText = itemView.findViewById(R.id.text_expire_time);
            mPostBtn = itemView.findViewById(R.id.button_post);

            mProductCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        mPresenter.setProductCondition("全新");
                    } else {
                        mPresenter.setProductCondition("二手");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mPresenter.setProductCondition("全新");
                }
            });

            mAuctionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        mPresenter.setAuctionType("English");
                        mIncreaseTitle.setVisibility(View.VISIBLE);
                        mIncrease.setVisibility(View.VISIBLE);
                    } else {
                        mPresenter.setAuctionType("Sealed");
                        mIncreaseTitle.setVisibility(View.GONE);
                        mIncrease.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mPresenter.setAuctionType("English");
                }
            });

            mIncrease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            mPresenter.setIncrease(1);
                            break;
                        case 1:
                            mPresenter.setIncrease(10);
                            break;
                        case 2:
                            mPresenter.setIncrease(100);
                            break;
                        case 3:
                            mPresenter.setIncrease(1000);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mPresenter.setIncrease(1);
                }
            });

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

            mTimePickerLayout.setOnClickListener(v -> mDialogMonthDayHourMinute.show(mMainActivity.getSupportFragmentManager(), "month_day_hour_minute"));

            mPostBtn.setOnClickListener(v -> {

                if ("".equals(mProductTitle.getText().toString()) ||
                "".equals(mProductIntro.getText().toString()) ||
                "".equals(mStartingPrice.getText().toString()) ||
                        mTime == -1) {
                    Toast.makeText(mMainActivity, "請填完整資訊", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImages(0);
                    mPresenter.showPostSuccessDialog();
                    mPresenter.updateToolbar("拍賣中心");
                    mPresenter.showBottomNavigation();
                    mMainActivity.onBackPressed();
                }
            });

        }

        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
            mTime = millSeconds;
            String text = getDateToString(millSeconds);
            mExpireTimeText.setText(text);
            mExpireTimeText.setVisibility(View.VISIBLE);
            mPresenter.setExpireTime(millSeconds);
        }

        private String getDateToString(long time) {
            Date d = new Date(time);
            return sf.format(d);
        }

        private void uploadImages(int i) {

            Uri file = Uri.fromFile(new File(mImagePath.get(i)));
            StorageReference riversRef = Firebase.getStorage().child(file.getLastPathSegment());
            int j = i + 1;

            riversRef.putFile(file)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d("Johnsi", "Photo Upload Success and ready to get URL");

                        riversRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Log.d("Johnsi", "Success to get Uri: " + uri);
                            setUrl(uri.toString());
                            if (j < mImagePath.size()) {
                                uploadImages(j);
                            } else {
                                Log.d("Johnsi", "UploadImages success then upload product");
                                uploadProduct();
                            }
                        });
                    })
                    .addOnFailureListener(exception -> Log.d("Johnsi", exception.getMessage()));
        }

        private void uploadProduct() {

            mPresenter.setAuctionCondition("bidding");
            mPresenter.setImages(getUrls());
            mPresenter.setSellerId(UserManager.getInstance().getUser().getId());
            mPresenter.setProductTitle(mProductTitle.getText().toString());
            mPresenter.setProductIntro(mProductIntro.getText().toString());
            mPresenter.setStartingPrice(Integer.parseInt(mStartingPrice.getText().toString()));
            mPresenter.setCurrentPrice(Integer.parseInt(mStartingPrice.getText().toString()));
            mPresenter.setParticipantsNumber(0);
            if ("".equals(mReservePrice.getText().toString())) {
                mPresenter.setReservePrice(0);
            } else {
                mPresenter.setReservePrice(Integer.parseInt(mReservePrice.getText().toString()));
            }
            long id = System.currentTimeMillis();
            mPresenter.setProductId(id);
            mPresenter.setStartingTime(id);

            Firebase.getFirestore().collection("products")
                    .document(String.valueOf(id))
                    .set(mPresenter.getProduct())
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Johnsi", "DocumentSnapshot added");
                        mPresenter.updateCenterData();
                    })
                    .addOnFailureListener(e -> Log.w("Johnsi", "Error adding document", e));

            mPresenter.setPostProductId2User(id);

            Firebase.getFirestore().collection("users")
                    .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                    .update("mySellingProductsId", FieldValue.arrayUnion(id))
                    .addOnSuccessListener(aVoid -> Log.d("Johnsi", "BID DocumentSnapshot successfully updated!"))
                    .addOnFailureListener(e -> Log.w("Johnsi", "BID Error updating document", e));
        }

        private void setUrl(String url) {
            mUrls.add(url);
        }

        private ArrayList<String> getUrls() {
            return mUrls;
        }

        private RecyclerView getPostPicGallery() {
            return mPostPicGallery;
        }
    }

    public void updateData(ArrayList<String> imagePath) {
        this.mImagePath = imagePath;
        notifyDataSetChanged();
    }
}
