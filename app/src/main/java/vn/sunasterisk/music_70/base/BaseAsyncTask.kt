package vn.sunasterisk.music_70.base

import android.os.AsyncTask
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class BaseAsyncTask<T> : AsyncTask<String, Void, List<out T>>() {
    abstract fun convertJsonToObject(response: String): List<out T>

    override fun doInBackground(vararg params: String?): List<out T> {
        val url = URL(params[0])
        val httpConnection = url.openConnection() as HttpURLConnection
        val inputStream = BufferedInputStream(httpConnection.inputStream)
        val response = readStream(inputStream)
        return convertJsonToObject(response)
    }

    private fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        inputStream.close()
        return stringBuilder.toString()
    }
}
