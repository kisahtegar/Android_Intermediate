package com.kisahcode.androidintermediate

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kisahcode.androidintermediate.model.Hero

/**
 * Activity class responsible for displaying details of a hero.
 * This activity displays information about a selected hero, including their name, description, and photo.
 */
class HeroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero)

        // Set up data to display information about the hero
        setupData()
    }

    /**
     * Sets up the data to display information about the selected hero.
     *
     * This method retrieves the Hero object passed from the previous activity and populates the
     * views with the hero's data.
     */
    private fun setupData() {
        // Retrieve the Hero object passed from the previous activity
        @Suppress("DEPRECATION")
        val hero = intent.getParcelableExtra<Hero>("Hero") as Hero

        // Load and display the hero's photo using Glide library
        Glide.with(applicationContext)
            .load(hero.photo)
            .circleCrop()
            .into(findViewById(R.id.profileImageView))

        // Set the hero's name and description to the corresponding TextViews
        findViewById<TextView>(R.id.nameTextView).text = hero.name
        findViewById<TextView>(R.id.descTextView).text = hero.description
    }
}