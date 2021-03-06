package com.johnson.bid;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.auction.auctionitem.AuctionItemFragment;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.data.User;
import com.johnson.bid.dialog.MessageDialog;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;
import com.johnson.bid.util.Constants;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.RotatePic;
import com.johnson.bid.util.UserManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.johnson.bid.MainMvpController.AUCTION;
import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.POST;
import static com.johnson.bid.dialog.MessageDialog.BID_SUCCESS;
import static com.johnson.bid.dialog.MessageDialog.DELETE_SUCCESS;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextView mToolbarLogout;
    private DisplayMetrics mPhone;
    private MessageDialog mMessageDialog;
    private View mBadge;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    //bid dialog
    private EditText mBidPrice;
    private TextView mWarningMsg;
    private BottomSheetDialog dialogPlaceBid;

    private final static int CAMERA_AUCTION = 666;
    private final static int CAMERA_POST = 667;
    public final static int CHOOSE_PHOTO_AUCTION = 222;
    public final static int CHOOSE_PHOTO_POST = 223;
    public final static int PHOTO_SETTINGS = 999;

    private MainContract.Presenter mPresenter;
    private MainMvpController mMainMvpController;

    private ArrayList<Bitmap> mImageBitmapList;

    private SearchView searchView;
    private String mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        sAnalytics = GoogleAnalytics.getInstance(this);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mMainMvpController = MainMvpController.create(this);

        setToolbar();

        if (UserManager.getInstance().isLoggedIn()) {
            UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
                @Override
                public void onSuccess() {
                    Log.d(Constants.TAG, "Load User Profile Success !");
                    UserManager.getInstance().setHasUserDataChange(false);

                    setBottomNavigation();
                    mPresenter.openCenter();
                    showToolbarUi();
                    showBottomNavigationUi();
                }

                @Override
                public void onFail(String errorMessage) {
                    Log.d(Constants.TAG, "Load User Profile Fail !");
                }
            });
        } else {
            mPresenter.openLogin();
        }
    }

    private void setToolbar() {
        // Retrieve the AppCompact Toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        // Set the padding to match the Status Bar height
        mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        mToolbarTitle = mToolbar.findViewById(R.id.text_toolbar_title);
        mToolbarLogout = mToolbar.findViewById(R.id.text_logout);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void setBottomNavigation() {

        mBottomNavigation = findViewById(R.id.bottom_navigation_main);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setItemIconTintList(null); //去除BottomNavigation上面icon的顏色

        BottomNavigationMenuView menuView =
                (BottomNavigationMenuView) mBottomNavigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);
        mBadge = LayoutInflater.from(this)
                .inflate(R.layout.badge_main_bottom, itemView, true);
        mBadge.findViewById(R.id.text_badge_main).setVisibility(View.GONE);

        mPresenter.updateTradeBadge();
    }

    @Override
    public void updateTradeBadgeUi(int unreadCount) {

        if (unreadCount > 0) {
            mBadge.findViewById(R.id.text_badge_main).setVisibility(View.VISIBLE);
            ((TextView) mBadge.findViewById(R.id.text_badge_main)).setText(getString(R.string.badge_exclamation_mark));
        } else {
            mBadge.findViewById(R.id.text_badge_main).setVisibility(View.GONE);
        }
    }

    @Override
    public void showWarningMsgNull() {
        mWarningMsg.setVisibility(View.VISIBLE);
        mWarningMsg.setText(R.string.please_place_bid);
    }

    @Override
    public void showWarningMsgPriceToLow() {
        mWarningMsg.setVisibility(View.VISIBLE);
        mWarningMsg.setText(getString(R.string.warning_msg_under_current_price));
    }

    @Override
    public void showWarningMsgPriceUnderIncrease() {
        mWarningMsg.setVisibility(View.VISIBLE);
        mWarningMsg.setText(getString(R.string.warning_msg_under_increase));
    }

    @Override
    public void hideWarningMsg() {
        mWarningMsg.setVisibility(View.GONE);
    }

    @Override
    public void dismissDialog() {
        dialogPlaceBid.dismiss();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = menuItem -> {

        switch (menuItem.getItemId()) {
            case R.id.navigation_center:
                mPresenter.updateToolbar(getString(R.string.toolbar_title_auction));
                mPresenter.openCenter();
                return true;
            case R.id.navigation_trade:
                mPresenter.updateToolbar(getString(R.string.toolbar_title_my_trade));
                mPresenter.openTrade();
                return true;
            case R.id.navigation_message:
                mPresenter.updateToolbar(getString(R.string.toolbar_title_chat));
                mPresenter.openChat();
                return true;
            case R.id.navigation_settings:
                mPresenter.updateToolbar(getString(R.string.toolbar_title_settings));
                mPresenter.openSettings();
                return true;
            default:
        }
        return false;
    };

    @Override
    public void openLoginUi() { mMainMvpController.createLoginView();}

    @Override
    public void openCenterUi() {
        mMainMvpController.findOrCreateCenterView();
    }

    @Override
    public void openTradeUi() {
        mMainMvpController.findOrCreateTradeView();
    }

    @Override
    public void openChatUi() {
        mMainMvpController.findOrCreateChatView();
    }

    @Override
    public void openSettingsUi() {
        mMainMvpController.findOrCreateSettingsView();
    }

    @Override
    public void openPostUi(ArrayList<Bitmap> imageBitmap) {
        mMainMvpController.createPostView(imageBitmap);
    }

    @Override
    public void openSearchUi(String keyword) {
        mMainMvpController.createSearchUi(keyword);
    }

    @Override
    public void openEyesOnUi() {
        mMainMvpController.createEyesOnView();
    }

    @Override
    public void openChatContentUi(ChatRoom chatRoom, String from) {
        mMainMvpController.createChatContentView(chatRoom, from);
    }

    @Override
    public void openSearchDialog() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.dialog_searchview, null);
        final Dialog dialog = new Dialog(this);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 400;
        window.setAttributes(params);

        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundColor(this.getColor(android.R.color.transparent));
        dialog.show();

        searchView = view.findViewById(R.id.search_view);
        searchView.setIconified(false);

        searchView.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackground(null);
        searchView.findViewById(android.support.v7.appcompat.R.id.submit_area).setBackground(null);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                Firebase.getInstance().getAllBiddingProductsFromFirebase(new UserManager.LoadCallback() {
                    @Override
                    public void onSuccess() {
                        mPresenter.openSearch(getString(R.string.toolbar_title_search_result), keyword);
                        hideBottomNavigationUi();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFail(String errorMessage) {}
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                return false;
            }
        });

    }

    @Override
    public void setPostPics(ArrayList<Bitmap> imageBitmap) { mMainMvpController.setPostPics(imageBitmap); }

    @Override
    public void setAfterBidData(Product product) {
        mMainMvpController.setAfterBidData(product);
    }

    @Override
    public void openGallery(String from) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum(from);
        }
    }

    @Override
    public void openCamera(String from) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        } else {
            if (Build.VERSION.SDK_INT <= 23) {
                openCamera23(mFrom);
            } else {
//                openCamera24(mFrom);
                Toast.makeText(this, "Camera is coming soon", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void openGalleryDialog(String from) {

        mFrom = from;
        if (from.equals(AUCTION)) {
            mImageBitmapList = new ArrayList<>();
        }

        Button mCancelBtn;
        Button mOpenGalleryBtn;
        Button mOpenCameraBtn;

        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.dialog_goto_gallery, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundColor(this.getColor(android.R.color.transparent));
        dialog.show();

        mCancelBtn = view.findViewById(R.id.button_cancel_delete_product);
        mOpenGalleryBtn = view.findViewById(R.id.button_goto_gallery);
        mOpenCameraBtn = view.findViewById(R.id.button_open_camera);

        mCancelBtn.setOnClickListener(v -> dialog.dismiss());

        mOpenGalleryBtn.setOnClickListener(v -> {
            dialog.dismiss();
            openGallery(from);
        });

        mOpenCameraBtn.setOnClickListener(v -> {
            dialog.dismiss();
            openCamera(from);
        });
    }

    @Override
    public void setSettingsProfile(Bitmap bitmap) {
        mMainMvpController.setSettingsProfile(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(mFrom);
                } else {
                    Toast.makeText(this, "授權失敗，無法操作", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT <= 23) {
                        openCamera23(mFrom);
                    } else {
//                        openCamera24(mFrom);
                        Toast.makeText(this, "Camera is coming soon", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "授權失敗，無法操作", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CHOOSE_PHOTO_AUCTION) {

            mImageBitmapList.add(RotatePic.handleImage(this, data));
            RotatePic.handlePostImage(this, data, CHOOSE_PHOTO_AUCTION);
            mPresenter.openPost(getString(R.string.toolbar_title_post), mImageBitmapList);

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_AUCTION) {

            mImageBitmapList.add(RotatePic.handleImage(this, data));
            RotatePic.handlePostImage(this, data, CAMERA_AUCTION);
            mPresenter.openPost(getString(R.string.toolbar_title_post), mImageBitmapList);

        } else if (resultCode == RESULT_OK && requestCode == CHOOSE_PHOTO_POST) {

            mImageBitmapList.add(RotatePic.handleImage(this, data));
            RotatePic.handlePostImage(this, data, CHOOSE_PHOTO_POST);
            setPostPics(mImageBitmapList);

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_POST) {

            mImageBitmapList.add(RotatePic.handleImage(this, data));
            RotatePic.handlePostImage(this, data, CAMERA_POST);
            setPostPics(mImageBitmapList);

        } else if (resultCode == RESULT_OK && requestCode == PHOTO_SETTINGS) {

            setSettingsProfile(RotatePic.handleImage(this, data));

        } else if (resultCode == RESULT_CANCELED) {

        } else {
            UserManager.getInstance().getFbCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public AuctionItemFragment findEnglishAuctionView() {
        return mMainMvpController.createEnglishAuctionView();
    }

    @Override
    public AuctionItemFragment findSealedAuctionView() {
        return mMainMvpController.createSealedAuctionView();
    }

    @Override
    public TradeItemFragment findMyBiddingView() {
        return mMainMvpController.findOrCreateMyBiddingView();
    }

    @Override
    public TradeItemFragment findMySellingView() {
        return mMainMvpController.findOrCreateMySellingView();
    }

    @Override
    public TradeItemFragment findMyBoughtView() {
        return mMainMvpController.findOrCreateMyBoughtView();
    }

    @Override
    public TradeItemFragment findMySoldView() {
        return mMainMvpController.findOrCreateMySoldView();
    }

    @Override
    public TradeItemFragment findNobodyBidView() {
        return mMainMvpController.findOrCreateNobodyBidView();
    }

    @Override
    public void findBiddingView(String auctionType, Product product) {
        mMainMvpController.createBiddingView(auctionType, product);
    }

    @Override
    public void findSellingView(String auctionType, Product product) {
        mMainMvpController.createSellingView(auctionType, product);
    }

    @Override
    public void findBoughtDetailView(Product product) {
        mMainMvpController.createBoughtDetailView(product);
    }

    @Override
    public void findSoldDetailView(Product product) {
        mMainMvpController.createSoldDetailView(product);
    }

    @Override
    public void findNobodyBidDetailView(Product product) {
        mMainMvpController.createNobodyBidDetailView(product);
    }

    @Override
    public void setToolbarTitleUi(String title) {

        if (title.equals(getString(R.string.toolbar_title_post)) || title.equals(getString(R.string.toolbar_title_search_result))) {
            mToolbar.setNavigationIcon(R.drawable.ic_left_arrow);
            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
            mToolbarLogout.setVisibility(View.GONE);
        } else if (title.equals(getString(R.string.toolbar_title_settings))) {

            mToolbarLogout.setVisibility(View.VISIBLE);
            mToolbarLogout.setOnClickListener(v -> {
                UserManager.getInstance().logout();
                Toast.makeText(this, getString(R.string.message_logout_success), Toast.LENGTH_SHORT).show();
                hideToolbarUi();
                hideBottomNavigationUi();
                mPresenter.openLogin();
            });
        } else {
            mToolbar.setNavigationIcon(null);
            mToolbarLogout.setVisibility(View.GONE);
        }

        mToolbarTitle.setText(title);
    }

    @Override
    public void hideToolbarUi() {
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbarUi() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomNavigationUi() {
        mBottomNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showBottomNavigationUi() {
        mBottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void openBidDialog(String from, Product product) {

        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.dialog_bid, null);
        dialogPlaceBid = new BottomSheetDialog(this);
        dialogPlaceBid.setContentView(view);
        ((View) view.getParent()).setBackgroundColor(this.getColor(android.R.color.transparent));
        dialogPlaceBid.show();

        TextView mCurrentPrice;
        TextView mCurrentPriceTitle;
        TextView mIncreaseHint;
        TextView mIncreaseHintTitle;
        Button mCancelBtn;
        Button mPlaceBidBtn;

        mCurrentPriceTitle = view.findViewById(R.id.text_current_price_hint_title);
        mCurrentPrice = view.findViewById(R.id.text_current_price_hint);
        mIncreaseHintTitle = view.findViewById(R.id.text_increase_hint_title);
        mIncreaseHint = view.findViewById(R.id.text_increase_hint);
        mBidPrice = view.findViewById(R.id.edit_bit_your_price);
        mCancelBtn = view.findViewById(R.id.button_bit_cancel);
        mPlaceBidBtn = view.findViewById(R.id.button_send_price);
        mWarningMsg = view.findViewById(R.id.text_bid_warning);

        mCurrentPrice.setText(String.valueOf(product.getCurrentPrice()));
        mIncreaseHint.setText(String.valueOf(product.getIncrease()));

        mCancelBtn.setOnClickListener(v ->
                dialogPlaceBid.dismiss()
        );

        if (from.equals(ENGLISH)) {
            mCurrentPriceTitle.setVisibility(View.VISIBLE);
            mCurrentPrice.setVisibility(View.VISIBLE);
            mIncreaseHintTitle.setVisibility(View.VISIBLE);
            mIncreaseHint.setVisibility(View.VISIBLE);
        } else {
            mCurrentPriceTitle.setVisibility(View.GONE);
            mCurrentPrice.setVisibility(View.GONE);
            mIncreaseHintTitle.setVisibility(View.GONE);
            mIncreaseHint.setVisibility(View.GONE);
        }

        mPlaceBidBtn.setOnClickListener(v -> {
            String price = mBidPrice.getText().toString();
            mPresenter.placeBid(product, from, price);
        });
    }

    @Override
    public void openDeleteProductDialog(Product product) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.dialog_delete_product, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundColor(this.getColor(android.R.color.transparent));
        dialog.show();

        Button mCancelBtn;
        Button mDeleteBtn;

        mCancelBtn = view.findViewById(R.id.button_cancel_delete_product);
        mDeleteBtn = view.findViewById(R.id.button_delete_product);

        mCancelBtn.setOnClickListener(v -> dialog.dismiss());

        mDeleteBtn.setOnClickListener(v -> {

            UserManager.getInstance().removeSellingProductId(product.getProductId());
            UserManager.getInstance().setHasUserDataChange(true);

            Firebase.getInstance().getFirestore().collection("users")
                    .document(String.valueOf(UserManager.getInstance().getUser().getId()))
                    .update("mySellingProductsId", FieldValue.arrayRemove(product.getProductId()))
                    .addOnSuccessListener(aVoid -> Log.d(Constants.TAG, "Bidding Products Id successfully removed!"))
                    .addOnFailureListener(e -> Log.w(Constants.TAG, "Bidding Products Id Error updating document", e));

            Firebase.getInstance().getFirestore().collection(getString(R.string.firebase_products))
                    .document(String.valueOf(product.getProductId()))
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d(Constants.TAG, product.getProductId() + " successfully deleted!"))
                    .addOnFailureListener(e -> Log.w(Constants.TAG, "Error deleting document", e));

            Firebase.getInstance().getFirestore().collection("users")
                    .whereArrayContains("myBiddingProductsId", product.getProductId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Firebase.getInstance().getFirestore().collection("users")
                                        .document(document.getId())
                                        .update("myBiddingProductsId", FieldValue.arrayRemove(product.getProductId()))
                                        .addOnSuccessListener(aVoid -> Log.d(Constants.TAG, "Bidding Products Id successfully removed!"))
                                        .addOnFailureListener(e -> Log.w(Constants.TAG, "Bidding Products Id Error updating document", e));

                            }
                        } else {
                            Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                        }
                    });

            Firebase.getInstance().getFirestore().collection("users")
                    .whereArrayContains("eyesOn", product.getProductId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Firebase.getInstance().getFirestore().collection("users")
                                        .document(document.getId())
                                        .update("eyesOn", FieldValue.arrayRemove(product.getProductId()))
                                        .addOnSuccessListener(aVoid -> Log.d(Constants.TAG, "Bidding Products Id successfully removed!"))
                                        .addOnFailureListener(e -> Log.w(Constants.TAG, "Bidding Products Id Error updating document", e));

                            }
                        } else {
                            Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                        }
                    });

            dialog.dismiss();
            showMessageDialogUi(DELETE_SUCCESS);
            onBackPressed();
        });
    }

    @Override
    public void showMessageDialogUi(@MessageDialog.MessageType int type) {

        if (mMessageDialog == null) {

            mMessageDialog = new MessageDialog();
            mMessageDialog.setMessage(type);
            mMessageDialog.show(getSupportFragmentManager(), "");

        } else if (!mMessageDialog.isAdded()) {

            mMessageDialog.setMessage(type);
            mMessageDialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (UserManager.getInstance().isHasUserDataChange()) {

            UserManager.getInstance().updateUser2Firebase();
            UserManager.getInstance().setHasUserDataChange(false);
        }
    }

    public void openAlbum(String from) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");

        if (from.equals(AUCTION)) {
            startActivityForResult(intent, CHOOSE_PHOTO_AUCTION);
        } else if (from.equals(POST)) {
            startActivityForResult(intent, CHOOSE_PHOTO_POST);
        } else {
            startActivityForResult(intent, PHOTO_SETTINGS);
        }
    }

    public void openCamera23(String from) {

        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        ContentValues value = new ContentValues();
        value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                value);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());

        if (from.equals(AUCTION)) {
            startActivityForResult(intent, CAMERA_AUCTION);
        } else if (from.equals(POST)) {
            startActivityForResult(intent, CAMERA_POST);
        } else {
            startActivityForResult(intent, PHOTO_SETTINGS);
        }

    }

    public void openCamera24(String from) {

        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        Uri imageUri = FileProvider.getUriForFile(this, "com.johnson.bid.fileprovider", file);
        Log.d("camera24test", "openCamera24 imageUri : " + imageUri);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri.getPath());

        if (from.equals(AUCTION)) {
            startActivityForResult(intent, CAMERA_AUCTION);
        } else if (from.equals(POST)) {
            startActivityForResult(intent, CAMERA_POST);
        } else {
            startActivityForResult(intent, PHOTO_SETTINGS);
        }
    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }
}
