package com.legalist.movienest.ui


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.legalist.common.utils.ImageDLoader
import com.legalist.movienest.base.BaseFragment
import com.legalist.movienest.databinding.FragmentDetailBinding
import com.legalist.movienest.viewmodel.DetaiViewModel


class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    private val viewModel by viewModels<DetaiViewModel>()


    override fun buildUi() {

        viewModel.getMovieDetail(movieId = arguments?.getInt("movieId").toString())
        viewModel.getMovieDetail(movieId = arguments?.getInt("movieId").toString())
        observeEvents()


    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(layoutInflater, container, false)
    }


    @SuppressLint("SetTextI18n")
    private fun observeEvents() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBarDetail.isVisible = it
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.textViewErrorDetail.text = it
            binding.textViewErrorDetail.isVisible = true
        }
        viewModel.movieResponse.observe(viewLifecycleOwner) { movie ->

            ImageDLoader.loadImage2(
                binding.imageViewDetail,
                movie.imageUrl
            )
            binding.textViewDetailVote.text = movie.vote_average.toString()
            binding.textViewDetailStudio.text = movie.production_companies.first().name
            binding.textViewDetailLanguage.text = movie.spoken_languages.first().english_name

            binding.textViewDetailOverview.text = movie.overview

            binding.movieDetailGroup.isVisible = true



            (requireActivity() as? MainActivity)?.supportActionBar?.title = movie.title

        }
    }
}
