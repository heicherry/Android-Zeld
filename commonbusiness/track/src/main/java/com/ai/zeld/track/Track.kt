package com.ai.zeld.track

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ai.zeld.util.app.App
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import okhttp3.OkHttpClient
import java.util.*
import kotlin.collections.HashMap


object Track {
    private const val BASE_URL = "http://39.100.242.150:8090"
    private val sp = App.application.getSharedPreferences("main", Context.MODE_PRIVATE)

    private var uuid: String
        get() {
            val id = sp.getString("uuid", "")
            return if (id.isNullOrEmpty()) {
                val newId = UUID.randomUUID().toString()
                uuid = newId
                newId
            } else {
                id
            }
        }
        set(value) {
            sp.edit().putString("uuid", value).apply()
        }

    private fun query(
        event: String,
        parametersInvoke: ((MutableMap<String, String>) -> Unit)? = null
    ) {
        val url = "$BASE_URL/$event"
        val okHttpClient = OkHttpClient()

        val urlBuilder = url.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("uuid", uuid)
        urlBuilder.addQueryParameter("os_model", Build.BRAND + "_" + Build.MODEL)
        val realParameters = mutableMapOf<String, String>()
        parametersInvoke?.invoke(realParameters)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            realParameters.forEach { (key, value) ->
                urlBuilder.addQueryParameter(key, value)
            }
        }
        val request = Request.Builder().url(urlBuilder.build()).get().build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ayy", "onFailure: ", e)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("ayy", "onResponse: " + response.body!!.string())
            }
        })
    }

    fun onAppLaunch() {
        query("launch")
    }

    fun onSceneEnter(scene: String) {
        query("sceneenter") {
            it["scene"] = scene
        }
    }

    fun onSceneFinished(scene: String, succeed: Boolean) {
        query("gameresult") {
            it["scene"] = scene
            it["result"] = if (succeed) "1" else "0"
        }
    }



}