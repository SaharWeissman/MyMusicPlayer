package com.saharw.mymusicplayer.presentation.main.dagger

import android.support.v7.app.AppCompatActivity
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
import com.saharw.mymusicplayer.presentation.main.MainActivity
import com.saharw.mymusicplayer.presentation.main.mvp.MainModel
import com.saharw.mymusicplayer.presentation.main.mvp.MainPresenter
import com.saharw.mymusicplayer.presentation.main.mvp.MainView
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by saharw on 06/05/2018.
 */
@Component(modules = arrayOf(MainActivityComponent.MainActivityModule::class))
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    @Module
    class MainActivityModule(private val activity: AppCompatActivity, private val layoutId: Int){

        @Provides
        fun provideMainView(): IView {
            return MainView(activity, layoutId)
        }

        @Provides
        fun provideMainModel(): IModel {
            return MainModel()
        }

        @Provides
        fun provideMainPresenter(view : IView, model: IModel): IPresenter {
            return MainPresenter(view, model)
        }
    }
}