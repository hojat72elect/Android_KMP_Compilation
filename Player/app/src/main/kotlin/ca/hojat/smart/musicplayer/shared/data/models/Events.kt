package ca.hojat.smart.musicplayer.shared.data.models

class Events {
    class SleepTimerChanged(val seconds: Int)
    class PlaylistsUpdated
    class RefreshFragments
    class RefreshTracks
}
