package ca.hojat.smart.musicplayer.shared.ui.compose.extensions

import android.content.Context
import ca.hojat.smart.musicplayer.shared.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)
