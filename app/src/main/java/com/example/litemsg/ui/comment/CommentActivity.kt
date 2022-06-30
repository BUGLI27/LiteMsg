package com.example.litemsg.ui.comment

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.litemsg.LiteMsgApplication
import com.example.litemsg.R
import com.example.litemsg.logic.model.AllCommentResponse
import com.example.litemsg.logic.model.Comment
import com.example.litemsg.logic.model.ListLikeResponse
import com.example.litemsg.logic.model.Story
import com.example.litemsg.ui.BaseActivity
import com.example.litemsg.ui.collect.CollectItemAdapter
import com.example.litemsg.ui.read.StoryCardAdapter
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class CommentActivity : BaseActivity() {
    private lateinit var adapter: CommentItemAdapter
    private lateinit var commentToolBarTitleText: TextView
    private lateinit var commentToolBar: Toolbar
    private lateinit var noCommentTipLayout: RelativeLayout

    // recyclerView
    private lateinit var commentSwipeRefresh: SwipeRefreshLayout
    private lateinit var commentRecyclerView: RecyclerView
    private val commentList = ArrayList<Comment>()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.litemsg.R.layout.activity_comment)

        // 初始化
        commentToolBarTitleText = findViewById(R.id.commentToolBarTitleText)
        commentToolBar = findViewById(R.id.commentToolBar)
        commentRecyclerView = findViewById(R.id.commentRecyclerView)
        commentSwipeRefresh = findViewById(R.id.commentSwipeRefresh)
        noCommentTipLayout = findViewById(R.id.noCommentTipLayout)
        getListComment()

        // 设置标题
        commentToolBarTitleText.text = "评论"
        setSupportActionBar(commentToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // 设置recyclerView
        val layoutManager = LinearLayoutManager(this)
        commentRecyclerView.layoutManager = layoutManager
        adapter = CommentItemAdapter(commentList)
        commentRecyclerView.adapter = adapter

        // 初始化刷新信息
        commentSwipeRefresh.setColorSchemeColors(R.color.textColorAccent)
        commentSwipeRefresh.setOnRefreshListener {
            refreshComments(adapter)
        }

    }

    private fun refreshComments(adapter: CommentItemAdapter) {
        getListComment()
    }

    private fun getListComment() {
        commentList.clear()
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/getAllCommentByUid")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {
                    parseJsonWithGson(responseData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun parseJsonWithGson(jsonData: String) {
        runOnUiThread {
            val gson = Gson()
            val allCommentResponse = gson.fromJson(jsonData, AllCommentResponse::class.java)
            if (allCommentResponse.flag) {

                commentList.addAll(allCommentResponse.data)
                adapter.notifyDataSetChanged()
                println(allCommentResponse)
            }
            if (commentList.isEmpty()) {
                noCommentTipLayout.visibility = View.VISIBLE
            } else {
                noCommentTipLayout.visibility = View.GONE
            }
            commentSwipeRefresh.isRefreshing = false
        }
    }
}