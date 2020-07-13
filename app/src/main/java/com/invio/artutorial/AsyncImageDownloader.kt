package com.invio.artutorial

import android.content.Context
import android.os.AsyncTask
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL

class AsyncImageDownloader(
    private val context: Context,
    private val fileName: String,
    private val imageUrl: String?,
    private val listener: ResultListener
) :
    AsyncTask<Void?, Void?, Void?>() {
    override fun doInBackground(vararg p0: Void?): Void? {
        if (imageUrl == null || imageUrl.isEmpty()) {
            listener.actionCompleted(false)
            return null
        }
        try {
            cacheImage(imageUrl, fileName)
            listener.actionCompleted(true)
        } catch (e: Exception) {
            e.printStackTrace()
            listener.actionCompleted(false)
        }
        return null
    }

    @Throws(IOException::class)
    private fun cacheImage(urlPath: String, fileName: String) {
        var count: Int
        val url = URL(urlPath)
        val connection = url.openConnection()
        connection.connect()
        val outputStream =
            context.openFileOutput(fileName, Context.MODE_PRIVATE)
        val input: InputStream = BufferedInputStream(url.openStream(), 8192)
        val data = ByteArray(1024)
        while (input.read(data).also { count = it } != -1) {
            outputStream.write(data, 0, count)
        }
        outputStream.flush()
        outputStream.close()
        input.close()
    }

    interface ResultListener {
        fun actionCompleted(isSuccess: Boolean)
    }
}