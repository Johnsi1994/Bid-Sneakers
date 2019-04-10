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
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.johnson.bid.centre.auction.AuctionFragment;
import com.johnson.bid.dialog.MessageDialog;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private DisplayMetrics mPhone;
    private MessageDialog mMessageDialog;
    private final static int CAMERA = 666 ;
    private final static int CAMERA_INNER = 667 ;
    public final static int CHOOSE_PHOTO = 222;
    public final static int CHOOSE_PHOTO_INNER = 223;

    private MainContract.Presenter mPresenter;
    private MainMvpController mMainMvpController;

    private ArrayList<String> mImagePath;

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
    public void setPostPics(ArrayList<String> imagePath) {
        mMainMvpController.setPostPics(imagePath);
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
            mPhone = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mPhone);

            ContentValues value = new ContentValues();
            value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    value);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());

            if (from.equals("CENTER")) {
                startActivityForResult(intent, CAMERA);
            } else {
                startActivityForResult(intent, CAMERA_INNER);
            }

        }
    }

    @Override
    public void openGalleryDialog(String from) {

        if (from.equals("CENTER")) {
            mImagePath = new ArrayList<>();
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

        mCancelBtn = view.findViewById(R.id.button_cancel);
        mOpenGalleryBtn = view.findViewById(R.id.button_goto_gallery);
        mOpenCameraBtn = view.findViewById(R.id.button_open_camera);

        mCancelBtn.setOnClickListener( v -> dialog.dismiss());

        mOpenGalleryBtn.setOnClickListener( v -> {
            dialog.dismiss();
            openGallery(from);
        });

        mOpenCameraBtn.setOnClickListener( v -> {
            dialog.dismiss();
            openCamera(from);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum("CENTER");
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

        } else {

            UserManager.getInstance().getFbCallbackManager().onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public AuctionFragment findEnglishAuctionView() {
        return mMainMvpController.findOrCreateEnglishAuctionView();
    }

    @Override
    public AuctionFragment findSealedAuctionView() {
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
    public void findBiddingView() {
        mMainMvpController.createBiddingView();
    }

    @Override
    public void setToolbarTitleUi(String title) {

        if (title.equals("刊登")) {
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

    private void init() {
        mMainMvpController = MainMvpController.create(this);

        setToolbar();
        setBottomNavigation();

        if (UserManager.getInstance().isLoggedIn()) {
            mPresenter.openCenter();
            showToolbarUi();
            showBottomNavigationUi();
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

        if (from.equals("CENTER")) {
            startActivityForResult(intent, CHOOSE_PHOTO);
        } else {
            startActivityForResult(intent, CHOOSE_PHOTO_INNER);
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

    private void ScalePic(Bitmap bitmap,int phone) {
        //縮放比例預設為1
        float mScale = 1 ;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if(bitmap.getWidth() > phone ) {
            //判斷縮放比例
            mScale = (float)phone/(float)bitmap.getWidth();

            Matrix mMat = new Matrix() ;
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
