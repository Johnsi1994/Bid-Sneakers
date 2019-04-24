package com.johnson.bid.search;

import android.support.annotation.NonNull;

import com.johnson.bid.data.Product;
import com.johnson.bid.util.Firebase;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View mSearchView;
    private String mKeyword;

    public SearchPresenter(@NonNull SearchContract.View searchView) {
        mSearchView = checkNotNull(searchView, "searchView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void updateToolbar(String toolbarTitle) {

    }

    @Override
    public void openBidding(String from, Product product) {

    }

    @Override
    public void hideToolbarAndBottomNavigation() {

    }

    @Override
    public void loadSearchData() {

        ArrayList<Product> allBiddingProducts = Firebase.getInstance().getAllBiddingProducts();
        ArrayList<Product> searchProducts = new ArrayList<>();
        String keywordLowercase = mKeyword.toLowerCase();
        String productLowercase;

        if (allBiddingProducts != null) {
            for (int i = 0; i < allBiddingProducts.size(); i++) {
                productLowercase = allBiddingProducts.get(i).getTitle().toLowerCase();

                if (productLowercase.contains(keywordLowercase)) {
                    searchProducts.add(allBiddingProducts.get(i));
                }
            }
        }

        mSearchView.showSearchUi(searchProducts);
    }

    @Override
    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    @Override
    public void showBottomNavigation() {

    }
}
