package com.example.litemsg.ui.like

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.litemsg.LiteMsgApplication
import com.example.litemsg.R
import com.example.litemsg.logic.model.ListLikeResponse
import com.example.litemsg.logic.model.Story
import com.example.litemsg.ui.BaseActivity
import com.example.litemsg.ui.like.LikeItemAdapter
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import kotlin.concurrent.thread

class LikeActivity : BaseActivity() {
    private lateinit var adapter: LikeItemAdapter

    private lateinit var likeToolBarTitleText: TextView
    private lateinit var likeToolBar: Toolbar
    private lateinit var noLikeTipLayout: RelativeLayout

    // recyclerView
    private lateinit var likeSwipeRefresh: SwipeRefreshLayout
    private lateinit var likeRecyclerView: RecyclerView
    private val likeList = ArrayList<Story>()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        // 初始化
        likeToolBarTitleText = findViewById(R.id.likeToolBarTitleText)
        likeToolBar = findViewById(R.id.likeToolBar)
        likeRecyclerView = findViewById(R.id.likeRecyclerView)
        likeSwipeRefresh = findViewById(R.id.likeSwipeRefresh)
        noLikeTipLayout = findViewById(R.id.noLikeTipLayout)
        getListlike()

        // 设置标题
        likeToolBarTitleText.text = "喜欢"
        setSupportActionBar(likeToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // 设置recyclerView
        val layoutManager = GridLayoutManager(this, 2)
        likeRecyclerView.layoutManager = layoutManager
        adapter = LikeItemAdapter(likeList)
        likeRecyclerView.adapter = adapter

        // 初始化刷新信息
        likeSwipeRefresh.setColorSchemeColors(R.color.textColorAccent)
        likeSwipeRefresh.setOnRefreshListener {
            refreshlike(adapter)
        }
    }

    private fun refreshlike(adapter: LikeItemAdapter) {
        getListlike()
    }

    private fun getListlike() {
        likeList.clear()
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/listLike")
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
            val listLikeResponse = gson.fromJson(jsonData, ListLikeResponse::class.java)
            if (listLikeResponse.flag) {

                likeList.addAll(listLikeResponse.data)
                adapter.notifyDataSetChanged()
                println(listLikeResponse)
            }
            if (likeList.isEmpty()) {
                noLikeTipLayout.visibility = View.VISIBLE
            } else {
                noLikeTipLayout.visibility = View.GONE
            }
            likeSwipeRefresh.isRefreshing = false
        }
    }
}