package com.kisahcode.androidintermediate

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kisahcode.androidintermediate.adapter.ListHeroAdapter
import com.kisahcode.androidintermediate.model.Hero

/**
 * The main activity of the application responsible for displaying a list of heroes.
 *
 * This activity displays a list of heroes in a RecyclerView. The layout of the RecyclerView is
 * determined based on the device orientation.
 */
class MainActivity : AppCompatActivity() {

    // RecyclerView to display the list of heroes
    private lateinit var rvHeroes: RecyclerView
    // List to store the heroes
    private val list = ArrayList<Hero>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        rvHeroes = findViewById(R.id.rv_heroes)
        rvHeroes.setHasFixedSize(true)

        // Populate the list of heroes
        list.addAll(listHeroes)

        // Display the list of heroes in the RecyclerView
        showRecyclerList()
    }

    /**
     * Sets up the RecyclerView to display the list of heroes.
     *
     * The layout manager of the RecyclerView is determined based on the device orientation.
     */
    private fun showRecyclerList() {
        // Determine the device orientation
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If landscape orientation, use GridLayoutManager with 2 columns
            rvHeroes.layoutManager = GridLayoutManager(this, 2)
        } else {
            // If portrait orientation, use LinearLayoutManager
            rvHeroes.layoutManager = LinearLayoutManager(this)
        }

        // Create and set the adapter for the RecyclerView
        val listHeroAdapter = ListHeroAdapter(list)
        rvHeroes.adapter = listHeroAdapter
    }

    /**
     * Generates a list of hero objects from string arrays defined in resources.
     *
     * @return List of hero objects.
     */
    private val listHeroes: ArrayList<Hero>
        get() {
            // Retrieve hero data from string arrays defined in resources
            val dataName = resources.getStringArray(R.array.data_name)
            val dataDescription = resources.getStringArray(R.array.data_description)
            val dataPhoto = resources.getStringArray(R.array.data_photo)

            // Create and populate a list of hero objects
            val listHero = ArrayList<Hero>()
            for (i in dataName.indices) {
                val hero = Hero(dataName[i], dataDescription[i], dataPhoto[i])
                listHero.add(hero)
            }
            return listHero
        }
}