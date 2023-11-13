package com.example.bpsautoreply.ui.listmessage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bpsautoreply.R
import com.example.bpsautoreply.utils.AutoReplyMessage

class AutoReplyMessageAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<AutoReplyMessageAdapter.ViewHolder>() {

    private val messages: MutableList<AutoReplyMessage> = mutableListOf()

    fun submitList(newList: List<AutoReplyMessage>) {
        messages.clear()
        messages.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_auto_reply_message, parent, false)
        return ViewHolder(view, itemClickListener, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ViewHolder(view: View, private val itemClickListener: OnItemClickListener, private val adapter: AutoReplyMessageAdapter) : RecyclerView.ViewHolder(view) {
        private val triggerTextView: TextView = view.findViewById(R.id.triggerTextView)
        private val responseTextView: TextView = view.findViewById(R.id.responseTextView)
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
            triggerTextView.text = message.trigger
            responseTextView.text = message.response
        }
    }
    interface OnItemClickListener {
        fun onEditClick(message: AutoReplyMessage)
        fun onDeleteClick(message: AutoReplyMessage)
    }
}
