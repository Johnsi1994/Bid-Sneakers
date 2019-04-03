package com.johnson.bid.settings;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View mSettingsView;

    public SettingsPresenter(@NonNull SettingsContract.View settingsView) {
        mSettingsView = checkNotNull(settingsView, "chatView cannot be null!");
    }

    @Override
    public void start() {

    }
}
