package com.example.litemsg.ui.collect

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.litemsg.LiteMsgApplication
import com.example.litemsg.R
import com.example.litemsg.logic.model.ListLikeResponse
import com.example.litemsg.logic.model.Story
import com.example.litemsg.ui.BaseActivity
import com.example.litemsg.ui.read.StoryCardAdapter
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import kotlin.concurrent.thread

class CollectActivity : BaseActivity() {

    private lateinit var adapter: CollectItemAdapter

    private lateinit var collectToolBarTitleText: TextView
    private lateinit var collectToolBar: Toolbar
    private lateinit var noCollectTipLayout: RelativeLayout

    // recyclerView
    private lateinit var collectSwipeRefresh: SwipeRefreshLayout
    private lateinit var collectRecyclerView: RecyclerView
    private val collectList = ArrayList<Story>()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)

        // 初始化
        collectToolBarTitleText = findViewById(R.id.collectToolBarTitleText)
        collectToolBar = findViewById(R.id.collectToolBar)
        collectRecyclerView = findViewById(R.id.collectRecyclerView)
        collectSwipeRefresh = findViewById(R.id.collectSwipeRefresh)
        noCollectTipLayout = findViewById(R.id.noCollectTipLayout)
        getListCollect()

        // 设置标题
        collectToolBarTitleText.text = "收藏"
        setSupportActionBar(collectToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // 设置recyclerView
        val layoutManager = GridLayoutManager(this, 2)
        collectRecyclerView.layoutManager = layoutManager
        adapter = CollectItemAdapter(collectList)
        collectRecyclerView.adapter = adapter

        // 初始化刷新信息
        collectSwipeRefresh.setColorSchemeColors(R.color.textColorAccent)
        collectSwipeRefresh.setOnRefreshListener {
            refreshCollect(adapter)
        }
    }

    private fun refreshCollect(adapter: CollectItemAdapter) {
        getListCollect()
    }

    private fun getListCollect() {
        collectList.clear()
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/listCollect")
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
            val listCollectResponse = gson.fromJson(jsonData, ListLikeResponse::class.java)
            if (listCollectResponse.flag) {

                collectList.addAll(listCollectResponse.data)
                adapter.notifyDataSetChanged()
                println(listCollectResponse)
            }
            if (collectList.isEmpty()) {
                noCollectTipLayout.visibility = View.VISIBLE
            } else {
                noCollectTipLayout.visibility = View.GONE
            }
            collectSwipeRefresh.isRefreshing = false
        }
    }
}