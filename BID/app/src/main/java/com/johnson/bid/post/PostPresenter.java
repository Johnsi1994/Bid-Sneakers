package com.johnson.bid.post;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.johnson.bid.Bid;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Constants;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class PostPresenter implements PostContract.Presenter {

    private PostContract.View mPostView;

    private Product mProduct;
    private ArrayList<Bitmap> mImageBitmap;
    private ArrayList<String> mUrlList = new ArrayList<>();

    public PostPresenter(@NonNull PostContract.View postView) {
        mPostView = checkNotNull(postView, "postView cannot be null!");
        mProduct = new Product();
    }

    @Override
    public void start() {}

    @Override
    public void setProductTitle(String productTitle) {
        mProduct.setTitle(productTitle);
    }

    @Override
    public void setAuctionCondition(String auctionCondition) {
        mProduct.setAuctionCondition(auctionCondition);
    }

    @Override
    public void setProductIntro(String productIntro) {
        mProduct.setIntroduction(productIntro);
    }

    @Override
    public void setStartingPrice(int startingPrice) {
        mProduct.setStartPrice(startingPrice);
    }

    @Override
    public void setReservePrice(int reservePrice) {
        mProduct.setReservePrice(reservePrice);
    }

    @Override
    public void setProductCondition(String condition) {
        mProduct.setCondition(condition);
    }

    @Override
    public void setAuctionType(String auctionType) {
        mProduct.setAuctionType(auctionType);
    }

    @Override
    public void setIncrease(int increase) {
        mProduct.setIncrease(increase);
    }

    @Override
    public void setExpireTime(long expireTime) {
        mProduct.setExpired(expireTime);
    }

    @Override
    public void setProductId(long productId) {
        mProduct.setProductId(productId);
    }

    @Override
    public void setPostProductId2User(long productId) {
        UserManager.getInstance().addSellingProductId(productId);
        UserManager.getInstance().updateUser2Firebase();
    }

    @Override
    public void setStartingTime(long startingTime) {
        mProduct.setStartTime(startingTime);
    }

    @Override
    public void setImages() {
        mProduct.setImages(mUrlList);
    }

    @Override
    public void setSellerId(long sellerId) {
        mProduct.setSellerId(sellerId);
    }

    @Override
    public void setPlaceBidTimes(int participantsNumber) {
        mProduct.setPlaceBidTimes(participantsNumber);
    }

    @Override
    public void setCurrentPrice(int currentPrice) {
        mProduct.setCurrentPrice(currentPrice);
    }

    @Override
    public void showBottomNavigation() {}

    @Override
    public void updateToolbar(String title) {}

    @Override
    public void setPostPics(ArrayList<Bitmap> imageBitmap) {
        mImageBitmap = imageBitmap;
        loadPostPics();
    }

    @Override
    public void loadPostPics() {
        if (mImageBitmap != null) {
            mPostView.showPostUi(mImageBitmap);
        }
    }

    @Override
    public void openGalleryDialog(String from) {}

    @Override
    public void showPostSuccessDialog() {}

    @Override
    public void updateAuctionData() {}

    @Override
    public void setSellerHasRead(boolean isRead) {
        mProduct.setSellerHasRead(isRead);
    }

    @Override
    public void setBuyerHasRead(boolean isRead) {
        mProduct.setBuyerHasRead(isRead);
    }

    @Override
    public void setSellerName(String name) {
        mProduct.setSellerName(name);
    }

    @Override
    public void uploadImages(int i, UserManager.LoadCallback loadCallback) {

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
                                uploadImages(j, loadCallback);
                            } else {
                                Log.d(Constants.TAG, "UploadImages success then upload product");
                                loadCallback.onSuccess();
                            }
                        })
                )
                .addOnFailureListener(exception -> Log.d(Constants.TAG, exception.getMessage()));
    }

    @Override
    public void uploadProduct(long id) {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_products))
                .document(String.valueOf(id))
                .set(mProduct)
                .addOnSuccessListener(documentReference -> {
                    Log.d(Constants.TAG, "DocumentSnapshot added");
                    mPostView.showAuctionDataUI();
                })
                .addOnFailureListener(e -> Log.w(Constants.TAG, "Error adding document", e));
    }

    private void setUrl(String url) {
        mUrlList.add(url);
    }
}
