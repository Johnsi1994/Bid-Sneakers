package com.johnson.bid.post;

import android.graphics.Bitmap;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;

import java.util.ArrayList;

public interface PostContract {

    interface View extends BaseView<Presenter> {
        void showPostUi(ArrayList<Bitmap> bitmaps);
    }

    interface Presenter extends BasePresenter {

        void setProductTitle(String productTitle);

        void setProductIntro(String productIntro);

        void setStartingPrice(int startingPrice);

        void setReservePrice(int reservePrice);

        void setProductCondition(String condition);

        void setAuctionType(String auctionType);

        void setIncrease(int increase);

        void setExpireTime(long expireTime);

        void setProductId(long productId);

        void setStartingTime(long startingTime);

        void showBottomNavigation();

        void updateToolbar(String title);

        void setPostPics(ArrayList<Bitmap> bitmaps);

        void loadPostPics();


    }

}
