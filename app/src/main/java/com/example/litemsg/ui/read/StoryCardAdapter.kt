package com.example.litemsg.ui.read

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

class StoryCardAdapter(val storyCardList: List<Story>) : RecyclerView.Adapter<StoryCardAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardImage: ImageView = view.findViewById(R.id.cardImage)
        val cardTitle: TextView = view.findViewById(R.id.cardTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.read_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyCard = storyCardList[position]
        Glide.with(LiteMsgApplication.context).load(storyCard.picture).into(holder.cardImage)
        holder.cardTitle.text = storyCard.title
    }

    override fun getItemCount() = storyCardList.size


}