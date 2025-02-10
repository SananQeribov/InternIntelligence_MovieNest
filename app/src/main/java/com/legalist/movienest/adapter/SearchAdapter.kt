package com.legalist.movienest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.legalist.data.model.Category
import com.legalist.movienest.databinding.CategoryItemBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.ArrayList

class SearchAdapter(private var mList: ArrayList<Category>) :
    RecyclerView.Adapter<SearchAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val logo: ImageView = binding.logoIv
        val titleTv: TextView = binding.titleTv
        val video: YouTubePlayerView = binding.youtubePlayerView
        val constraintLayout: ConstraintLayout = binding.constraintLayout

        fun collapseExpandedView() {
            video.visibility = View.GONE
        }
    }

    fun setFilteredList(mList: ArrayList<Category>) {
        this.mList = mList
        //notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val languageData = mList[position]
        holder.logo.setImageResource(languageData.imageUrl)
        holder.titleTv.text = languageData.title

        val videoId = extractVideoIdFromUrl(languageData.videoId)


        holder.video.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {

                youTubePlayer.cueVideo(videoId, 0f)
            }
        })


        holder.video.visibility = if (languageData.isExpandable) View.VISIBLE else View.GONE
        holder.constraintLayout.setOnClickListener {
            isAnyItemExpanded(position)
            languageData.isExpandable = !languageData.isExpandable
            notifyItemChanged(position)
        }
    }

    private fun isAnyItemExpanded(position: Int) {
        val temp = mList.indexOfFirst { it.isExpandable }
        if (temp >= 0 && temp != position) {
            mList[temp].isExpandable = false
            notifyItemChanged(temp, 0)
        }
    }

    override fun onBindViewHolder(
        holder: LanguageViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] == 0) {
            holder.collapseExpandedView()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // YouTube video ID çıxarmaq üçün daha etibarlı metod
    fun extractVideoIdFromUrl(url: String): String {
        val regex =
            "(?:https?://(?:www\\.)?youtube\\.com(?:/[^/]+)?(?:/.*?)(?:v=|embed\\/)([\\w-]+))".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1) ?: ""
    }
}
