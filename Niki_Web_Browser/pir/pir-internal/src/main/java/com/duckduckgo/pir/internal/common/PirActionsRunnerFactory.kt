

package com.duckduckgo.pir.internal.common

import android.content.Context
import com.duckduckgo.common.utils.CurrentTimeProvider
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.js.messaging.api.JsMessageHelper
import com.duckduckgo.pir.internal.scripts.PirMessagingInterface
import com.duckduckgo.pir.internal.scripts.RealBrokerActionProcessor
import com.duckduckgo.pir.internal.store.PirRepository
import javax.inject.Inject

class PirActionsRunnerFactory @Inject constructor(
    private val pirDetachedWebViewProvider: PirDetachedWebViewProvider,
    private val dispatcherProvider: DispatcherProvider,
    private val jsMessageHelper: JsMessageHelper,
    private val currentTimeProvider: CurrentTimeProvider,
    private val pirRunStateHandler: PirRunStateHandler,
    private val pirRepository: PirRepository,
    private val captchaResolver: CaptchaResolver,
) {
    /**
     * Every instance of PirActionsRunner is created with its own instance of [PirMessagingInterface] and [RealBrokerActionProcessor]
     */
    fun createInstance(
        context: Context,
        pirScriptToLoad: String,
        runType: RunType,
    ): PirActionsRunner {
        return RealPirActionsRunner(
            dispatcherProvider,
            pirDetachedWebViewProvider,
            RealBrokerActionProcessor(
                PirMessagingInterface(
                    jsMessageHelper,
                ),
            ),
            context,
            pirScriptToLoad,
            runType,
            currentTimeProvider,
            RealNativeBrokerActionHandler(
                pirRepository,
                dispatcherProvider,
                captchaResolver,
            ),
            pirRunStateHandler,
        )
    }

    enum class RunType {
        MANUAL,
        SCHEDULED,
        OPTOUT,
    }
}
