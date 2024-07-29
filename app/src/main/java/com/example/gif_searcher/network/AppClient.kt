package com.example.gif_searcher.network

import okhttp3.OkHttpClient
import okhttp3.Request

class AppClient {
    private val client = OkHttpClient()

    fun createRequest(url: String): Request {
        return Request.Builder().url(url).build()
    }

    fun executeRequest(request: Request): Result<String> {
        return try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Result.failure(Exception("HTTP ${response.code}"))
            } else {
                Result.success(response.body?.string().orEmpty())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
