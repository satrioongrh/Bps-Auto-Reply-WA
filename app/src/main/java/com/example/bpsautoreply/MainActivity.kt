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

        buttonSimpan.setOnClickListener {
            val trigger = triggerEditText.text.toString()

            // Combine responses from all EditTexts, including the default response
            val allResponses = mutableListOf<String>()
            allResponses.add(responseEditText.text.toString()) // Default response from the first EditText

            for (i in 0 until editTextContainer.childCount) {
                if (editTextContainer.getChildAt(i) is EditText) {
                    val currentEditText = editTextContainer.getChildAt(i) as EditText
                    allResponses.add(currentEditText.text.toString())
                }
            }

            // Join all responses using the separator
            val combinedResponse = allResponses.joinToString("\n--NEW_RESPONSE--\n").trim()

            // Save the combined response to SharedPreferences
            val messages = sharePref.getAutoReplyMessages().toMutableList()
            messages.add(AutoReplyMessage(UUID.randomUUID().toString(), trigger, combinedResponse))
            sharePref.setAutoReplyMessages(messages)

            // Clear the EditText fields after saving
            triggerEditText.text.clear()
            responseEditText.text.clear()

            // Clear all additional EditTexts
            editTextContainer.removeAllViews()

            Toast.makeText(this, "Berhasil menambahkan pesan-pesan balasan", Toast.LENGTH_LONG).show()
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