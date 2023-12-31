package com.example.bpsautoreply.ui.listmessage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bpsautoreply.R
import com.example.bpsautoreply.utils.AutoReplyMessage

class AutoReplyMessageAdapter(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<AutoReplyMessageAdapter.ViewHolder>() {

    private val messages: MutableList<AutoReplyMessage> = mutableListOf()

    fun submitList(newList: List<AutoReplyMessage>) {
        messages.clear()
        messages.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_auto_reply_message, parent, false)
        return ViewHolder(view, itemClickListener, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ViewHolder(
        view: View,
        private val itemClickListener: OnItemClickListener,
        private val adapter: AutoReplyMessageAdapter
    ) : RecyclerView.ViewHolder(view) {
        private val triggerTextView: TextView = view.findViewById(R.id.triggerTextView)
        private val responseContainer: LinearLayout = view.findViewById(R.id.responseContainer)
        private val deleteButton: Button = view.findViewById(R.id.deleteButton)
        private val updateButton: Button = view.findViewById(R.id.updateButton)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val message = adapter.messages[position]
                    itemClickListener.onDeleteClick(message)
                }
            }
            updateButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val message = adapter.messages[position]
                    itemClickListener.onEditClick(message)
                }
            }
        }

        fun bind(message: AutoReplyMessage) {
            triggerTextView.text = "Trigger: " + message.trigger

            // Clear existing response views
            responseContainer.removeAllViews()

            // Split responses and add them as separate CardViews
            val responses = message.response.split("\n--NEW_RESPONSE--\n")
            val totalResponses = responses.size

            for ((index, response) in responses.withIndex()) {
                val responseCard = CardView(responseContainer.context)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                if (index < totalResponses - 1) {
                    layoutParams.setMargins(0, 0, 0, 16) // Set bottom margin for all cards except the last one
                }

                responseCard.layoutParams = layoutParams
                responseCard.radius = 8f
                responseCard.cardElevation = 4f
                responseCard.setContentPadding(16, 16, 16, 16)
                responseCard.setCardBackgroundColor(ContextCompat.getColor(responseCard.context, R.color.blue))

                val responseTextView = TextView(responseCard.context)
                responseTextView.setTextColor(Color.BLACK)
                responseTextView.text = response

                responseCard.addView(responseTextView)
                responseContainer.addView(responseCard)
            }
        }

    }

    interface OnItemClickListener {
        fun onEditClick(message: AutoReplyMessage)
        fun onDeleteClick(message: AutoReplyMessage)
    }
}

