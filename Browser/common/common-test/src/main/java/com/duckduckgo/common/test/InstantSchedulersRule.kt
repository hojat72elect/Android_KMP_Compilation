

package com.duckduckgo.common.test

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class InstantSchedulersRule : TestRule {

    override fun apply(
        base: Statement,
        description: Description,
    ): Statement {
        return object : Statement() {

            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.reset()
                RxAndroidPlugins.reset()

                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setSingleSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }

                base.evaluate()

                RxJavaPlugins.reset()
                RxAndroidPlugins.reset()
            }
        }
    }
}
