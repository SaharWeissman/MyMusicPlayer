package com.saharw.mymusicplayer.presentation.activities.files.dagger

import com.saharw.mymusicplayer.presentation.activities.files.FilesActivity
import com.saharw.mymusicplayer.presentation.fragments.base.dagger.FilesActivityModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by saharw on 10/05/2018.
 */
@Singleton
@Component(modules = arrayOf(FilesActivityModule::class))
interface FilesActivityComponent {

    fun inject(activityFiles: FilesActivity)
}