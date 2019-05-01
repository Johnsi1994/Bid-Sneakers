package com.johnson.bid.util;

import android.content.Context;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.johnson.bid.Bid;

public class CardViewImageOutlineProvider extends ViewOutlineProvider {

    @Override
    public void getOutline(View view, Outline outline) {
        view.setClipToOutline(true);
        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), dip2px(Bid.getAppContext(), 4));
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
