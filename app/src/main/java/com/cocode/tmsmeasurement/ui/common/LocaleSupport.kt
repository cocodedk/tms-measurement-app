package com.cocode.tmsmeasurement.ui.common

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.cocode.tmsmeasurement.R

data class LanguageOption(
    val tag: String,
    val labelRes: Int
)

const val SYSTEM_LANGUAGE_TAG = "system"
const val HELP_URL = "https://cocodedk.github.io/tms-measurement-app/"

fun openHelpPage(context: Context) {
    val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(HELP_URL)).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
    }
    val chooser = Intent.createChooser(viewIntent, context.getString(R.string.action_help))
    if (context !is Activity) {
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(chooser)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            context.getString(R.string.error_no_browser),
            Toast.LENGTH_LONG
        ).show()
    }
}

fun normalizeLanguageTag(rawTags: String): String {
    val tag = rawTags.split(',').firstOrNull()?.trim().orEmpty()
    if (tag.isBlank()) {
        return SYSTEM_LANGUAGE_TAG
    }
    val lower = tag.lowercase()
    return when {
        lower.startsWith("fa") -> "fa"
        lower.startsWith("ar") -> "ar"
        lower == "zh-tw" || lower.startsWith("zh-hant") -> "zh-TW"
        lower.startsWith("en") -> "en"
        else -> SYSTEM_LANGUAGE_TAG
    }
}
