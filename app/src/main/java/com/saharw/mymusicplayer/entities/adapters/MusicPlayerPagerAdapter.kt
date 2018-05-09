package com.saharw.mymusicplayer.entities.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.saharw.mymusicplayer.presentation.base.BaseFragment

/**
 * Created by saharw on 08/05/2018.
 */
class MusicPlayerPagerAdapter(private val fragments: Array<Fragment>,
                              fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return (fragments[position] as BaseFragment).getTabTitle()
    }
}