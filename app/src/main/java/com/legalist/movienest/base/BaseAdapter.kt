package com.legalist.movienest.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
 class BaseAdapter<T, Vb : ViewBinding>(
    private var itemList: List<T>,
    private val createBinding: (LayoutInflater, ViewGroup) -> Vb,
    private val bind: (Vb, T) -> Unit
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<Vb>>() {

    class BaseViewHolder<Vb : ViewBinding>(val binding: Vb) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Vb> {
        val binding = createBinding(LayoutInflater.from(parent.context), parent)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Vb>, position: Int) {
        bind(holder.binding, itemList[position])

    }

    override fun getItemCount(): Int = itemList.size

    fun updateList(newList: List<T>) {
        itemList = newList
        notifyDataSetChanged()
    }

    }


