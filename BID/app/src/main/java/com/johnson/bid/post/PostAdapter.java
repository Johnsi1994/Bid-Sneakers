package com.johnson.bid.post;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
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

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.johnson.bid.Bid;
import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.util.Constants;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.RotatePic;
import com.johnson.bid.util.UserManager;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter {

    private PostContract.Presenter mPresenter;
    private MainActivity mMainActivity;

    private ArrayList<Bitmap> mImageBitmap;
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
        PostPicsGalleryAdapter postPicsGalleryAdapter = new PostPicsGalleryAdapter(RotatePic.getPostImageBitmapList(), mPresenter);
        if (mLinearSnapHelper == null) {
            mLinearSnapHelper = new LinearSnapHelper();
            mLinearSnapHelper.attachToRecyclerView(((ViewHolder) viewHolder).getPostPicGallery());
        }
        ((ViewHolder) viewHolder).getPostPicGallery().setAdapter(postPicsGalleryAdapter);
        ((ViewHolder) viewHolder).getPostPicGallery().setLayoutManager(layoutManager);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements OnDateSetListener {

        private RecyclerView mPostPicGallery;
        private EditText mEditProductTitle;
        private EditText mEditProductIntro;
        private Spinner mSpinnerProductCondition;
        private EditText mEditStartingPrice;
        private EditText mEditReservePrice;
        private Spinner mSpinnerAuctionType;
        private Spinner mSpinnerIncrease;
        private ConstraintLayout mLayoutTimePicker;
        private TextView mTextExpireTime;
        private TimePickerDialog mDialogMonthDayHourMinute;
        private Button mBtnPost;
        private TextView mTextIncreaseTitle;
        private View mView;

        private ArrayList<String> mUrlList = new ArrayList<>();
        private long mTime = -1;

        @SuppressLint("SimpleDateFormat")
        private SimpleDateFormat sf = new SimpleDateFormat(mMainActivity.getString(R.string.simple_date_format_MdHm));
        long oneMinute = 1000 * 60L;
        long oneHour = 1000 * 60 * 60 * 1L;
        long oneWeek = 7 * 1000 * 60 * 60 * 24L;

        public ViewHolder(View itemView) {
            super(itemView);

            mPostPicGallery = itemView.findViewById(R.id.recycler_post_pics);
            mEditProductTitle = itemView.findViewById(R.id.edit_product_title);
            mEditProductIntro = itemView.findViewById(R.id.edit_product_intro);
            mSpinnerProductCondition = itemView.findViewById(R.id.spinner_product_condition);
            mEditStartingPrice = itemView.findViewById(R.id.edit_starting_price);
            mEditReservePrice = itemView.findViewById(R.id.edit_reserve_price);
            mSpinnerAuctionType = itemView.findViewById(R.id.spinner_auction_type);
            mTextIncreaseTitle = itemView.findViewById(R.id.text_increase);
            mSpinnerIncrease = itemView.findViewById(R.id.spinner_increase);
            mLayoutTimePicker = itemView.findViewById(R.id.layout_time_picker);
            mTextExpireTime = itemView.findViewById(R.id.text_expire_time);
            mView = itemView.findViewById(R.id.view_increase);
            mBtnPost = itemView.findViewById(R.id.button_post);

            mSpinnerProductCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        mPresenter.setProductCondition(mMainActivity.getString(R.string.product_condition_new));
                    } else {
                        mPresenter.setProductCondition(mMainActivity.getString(R.string.product_condition_used));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mPresenter.setProductCondition(mMainActivity.getString(R.string.product_condition_new));
                }
            });

            mSpinnerAuctionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        mPresenter.setAuctionType(mMainActivity.getString(R.string.firebase_auction_type_English));
                        mTextIncreaseTitle.setVisibility(View.VISIBLE);
                        mSpinnerIncrease.setVisibility(View.VISIBLE);
                        mView.setVisibility(View.VISIBLE);
                    } else {
                        mPresenter.setAuctionType(mMainActivity.getString(R.string.firebase_auction_type_sealed));
                        mTextIncreaseTitle.setVisibility(View.GONE);
                        mSpinnerIncrease.setVisibility(View.GONE);
                        mView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mPresenter.setAuctionType(mMainActivity.getString(R.string.firebase_auction_type_English));
                }
            });

            mSpinnerIncrease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    .setCancelStringId(mMainActivity.getString(R.string.cancel))
                    .setSureStringId(mMainActivity.getString(R.string.yes))
                    .setTitleStringId(mMainActivity.getString(R.string.finish_time_post))
                    .setMonthText(mMainActivity.getString(R.string.timer_month))
                    .setDayText(mMainActivity.getString(R.string.timer_date))
                    .setHourText(mMainActivity.getString(R.string.timer_hour))
                    .setMinuteText(mMainActivity.getString(R.string.timer_minute))
                    .setCyclic(false)
                    .setMinMillseconds(System.currentTimeMillis() + oneMinute)
                    .setMaxMillseconds(System.currentTimeMillis() + oneWeek)
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(mMainActivity.getResources().getColor(R.color.colorPrimary))
                    .setWheelItemTextSize(12)
                    .build();

            mLayoutTimePicker.setOnClickListener(v -> mDialogMonthDayHourMinute.show(mMainActivity.getSupportFragmentManager(), "month_day_hour_minute"));

            mBtnPost.setOnClickListener(v -> {

                if ("".equals(mEditProductTitle.getText().toString()) ||
                        "".equals(mEditProductIntro.getText().toString()) ||
                        "".equals(mEditStartingPrice.getText().toString()) ||
                        mTime == -1) {
                    Toast.makeText(mMainActivity, mMainActivity.getString(R.string.warning_complete_post_info), Toast.LENGTH_SHORT).show();
                } else {
                    uploadImages(0);
                    mPresenter.showPostSuccessDialog();
                    mPresenter.updateToolbar(mMainActivity.getString(R.string.toolbar_title_auction));
                    mPresenter.showBottomNavigation();
                    mMainActivity.onBackPressed();
                }
            });

        }

        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
            mTime = millSeconds;
            String text = getDateToString(millSeconds);
            mTextExpireTime.setText(text);
            mTextExpireTime.setVisibility(View.VISIBLE);
            mPresenter.setExpireTime(millSeconds);
        }

        private String getDateToString(long time) {
            Date d = new Date(time);
            return sf.format(d);
        }

        private void uploadImages(int i) {

           StorageReference ref = Firebase.getInstance().getStorage().child(mImageBitmap.get(i).toString());
            int j = i + 1;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mImageBitmap.get(i).compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask
                    .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {

                            Log.d(Constants.TAG, "Success to get Uri: " + uri);
                            setUrl(uri.toString());
                            if (j < mImageBitmap.size()) {
                                uploadImages(j);
                            } else {
                                Log.d(Constants.TAG, "UploadImages success then upload product");
                                uploadProduct();
                            }
                        })
                    )
                    .addOnFailureListener(exception -> Log.d(Constants.TAG, exception.getMessage()));
        }

        private void uploadProduct() {

            mPresenter.setAuctionCondition(mMainActivity.getString(R.string.firebase_auction_condition_bidding));
            mPresenter.setImages(getUrlList());
            mPresenter.setSellerId(UserManager.getInstance().getUser().getId());
            mPresenter.setProductTitle(mEditProductTitle.getText().toString());
            mPresenter.setProductIntro(mEditProductIntro.getText().toString());
            mPresenter.setStartingPrice(Integer.parseInt(mEditStartingPrice.getText().toString()));
            mPresenter.setCurrentPrice(Integer.parseInt(mEditStartingPrice.getText().toString()));
            mPresenter.setPlaceBidTimes(0);
            if ("".equals(mEditReservePrice.getText().toString())) {
                mPresenter.setReservePrice(0);
            } else {
                mPresenter.setReservePrice(Integer.parseInt(mEditReservePrice.getText().toString()));
            }
            long id = System.currentTimeMillis();
            mPresenter.setProductId(id);
            mPresenter.setStartingTime(id);
            mPresenter.setSellerHasRead(false);
            mPresenter.setBuyerHasRead(false);
            mPresenter.setSellerName(UserManager.getInstance().getUser().getName());

            Firebase.getInstance().getFirestore().collection(mMainActivity.getString(R.string.firebase_products))
                    .document(String.valueOf(id))
                    .set(mPresenter.getProduct())
                    .addOnSuccessListener(documentReference -> {
                        Log.d(Constants.TAG, "DocumentSnapshot added");
                        mPresenter.updateCenterData();
                    })
                    .addOnFailureListener(e -> Log.w(Constants.TAG, "Error adding document", e));

            mPresenter.setPostProductId2User(id);
        }

        private void setUrl(String url) {
            mUrlList.add(url);
        }

        private ArrayList<String> getUrlList() {
            return mUrlList;
        }

        private RecyclerView getPostPicGallery() {
            return mPostPicGallery;
        }
    }

    public void updateData(ArrayList<Bitmap> imageBitmap) {
        mImageBitmap = imageBitmap;
        notifyDataSetChanged();
    }
}