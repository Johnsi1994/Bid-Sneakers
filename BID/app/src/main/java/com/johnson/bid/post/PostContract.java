package com.johnson.bid.post;

import android.graphics.Bitmap;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

public interface PostContract {

    interface View extends BaseView<Presenter> {
        void showPostUi(ArrayList<Bitmap> imageBitmap);

        void showAuctionDataUI();
    }

    interface Presenter extends BasePresenter {

        void setProductTitle(String productTitle);

        void setAuctionCondition(String auctionCondition);

        void setProductIntro(String productIntro);

        void setStartingPrice(int startingPrice);

        void setReservePrice(int reservePrice);

        void setProductCondition(String condition);

        void setAuctionType(String auctionType);

        void setIncrease(int increase);

        void setExpireTime(long expireTime);

        void setProductId(long productId);

        void setPostProductId2User(long productId);

        void setStartingTime(long startingTime);

        void setImages();

        void setSellerId(long sellerId);

        void setPlaceBidTimes(int participantsNumber);

        void setCurrentPrice(int currentPrice);

        void showBottomNavigation();

        void updateToolbar(String title);

        void setPostPics(ArrayList<Bitmap> imageBitmap);

        void loadPostPics();

        void openGalleryDialog(String from);

        void showPostSuccessDialog();

        void updateAuctionData();

        void setSellerHasRead(boolean isRead);

        void setBuyerHasRead(boolean isRead);

        void setSellerName(String name);

        void uploadImages(int i, UserManager.LoadCallback loadCallback);

        void uploadProduct(long id);
    }

}
