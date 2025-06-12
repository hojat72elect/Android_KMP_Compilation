package com.amaze.fileutilities.home_page.welcome_helper;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

/**
 * A page with a large image, header, and description.
 */
public class BasicPage extends WelcomePage<com.amaze.fileutilities.home_page.welcome_helper.BasicPage> {

    private final int drawableResId;
    private final String title;
    private final String description;
    private String headerTypefacePath = null;
    private String descriptionTypefacePath = null;

    /**
     * A page with a large image, header, and description
     *
     * @param drawableResId Resource id of drawable to show
     * @param title         Title, shown in large font
     * @param description   Description, shown beneath title
     */
    public BasicPage(@DrawableRes int drawableResId, String title, String description) {
        this.drawableResId = drawableResId;
        this.title = title;
        this.description = description;
    }

    /**
     * Set the typeface of the header
     *
     * @param typefacePath The path to a typeface in the assets folder
     */
    public void headerTypeface(String typefacePath) {
        this.headerTypefacePath = typefacePath;
    }

    /**
     * Set the typeface of the description
     *
     * @param typefacePath The path to a typeface in the assets folder
     */
    public void descriptionTypeface(String typefacePath) {
        this.descriptionTypefacePath = typefacePath;
    }

    @Override
    public void setup(WelcomeConfiguration config) {
        super.setup(config);

        if (this.headerTypefacePath == null) {
            headerTypeface(config.getDefaultHeaderTypefacePath());
        }

        if (this.descriptionTypefacePath == null) {
            descriptionTypeface(config.getDefaultDescriptionTypefacePath());
        }

    }

    @Override
    public Fragment fragment() {
        // TODO: So many arguments...refactor?
        int headerColor = WelcomeUtils.NO_COLOR_SET;
        boolean showParallax = true;
        int descriptionColor = WelcomeUtils.NO_COLOR_SET;
        return WelcomeBasicFragment.newInstance(drawableResId,
                title,
                description,
                showParallax,
                headerTypefacePath,
                descriptionTypefacePath,
                headerColor,
                descriptionColor);
    }

}
