package com.example.gif_searcher.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gif_searcher.R
import com.example.gif_searcher.data.parser.ResponseParser
import com.example.gif_searcher.network.AppClient
import com.example.gif_searcher.network.GifRepository
import com.example.gif_searcher.network.helper.NetworkHelper
import com.example.gif_searcher.ui.adapter.GifAdapter
import com.example.gif_searcher.ui.viewmodel.MainViewModel
import com.example.gif_searcher.util.listener.ScrollListener
import com.example.gif_searcher.util.listener.SearchTextListener
import com.example.gif_searcher.util.manager.ErrorDialogManager

class MainActivity : AppCompatActivity() {
    private lateinit var gifAdapter: GifAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchBar: EditText
    private lateinit var gifRecyclerView: RecyclerView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var networkStatusHelper: NetworkHelper
    private lateinit var errorManager: ErrorDialogManager


    private fun init() {
        progressBar = findViewById(R.id.progressBar)
        searchBar = findViewById(R.id.searchBar)
        gifRecyclerView = findViewById(R.id.gifRecyclerView)
        networkStatusHelper = NetworkHelper(this)
        errorManager = ErrorDialogManager(this)

    }

    private fun setupActivities() {
        setupRecyclerView()
        setupViewModel()
        setupSearchBar()
        setupNetworkObserver()
        setupScrollListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setupActivities()
    }

    private fun setupScrollListener() {
        gifRecyclerView.addOnScrollListener(
            ScrollListener(
                loadMore = { mainViewModel.loadMoreGifs() })
        )
    }

    private fun setupSearchBar() {
        searchBar.addTextChangedListener(SearchTextListener {
            mainViewModel.onSearchQueryChanged(it)
        })
    }

    private fun setupRecyclerView() {
        gifRecyclerView.layoutManager = LinearLayoutManager(this)
        gifAdapter = GifAdapter(mutableListOf()) { gifItem ->
            val intent = Intent(this@MainActivity, GifDetailActivity::class.java).apply {
                putExtra("gif_url", gifItem.url)
                putExtra("gif_title", gifItem.title)
                putExtra("gif_source", gifItem.source)
                putExtra("gif_username", gifItem.username)
                putExtra("gif_rating", gifItem.rating)
            }
            startActivity(intent)
        }
        gifRecyclerView.adapter = gifAdapter
    }


    private fun setupViewModel() {
        val apiClient = AppClient()
        val responseParser = ResponseParser()
        val repository = GifRepository(apiClient, responseParser)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.gifList.observe(this) { gifs ->
            gifAdapter.setGifs(gifs)
        }

        mainViewModel.loading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mainViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                errorManager.showErrorDialog(it)
            }
        }
    }

    private fun setupNetworkObserver() {
        networkStatusHelper.isNetworkAvailable.observe(this) { isAvailable ->
            if (!isAvailable) {
                errorManager.showErrorDialog(getString(R.string.check_internet_text))
            } else {
                errorManager.dismissErrorDialog()
            }
        }
        networkStatusHelper.registerNetworkCallback()
    }
}