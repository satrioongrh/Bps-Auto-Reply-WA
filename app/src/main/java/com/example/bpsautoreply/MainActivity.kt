package com.example.bpsautoreply

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
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

        buttonSimpan.setOnClickListener {
            val trigger = triggerEditText.text.toString()
            val response = responseEditText.text.toString()

            val messages = sharePref.getAutoReplyMessages().toMutableList()
            messages.add(AutoReplyMessage(UUID.randomUUID().toString(), trigger, response))

            sharePref.setAutoReplyMessages(messages)

            // Clear the EditText fields after saving
            triggerEditText.text.clear()
            responseEditText.text.clear()

            Toast.makeText(this, "berhasil menambahkan pesan balasan", Toast.LENGTH_LONG).show()
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