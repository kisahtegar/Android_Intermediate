package com.kisahcode.androidintermediate.ui.list

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * HomeActivity serves as the entry point to the application, displaying the main screen with tabs
 * for different sections. It sets up the ViewPager and TabLayout to enable navigation between the
 * NewsFragment and BookmarkFragment.
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SectionsPagerAdapter to manage fragments within ViewPager
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        // Attach TabLayout to ViewPager using TabLayoutMediator
        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            // Set tab titles based on resource string array
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        // Remove elevation from ActionBar to match the design
        supportActionBar?.elevation = 0f
    }

    companion object {
        // Array of resource IDs for tab titles
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.home, R.string.bookmark)
    }
}