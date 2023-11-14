package com.example.bpsautoreply.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AutoReplyMessage(
    val id: String,
    var trigger: String,
    var response: String
) : Parcelable
