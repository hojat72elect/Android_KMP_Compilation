package com.amaze.fileutilities.home_page.ui.options

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amaze.fileutilities.BuildConfig
import com.amaze.fileutilities.R
import com.amaze.fileutilities.databinding.ActivityAboutBinding
import com.amaze.fileutilities.home_page.ui.files.FilesViewModel

class AboutActivity : AppCompatActivity() {

    private var _binding: ActivityAboutBinding? = null
    private lateinit var viewModel: FilesViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FilesViewModel::class.java)
        _binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.homeScreenDesign.text = Html.fromHtml(getString(R.string.home_screen_design))
        binding.version.text = BuildConfig.SUDO_VERSION_NAME
        Linkify.addLinks(binding.homeScreenDesign, Linkify.WEB_URLS)
        binding.homeScreenDesign.movementMethod = LinkMovementMethod.getInstance()
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources
                    .getColor(R.color.navy_blue)
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
