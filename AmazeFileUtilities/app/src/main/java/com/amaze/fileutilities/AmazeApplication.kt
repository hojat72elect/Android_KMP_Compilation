package com.amaze.fileutilities

import androidx.multidex.MultiDexApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory

open class AmazeApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH)
    }
}
