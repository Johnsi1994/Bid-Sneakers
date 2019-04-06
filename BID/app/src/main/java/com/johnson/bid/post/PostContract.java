package com.johnson.bid.post;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;

public interface PostContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void setProductTitle(String productTitle);

        void setProductIntro(String productIntro);

        void setStartingPrice(int startingPrice);

        void setReservePrice(int reservePrice);

        void setProductCondition(String condition);

        void setAuctionType(String auctionType);

        void setIncrease(int increase);

        void setStartingTime(long startingTime);

    }

}
