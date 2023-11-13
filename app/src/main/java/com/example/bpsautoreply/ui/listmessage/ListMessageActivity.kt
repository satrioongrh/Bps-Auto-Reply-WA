package com.example.bpsautoreply.ui.listmessage

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = AutoReplyMessageAdapter(this) // Inisialisasi adapter Anda
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ambil pesan trigger dan balasan dari SharedPreferences
        val sharedPrefs = SharePreferences(this)
        val messages = sharedPrefs.getAutoReplyMessages()

        // Tampilkan pesan trigger dan balasan dalam RecyclerView
        adapter.submitList(messages)
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
        adapter.submitList(messages)

        Toast.makeText(this, "item telah terhapus", Toast.LENGTH_LONG).show()
    }
}