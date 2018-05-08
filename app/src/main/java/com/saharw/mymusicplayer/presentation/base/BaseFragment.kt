package com.saharw.mymusicplayer.presentation.base

import android.support.v4.app.Fragment

/**
 * Created by saharw on 08/05/2018.
 */
abstract class BaseFragment : Fragment() {

    abstract fun getTabTitle() : CharSequence
}