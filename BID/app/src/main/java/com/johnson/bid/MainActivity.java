package com.johnson.bid;

import android.Manifest;
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
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.auction.auctionitem.AuctionItemFragment;
import com.johnson.bid.data.Product;
import com.johnson.bid.dialog.MessageDialog;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.dialog.MessageDialog.BID_SUCCESS;
import static com.johnson.bid.dialog.MessageDialog.DELETE_SUCCESS;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private DisplayMetrics mPhone;
    private MessageDialog mMessageDialog;
    private View mBadge;

    private final static int CAMERA = 666;
    private final static int CAMERA_INNER = 667;
    public final static int CHOOSE_PHOTO = 222;
    public final static int CHOOSE_PHOTO_INNER = 223;

    private MainContract.Presenter mPresenter;
    private MainMvpController mMainMvpController;

    private ArrayList<String> mImagePath;
    private Boolean mIsFromCenter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void openEyesOnUi() {
        mMainMvpController.createEyesOnView();
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
    public void openGallery(Boolean isFromCenter) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum(isFromCenter);
        }
    }

    @Override
    public void openCamera(Boolean isFromCenter) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        } else {
            opennCamera(isFromCenter);
        }
    }

    @Override
    public void openGalleryDialog(String from) {

        if (from.equals("CENTER")) {
            mImagePath = new ArrayList<>();
            mIsFromCenter = true;
        } else {
            mIsFromCenter = false;
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
            openGallery(mIsFromCenter);
        });

        mOpenCameraBtn.setOnClickListener(v -> {
            dialog.dismiss();
            openCamera(mIsFromCenter);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(mIsFromCenter);
                } else {
                    Toast.makeText(this, "授權失敗，無法操作", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    opennCamera(mIsFromCenter);
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

        if (resultCode == RESULT_OK && requestCode == CHOOSE_PHOTO) {

            mImagePath.add(handleImage(data));
            mPresenter.openPost(getString(R.string.toolbar_title_post), mImagePath);

        } else if (resultCode == RESULT_OK && requestCode == CAMERA) {

            mImagePath.add(handleImage(data));
            mPresenter.openPost(getString(R.string.toolbar_title_post), mImagePath);

        } else if (resultCode == RESULT_OK && requestCode == CHOOSE_PHOTO_INNER) {

            mImagePath.add(handleImage(data));
            setPostPics(mImagePath);

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_INNER) {

            mImagePath.add(handleImage(data));
            setPostPics(mImagePath);

        } else if (resultCode == RESULT_CANCELED) {

        } else {
            UserManager.getInstance().getFbCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public AuctionItemFragment findEnglishAuctionView() {
        return mMainMvpController.findOrCreateEnglishAuctionView();
    }

    @Override
    public AuctionItemFragment findSealedAuctionView() {
        return mMainMvpController.findOrCreateSealedAuctionView();
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

        if (title.equals("刊登") || title.equals("關注")) {
            mToolbar.setNavigationIcon(R.drawable.ic_left_arrow);
            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        } else {
            mToolbar.setNavigationIcon(null);
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
                    product.setPlaceBidTimes(placeBidTimes + 1);

                    setAfterBidData(product);

                    Firebase.getFirestore().collection("products")
                            .document(String.valueOf(product.getProductId()))
                            .update("currentPrice", Integer.parseInt(mBidPrice.getText().toString()),
                                    "highestUserId", UserManager.getInstance().getUser().getId(),
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
                    }
                    UserManager.getInstance().setHasUserDataChange(true);

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

                        Firebase.getFirestore().collection("products")
                                .document(String.valueOf(product.getProductId()))
                                .update("currentPrice", Integer.parseInt(mBidPrice.getText().toString()),
                                        "highestUserId", UserManager.getInstance().getUser().getId(),
                                        "placeBidTimes", placeBidTimes + 1)
                                .addOnSuccessListener(aVoid -> Log.d("Johnsi", "BID DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("Johnsi", "BID Error updating document", e));
                    } else {
                        Firebase.getFirestore().collection("products")
                                .document(String.valueOf(product.getProductId()))
                                .update("placeBidTimes", placeBidTimes + 1)
                                .addOnSuccessListener(aVoid -> Log.d("Johnsi", "BID DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("Johnsi", "BID Error updating document", e));
                    }

                    setAfterBidData(product);

                    UserManager.getInstance().addBiddingProductId(product.getProductId());
                    UserManager.getInstance().setHasUserDataChange(true);

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

            Firebase.getFirestore().collection("products")
                    .document(String.valueOf(product.getProductId()))
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("Johnsi", product.getProductId() + " successfully deleted!"))
                    .addOnFailureListener(e -> Log.w("Johnsi", "Error deleting document", e));

            Firebase.getFirestore().collection("users")
                    .whereArrayContains("myBiddingProductsId", product.getProductId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Johnsi", document.getId() + " => " + document.getData());

                                Firebase.getFirestore().collection("users")
                                        .document(document.getId())
                                        .update("myBiddingProductsId", FieldValue.arrayRemove(product.getProductId()))
                                        .addOnSuccessListener(aVoid -> Log.d("Johnsi", "Bidding Products Id successfully removed!"))
                                        .addOnFailureListener(e -> Log.w("Johnsi", "Bidding Products Id Error updating document", e));

                            }
                        } else {
                            Log.d("Johnsi", "Error getting documents: ", task.getException());
                        }
                    });

            dialog.dismiss();
            showMessageDialogUi(DELETE_SUCCESS);
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



        if (UserManager.getInstance().isLoggedIn()) {
            UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
                @Override
                public void onSuccess() {
                    Log.d("Johnsi", "Load User Profile Success !");
                    UserManager.getInstance().setHasUserDataChange(false);

                    setToolbar();
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
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setBottomNavigation() {

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

    public void openAlbum(Boolean isFromCenter) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");

        if (isFromCenter) {
            startActivityForResult(intent, CHOOSE_PHOTO);
        } else {
            startActivityForResult(intent, CHOOSE_PHOTO_INNER);
        }
    }

    public void opennCamera(Boolean isFromCenter) {

        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        ContentValues value = new ContentValues();
        value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                value);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());

        if (isFromCenter) {
            startActivityForResult(intent, CAMERA);
        } else {
            startActivityForResult(intent, CAMERA_INNER);
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

}
