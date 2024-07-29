package com.example.gif_searcher.util.listener

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchTextListener(
    private val onQueryChanged: (String) -> Unit
) : TextWatcher {


    private var searchJob: Job? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        searchJob?.cancel()
        searchJob = CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            onQueryChanged(s.toString())
        }
    }
}
