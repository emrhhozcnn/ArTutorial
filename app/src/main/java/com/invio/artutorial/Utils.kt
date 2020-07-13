package com.invio.artutorial

import android.content.Context
import java.io.File


class Utils {
    companion object {
        private const val KEY_SH_PRE_FILE = "KEY_SH_PRE_FILE"

        @JvmStatic
        fun getLastSavedModelUrl(context: Context, key: String): String? {
            val sharedPreferences =
                context.getSharedPreferences(KEY_SH_PRE_FILE, Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, null)
        }

        fun isFileExist(context: Context, fileName: String): Boolean {
            val file = File(context.filesDir.toString() + "/" + fileName)
            return file.exists()
        }
    }
}