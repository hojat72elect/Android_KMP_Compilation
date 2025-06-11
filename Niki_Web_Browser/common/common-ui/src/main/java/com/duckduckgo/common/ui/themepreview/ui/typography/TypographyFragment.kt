

package com.duckduckgo.common.ui.themepreview.ui.typography

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duckduckgo.common.ui.view.text.DaxTextView
import com.duckduckgo.common.ui.view.text.DaxTextView.Typography
import com.duckduckgo.mobile.android.R

/** Fragment to display a list of subsystems that show the values of this app's theme. */
@SuppressLint("NoFragment") // we don't use DI here
class TypographyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_components_typography, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceBundle: Bundle?,
    ) {
        val daxTextView = view.findViewById<DaxTextView>(R.id.typographyTitle)
        daxTextView.setTypography(Typography.Body1)
    }
}
