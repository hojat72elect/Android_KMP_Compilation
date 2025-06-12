package com.amaze.fileutilities.home_page.welcome_helper;


class NextButton extends WelcomeViewWrapper {

    private boolean shouldShow = true;

    public NextButton(android.view.View button) {
        super(button);
    }

    @Override
    public void setup(WelcomeConfiguration config) {
        super.setup(config);
        this.shouldShow = config.getShowNextButton();
    }

    @Override
    public void onPageSelected(int pageIndex, int firstPageIndex, int lastPageIndex) {
        setVisibility(shouldShow && WelcomeUtils.isIndexBeforeLastPage(pageIndex, lastPageIndex, isRtl));
    }
}
