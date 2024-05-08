package com.kisahcode.androidintermediate.ui.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter for managing the fragments within the ViewPager2 in the HomeActivity.
 *
 * This adapter is responsible for creating and managing fragments for different sections
 * (news and bookmarks) within the ViewPager2.
 *
 * @param activity The parent activity of the adapter.
 */
class SectionsPagerAdapter internal constructor(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    /**
     * Creates a new fragment for the specified position.
     *
     * @param position The position of the fragment.
     * @return Returns the created fragment.
     */
    override fun createFragment(position: Int): Fragment {
        val fragment = NewsFragment()
        val bundle = Bundle()
        if (position == 0) {
            bundle.putString(NewsFragment.ARG_TAB, NewsFragment.TAB_NEWS)
        } else {
            bundle.putString(NewsFragment.ARG_TAB, NewsFragment.TAB_BOOKMARK)
        }
        fragment.arguments = bundle
        return fragment
    }

    /**
     * Returns the total number of fragments managed by the adapter.
     *
     * @return Returns the count of fragments.
     */
    override fun getItemCount(): Int {
        return 2
    }
}