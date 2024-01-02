package com.example.myapplication.Activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class DimBackgroundPopup extends PopupWindow {

    public DimBackgroundPopup(Context context, int layoutResourceId, int width, int height) {
        super(context);
        setOutsideTouchable(true);
        setFocusable(true);
        setWidth(width);
        setHeight(height);
        setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View view = LayoutInflater.from(context).inflate(layoutResourceId, null);
        setContentView(view);

        setOnDismissListener(() -> dimBehind(1.0f));
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        dimBehind(0.5f);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor) {
        dimBehind(0.5f);
        super.showAsDropDown(anchor);
    }

    private void dimBehind(float alpha) {
        View container = getContentView().getRootView();
        Context context = container.getContext();

        if (container.getWindowToken() != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new WindowManager.LayoutParams();
            }

            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = alpha;
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.updateViewLayout(container, layoutParams);
            }
        }
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();

        if (layoutParams == null) {
            layoutParams = new WindowManager.LayoutParams();
        }

        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = alpha;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.updateViewLayout(container, layoutParams);
        }
    }
}