package com.legalist.movienest.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<Vb : ViewBinding>() : Fragment() {
    protected lateinit var binding: Vb

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): Vb

open  fun buildUi(){}
    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = inflateBinding(inflater, container)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildUi()

    }



    override fun onResume() {
        super.onResume()

    }


    protected fun Vb?.navigateTo(view: View? = null,destinationId: Int) {
        if (view != null) {
            view.setOnClickListener {
                this?.root?.findNavController()?.navigate(destinationId)

            }
        }


    }
}