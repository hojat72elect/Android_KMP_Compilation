package com.amaze.fileutilities.home_page.welcome_helper;

import android.annotation.SuppressLint;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.amaze.fileutilities.R;

public abstract class WelcomeActivity extends AppCompatActivity {

    public static final String WELCOME_SCREEN_KEY = "welcome_screen_key";

    protected ViewPager viewPager;
    private WelcomeConfiguration configuration;
    private WelcomeItemList responsiveItems = new WelcomeItemList();

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        configuration = configuration();

        /* Passing null for savedInstanceState fixes issue with fragments in list not matching
           the displayed ones after the screen was rotated. (Parallax animations would stop working)
           TODO: Look into this more
         */
        super.onCreate(null);
        setContentView(R.layout.wel_activity_welcome);

        com.amaze.fileutilities.home_page.welcome_helper.WelcomeActivity.WelcomeFragmentPagerAdapter adapter = new com.amaze.fileutilities.home_page.welcome_helper.WelcomeActivity.WelcomeFragmentPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.wel_view_pager);
        viewPager.setAdapter(adapter);

        responsiveItems = new WelcomeItemList();

        // -- Inflate the bottom layout -- //

        android.widget.FrameLayout bottomFrame = findViewById(R.id.wel_bottom_frame);
        android.view.View.inflate(this, configuration.getBottomLayoutResId(), bottomFrame);

        if (configuration.getShowActionBarBackButton()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(true);
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        SkipButton skip = new SkipButton(findViewById(R.id.wel_button_skip));
        addViewWrapper(skip, v -> completeWelcomeScreen());

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        PreviousButton prev = new PreviousButton(findViewById(R.id.wel_button_prev));
        addViewWrapper(prev, v -> scrollToPreviousPage());

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        NextButton next = new NextButton(findViewById(R.id.wel_button_next));
        addViewWrapper(next, v -> scrollToNextPage());

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        DoneButton done = new DoneButton(findViewById(R.id.wel_button_done));
        addViewWrapper(done, v -> completeWelcomeScreen());

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        android.view.View firstBarButton = findViewById(R.id.wel_button_bar_first);
        if (firstBarButton != null) {
            firstBarButton.setOnClickListener(v -> onButtonBarFirstPressed());
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        android.view.View secondBarButton = findViewById(R.id.wel_button_bar_second);
        if (secondBarButton != null) {
            secondBarButton.setOnClickListener(v -> onButtonBarSecondPressed());
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        WelcomeViewPagerIndicator indicator = findViewById(R.id.wel_pager_indicator);
        if (indicator != null) {
            responsiveItems.add(indicator);
        }

        WelcomeBackgroundView background = findViewById(R.id.wel_background_view);

        WelcomeViewHider hider = new WelcomeViewHider(findViewById(R.id.wel_root));
        hider.setOnViewHiddenListener(this::completeWelcomeScreen);

        responsiveItems.addAll(background, hider, configuration.getPages());

        responsiveItems.setup(configuration);

        viewPager.addOnPageChangeListener(responsiveItems);
        viewPager.setCurrentItem(configuration.firstPageIndex());

        responsiveItems.onPageSelected(viewPager.getCurrentItem());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewPager != null) {
            viewPager.clearOnPageChangeListeners();
        }
    }

    /**
     * Helper method to add button to list,
     * checks for null first
     */
    private void addViewWrapper(WelcomeViewWrapper wrapper, View.OnClickListener onClickListener) {
        if (wrapper.getView() != null) {
            wrapper.setOnClickListener(onClickListener);
            responsiveItems.add(wrapper);
        }
    }

    void scrollToNextPage() {
        if (!canScrollToNextPage()) {
            return;
        }
        viewPager.setCurrentItem(getNextPageIndex());
    }

    boolean scrollToPreviousPage() {
        if (!canScrollToPreviousPage()) {
            return false;
        }
        viewPager.setCurrentItem(getPreviousPageIndex());
        return true;
    }

    protected int getNextPageIndex() {
        return viewPager.getCurrentItem() + (configuration.isRtl() ? -1 : 1);
    }

    protected int getPreviousPageIndex() {
        return viewPager.getCurrentItem() + (configuration.isRtl() ? 1 : -1);
    }

    protected boolean canScrollToNextPage() {
        return configuration.isRtl() ? getNextPageIndex() >= configuration.lastViewablePageIndex() : getNextPageIndex() <= configuration.lastViewablePageIndex();
    }

    protected boolean canScrollToPreviousPage() {
        return configuration.isRtl() ? getPreviousPageIndex() <= configuration.firstPageIndex() : getPreviousPageIndex() >= configuration.firstPageIndex();
    }

    /**
     * Called when the first (left in LTR layout direction) button
     * is pressed when using the button bar bottom layout
     */
    protected void onButtonBarFirstPressed() {

    }

    /**
     * Called when the second (right in LTR layout direction) button
     * is pressed when using the button bar bottom layout
     */
    protected void onButtonBarSecondPressed() {

    }

    /**
     * Closes the activity and saves it as completed.
     * A subsequent call to WelcomeScreenHelper.show() would not show this again,
     * unless the key is changed.
     */
    protected void completeWelcomeScreen() {
        WelcomeSharedPreferencesHelper.storeWelcomeCompleted(this, getKey());
        setWelcomeScreenResult(RESULT_OK);
        finish();
        if (configuration.getExitAnimation() != WelcomeConfiguration.NO_ANIMATION_SET)
            overridePendingTransition(R.anim.wel_none, configuration.getExitAnimation());
    }

    /**
     * Closes the activity, doesn't save as completed.
     * A subsequent call to WelcomeScreenHelper.show() would show this again.
     */
    protected void cancelWelcomeScreen() {
        setWelcomeScreenResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {

        // Scroll to previous page and return if back button navigates
        if (configuration.getBackButtonNavigatesPages() && scrollToPreviousPage()) {
            return;
        }

        if (configuration.getCanSkip() && configuration.getBackButtonSkips()) {
            completeWelcomeScreen();
        } else {
            cancelWelcomeScreen();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (configuration.getShowActionBarBackButton() && item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setWelcomeScreenResult(int resultCode) {
        android.content.Intent intent = this.getIntent();
        intent.putExtra(WELCOME_SCREEN_KEY, getKey());
        this.setResult(resultCode, intent);
    }

    private String getKey() {
        return WelcomeUtils.getKey(this.getClass());
    }

    protected abstract WelcomeConfiguration configuration();

    private class WelcomeFragmentPagerAdapter extends FragmentPagerAdapter {

        public WelcomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return configuration.createFragment(position);
        }

        @Override
        public int getCount() {
            return configuration.pageCount();
        }
    }
}
