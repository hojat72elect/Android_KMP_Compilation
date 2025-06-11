

package com.duckduckgo.common.ui.internal.experiments.visual

import android.content.Context
import android.view.View
import com.duckduckgo.common.ui.internal.experiments.ExperimentalUIPlugin
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class VisualDesignExperimentPlugin @Inject constructor() : ExperimentalUIPlugin {

    override fun getView(context: Context): View {
        return VisualDesignExperimentView(context)
    }
}
