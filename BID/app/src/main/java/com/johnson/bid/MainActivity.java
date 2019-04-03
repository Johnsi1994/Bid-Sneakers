package com.johnson.bid;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private BottomNavigationView mBottomNavigation;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void openCenterUi() {

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private void init() {
        setBottomNavigation();
    }

    private void setBottomNavigation() {

        mBottomNavigation = findViewById(R.id.bottom_navigation_main);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setItemIconTintList(null); //去除BottomNavigation上面icon的顏色

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = menuItem -> {

        switch (menuItem.getItemId()) {
            case R.id.navigation_center:
                return true;
            case R.id.navigation_trade:
                return true;
            case R.id.navigation_message:
                return true;
            case R.id.navigation_settings:
                return true;
            default:
        }
        return false;
    };
}
