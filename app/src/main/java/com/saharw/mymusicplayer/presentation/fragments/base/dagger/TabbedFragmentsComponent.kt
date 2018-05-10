package com.saharw.mymusicplayer.presentation.fragments.base.dagger

import com.saharw.mymusicplayer.presentation.fragments.albums.FragmentAlbums
import com.saharw.mymusicplayer.presentation.fragments.artists.FragmentArtists
import com.saharw.mymusicplayer.presentation.fragments.playlists.FragmentPlaylists
import dagger.Component
import javax.inject.Singleton

/**
 * Created by saharw on 08/05/2018.
 */

@Singleton
@Component(modules = arrayOf(TabbedFragmentModule::class))
interface TabbedFragmentsComponent {

    fun inject(fragmentArtists: FragmentArtists)
    fun inject(fragmentAlbums: FragmentAlbums)
    fun inject(fragmentPlaylists: FragmentPlaylists)
}