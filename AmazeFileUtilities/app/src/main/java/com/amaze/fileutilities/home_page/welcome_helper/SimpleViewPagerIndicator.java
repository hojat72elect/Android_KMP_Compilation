package com.amaze.fileutilities.home_page.welcome_helper;

import android.content.res.TypedArray;
import android.view.View;
import androidx.viewpager.widget.ViewPager;
import com.amaze.fileutilities.R;

/**
 * A quick and dirty ViewPager indicator.
 */
class SimpleViewPagerIndicator extends View implements ViewPager.OnPageChangeListener {

    public enum Animation {
        NONE, SLIDE, FADE
    }

    private android.graphics.Paint paint;

    private int currentPageColor = 0x99ffffff;
    private int otherPageColor = 0x22000000;

    private float currentMaxAlpha;

    private int totalPages = 0;
    private int currentPage = 0;
    private int displayedPage = 0;
    private float currentPageOffset = 0;

    //Used to show correct position when rtl and swipeToDismiss
    private int pageIndexOffset = 0;

    private int spacing = 16;
    private int size = 4;

    private com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation animation = com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation.FADE;
    private boolean isRtl = false;

    public SimpleViewPagerIndicator(android.content.Context context) {
        this(context, null);
    }

    public SimpleViewPagerIndicator(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.welcomeIndicatorStyle);
    }

    public SimpleViewPagerIndicator(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleViewPagerIndicator, defStyleAttr, 0);
            currentPageColor = a.getColor(R.styleable.SimpleViewPagerIndicator_currentPageColor, currentPageColor);
            otherPageColor = a.getColor(R.styleable.SimpleViewPagerIndicator_indicatorColor, otherPageColor);
            int animationInt = a.getInt(R.styleable.SimpleViewPagerIndicator_animation, animation.ordinal());
            animation = animationFromInt(animationInt, animation);

            a.recycle();
        }

        init(context);
    }

    private void init(android.content.Context context) {
        paint = new android.graphics.Paint();
        paint.setAntiAlias(true);

        float density = context.getResources().getDisplayMetrics().density;
        spacing *= (int) density;
        size *= (int) density;

        currentMaxAlpha = android.graphics.Color.alpha(currentPageColor);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (animation != com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation.NONE) {
            setPosition(position);
            currentPageOffset = canShowAnimation() ? 0 : positionOffset;
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (animation == com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation.NONE) {
            setPosition(position);
            currentPageOffset = 0;
            invalidate();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Not used
    }

    private com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation animationFromInt(int i, com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation fallback) {
        for (com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation animation : com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation.values()) {
            if (animation.ordinal() == i) {
                return animation;
            }
        }
        return fallback;
    }

    private boolean canShowAnimation() {
        return isRtl ? currentPage < 0 : currentPage == totalPages - 1;
    }

    private android.graphics.Point getCenter() {
        return new android.graphics.Point((getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2);
    }

    public void setPosition(int position) {
        currentPage = position + pageIndexOffset;
        displayedPage = isRtl ? Math.max(currentPage, 0) : Math.min(currentPage, totalPages - 1);
        invalidate();
    }

    public int getPosition() {
        return currentPage;
    }


    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        invalidate();
    }

    public void setRtl(boolean rtl) {
        this.isRtl = rtl;
    }

    public boolean isRtl() {
        return isRtl;
    }

    public void setPageIndexOffset(int offset) {
        this.pageIndexOffset = offset;
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        android.graphics.Point center = getCenter();
        float startX = getFirstDotPosition(center.x);

        paint.setColor(otherPageColor);
        for (int i = 0; i < totalPages; i++) {
            canvas.drawCircle(startX + spacing * i, center.y, size, paint);
        }

        paint.setColor(currentPageColor);

        if (animation == com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation.NONE || animation == com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation.SLIDE) {
            canvas.drawCircle(startX + (spacing * (displayedPage + currentPageOffset)), center.y, size, paint);
        } else if (animation == com.amaze.fileutilities.home_page.welcome_helper.SimpleViewPagerIndicator.Animation.FADE) {
            paint.setAlpha((int) (currentMaxAlpha * (1f - currentPageOffset)));
            canvas.drawCircle(startX + (spacing * displayedPage), center.y, size, paint);
            paint.setAlpha((int) (currentMaxAlpha * currentPageOffset));
            canvas.drawCircle(startX + (spacing * (displayedPage + 1)), center.y, size, paint);
        }

    }

    private float getFirstDotPosition(float centerX) {
        float centerIndex = totalPages % 2 == 0 ? (totalPages - 1) / 2F : totalPages / 2F;
        float spacingMult = (float) Math.floor(centerIndex);
        if (totalPages % 2 == 0)
            spacingMult += 0.5F;
        return centerX - (spacing * spacingMult);
    }

}
