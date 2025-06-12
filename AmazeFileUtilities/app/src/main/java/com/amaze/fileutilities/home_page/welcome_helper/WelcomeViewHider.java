package com.amaze.fileutilities.home_page.welcome_helper;

import android.view.View;


class WelcomeViewHider implements OnWelcomeScreenPageChangeListener {

    private final View view;
    private Integer lastPage = null;
    private boolean isRtl = false;
    private WelcomeViewHider.OnViewHiddenListener listener = null;
    private boolean enabled = false;

    public WelcomeViewHider(android.view.View viewToHide) {
        view = viewToHide;
    }

    public interface OnViewHiddenListener {
        void onViewHidden();
    }

    public void setOnViewHiddenListener(WelcomeViewHider.OnViewHiddenListener listener) {
        this.listener = listener;
    }

    @Override
    public void setup(WelcomeConfiguration config) {
        enabled = config.getSwipeToDismiss();
        lastPage = config.lastPageIndex();
        isRtl = config.isRtl();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (!enabled) {
            return;
        }

        if (position == lastPage && positionOffset == 0 && listener != null) {
            listener.onViewHidden();
        }

        boolean shouldSetAlpha = position == (isRtl ? lastPage : lastPage - 1);
        float alpha = isRtl ? positionOffset : 1f - positionOffset;

        boolean beforeLastPage = isRtl ? position > lastPage : position < lastPage - 1;

        if (shouldSetAlpha) {
            view.setAlpha(alpha);
        } else if (beforeLastPage && view.getAlpha() != 1f) {
            view.setAlpha(1f);
        }

    }

    @Override
    public void onPageSelected(int position) {
        //Not needed
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Not needed
    }
}
