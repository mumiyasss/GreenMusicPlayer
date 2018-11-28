package com.grebnevstudio.musicplayer.di

import android.app.Application
import com.grebnevstudio.musicplayer.service.PlayerService
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import com.grebnevstudio.musicplayer.ui.main.MainFragment
import com.grebnevstudio.musicplayer.ui.main.SongViewHolder
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

    fun injectMainFragment(mainFragment: MainFragment)
    fun injectServiceConnection(serviceConnectionObject: PlayerServiceConnection)
    fun injectService(playerService: PlayerService)
    fun injectPlayerViewModel(viewModel: PlayerViewModel)
    fun injectViewHolder(songViewHolder: SongViewHolder)
}