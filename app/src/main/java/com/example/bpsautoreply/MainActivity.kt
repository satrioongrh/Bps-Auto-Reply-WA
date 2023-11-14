package com.example.bpsautoreply

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.example.bpsautoreply.preferences.SharePreferences
import com.example.bpsautoreply.ui.listmessage.ListMessageActivity
import com.example.bpsautoreply.utils.AutoReplyMessage
import com.google.gson.Gson
import timber.log.Timber
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var sharePref: SharePreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharePref = SharePreferences(this)

        Timber.plant(Timber.DebugTree())

        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        val triggerEditText = findViewById<EditText>(R.id.triggerEditText)
        val responseEditText = findViewById<EditText>(R.id.responseEditText)
        val buttonSimpan = findViewById<Button>(R.id.button_save)
        val buttonMoveListActivity = findViewById<Button>(R.id.button_s)
        val tambahTextView = findViewById<TextView>(R.id.tambahET)
        val editTextContainer = findViewById<LinearLayout>(R.id.edit_text_count)
        val resetTextView = findViewById<TextView>(R.id.reset)

        tambahTextView.setOnClickListener {
            val newEditText = EditText(this)
            newEditText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            newEditText.hint = "Pesan Balasan Otomatis"
            editTextContainer.addView(newEditText)
        }

        resetTextView.setOnClickListener {
            // Hapus semua EditText dari editTextContainer
            editTextContainer.removeAllViews()
        }

//        buttonSimpan.setOnClickListener {
//            val trigger = triggerEditText.text.toString()
//            val response = responseEditText.text.toString()
//
//            val messages = sharePref.getAutoReplyMessages().toMutableList()
//            messages.add(AutoReplyMessage(UUID.randomUUID().toString(), trigger, response))
//
//            sharePref.setAutoReplyMessages(messages)
//
//            // Clear the EditText fields after saving
//            triggerEditText.text.clear()
//            responseEditText.text.clear()
//
//            Toast.makeText(this, "berhasil menambahkan pesan balasan", Toast.LENGTH_LONG).show()
//        }

        buttonSimpan.setOnClickListener {
            val trigger = triggerEditText.text.toString()
            val defaultResponse = responseEditText.text.toString()

            val responseEditTexts = mutableListOf<EditText>()

            // Iterate through all EditTexts in editTextContainer and get their text
            for (i in 0 until editTextContainer.childCount) {
                if (editTextContainer.getChildAt(i) is EditText) {
                    responseEditTexts.add(editTextContainer.getChildAt(i) as EditText)
                }
            }

            val responses = responseEditTexts.map { it.text.toString() }

            if (trigger.isNotEmpty()) {
                val messages = sharePref.getAutoReplyMessages().toMutableList()

                val combinedResponse = (defaultResponse + "\n" + responses.joinToString("\n")).trim()

//                val combinedResponse = responses.joinToString("\n") // Menggabungkan pesan-pesan dengan baris baru sebagai pemisah

                messages.add(AutoReplyMessage(UUID.randomUUID().toString(), trigger, combinedResponse))

                sharePref.setAutoReplyMessages(messages)

                // Clear the EditText fields after saving
                triggerEditText.text.clear()
                responseEditText.text.clear()

                for (responseEditText in responseEditTexts) {
                    responseEditText.text.clear()
                }

                Toast.makeText(this, "Berhasil menambahkan pesan-pesan balasan", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Isi trigger dan pesan balasan yang diperlukan", Toast.LENGTH_LONG).show()
            }
        }



        buttonMoveListActivity.setOnClickListener {
            startActivity(Intent(this, ListMessageActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()

        val enabled = NotificationManagerCompat.getEnabledListenerPackages(this).contains(
            "com.example.bpsautoreply"  //BuildConfig.APPLICATION_ID
        )
        findViewById<TextView>(R.id.textView).text =
            if (enabled) "Listener enabled" else "Click to enable listener"
    }
}