package com.amaze.fileutilities.home_page.welcome_helper;

class ColorChangingBackgroundView extends android.view.View {

    private BackgroundColor[] mColors = new BackgroundColor[0];

    private int mCurrentPosition = 0;
    private float mOffset = 0;

    private android.graphics.Paint mPaint = null;
    private final android.graphics.Rect mRect = new android.graphics.Rect();

    public ColorChangingBackgroundView(android.content.Context context) {
        super(context);
    }

    public ColorChangingBackgroundView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorChangingBackgroundView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPosition(int position, float offset) {
        mCurrentPosition = position;
        mOffset = offset;
        invalidate();
    }

    public void setColors(BackgroundColor[] colors) {
        mColors = colors;
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        if (mPaint == null) {
            mPaint = new android.graphics.Paint();
        }

        if (mColors == null || mColors.length == 0)
            return;

        getDrawingRect(mRect);
        mPaint.setColor(mColors[mCurrentPosition].value());
        mPaint.setAlpha(255);
        canvas.drawRect(mRect, mPaint);

        if (mCurrentPosition != mColors.length - 1) {
            mPaint.setColor(mColors[mCurrentPosition + 1].value());
            mPaint.setAlpha((int) (mOffset * 255f));
            canvas.drawRect(mRect, mPaint);
        }

    }

}
