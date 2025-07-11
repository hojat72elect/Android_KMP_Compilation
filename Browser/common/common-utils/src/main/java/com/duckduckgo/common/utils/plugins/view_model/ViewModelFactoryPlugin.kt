

package com.duckduckgo.common.utils.plugins.view_model

import androidx.lifecycle.ViewModel

interface ViewModelFactoryPlugin {
    fun <T : ViewModel?> create(modelClass: Class<T>): T?
}
