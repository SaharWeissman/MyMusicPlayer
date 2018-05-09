package com.saharw.mymusicplayer.presentation.base.dagger

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.saharw.mymusicplayer.presentation.albums.FragmentAlbums
import com.saharw.mymusicplayer.presentation.albums.mvp.AlbumsModel
import com.saharw.mymusicplayer.presentation.albums.mvp.AlbumsPresenter
import com.saharw.mymusicplayer.presentation.albums.mvp.AlbumsView
import com.saharw.mymusicplayer.presentation.artists.FragmentArtists
import com.saharw.mymusicplayer.presentation.artists.mvp.*
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
import com.saharw.mymusicplayer.presentation.playlists.FragmentPlaylists
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by saharw on 08/05/2018.
 */
@Component(modules = arrayOf(FragmentsComponent.FragmentsModule::class))
interface FragmentsComponent {

    fun inject(fragmentArtists: FragmentArtists)
    fun inject(fragmentAlbums: FragmentAlbums)
    fun inject(fragmentPlaylists: FragmentPlaylists)

    @Module
    class FragmentsModule(private val frag: Fragment, private val activity: Activity){

        @Provides
        fun provideMainView(): IView {
            var res : IView
            when(frag){
                is FragmentArtists -> {
                    res = ArtistsView(frag)
                }
                is FragmentAlbums -> {
                    res = AlbumsView(frag)
                }
                is FragmentPlaylists -> {
                    res = PlaylistsView(frag)
                }
                else -> {

                    // dummy
                    res = object : IView {
                        override fun getContext(): Context {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onViewCreate() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onViewResume() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onViewPause() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onViewDestroy() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    }
                }
            }
            return res
        }

        @Provides
        fun provideMainModel(): IModel {
            var res : IModel
            when(frag){
                is FragmentArtists -> {
                    res = ArtistsModel()
                }
                is FragmentAlbums -> {
                    res = AlbumsModel()
                }
                is FragmentPlaylists -> {
                    res = PlaylistsModel()
                }
                else -> {
                    res = object : IModel {
                        override fun onModelCreate() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onModelDestroy() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    }
                }
            }
            return res
        }

        @Provides
        fun provideMainPresenter(view : IView, model: IModel): IPresenter {
            var res : IPresenter
            when(frag){
                is FragmentArtists -> {
                    res = ArtistsPresenter(view, model)
                }
                is FragmentAlbums -> {
                    res = AlbumsPresenter(view, model)
                }
                is FragmentPlaylists -> {
                    res = PlaylistsPresenter(view, model)
                }
                else -> {
                    res = object : IPresenter {
                        override fun onPresenterCreate() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onPresenterResume() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onPresenterPause() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onPresenterDestroy() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    }
                }
            }
            return res
        }
    }
}