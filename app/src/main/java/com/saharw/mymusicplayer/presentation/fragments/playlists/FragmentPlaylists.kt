package com.saharw.mymusicplayer.presentation.fragments.playlists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.fragments.base.BaseFragment
import com.saharw.mymusicplayer.presentation.fragments.base.dagger.DaggerTabbedFragmentsComponent
import com.saharw.mymusicplayer.presentation.fragments.base.dagger.TabbedFragmentModule
import javax.inject.Inject

/**
 * Created by saharw on 08/05/2018.
 */
class FragmentPlaylists : BaseFragment() {

    private val TAG = "FragmentPlaylists"
    private val mTabTitle = "Playlists"

    @Inject
    lateinit var mPresenter : IPresenter

    companion object {
        fun newInstance() : FragmentPlaylists {
            var instance = FragmentPlaylists()
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        DaggerTabbedFragmentsComponent.builder().tabbedFragmentModule(TabbedFragmentModule(this)).build().inject(this)
        mPresenter.onPresenterCreate()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater?.inflate(R.layout.fragment_artists, container,false)
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

    override fun getTabTitle(): CharSequence {
        Log.d(TAG, "getTabTitle")
        return mTabTitle
    }
}