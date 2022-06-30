package com.example.litemsg.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litemsg.LiteMsgApplication
import com.example.litemsg.R
import com.example.litemsg.logic.model.Story

class SearchResultItemAdapter(val searchResultList: MutableList<Story>): RecyclerView.Adapter<SearchResultItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val searchImage: ImageView = view.findViewById(R.id.searchImage)
        val searchTitle: TextView = view.findViewById(R.id.searchTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchResultItem = searchResultList[position]
        Glide.with(LiteMsgApplication.context).load(searchResultItem.picture).into(holder.searchImage)
        holder.searchTitle.text = searchResultItem.title
    }

    override fun getItemCount() = searchResultList.size

}