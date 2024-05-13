package com.kisahcode.androidintermediate

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.kisahcode.androidintermediate.databinding.ItemMessageBinding

/**
 * Adapter for binding Message data to RecyclerView.
 *
 * This class extends FirebaseRecyclerAdapter to bind Message data to RecyclerView. It inflates
 * item_message layout for each message item and binds data to the corresponding views.
 *
 * @param options FirebaseRecyclerOptions<Message> Options to configure the adapter.
 * @param currentUserName The name of the currently logged-in user.
 */
class FirebaseMessageAdapter(
    options: FirebaseRecyclerOptions<Message>,
    private val currentUserName: String?
) : FirebaseRecyclerAdapter<Message, FirebaseMessageAdapter.MessageViewHolder>(options) {

    /**
     * Creates a ViewHolder for a message item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_message, parent, false)
        val binding = ItemMessageBinding.bind(view)

        return MessageViewHolder(binding)
    }

    /**
     * Binds the message data to the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item within the adapter's data set.
     * @param model The Message object to bind.
     */
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
        holder.bind(model)
    }

    /**
     * ViewHolder for a message item.
     *
     * @param binding The binding for the item_message layout.
     */
    inner class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the Message object to the views in the ViewHolder.
         *
         * @param item The Message object to bind.
         */
        fun bind(item: Message) {
            binding.tvMessage.text = item.text
            setTextColor(item.name, binding.tvMessage)
            binding.tvMessenger.text = item.name
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .circleCrop()
                .into(binding.ivMessenger)

            if (item.timestamp != null) {
                binding.tvTimestamp.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
            }
        }

        /**
         * Sets the background color of the message TextView based on the sender's username.
         *
         * @param userName The username of the message sender.
         * @param textView The TextView to set the background color for.
         */
        private fun setTextColor(userName: String?, textView: TextView) {
            if (currentUserName == userName && userName != null) {
                textView.setBackgroundResource(R.drawable.rounded_message_blue)
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_yellow)
            }
        }
    }

}