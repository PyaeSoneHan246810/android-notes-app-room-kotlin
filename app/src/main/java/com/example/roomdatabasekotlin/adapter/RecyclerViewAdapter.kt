package com.example.roomdatabasekotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter<T>(
    private val layout: Int,
    private var items: List<T>,
    private val isRecyclable: Boolean,
    private val bindView: (view: View, item: T, position: Int) -> Unit
): RecyclerView.Adapter<RecyclerViewAdapter<T>.BaseViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter<T>.BaseViewHolder = BaseViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))

    override fun onBindViewHolder(
        holder: RecyclerViewAdapter<T>.BaseViewHolder,
        position: Int
    ) {
        holder.bind(items[position], position)
        holder.setIsRecyclable(isRecyclable)
    }

    override fun getItemCount(): Int = items.size

    inner class BaseViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        fun bind(item: T, position: Int){
            bindView.invoke(itemView, item, position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newItems: List<T>){
        items = newItems
        notifyDataSetChanged()
    }
}