package com.johnson.bid.settings;

import android.graphics.Bitmap;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;

public interface SettingsContract {

    interface View extends BaseView<Presenter> {

        void showProfile(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter {

        void setUserName(String userName);

        void openGalleryDialog(String from);

        void setProfile(Bitmap bitmap);

    }

}
