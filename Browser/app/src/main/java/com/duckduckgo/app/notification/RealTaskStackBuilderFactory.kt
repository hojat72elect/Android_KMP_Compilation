

package com.duckduckgo.app.notification

import android.app.TaskStackBuilder
import android.content.Context
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealTaskStackBuilderFactory @Inject constructor(
    private val context: Context,
) : TaskStackBuilderFactory {
    override fun createTaskBuilder(): TaskStackBuilder {
        return TaskStackBuilder.create(context)
    }
}
