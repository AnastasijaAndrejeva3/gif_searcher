package com.example.gif_searcher.data.parser

import com.example.gif_searcher.data.model.GifItem
import org.json.JSONObject

class ResponseParser {
    fun getGifsFromResponse(jsonResponse: String): Result<List<GifItem>> {
        return try {
            val jsonObject = JSONObject(jsonResponse)
            val dataArray = jsonObject.getJSONArray("data")

            val gifList = mutableListOf<GifItem>()
            for (i in 0 until dataArray.length()) {
                val gifObject = dataArray.getJSONObject(i)
                val imagesObject = gifObject.getJSONObject("images")
                val originalObject = imagesObject.getJSONObject("original")
                val gifUrl = originalObject.getString("url")
                val gifTitle = gifObject.getString("title")
                val gifSource = gifObject.getString("source")
                val gifUsername = gifObject.getString("username")
                val gifRating = gifObject.getString("rating")

                val newGif = GifItem(gifUrl, gifTitle, gifSource, gifUsername, gifRating)
                gifList.add(newGif)
            }

            Result.success(gifList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
