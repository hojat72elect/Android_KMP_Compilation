package com.amaze.fileutilities.home_page.welcome_helper;

class DoneButton extends WelcomeViewWrapper {

    private boolean shouldShow = true;

    public DoneButton(android.view.View button) {
        super(button);
        if (button != null) hideImmediately();
    }

    @Override
    public void setup(WelcomeConfiguration config) {
        super.setup(config);
        shouldShow = !config.getUseCustomDoneButton();
        if (this.getView() instanceof android.widget.TextView) {
            WelcomeUtils.setTypeface((android.widget.TextView) this.getView(), config.getDoneButtonTypefacePath(), config.getContext());
        }
    }

    @Override
    public void onPageSelected(int pageIndex, int firstPageIndex, int lastPageIndex) {
        setVisibility(shouldShow && !WelcomeUtils.isIndexBeforeLastPage(pageIndex, lastPageIndex, isRtl));
    }


}
