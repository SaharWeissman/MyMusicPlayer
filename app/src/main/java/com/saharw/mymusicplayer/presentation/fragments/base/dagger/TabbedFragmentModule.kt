package com.saharw.mymusicplayer.presentation.fragments.base.dagger

import android.content.Context
import android.support.v4.app.Fragment
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
import com.saharw.mymusicplayer.presentation.fragments.albums.FragmentAlbums
import com.saharw.mymusicplayer.presentation.fragments.albums.mvp.AlbumsModel
import com.saharw.mymusicplayer.presentation.fragments.albums.mvp.AlbumsPresenter
import com.saharw.mymusicplayer.presentation.fragments.albums.mvp.AlbumsView
import com.saharw.mymusicplayer.presentation.fragments.artists.FragmentArtists
import com.saharw.mymusicplayer.presentation.fragments.artists.mvp.*
import com.saharw.mymusicplayer.presentation.fragments.playlists.FragmentPlaylists
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TabbedFragmentModule(private val frag: Fragment){

    @Provides
    @Singleton
    fun provideView(): IView{
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
    @Singleton
    fun provideModel(): IModel {
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
    @Singleton
    fun providePresenter(view : IView, model : IModel): IPresenter {
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