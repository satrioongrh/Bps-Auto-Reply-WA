package com.example.bpsautoreply.notification

import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import com.example.bpsautoreply.preferences.SharePreferences
import com.example.bpsautoreply.utils.Action
import com.example.bpsautoreply.utils.NotificationUtils
import timber.log.Timber

class NotificationService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()

        Timber.w("onListenerConnected")
    }


    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val notification = sbn.notification
        val ticker = notification?.tickerText
        val bundle: Bundle? = notification?.extras
        val title: String = when (val titleObj = bundle?.get("android.title")) {
            is String -> titleObj
            is SpannableString -> titleObj.toString()
            else -> null.toString()
        }
        val body: String = bundle?.getCharSequence("android.text").toString()

        val appInfo = applicationContext.packageManager.getApplicationInfo(
            sbn.packageName,
            PackageManager.GET_META_DATA
        )
        val appName = applicationContext.packageManager.getApplicationLabel(appInfo)

        Timber.w("onNotificationPosted {key=${sbn.key},app=${appName},id=${sbn.id},ticker=$ticker,title=$title,body=$body,posted=${sbn.postTime},package=${sbn.packageName}}")

        if (sbn.packageName == "com.whatsapp" || sbn.packageName == "com.whatsapp.w4b") {
            val sharedPrefs = SharePreferences(applicationContext)
            val messages = sharedPrefs.getAutoReplyMessages()

            for (message in messages.sortedByDescending { it.trigger.length }) {
                val trigger = message.trigger.lowercase() // Mengonversi trigger menjadi huruf kecil
                val bodyLower = body.lowercase() // Mengonversi pesan yang diterima menjadi huruf kecil
                val responses = message.response.split("--NEW_RESPONSE--")

                // Pengecekan trigger yang lebih teliti
                if (bodyLower.startsWith(trigger) && bodyLower.length == trigger.length) {
                    // Kirim setiap pesan secara terpisah
                    for (response in responses) {
                        if (response.isNotEmpty()) {
                            reply(sbn, response.trim())
                        }
                    }
                    break
                }
            }
        }

        if (sbn.packageName == "com.twitter.android") {
            clickButton(sbn, "retweet")
            clickButton(sbn, "like")
        }

    }

    private fun clickButton(sbn: StatusBarNotification, button: String) {
        val click: Int? = NotificationUtils.getClickAction(sbn.notification, button)
        if (click != null) {
            Timber.w("Found $button action")
            sbn.notification.actions[click].actionIntent.send()
        }
        this.cancelNotification(sbn.key)
    }

    private fun reply(sbn: StatusBarNotification, message: String) {
        val action: Action? = NotificationUtils.getQuickReplyAction(sbn.notification, packageName)
        if (action != null) {
            Timber.i("Found reply action")
            try {
                action.sendReply(
                    applicationContext,
                    message
                )
            } catch (e: PendingIntent.CanceledException) {
                Timber.i("CRAP $e")
            }
        } else {
            Timber.i("Reply action not found")
        }
        this.cancelNotification(sbn.key)
    }
}