package com.saharw.mymusicplayer.presentation.activities.main.dagger

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.saharw.mymusicplayer.presentation.activities.main.MainActivity
import com.saharw.mymusicplayer.presentation.activities.main.mvp.MainModel
import com.saharw.mymusicplayer.presentation.activities.main.mvp.MainPresenter
import com.saharw.mymusicplayer.presentation.activities.main.mvp.MainView
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
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
    class MainActivityModule(private val activity: AppCompatActivity, private val layoutId: Int, private val tabsFragArray : Array<Fragment>){

        @Provides
        fun provideMainView(): IView {
            return MainView(activity, layoutId, tabsFragArray)
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