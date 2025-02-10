package com.legalist.movienest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.legalist.movienest.R
import com.legalist.movienest.base.BaseAdapter
import com.legalist.movienest.base.BaseFragment
import com.legalist.movienest.databinding.EachItemBinding
import com.legalist.movienest.databinding.FragmentBoardingBinding

class BoardingFragment : BaseFragment<FragmentBoardingBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBoardingBinding {
        return FragmentBoardingBinding.inflate(layoutInflater, container, false)
    }

    override fun buildUi() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = CarouselLayoutManager()
        CarouselSnapHelper().attachToRecyclerView(binding.recyclerView)

        val imageList = mutableListOf<Int>()
        imageList.add(R.drawable.spiderman)
        imageList.add(R.drawable.batman)
        imageList.add(R.drawable.panda)
        val adapter = BaseAdapter(itemList = imageList, createBinding = { inflater, parent ->
            EachItemBinding.inflate(inflater, parent, false)
        },
            bind = { binding, item ->
                binding.imageView.setImageResource(item)
            })

        binding.recyclerView.layoutManager = CarouselLayoutManager()

        binding.recyclerView.adapter = adapter
      binding.navigateTo(binding.button,R.id.action_boardingFragment_to_homeFragment)

    }


}




