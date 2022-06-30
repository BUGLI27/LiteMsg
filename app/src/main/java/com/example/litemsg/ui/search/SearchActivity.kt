package com.example.litemsg.ui.search

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.litemsg.LiteMsgApplication
import com.example.litemsg.R
import com.example.litemsg.logic.model.Comment
import com.example.litemsg.logic.model.ListLikeResponse
import com.example.litemsg.logic.model.SearchResponse
import com.example.litemsg.logic.model.Story
import com.example.litemsg.ui.BaseActivity
import com.example.litemsg.ui.comment.CommentItemAdapter
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class SearchActivity : BaseActivity() {

    private lateinit var adapter: SearchResultItemAdapter

    private lateinit var searchToolBarTitleText: TextView
    private lateinit var searchToolBar: Toolbar
    private lateinit var searchEditText: EditText
    private lateinit var clearImageButton: ImageButton
    private lateinit var searchTipText: TextView

    // recyclerView
    private lateinit var searchRecyclerView: RecyclerView
    private val searchResultList = ArrayList<Story>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // 初始化
        searchToolBarTitleText = findViewById(R.id.searchToolBarTitleText)
        searchToolBar = findViewById(R.id.searchToolBar)
        searchRecyclerView = findViewById(R.id.searchRecyclerView)
        clearImageButton = findViewById(R.id.clearImageButton)
        searchEditText = findViewById(R.id.searchEditText)
        searchTipText = findViewById(R.id.searchTipText)
//        initSearchResults()
        initIfVisible()

        // 设置标题
        searchToolBarTitleText.text = "溯查"
        setSupportActionBar(searchToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // 设置recyclerView
        val layoutManager = LinearLayoutManager(this)
        searchRecyclerView.layoutManager = layoutManager
        adapter = SearchResultItemAdapter(searchResultList)
        searchRecyclerView.adapter = adapter

        // 点击清空按钮
        clearImageButton.setOnClickListener {
            clearEditText(adapter)
            initIfVisible()
        }

        // 搜索框搜索
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 0) {
                    Log.d("char_test", s?.length.toString())
                    clearImageButton.visibility = View.VISIBLE
                    searchTipText.visibility = View.VISIBLE
//                    Toast.makeText(LiteMsgApplication.context, s?.toString(), Toast.LENGTH_SHORT).show()
                    if (s?.length!! >= 2) {
                        search(s.toString())
                    }
                } else {
                    initIfVisible()
                    searchResultList.clear()
                    adapter.notifyDataSetChanged()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // 设置控件初始可见性
    private fun initIfVisible() {
        searchTipText.visibility = View.GONE
        clearImageButton.visibility = View.GONE
    }

    // 清空输入框
    @SuppressLint("NotifyDataSetChanged")
    private fun clearEditText(adapter: SearchResultItemAdapter) {
        searchEditText.setText("")
        searchResultList.clear()
        adapter.notifyDataSetChanged()
    }

    // 输入框查询
    private fun search(query: String) {
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("title", query)
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/searchStory")
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
            searchResultList.clear()
            val gson = Gson()
            val searchResponse = gson.fromJson(jsonData, SearchResponse::class.java)
            if (searchResponse.flag == true) {
                searchResultList.addAll(searchResponse.data)
                adapter.notifyDataSetChanged()
            }
            println(searchResponse)
            if (searchResultList.isNotEmpty()) {
                searchTipText.setText(R.string.search_exist_result)
            } else {
                searchTipText.setText(R.string.search_no_result)
            }
        }
    }
}