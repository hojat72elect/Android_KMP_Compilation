package ca.hojat.smart.musicplayer.shared.data


interface HashListener {
    fun receivedHash(hash: String, type: Int)
}
