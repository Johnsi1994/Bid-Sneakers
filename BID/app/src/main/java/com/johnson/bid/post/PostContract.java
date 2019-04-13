package com.johnson.bid.post;

import android.graphics.Bitmap;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

import java.util.ArrayList;

public interface PostContract {

    interface View extends BaseView<Presenter> {
        void showPostUi(ArrayList<String> imagePath);
    }

    interface Presenter extends BasePresenter {

        Product getProduct();

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

        void setProductId2User(long productId);

        void setStartingTime(long startingTime);

        void setImages(ArrayList<String> Url);

        void setSellerId(long sellerId);

        void setParticipantsNumber(int participantsNumber);

        void setCurrentPrice(int currentPrice);

        void showBottomNavigation();

        void updateToolbar(String title);

        void setPostPics(ArrayList<String> imagePath);

        void loadPostPics();

        void openGalleryDialog(String from);

        void showPostSuccessDialog();

        void openCenter();
    }

}
