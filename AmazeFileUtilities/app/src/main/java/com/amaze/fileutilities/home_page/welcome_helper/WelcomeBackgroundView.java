package com.amaze.fileutilities.home_page.welcome_helper;


public class WelcomeBackgroundView extends ColorChangingBackgroundView implements OnWelcomeScreenPageChangeListener {

    public WelcomeBackgroundView(android.content.Context context) {
        super(context);
    }

    public WelcomeBackgroundView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public WelcomeBackgroundView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setup(WelcomeConfiguration config) {
        setColors(config.getBackgroundColors());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setPosition(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        //Not used
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Not used
    }

}
