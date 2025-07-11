

package com.duckduckgo.app.di

import com.duckduckgo.app.di.ActivityComponent.Factory
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.SingleInstanceIn
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.android.DaggerActivity
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@MergeSubcomponent(ActivityScope::class)
@SingleInstanceIn(ActivityScope::class)
interface ActivityComponent : AndroidInjector<DaggerActivity> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<DaggerActivity, ActivityComponent> {
        override fun create(@BindsInstance instance: DaggerActivity): ActivityComponent
    }

    @ContributesTo(AppScope::class)
    interface ParentComponent {
        fun activityComponentFactory(): Factory
    }
}

@Module
@ContributesTo(AppScope::class)
abstract class DaggerActivityModule {
    @Binds
    @IntoMap
    @ClassKey(DaggerActivity::class)
    abstract fun bindsDaggerActivityComponent(factory: Factory): AndroidInjector.Factory<*, *>
}
