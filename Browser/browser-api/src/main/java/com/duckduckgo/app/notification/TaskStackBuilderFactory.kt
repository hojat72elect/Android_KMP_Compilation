

package com.duckduckgo.app.notification

import android.app.TaskStackBuilder

interface TaskStackBuilderFactory {
    fun createTaskBuilder(): TaskStackBuilder
}
