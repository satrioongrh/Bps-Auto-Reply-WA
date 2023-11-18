package com.example.bpsautoreply.ui.listmessage

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bpsautoreply.R
import com.example.bpsautoreply.preferences.SharePreferences
import com.example.bpsautoreply.ui.detailmessage.DetailMessageActivity
import com.example.bpsautoreply.utils.AutoReplyMessage

class ListMessageActivity : AppCompatActivity(), AutoReplyMessageAdapter.OnItemClickListener {
    private val EDIT_MESSAGE_REQUEST = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AutoReplyMessageAdapter // Buat adapter kustom untuk menampilkan pesan trigger dan balasan
    private lateinit var searchView: SearchView
    private var filteredMessages: List<AutoReplyMessage> = mutableListOf()
    private var messages: List<AutoReplyMessage> = mutableListOf()
    private var currentQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        adapter = AutoReplyMessageAdapter(this) // Inisialisasi adapter Anda
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ambil pesan trigger dan balasan dari SharedPreferences
        val sharedPrefs = SharePreferences(this)
        messages = sharedPrefs.getAutoReplyMessages()

        filteredMessages = messages

        // Tampilkan pesan trigger dan balasan dalam RecyclerView
        adapter.submitList(filteredMessages)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return onQueryTextChanges(newText)
            }
        })
    }

    private fun onQueryTextChanges(newText: String?): Boolean {
        currentQuery = newText.orEmpty()
        filteredMessages = if (currentQuery.isNotBlank()) {
            messages.filter { it.trigger.contains(currentQuery, ignoreCase = true) }
        } else {
            messages
        }
        adapter.submitList(filteredMessages)
        return true
    }

    override fun onResume() {
        super.onResume()

        // Ambil pesan trigger dan balasan dari SharedPreferences
        val sharedPrefs = SharePreferences(this)
        messages = sharedPrefs.getAutoReplyMessages()

        filteredMessages = if (currentQuery.isNotBlank()) {
            messages.filter { it.trigger.contains(currentQuery, ignoreCase = true) }
        } else {
            messages
        }
        // Perbarui data dalam RecyclerView
        adapter.submitList(filteredMessages)
        searchView.setQuery(currentQuery, false)
        searchView.clearFocus()
    }

    override fun onEditClick(message: AutoReplyMessage) {
        val intent = Intent(this, DetailMessageActivity::class.java)
        intent.putExtra("messageId", message.id)
        startActivityForResult(intent, EDIT_MESSAGE_REQUEST)
    }


    override fun onDeleteClick(message: AutoReplyMessage) {
        val sharedPrefs = SharePreferences(this)
        val messages = sharedPrefs.getAutoReplyMessages().toMutableList()
        messages.removeAll { it.id == message.id }
        sharedPrefs.setAutoReplyMessages(messages)

        // Update the messages and filteredMessages lists
        this.messages = sharedPrefs.getAutoReplyMessages()
        filteredMessages = if (currentQuery.isNotBlank()) {
            messages.filter { it.trigger.contains(currentQuery, ignoreCase = true) }
        } else {
            messages
        }

        adapter.submitList(messages)

        Toast.makeText(this, "item telah terhapus", Toast.LENGTH_LONG).show()
    }
}