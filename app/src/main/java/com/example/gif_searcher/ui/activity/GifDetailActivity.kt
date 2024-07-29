package com.example.gif_searcher.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.gif_searcher.R

class GifDetailActivity : AppCompatActivity() {
    private lateinit var gifImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var sourceTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var ratingTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif_detail)

        init()
        setGifParametersToView()
    }

    private fun init() {
        gifImageView = findViewById(R.id.gifDetailImageView)
        titleTextView = findViewById(R.id.gifTitleTextView)
        sourceTextView = findViewById(R.id.gifSourceTextView)
        usernameTextView = findViewById(R.id.gifUsernameTextView)
        ratingTextView = findViewById(R.id.gifRatingTextView)
    }

    @SuppressLint("PrivateResource")
    private fun setGifParametersToView() {

        val gifUrl = intent.getStringExtra("gif_url").orEmpty()
        val gifTitle = intent.getStringExtra("gif_title").orEmpty()
        val gifSource = intent.getStringExtra("gif_source").orEmpty()
        val gifUsername = intent.getStringExtra("gif_username").orEmpty()
        val gifRating = intent.getStringExtra("gif_rating").orEmpty()


        if (gifUrl.isNotBlank()) {
            Glide.with(this)
                .load(gifUrl)
                .into(gifImageView)
        } else {
            gifImageView.setImageResource(com.google.android.material.R.drawable.mtrl_ic_error)
        }

        titleTextView.text = if (gifTitle.isNotBlank()) "Title: $gifTitle" else "Title: N/A"
        sourceTextView.text = if (gifSource.isNotBlank()) "Source: $gifSource" else "Source: N/A"
        usernameTextView.text =
            if (gifUsername.isNotBlank()) "Username: $gifUsername" else "Username: N/A"
        ratingTextView.text = if (gifRating.isNotBlank()) "Rating: $gifRating" else "Rating: N/A"
    }
}
