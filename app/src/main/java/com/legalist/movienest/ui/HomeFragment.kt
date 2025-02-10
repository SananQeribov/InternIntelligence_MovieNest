package com.legalist.movienest.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.legalist.common.utils.ImageDLoader
import com.legalist.data.model.MovieItem
import com.legalist.data.util.Resource
import com.legalist.movienest.R
import com.legalist.movienest.base.BaseAdapter
import com.legalist.movienest.base.BaseFragment
import com.legalist.movienest.databinding.FragmenHomeBinding
import com.legalist.movienest.databinding.HomeItemsBinding
import com.legalist.movienest.util.MovieClickListener
import com.legalist.movienest.viewmodel.HomeViewModel
import com.legalist.movienest.viewmodel.UpcomingViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class HomeFragment() : BaseFragment<FragmenHomeBinding>(), MovieClickListener {
    private val viewModel by viewModels<HomeViewModel>()
    private val viewModel2 by viewModels<UpcomingViewModel>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun buildUi() {
        super.buildUi()
        viewModel.getMovieList()
        observeEvents()
        viewModel2.getMovieList()
        observeUpcoming()
        setupBottomNav()

        lifecycle.addObserver(binding.youtubePlayerView)
        val urlList = listOf(
            "https://www.youtube.com/watch?v=JfVOs4VSpmA",
            "https://www.youtube.com/watch?v=mcvLKldPM08?si=sKCJU9JbRhhzHaaT",
            "https://www.youtube.com/watch?v=NTvudSGfHRI?si=6Dr7fAgpXqV-1oOT",
            "https://www.youtube.com/watch?v=uPzOyzsnmio ? si = oO -JTm_C0jcKYGzN",
            "https://www.youtube.com/watch?v=LPLPz92BMDk?si=OnOrm23xGDlloVc9",
            "https://www.youtube.com/watch?v=_inKs4eeHiI?si=KYllbs2nL7OJ33qx"

        )
        val videoUrl = urlList.random()
        val videoId = extractVideoIdFromUrl(videoUrl)
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {

                youTubePlayer.loadVideo(videoId, 0f)
            }
        })


    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmenHomeBinding {
        return FragmenHomeBinding.inflate(inflater, container, false)
    }

    @SuppressLint("SetTextI18n")
    private fun observeEvents() {
        // Observe LiveData values using viewLifecycleOwner
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            binding.tvErrorMessage.text = error
            if (error != null) {
                binding.tvErrorMessage.isVisible = error.isNotEmpty()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.isVisible = loading
        }



        viewModel.movieList.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { list ->
                        if (list.isNotEmpty()) {
                            setupRecyclerView(list)


                        } else if (list.isEmpty()) {
                            binding.tvErrorMessage.text = "No movies found."
                            binding.tvErrorMessage.isVisible = true
                        }


                    }

                }

                is Resource.Error -> {
                    binding.tvErrorMessage.text = "Error: ${resource.message}"
                    binding.tvErrorMessage.isVisible = true
                }

                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun setupRecyclerView(list: List<MovieItem?>) {
        val adapter = BaseAdapter(
            itemList = list,
            createBinding = { inflater, parent ->
                HomeItemsBinding.inflate(inflater, parent, false)
            },
            bind = { binding, item ->

                if (item != null) {
                    Log.e("this image url", "${item.imageUrl} posterPath ${item.backdrop_path}")
                    ImageDLoader.loadImage2(binding.imageViewMovie, item.imageUrl)
                }
                binding.root.setOnClickListener {
                    if (item != null) {
                        onMovieClicked(movieId = item.id)
                    }
                }
            }
        )
        binding.recyclerPopularMovies.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }
    }

    private fun observeUpcoming() {
        viewModel2.errorMessage.observe(viewLifecycleOwner) { error ->
            binding.tvErrorMessage.text = error
            if (error != null) {
                binding.tvErrorMessage.isVisible = error.isNotEmpty()
            }
        }

        viewModel2.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.isVisible = loading
        }
        viewModel2.upcoming.observe(viewLifecycleOwner) { resource2 ->
            when (resource2) {
                is Resource.Success -> {
                    resource2.data?.let { list2 ->
                        if (list2.isNotEmpty()) {
                            val adapter = BaseAdapter(
                                itemList = list2,
                                createBinding = { inflater, parent ->
                                    HomeItemsBinding.inflate(inflater, parent, false)
                                },
                                bind = { binding, item ->

                                    if (item != null) {
                                        Log.e(
                                            "this image url",
                                            "${item.imageUrl} posterPath ${item.backdrop_path}"
                                        )
                                        ImageDLoader.loadImage2(
                                            binding.imageViewMovie,
                                            item.imageUrl
                                        )

                                        binding.root.setOnClickListener {
                                            if (item != null) {
                                                onMovieClicked(movieId = item.id)
                                            }
                                        }
                                    }


                                }
                            )
                            binding.recyclerLatestMovies.apply {
                                this.adapter = adapter
                                layoutManager = LinearLayoutManager(
                                    context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )

                            }
                        } else {
                            binding.tvErrorMessage.text = "No movies found."
                            binding.tvErrorMessage.isVisible = true
                        }
                    }
                }

                is Resource.Error -> {
                    binding.tvErrorMessage.text = "Error: ${resource2.message}"
                    binding.tvErrorMessage.isVisible = true
                }

                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    fun extractVideoIdFromUrl(url: String): String {

        val regex =
            "(?:https?://(?:www\\.)?youtube\\.com(?:/[^/]+)?(?:/.*?)(?:v=|embed\\/)([\\w-]+))".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1) ?: ""
    }

    override fun onMovieClicked(movieId: Int?) {
        movieId?.let {
            val bundle = Bundle().apply {
                putInt("movieId", it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }

    }

    private fun setupBottomNav() {

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {

                    true
                }

                R.id.category -> {
                    // Replace with CategoryFragment
                    findNavController().navigate(R.id.categoryFragment)
                    true
                }

                R.id.profile -> {
                    findNavController().navigate(R.id.profileFragment)
                    true
                }

                else -> false
            }
        }

    }

}






