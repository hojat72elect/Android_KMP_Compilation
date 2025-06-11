

package com.duckduckgo.remote.messaging.impl.di

import com.duckduckgo.di.DaggerSet
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.Content
import com.duckduckgo.remote.messaging.api.Content.BigSingleAction
import com.duckduckgo.remote.messaging.api.Content.BigTwoActions
import com.duckduckgo.remote.messaging.api.Content.Medium
import com.duckduckgo.remote.messaging.api.Content.MessageType
import com.duckduckgo.remote.messaging.api.Content.PromoSingleAction
import com.duckduckgo.remote.messaging.api.Content.Small
import com.duckduckgo.remote.messaging.api.MessageActionMapperPlugin
import com.duckduckgo.remote.messaging.api.RemoteMessage
import com.duckduckgo.remote.messaging.impl.mappers.ActionAdapter
import com.duckduckgo.remote.messaging.impl.mappers.MessageMapper
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
object RMFMapperModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesMessageMapper(
        messageAdapter: Lazy<JsonAdapter<RemoteMessage>>,
    ): MessageMapper {
        return MessageMapper(messageAdapter)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideMoshiAdapter(
        actionMappers: DaggerSet<MessageActionMapperPlugin>,
    ): JsonAdapter<RemoteMessage> {
        val moshi = Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Content::class.java, "messageType")
                    .withSubtype(Small::class.java, MessageType.SMALL.name)
                    .withSubtype(Medium::class.java, MessageType.MEDIUM.name)
                    .withSubtype(BigSingleAction::class.java, MessageType.BIG_SINGLE_ACTION.name)
                    .withSubtype(BigTwoActions::class.java, MessageType.BIG_TWO_ACTION.name)
                    .withSubtype(PromoSingleAction::class.java, MessageType.PROMO_SINGLE_ACTION.name),
            )
            .add(ActionAdapter(actionMappers))
            .add(KotlinJsonAdapterFactory())
            .build()
        return moshi.adapter(RemoteMessage::class.java)
    }
}
