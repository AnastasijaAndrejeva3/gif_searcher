package com.example.gif_searcher.network

import com.example.gif_searcher.BuildConfig
import com.example.gif_searcher.data.model.GifItem
import com.example.gif_searcher.data.parser.ResponseParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GifRepository(
    private val apiClient: AppClient,
    private val gifResponseParser: ResponseParser
) {
    private val baseUrl = BuildConfig.BASE_URL

    private val apiKey = BuildConfig.API_KEY


    suspend fun searchGifs(query: String, loadMore: Boolean): Result<List<GifItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val offset = if (loadMore) GifsDataHolder.gifTitles.size else 0
                val url = "$baseUrl?api_key=$apiKey&q=$query&limit=15&offset=$offset"
                val request = apiClient.createRequest(url)

                val response = apiClient.executeRequest(request)
                response.fold(
                    onSuccess = { jsonResponse ->
                        val parseResult = gifResponseParser.getGifsFromResponse(jsonResponse)
                        parseResult.fold(
                            onSuccess = { gifList ->
                                val filteredGifs =
                                    gifList.filterNot { GifsDataHolder.gifTitles.contains(it.title) }
                                GifsDataHolder.gifs.addAll(filteredGifs)
                                GifsDataHolder.gifTitles.addAll(filteredGifs.map { it.title })
                                Result.success(filteredGifs)
                            },
                            onFailure = { e -> Result.failure(e) }
                        )
                    },
                    onFailure = { e -> Result.failure(e) }
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    object GifsDataHolder {
        val gifs = LinkedHashSet<GifItem>()
        val gifTitles = mutableSetOf<String>()
    }
}
