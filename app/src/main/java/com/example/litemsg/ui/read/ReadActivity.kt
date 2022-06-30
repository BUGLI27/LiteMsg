package com.example.litemsg.ui.read

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.litemsg.R
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.litemsg.LiteMsgApplication
import com.example.litemsg.logic.model.ListLikeResponse
import com.example.litemsg.logic.model.Story
import com.example.litemsg.ui.BaseActivity
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import kotlin.concurrent.thread


class ReadActivity : BaseActivity() {

//    val viewModel by lazy { ViewModelProvider(this).get(ReadViewModel::class.java) }

    private lateinit var adapter: StoryCardAdapter

    private lateinit var toolBarTitleText: TextView
    private lateinit var readToolBar: Toolbar
    private lateinit var noReadTipLayout: RelativeLayout

    // recyclerView
    private lateinit var readSwipeRefresh: SwipeRefreshLayout
    private lateinit var readRecyclerView: RecyclerView
    private val storyCardList = ArrayList<Story>()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.litemsg.R.layout.activity_read)

        // 初始化
        toolBarTitleText = findViewById(R.id.toolBarTitleText)
        readToolBar = findViewById(R.id.readToolBar)
        readRecyclerView = findViewById(R.id.readRecyclerView)
        readSwipeRefresh = findViewById(R.id.readSwipeRefresh)
        noReadTipLayout = findViewById(R.id.noReadTipLayout)
        getListLike()

        // 设置标题
        toolBarTitleText.text = "已读"
        setSupportActionBar(readToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // 设置recyclerView
        val layoutManager = GridLayoutManager(this, 2)
        readRecyclerView.layoutManager = layoutManager
        adapter = StoryCardAdapter(storyCardList)
        readRecyclerView.adapter = adapter

        // 初始化刷新信息
        readSwipeRefresh.setColorSchemeColors(R.color.textColorAccent)
        readSwipeRefresh.setOnRefreshListener {
            refreshRead(adapter)
        }

    }

    private fun refreshRead(adapter: StoryCardAdapter) {
        getListLike()
    }

    private fun getListLike() {
        storyCardList.clear()
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/listRead")
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
                storyCardList.addAll(listLikeResponse.data)
                adapter.notifyDataSetChanged()
                println(listLikeResponse)
            }
            Log.d("test", storyCardList.toString())
            if (storyCardList.isEmpty()) {
                noReadTipLayout.visibility = View.VISIBLE
            } else {
                noReadTipLayout.visibility = View.GONE
            }
            readSwipeRefresh.isRefreshing = false
        }
    }
}