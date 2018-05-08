package com.saharw.mymusicplayer.presentation.artists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.presentation.artists.dagger.DaggerFragmentsComponent
import com.saharw.mymusicplayer.presentation.artists.dagger.FragmentsComponent
import com.saharw.mymusicplayer.presentation.base.BaseFragment
import com.saharw.mymusicplayer.presentation.base.IPresenter
import javax.inject.Inject

/**
 * Created by saharw on 08/05/2018.
 */
class ArtistsFragment : BaseFragment() {

    private val TAG = "ArtistsFragment"
    private val TAB_TITLE = "Artists"

    @Inject
    lateinit var mPresenter : IPresenter

    companion object {
        fun newInstance(title: String) : ArtistsFragment {
            var fragment = ArtistsFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerFragmentsComponent.builder().fragmentsModule(FragmentsComponent.FragmentsModule(this, activity)).build().inject(this)
        mPresenter.onPresenterCreate()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        var view = inflater?.inflate(R.layout.fragment_artists, container,false)
        return view
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
        return TAB_TITLE
    }
}