

package com.duckduckgo.autofill.api

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this)) // Set a simple view as the content
    }
}
