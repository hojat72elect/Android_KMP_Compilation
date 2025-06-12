package ca.hojat.smart.musicplayer.shared.data.models

import androidx.compose.runtime.Immutable

@Immutable
data class RadioItem(val id: Int, val title: String, val value: Any = id)
