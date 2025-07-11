

package com.duckduckgo.app.onboarding.ui

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.duckduckgo.app.onboarding.ui.page.OnboardingPageFragment

class PagerAdapter(
    fragmentManager: FragmentManager,
    private val viewModel: OnboardingViewModel,
) :
    FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return viewModel.pageCount()
    }

    override fun getItem(position: Int): OnboardingPageFragment {
        return viewModel.getItem(position) ?: throw IllegalArgumentException("No items exists at position $position")
    }
}
