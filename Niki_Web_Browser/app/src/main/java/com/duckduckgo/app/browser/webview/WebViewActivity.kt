package com.duckduckgo.app.browser.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.duckduckgo.anvil.annotations.ContributeToActivityStarter
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.BrowserActivity
import com.duckduckgo.app.browser.BrowserWebViewClient
import com.duckduckgo.app.browser.databinding.ActivityWebviewBinding
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.browser.api.ui.BrowserScreens.WebViewActivityWithParams
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.navigation.api.getActivityParams
import com.duckduckgo.user.agent.api.UserAgentProvider
import javax.inject.Inject

@InjectWith(ActivityScope::class)
@ContributeToActivityStarter(WebViewActivityWithParams::class)
class WebViewActivity : DuckDuckGoActivity() {

    @Inject
    lateinit var userAgentProvider: UserAgentProvider

    @Inject
    lateinit var webViewClient: BrowserWebViewClient

    @Inject
    lateinit var pixel: Pixel

    private val binding: ActivityWebviewBinding by viewBinding()

    private val toolbar
        get() = binding.includeToolbar.toolbar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupToolbar(toolbar)

        val params = intent.getActivityParams(WebViewActivityWithParams::class.java)
        val url = params?.url
        title = params?.screenTitle.orEmpty()
        val supportNewWindows = params?.supportNewWindows ?: false

        binding.simpleWebview.let {
            it.webViewClient = webViewClient

            if (supportNewWindows) {
                it.webChromeClient = object : WebChromeClient() {
                    override fun onCreateWindow(
                        view: WebView?,
                        isDialog: Boolean,
                        isUserGesture: Boolean,
                        resultMsg: Message?,
                    ): Boolean {
                        view?.requestFocusNodeHref(resultMsg)
                        val newWindowUrl = resultMsg?.data?.getString("url")
                        if (newWindowUrl != null) {
                            startActivity(BrowserActivity.intent(this@WebViewActivity, newWindowUrl))
                            return true
                        }
                        return false
                    }
                }
            }

            it.settings.apply {
                userAgentString = userAgentProvider.userAgent()
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                displayZoomControls = false
                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                setSupportMultipleWindows(supportNewWindows)
                databaseEnabled = false
                setSupportZoom(true)
            }
        }

        url?.let {
            binding.simpleWebview.loadUrl(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.simpleWebview.canGoBack()) {
            binding.simpleWebview.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
