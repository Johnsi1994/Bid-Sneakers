package com.johnson.bid.util;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class ProfileAvatarOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        view.setClipToOutline(true);
        outline.setOval(0, 0, view.getWidth(), view.getHeight());
    }
}
