package com.johnson.bid.eyeson;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

import java.util.ArrayList;

public class EyesOnContract {

    interface View extends BaseView<Presenter> {

        void showEyesOnUi(ArrayList<Product> products);

    }

    public interface Presenter extends BasePresenter {

        void showBottomNavigation();

        void updateToolbar(String title);

        void openBidding(String from, Product product);

        void hideToolbarAndBottomNavigation();

        void loadEyesOnData();

        void setMyEyesOnData(ArrayList<Product> products);

    }

}
