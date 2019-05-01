package com.johnson.bid;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.auction.auctionitem.AuctionItemFragment;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.dialog.MessageDialog;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

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

    private final static int CAMERA_AUCTION = 666;
    private final static int CAMERA_POST = 667;
    public final static int CHOOSE_PHOTO_AUCTION = 222;
    public final static int CHOOSE_PHOTO_POST = 223;
    public final static int PHOTO_SETTINGS = 999;

    private MainContract.Presenter mPresenter;
    private MainMvpController mMainMvpController;

    private ArrayList<String> mImagePathList;

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

    @Override
    public void openLoginUi() {
        mMainMvpController.createLoginView();
    }

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
    public void openPostUi(ArrayList<String> imagePath) {
        mMainMvpController.createPostView(imagePath);
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
    public void openChatContentUi(ChatRoom chatRoom) {
        mMainMvpController.createChatContentView(chatRoom);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                Firebase.getInstance().getAllBiddingProductsFromFirebase(new UserManager.LoadCallback() {
                    @Override
                    public void onSuccess() {
                        mPresenter.openSearch("搜尋結果", keyword);
                        hideBottomNavigationUi();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFail(String errorMessage) {

                    }
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
    public void setPostPics(ArrayList<String> imagePath) {
        mMainMvpController.setPostPics(imagePath);
    }

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        } else {
            opennCamera(from);
        }
    }

    @Override
    public void openGalleryDialog(String from) {

        mFrom = from;
        if (from.equals(AUCTION)) {
            mImagePathList = new ArrayList<>();
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
    public void setSettingsProfile(String imagePath) {
        mMainMvpController.setSettingsProfile(imagePath);
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
                    opennCamera(mFrom);
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

            mImagePathList.add(handleImage(data));
            mPresenter.openPost(getString(R.string.toolbar_title_post), mImagePathList);

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_AUCTION) {

            mImagePathList.add(handleImage(data));
            mPresenter.openPost(getString(R.string.toolbar_title_post), mImagePathList);

        } else if (resultCode == RESULT_OK && requestCode == CHOOSE_PHOTO_POST) {

            mImagePathList.add(handleImage(data));
            setPostPics(mImagePathList);

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_POST) {

            mImagePathList.add(handleImage(data));
            setPostPics(mImagePathList);

        } else if (resultCode == RESULT_OK && requestCode == PHOTO_SETTINGS) {

            setSettingsProfile(handleImage(data));

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

        if (title.equals("刊登") || title.equals("搜尋結果")) {
            mToolbar.setNavigationIcon(R.drawable.ic_left_arrow);
            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
            mToolbarLogout.setVisibility(View.GONE);
        } else if (title.equals("設定")) {

            mToolbarLogout.setVisibility(View.VISIBLE);
            mToolbarLogout.setOnClickListener( v -> {
                UserManager.getInstance().logout();
                Toast.makeText(this, "登出成功", Toast.LENGTH_SHORT).show();
                hideToolbarUi();
                hideBottomNavigationUi();
                mPresenter.openLogin();
            });
        }else {
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
        final View view = factory.inflate(R.layout.dialog_bid, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ((View) view.getParent()).setBackgroundColor(this.getColor(android.R.color.transparent));
        dialog.show();

        TextView mCurrentPrice;
        TextView mCurrentPriceTitle;
        TextView mIncreaseHint;
        TextView mIncreaseHintTitle;
        EditText mBidPrice;
        Button mCancelBtn;
        Button mPlaceBidBtn;
        TextView mWarningMsg;
        int placeBidTimes = product.getPlaceBidTimes();

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
                dialog.dismiss()
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

            if (from.equals(ENGLISH)) {

                if ("".equals(mBidPrice.getText().toString())) {

                    mWarningMsg.setVisibility(View.VISIBLE);
                    mWarningMsg.setText(R.string.please_place_bid);

                } else if (Integer.parseInt(mBidPrice.getText().toString()) < product.getCurrentPrice()) {

                    mWarningMsg.setVisibility(View.VISIBLE);
                    mWarningMsg.setText(getString(R.string.warning_msg_under_current_price));

                } else if ((Integer.parseInt(mBidPrice.getText().toString()) - product.getCurrentPrice()) < product.getIncrease()) {

                    mWarningMsg.setVisibility(View.VISIBLE);
                    mWarningMsg.setText(getString(R.string.warning_msg_under_increase));

                } else {

                    if (Integer.parseInt(mBidPrice.getText().toString()) < product.getReservePrice()) {
                        Toast.makeText(this, getString(R.string.warning_msg_under_reserve_price), Toast.LENGTH_LONG).show();
                    }

                    mWarningMsg.setVisibility(View.GONE);

                    product.setCurrentPrice(Integer.parseInt(mBidPrice.getText().toString()));
                    product.setHighestUserId(UserManager.getInstance().getUser().getId());
                    product.setBuyerName(UserManager.getInstance().getUser().getName());
                    product.setPlaceBidTimes(placeBidTimes + 1);

                    setAfterBidData(product);

                    Firebase.getInstance().getFirestore().collection("products")
                            .document(String.valueOf(product.getProductId()))
                            .update("currentPrice", Integer.parseInt(mBidPrice.getText().toString()),
                                    "highestUserId", UserManager.getInstance().getUser().getId(),
                                    "buyerName", UserManager.getInstance().getUser().getName(),
                                    "placeBidTimes", placeBidTimes + 1)
                            .addOnSuccessListener(aVoid -> Log.d("Johnsi", "BID DocumentSnapshot successfully updated!"))
                            .addOnFailureListener(e -> Log.w("Johnsi", "BID Error updating document", e));

                    ArrayList<Long> myProductsId = UserManager.getInstance().getUser().getMyBiddingProductsId();
                    boolean hasProduct = false;

                    for (int i = 0; i < myProductsId.size(); i++) {
                        if (myProductsId.get(i).equals(product.getProductId())) {
                            hasProduct = true;
                        }
                    }

                    if (!hasProduct) {
                        UserManager.getInstance().addBiddingProductId(product.getProductId());
                        UserManager.getInstance().updateUser2Firebase();
                    }


                    dialog.dismiss();
                    showMessageDialogUi(BID_SUCCESS);
                }

            } else {

                if ("".equals(mBidPrice.getText().toString())) {
                    mWarningMsg.setVisibility(View.VISIBLE);
                    mWarningMsg.setText(R.string.please_place_bid);
                } else {
                    mWarningMsg.setVisibility(View.GONE);

                    product.setPlaceBidTimes(placeBidTimes + 1);

                    if (Integer.parseInt(mBidPrice.getText().toString()) > product.getCurrentPrice()) {

                        Firebase.getInstance().getFirestore().collection("products")
                                .document(String.valueOf(product.getProductId()))
                                .update("currentPrice", Integer.parseInt(mBidPrice.getText().toString()),
                                        "highestUserId", UserManager.getInstance().getUser().getId(),
                                        "buyerName", UserManager.getInstance().getUser().getName(),
                                        "placeBidTimes", placeBidTimes + 1)
                                .addOnSuccessListener(aVoid -> Log.d("Johnsi", "BID DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("Johnsi", "BID Error updating document", e));
                    } else {
                        Firebase.getInstance().getFirestore().collection("products")
                                .document(String.valueOf(product.getProductId()))
                                .update("placeBidTimes", placeBidTimes + 1)
                                .addOnSuccessListener(aVoid -> Log.d("Johnsi", "BID DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("Johnsi", "BID Error updating document", e));
                    }

                    setAfterBidData(product);

                    UserManager.getInstance().addBiddingProductId(product.getProductId());
                    UserManager.getInstance().updateUser2Firebase();

                    dialog.dismiss();
                    showMessageDialogUi(BID_SUCCESS);
                }

            }
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

            Firebase.getInstance().getFirestore().collection("products")
                    .document(String.valueOf(product.getProductId()))
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("Johnsi", product.getProductId() + " successfully deleted!"))
                    .addOnFailureListener(e -> Log.w("Johnsi", "Error deleting document", e));

            Firebase.getInstance().getFirestore().collection("users")
                    .whereArrayContains("myBiddingProductsId", product.getProductId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Johnsi", document.getId() + " => " + document.getData());

                                Firebase.getInstance().getFirestore().collection("users")
                                        .document(document.getId())
                                        .update("myBiddingProductsId", FieldValue.arrayRemove(product.getProductId()))
                                        .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Bidding Products Id successfully removed!"))
                                        .addOnFailureListener(e -> Log.w("Johnsi", "Bidding Products Id Error updating document", e));

                            }
                        } else {
                            Log.d("Johnsi", "Error getting documents: ", task.getException());
                        }
                    });

            Firebase.getInstance().getFirestore().collection("users")
                    .whereArrayContains("eyesOn", product.getProductId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Johnsi", document.getId() + " => " + document.getData());

                                Firebase.getInstance().getFirestore().collection("users")
                                        .document(document.getId())
                                        .update("eyesOn", FieldValue.arrayRemove(product.getProductId()))
                                        .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Bidding Products Id successfully removed!"))
                                        .addOnFailureListener(e -> Log.w("Johnsi", "Bidding Products Id Error updating document", e));

                            }
                        } else {
                            Log.d("Johnsi", "Error getting documents: ", task.getException());
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
    public void updateTradeBadgeUi(int unreadCount) {

        if (unreadCount > 0) {
            mBadge.findViewById(R.id.text_badge_main).setVisibility(View.VISIBLE);
            ((TextView) mBadge.findViewById(R.id.text_badge_main)).setText("!");
        } else {
            mBadge.findViewById(R.id.text_badge_main).setVisibility(View.GONE);
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

    private void init() {
        mMainMvpController = MainMvpController.create(this);

        setToolbar();

        if (UserManager.getInstance().isLoggedIn()) {
            UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
                @Override
                public void onSuccess() {
                    Log.d("Johnsi", "Load User Profile Success !");
                    UserManager.getInstance().setHasUserDataChange(false);

                    setBottomNavigation();
                    mPresenter.openCenter();
                    showToolbarUi();
                    showBottomNavigationUi();
                }

                @Override
                public void onFail(String errorMessage) {
                    Log.d("Johnsi", "Load User Profile Fail !");
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = menuItem -> {

        switch (menuItem.getItemId()) {
            case R.id.navigation_center:
                mPresenter.updateToolbar("拍賣中心");
                mPresenter.openCenter();
                return true;
            case R.id.navigation_trade:
                mPresenter.updateToolbar("我的交易");
                mPresenter.openTrade();
                return true;
            case R.id.navigation_message:
                mPresenter.updateToolbar("聊聊");
                mPresenter.openChat();
                return true;
            case R.id.navigation_settings:
                mPresenter.updateToolbar("設定");
                mPresenter.openSettings();
                return true;
            default:
        }
        return false;
    };

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

    public void opennCamera(String from) {

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

    private String handleImage(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android,providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        }

        return imagePath;
    }

    public String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }
        cursor.close();
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitImage = BitmapFactory.decodeFile(imagePath);//格式化圖片

//            mImage.setImageBitmap(bitImage);//為imageView設定圖片
            Toast.makeText(MainActivity.this, imagePath, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.this, "獲取圖片失敗", Toast.LENGTH_SHORT).show();
        }
    }

    private void ScalePic(Bitmap bitmap, int phone) {
        //縮放比例預設為1
        float mScale = 1;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if (bitmap.getWidth() > phone) {
            //判斷縮放比例
            mScale = (float) phone / (float) bitmap.getWidth();

            Matrix mMat = new Matrix();
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
        } else {

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
