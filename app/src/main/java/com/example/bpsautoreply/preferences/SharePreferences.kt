package com.example.bpsautoreply.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.bpsautoreply.utils.AutoReplyMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharePreferences (context: Context) {

    private var SHARED_PREF = "BPS_REPLY"
    private var sharedPref: SharedPreferences
    private val editor: SharedPreferences.Editor
    val gson = Gson()

    init {
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun setStringPreference(prefKey: String, value: String) {
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun getStringPreference(key: String) : String?{
        return sharedPref.getString(key, "")
    }

    fun setAutoReplyMessages(messages: List<AutoReplyMessage>) {
        val json = gson.toJson(messages)
        editor.putString("auto_reply_messages", json)
        editor.apply()
    }

    fun getAutoReplyMessages(): List<AutoReplyMessage> {
        val json = sharedPref.getString("auto_reply_messages", null)
        return if (json != null) {
            gson.fromJson(json, object : TypeToken<List<AutoReplyMessage>>() {}.type)
        } else {
            emptyList()
        }
    }

    fun clearAutoReplyMessageById(id: String) {
        val messages = getAutoReplyMessages().toMutableList()
        messages.removeAll { it.id == id }
        setAutoReplyMessages(messages)
    }


    fun clearPreferenceByKey(prefKey: String) {
        editor.remove(prefKey)
        editor.apply()
    }

    fun clearPreferences() {
        editor.clear().apply()
    }
}