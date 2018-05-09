package com.saharw.mymusicplayer.presentation.base

import android.support.v4.app.Fragment

/**
 * Created by saharw on 08/05/2018.
 */

const val DEFAULT_TAB_TITLE = "no title"
const val BUDNLE_KEY_TAB_TITLE = "tab_title"

abstract class BaseFragment : Fragment() {
    abstract fun getTabTitle() : CharSequence
}