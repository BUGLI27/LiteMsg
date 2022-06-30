package com.example.litemsg.ui.comment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litemsg.LiteMsgApplication
import com.example.litemsg.R
import com.example.litemsg.logic.model.Comment
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class CommentItemAdapter(val commentList: MutableList<Comment>): RecyclerView.Adapter<CommentItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val commentHeadImage: CircleImageView = view.findViewById(R.id.commentHeadImage)
        val userNameText: TextView = view.findViewById(R.id.userNameText)
        val commentText: TextView = view.findViewById(R.id.commentText)
        val commentImage: ImageView = view.findViewById(R.id.commentImage)
        val commentTitleText: TextView = view.findViewById(R.id.commentTitleText)
        val commentContentText: TextView = view.findViewById(R.id.commentContentText)
        val commentTimeText: TextView = view.findViewById(R.id.commentTimeText)
        val deleteText: TextView = view.findViewById(R.id.deleteText)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        // 点击事件
        val holder = ViewHolder(view)
        holder.deleteText.setOnClickListener {
            val position = holder.adapterPosition
//            commentList.removeAt(position)
            Toast.makeText(LiteMsgApplication.context, "delete" + position.toString(), Toast.LENGTH_SHORT).show()
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentItem = commentList[position]
        // 根据uid获取头像、用户名
        Glide.with(LiteMsgApplication.context).load(commentItem.avatar).into(holder.commentHeadImage)
        holder.userNameText.text = commentItem.username
        holder.commentText.text = commentItem.content
        // 根据eid获取文章图片、标题、内容
        Glide.with(LiteMsgApplication.context).load(commentItem.picture).into(holder.commentImage)
        holder.commentTitleText.text = commentItem.title
        holder.commentContentText.text = commentItem.com_content

        holder.commentTimeText.text = Date().toString()
    }

    override fun getItemCount() = commentList.size

}