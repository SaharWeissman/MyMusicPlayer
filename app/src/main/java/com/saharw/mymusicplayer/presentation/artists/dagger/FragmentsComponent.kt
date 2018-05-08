package com.saharw.mymusicplayer.presentation.artists.dagger

import android.app.Activity
import android.support.v4.app.Fragment
import com.saharw.mymusicplayer.presentation.artists.ArtistsFragment
import com.saharw.mymusicplayer.presentation.artists.mvp.ArtistsModel
import com.saharw.mymusicplayer.presentation.artists.mvp.ArtistsPresenter
import com.saharw.mymusicplayer.presentation.artists.mvp.ArtistsView
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by saharw on 08/05/2018.
 */
@Component(modules = arrayOf(FragmentsComponent.FragmentsModule::class))
interface FragmentsComponent {

    fun inject(fragment: ArtistsFragment)

    @Module
    class FragmentsModule(private val frag: Fragment, private val activity: Activity){

        @Provides
        fun provideMainView(): IView {
            var res : IView
            when(frag){
                is ArtistsFragment -> {
                    res = ArtistsView(frag)
                }
                else -> {

                    // dummy
                    res = object : IView {
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
                is ArtistsFragment -> {
                    res = ArtistsModel()
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
                is ArtistsFragment -> {
                    res = ArtistsPresenter(view, model)
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