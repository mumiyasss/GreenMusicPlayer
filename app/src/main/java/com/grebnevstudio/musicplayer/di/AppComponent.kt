package com.grebnevstudio.musicplayer.di

import android.app.Application
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import com.grebnevstudio.musicplayer.ui.main.playlist.SongViewHolder
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun injectServiceConnection(serviceConnectionObject: PlayerServiceConnection)
    fun injectPlayerViewModel(viewModel: PlayerViewModel)
    fun injectViewHolder(songViewHolder: SongViewHolder)
}