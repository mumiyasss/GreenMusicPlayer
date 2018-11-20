package com.grebnevstudio.musicplayer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grebnevstudio.musicplayer.PlayerServiceConnection

class PlayerViewModel : ViewModel() {
    var service = PlayerServiceConnection.get()
    val isPlaying = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        service.disconnectService()
    }

    fun onPlayPause() {
        isPlaying.value = service.playerBinder.isPlaying()
    }

    init {
        if (service.bounded.not())
            service.connectService()
        isPlaying.value = false
    }
}
