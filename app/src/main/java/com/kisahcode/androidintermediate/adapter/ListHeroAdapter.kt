package com.kisahcode.androidintermediate.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kisahcode.androidintermediate.HeroActivity
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.model.Hero
import androidx.core.util.Pair

/**
 * RecyclerView adapter for displaying a list of heroes.
 *
 * @property listHero List of Hero objects to be displayed.
 */
class ListHeroAdapter(private val listHero: ArrayList<Hero>) : RecyclerView.Adapter<ListHeroAdapter.ListViewHolder>() {

    /**
     * Inflates the item layout and creates a new ViewHolder instance.
     * This method is called when RecyclerView needs a new ViewHolder instance.
     *
     * @param parent The ViewGroup into which the new View will be added after it's bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ListViewHolder that holds the inflated View.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)
        return ListViewHolder(view)
    }

    /**
     * Binds data to the views within the ViewHolder at the specified position.
     * This method is called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        // Retrieve the Hero object at the specified position in the list
        holder.bind(listHero[position])
    }

    /**
     * Returns the total number of items in the list.
     */
    override fun getItemCount(): Int = listHero.size

    /**
     * ViewHolder class to hold references to the views for each item in the RecyclerView.
     * This class represents an item view in the RecyclerView.
     *
     * @param itemView The root View of the item layout.
     */
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // References to the views within the item layout
        private var imgPhoto: ImageView = itemView.findViewById(R.id.profileImageView)
        private var tvName: TextView = itemView.findViewById(R.id.nameTextView)
        private var tvDescription: TextView = itemView.findViewById(R.id.descTextView)

        /**
         * Binds data from a Hero object to the views within the ViewHolder.
         * This method sets the appropriate data to be displayed in the item view.
         *
         * @param hero The Hero object containing data to be displayed.
         */
        fun bind(hero: Hero) {
            // Load and display the hero's photo using Glide library
            Glide.with(itemView.context)
                .load(hero.photo)
                .circleCrop()
                .into(imgPhoto)

            // Set the hero's name and description to the corresponding TextViews
            tvName.text = hero.name
            tvDescription.text = hero.description

            // Set an OnClickListener to handle item clicks
            itemView.setOnClickListener {
                // Create an intent to start the HeroActivity and pass the Hero object as an extra
                val intent = Intent(itemView.context, HeroActivity::class.java)
                intent.putExtra("Hero", hero)

                // Create ActivityOptionsCompat for shared element transition animation
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "profile"), // Shared element for hero's photo
                        Pair(tvName, "name"),      // Shared element for hero's name
                        Pair(tvDescription, "description"), // Shared element for hero's description
                    )

                // Start the activity with the intent and shared element transition animation
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

}