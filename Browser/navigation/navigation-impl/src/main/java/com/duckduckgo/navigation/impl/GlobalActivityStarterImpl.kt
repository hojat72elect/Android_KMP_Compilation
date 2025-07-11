

package com.duckduckgo.navigation.impl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.duckduckgo.di.DaggerSet
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.navigation.api.GlobalActivityStarter.ActivityParams
import com.duckduckgo.navigation.api.GlobalActivityStarter.DeeplinkActivityParams
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.lang.IllegalArgumentException
import javax.inject.Inject
import logcat.logcat

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class GlobalActivityStarterImpl @Inject constructor(
    private val activityMappers: DaggerSet<GlobalActivityStarter.ParamToActivityMapper>,
) : GlobalActivityStarter {
    override fun start(context: Context, params: GlobalActivityStarter.ActivityParams, options: Bundle?) {
        startIntent(context, params)?.let {
            logcat { "Activity $it for params $params found" }
            context.startActivity(it, options)
        } ?: throw IllegalArgumentException("Activity for params $params not found")
    }

    override fun startIntent(context: Context, params: GlobalActivityStarter.ActivityParams): Intent? {
        val activityClass = activityMappers.firstNotNullOfOrNull {
            it.map(params)
        }

        return activityClass?.let {
            logcat { "Activity $it for params $params found" }

            Intent(context, it).apply {
                putExtra(ACTIVITY_SERIALIZABLE_PARAMETERS_ARG, params)
            }
        }
    }

    override fun start(
        context: Context,
        deeplinkActivityParams: DeeplinkActivityParams,
        options: Bundle?,
    ) {
        startIntent(context, deeplinkActivityParams)?.let {
            logcat { "Activity $it for params $deeplinkActivityParams found" }
            context.startActivity(it, options)
        } ?: throw IllegalArgumentException("Activity for params $deeplinkActivityParams not found")
    }

    override fun startIntent(
        context: Context,
        deeplinkActivityParams: DeeplinkActivityParams,
    ): Intent? {
        val activityParams: ActivityParams? = activityMappers.firstNotNullOfOrNull {
            it.map(deeplinkActivityParams)
        }

        return activityParams?.let {
            startIntent(context, it)
        }
    }
}

private const val ACTIVITY_SERIALIZABLE_PARAMETERS_ARG = "ACTIVITY_SERIALIZABLE_PARAMETERS_ARG"
