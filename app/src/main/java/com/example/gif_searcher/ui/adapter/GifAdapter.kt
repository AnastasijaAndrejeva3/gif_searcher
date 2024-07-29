package com.example.gif_searcher.ui.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gif_searcher.R
import com.example.gif_searcher.data.model.GifItem

class GifAdapter(
    private var gifs: MutableList<GifItem>,
    private val onGifClick: (GifItem) -> Unit
) : RecyclerView.Adapter<GifAdapter.GifViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false)
        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val gif = gifs[position]
        holder.bind(gif)
    }

    override fun getItemCount(): Int = gifs.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGifs(newGifs: List<GifItem>) {
        gifs = newGifs.toMutableList()
        notifyDataSetChanged()
    }

    inner class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gifImageView: ImageView = itemView.findViewById(R.id.gifImageView)
        private val gifTitle: TextView = itemView.findViewById(R.id.gifTitle)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.itemProgressBar)

        fun bind(gif: GifItem) {
            gifTitle.text = gif.title

            progressBar.visibility = View.VISIBLE
            gifImageView.visibility = View.INVISIBLE

            Glide.with(itemView.context)
                .load(gif.url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        gifImageView.visibility = View.VISIBLE
                        return false
                    }
                })
                .into(gifImageView)

            itemView.setOnClickListener { onGifClick(gif) }
        }
    }
}
