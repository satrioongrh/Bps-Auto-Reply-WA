package com.example.bpsautoreply.ui.detailmessage

//class DetailMessageActivity : AppCompatActivity() {
//    private lateinit var editTriggerEditText: EditText
//    private lateinit var editResponseEditText: EditText
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_message)
//
//        editTriggerEditText = findViewById(R.id.editTriggerEditText)
//        editResponseEditText = findViewById(R.id.editResponseEditText)
//        val saveButton = findViewById<Button>(R.id.saveButton)
//
//        // Mendapatkan data pesan yang dikirim dari ListMessageActivity
//        val messageId = intent.getStringExtra("messageId")
//        val message = getMessageById(messageId!!) // Implementasi ini sesuai kebutuhan
//
//        // Menampilkan pesan trigger dan balasan dalam EditText
//        editTriggerEditText.setText(message.trigger)
//        editResponseEditText.setText(message.response)
//
//        // Menyimpan perubahan pesan trigger dan balasan
//        saveButton.setOnClickListener {
//            message.trigger = editTriggerEditText.text.toString()
//            message.response = editResponseEditText.text.toString()
//
//            // Menyimpan pesan yang diperbarui ke SharedPreferences
//            saveMessage(message)
//
//            // Mengirim data pesan yang diperbarui kembali ke ListMessageActivity
//            val resultIntent = Intent()
//            resultIntent.putExtra("updatedMessage", message)
//            setResult(RESULT_OK, resultIntent)
//            finish()
//
//            Toast.makeText(this, "Item Berhasil Di Update", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun getMessageById(messageId: String): AutoReplyMessage {
//        // Implementasikan logika untuk mendapatkan pesan berdasarkan ID
//        // Dalam contoh ini, kita asumsikan pesan tersimpan di SharedPreferences
//        val sharedPrefs = SharePreferences(this)
//        val messages = sharedPrefs.getAutoReplyMessages()
//        return messages.firstOrNull { it.id == messageId } ?: AutoReplyMessage("", "", "")
//    }
//
//    private fun saveMessage(message: AutoReplyMessage) {
//        // Implementasikan logika untuk menyimpan pesan
//        val sharedPrefs = SharePreferences(this)
//        val messages = sharedPrefs.getAutoReplyMessages().toMutableList()
//        val existingMessage = messages.firstOrNull { it.id == message.id }
//        if (existingMessage != null) {
//            // Update pesan yang sudah ada
//            existingMessage.trigger = message.trigger
//            existingMessage.response = message.response
//        } else {
//            // Tambahkan pesan baru jika belum ada
//            messages.add(message)
//        }
//        sharedPrefs.setAutoReplyMessages(messages)
//    }
//}

//class DetailMessageActivity : AppCompatActivity() {
//    private lateinit var editTriggerEditText: EditText
//    private lateinit var saveButton: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_message)
//
//        editTriggerEditText = findViewById(R.id.editTriggerEditText)
//        saveButton = findViewById(R.id.saveButton)
//
//        // Mendapatkan data pesan yang dikirim dari ListMessageActivity
//        val messageId = intent.getStringExtra("messageId")
//        val message = getMessageById(messageId!!) // Implementasi ini sesuai kebutuhan
//
//        // Menampilkan pesan trigger dalam EditText
//        editTriggerEditText.setText(message.trigger)
//
//        // Menampilkan pesan balasan dalam EditText secara dinamis
//        val responses = message.response.split("\n")
//        val responseContainer = findViewById<LinearLayout>(R.id.lineare)
//
//        for (response in responses) {
//            val responseEditText = EditText(this)
//            responseEditText.layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            responseEditText.hint = "Edit Response"
//            responseEditText.setText(response)
//            responseContainer.addView(responseEditText)
//        }
//
//        // Menyimpan perubahan pesan trigger dan balasan
//        saveButton.setOnClickListener {
//            message.trigger = editTriggerEditText.text.toString()
//
//            // Mengambil pesan balasan dari EditText
//            val updatedResponses = mutableListOf<String>()
//            for (i in 0 until responseContainer.childCount) {
//                if (responseContainer.getChildAt(i) is EditText) {
//                    val responseEditText = responseContainer.getChildAt(i) as EditText
//                    updatedResponses.add(responseEditText.text.toString())
//                }
//            }
//            message.response = updatedResponses.joinToString("\n")
//
//            // Menyimpan pesan yang diperbarui ke SharedPreferences
//            saveMessage(message)
//
//            // Mengirim data pesan yang diperbarui kembali ke ListMessageActivity
//            val resultIntent = Intent()
//            resultIntent.putExtra("updatedMessage", message)
//            setResult(RESULT_OK, resultIntent)
//            finish()
//
//            Toast.makeText(this, "Item Berhasil Di Update", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun getMessageById(messageId: String): AutoReplyMessage {
//        // Implementasikan logika untuk mendapatkan pesan berdasarkan ID
//        // Dalam contoh ini, kita asumsikan pesan tersimpan di SharedPreferences
//        val sharedPrefs = SharePreferences(this)
//        val messages = sharedPrefs.getAutoReplyMessages()
//        return messages.firstOrNull { it.id == messageId } ?: AutoReplyMessage("", "", "")
//    }
//
//    private fun saveMessage(message: AutoReplyMessage) {
//        // Implementasikan logika untuk menyimpan pesan
//        val sharedPrefs = SharePreferences(this)
//        val messages = sharedPrefs.getAutoReplyMessages().toMutableList()
//        val existingMessage = messages.firstOrNull { it.id == message.id }
//        if (existingMessage != null) {
//            // Update pesan yang sudah ada
//            existingMessage.trigger = message.trigger
//            existingMessage.response = message.response
//        } else {
//            // Tambahkan pesan baru jika belum ada
//            messages.add(message)
//        }
//        sharedPrefs.setAutoReplyMessages(messages)
//    }
//}

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bpsautoreply.R
import com.example.bpsautoreply.preferences.SharePreferences
import com.example.bpsautoreply.utils.AutoReplyMessage

class DetailMessageActivity : AppCompatActivity() {
    private lateinit var editTriggerEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_message)

        editTriggerEditText = findViewById(R.id.editTriggerEditText)
        saveButton = findViewById(R.id.saveButton)
        val tambahET = findViewById<TextView>(R.id.tambahETD)
        val kurangiET = findViewById<TextView>(R.id.resetD)

        // Mendapatkan data pesan yang dikirim dari ListMessageActivity
        val messageId = intent.getStringExtra("messageId")
        val message = getMessageById(messageId!!)

        // Menampilkan pesan trigger dalam EditText
        editTriggerEditText.setText(message.trigger)

        // Menampilkan pesan balasan yang ada
        val responses = message.response.split("\n")
        val responseContainer = findViewById<LinearLayout>(R.id.lineare)

        for (response in responses) {
            addResponseEditText(response)
        }

        tambahET.setOnClickListener {
            addResponseEditText("") // Tambahkan EditText kosong
        }

        kurangiET.setOnClickListener {
            if (responseContainer.childCount > 0) {
                responseContainer.removeViewAt(responseContainer.childCount - 1)
            }
        }

        // Menyimpan perubahan pesan trigger dan balasan
        saveButton.setOnClickListener {
            message.trigger = editTriggerEditText.text.toString()

            // Mengambil pesan dari setiap EditText, termasuk yang baru ditambahkan
            val updatedResponses = mutableListOf<String>()
            for (i in 0 until responseContainer.childCount) {
                if (responseContainer.getChildAt(i) is EditText) {
                    val responseEditText = responseContainer.getChildAt(i) as EditText
                    updatedResponses.add(responseEditText.text.toString())
                }
            }
            message.response = updatedResponses.joinToString("\n")

            // Simpan pesan yang diperbarui ke SharedPreferences
            saveMessage(message)

            // Kirim data pesan yang diperbarui kembali ke ListMessageActivity
            val resultIntent = Intent()
            resultIntent.putExtra("updatedMessage", message)
            setResult(RESULT_OK, resultIntent)
            finish()

            Toast.makeText(this, "Item Berhasil Di Update", Toast.LENGTH_LONG).show()
        }
    }

    private fun addResponseEditText(initialText: String) {
        val responseContainer = findViewById<LinearLayout>(R.id.lineare)
        val responseEditText = EditText(this)
        responseEditText.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        responseEditText.hint = "Edit Response"
        responseEditText.setText(initialText)
        responseContainer.addView(responseEditText)
    }

    private fun getMessageById(messageId: String): AutoReplyMessage {
        // Implementasikan logika untuk mendapatkan pesan berdasarkan ID
        // Dalam contoh ini, kita asumsikan pesan tersimpan di SharedPreferences
        val sharedPrefs = SharePreferences(this)
        val messages = sharedPrefs.getAutoReplyMessages()
        return messages.firstOrNull { it.id == messageId } ?: AutoReplyMessage("", "", "")
    }

    private fun saveMessage(message: AutoReplyMessage) {
        // Implementasikan logika untuk menyimpan pesan
        val sharedPrefs = SharePreferences(this)
        val messages = sharedPrefs.getAutoReplyMessages().toMutableList()
        val existingMessage = messages.firstOrNull { it.id == message.id }
        if (existingMessage != null) {
            // Update pesan yang sudah ada
            existingMessage.trigger = message.trigger
            existingMessage.response = message.response
        } else {
            // Tambahkan pesan baru jika belum ada
            messages.add(message)
        }
        sharedPrefs.setAutoReplyMessages(messages)
    }
}
