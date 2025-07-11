package com.simplemobiletools.smsmessenger.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.simplemobiletools.smsmessenger.compose.extensions.enableEdgeToEdgeSimple
import com.simplemobiletools.smsmessenger.compose.screens.FAQScreen
import com.simplemobiletools.smsmessenger.compose.theme.AppThemeSurface
import com.simplemobiletools.smsmessenger.helpers.APP_FAQ
import com.simplemobiletools.smsmessenger.models.FAQItem
import kotlinx.collections.immutable.toImmutableList

class FAQActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                val faqItems =
                    remember { intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem> }
                FAQScreen(
                    goBack = ::finish,
                    faqItems = faqItems.toImmutableList()
                )
            }
        }
    }
}
