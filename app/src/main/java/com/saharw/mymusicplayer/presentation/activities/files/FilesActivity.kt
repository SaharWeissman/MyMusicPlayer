package com.saharw.mymusicplayer.presentation.activities.files

import android.util.Log
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.activities.files.dagger.DaggerFilesActivityComponent
import com.saharw.mymusicplayer.presentation.base.BaseActivity
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.fragments.base.dagger.FilesActivityModule
import javax.inject.Inject

/**
 * Created by saharw on 10/05/2018.
 */
class FilesActivity : BaseActivity() {

    private val TAG = "FilesActivity"

    companion object {
        const val BUNDLE_KEY_MEDIA_ITEMS = "mediaItems"
    }

    @Inject
    lateinit var mPresenter : IPresenter

    lateinit var mMediaItems : Collection<MediaItem>

    override fun initActivity() {
        Log.d(TAG, "initActivity")
        var mediaItems = emptyList<MediaItem>()
        if(intent.hasExtra(BUNDLE_KEY_MEDIA_ITEMS)){
            mediaItems = intent.getSerializableExtra(BUNDLE_KEY_MEDIA_ITEMS) as List<MediaItem>
        }else {
            Log.e(TAG, "initActivity: no items exist!")
        }
        DaggerFilesActivityComponent.builder().filesActivityModule(FilesActivityModule(this, R.layout.fragment_files, R.layout.file_item, mediaItems)).build().inject(this)
        mPresenter.onPresenterCreate()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        mPresenter.onPresenterResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
        mPresenter.onPresenterPause()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        mPresenter.onPresenterDestroy()
    }
}