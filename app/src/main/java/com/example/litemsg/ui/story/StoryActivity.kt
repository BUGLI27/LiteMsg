package com.example.litemsg.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.litemsg.LiteMsgApplication
import com.example.litemsg.R
import com.example.litemsg.logic.model.IsLikeResponse
import com.example.litemsg.logic.model.StoryResponse
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import kotlin.concurrent.thread

class StoryActivity : AppCompatActivity() {

    private lateinit var storyLikeImage: ImageView
    private lateinit var storyCollectImage: ImageView
    private lateinit var likeLayout: LinearLayout
    private lateinit var storyTitleText: TextView
    private lateinit var storyAuthorText: TextView
    private lateinit var storyContentText: TextView
    private lateinit var storyImage: ImageView

    // 点赞状态
    private var liked: Boolean = false
    // 收藏状态
    private var collected: Boolean = false
    // 文章id
    private var eid: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        // 设置状态栏字体颜色
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //实现状态栏图标和文字颜色为暗色

        // 初始化
//        likeLayout = findViewById(R.id.likeLayout)
        storyLikeImage = findViewById(R.id.storyLikeImage)
        storyCollectImage = findViewById(R.id.storyCollectImage)
        storyTitleText = findViewById(R.id.storyTitleText)
        storyAuthorText = findViewById(R.id.storyAuthorText)
        storyContentText = findViewById(R.id.storyContentText)
        storyImage = findViewById(R.id.storyImage)
        getRandomStory()

        // 点击点赞按钮
        storyLikeImage.setOnClickListener {
            if (!liked) {
                // 点赞
                eid?.let { addLike(it) }
                // 替换图片，展示点赞动画
                val likeAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_out)
                storyLikeImage.setImageResource(R.drawable.like_red)
                storyLikeImage.startAnimation(likeAnimation)
                // 更改点赞状态
                liked = true
            } else {
                // 取消点赞
                eid?.let { delLike(it) }
                storyLikeImage.setImageResource(R.drawable.like_black)
                // 更改点赞状态
                liked = false
            }
        }

        // 点击收藏按钮
        storyCollectImage.setOnClickListener {
            if (!collected) {
                // 收藏
                eid?.let { addCollect(it) }
                // 替换图片，展示收藏动画
                val likeAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_out)
                storyCollectImage.setImageResource(R.drawable.collect_red)
                storyCollectImage.startAnimation(likeAnimation)
                // 更改收藏状态
                collected = true
            } else {
                // 取消收藏
                eid?.let { delCollect(it) }
                storyCollectImage.setImageResource(R.drawable.collect_black)
                // 更改收藏状态
                collected = false
            }
        }
    }

    // 随机获取文章
    private fun getRandomStory() {
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/getStory")
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

    private fun parseJsonWithGson(jsonData: String) {
        runOnUiThread {
            val gson = Gson()
            val storyResponse = gson.fromJson(jsonData, StoryResponse::class.java)
            println(storyResponse)
            storyTitleText.text = storyResponse.data?.title ?: "null"
            storyAuthorText.text = storyResponse.data?.author ?: "null"
            storyContentText.text = storyResponse.data?.content ?: "null"
            Glide.with(this).load(storyResponse.data?.picture).into(storyImage)
            eid = storyResponse.data?.eid
            getIsLike(storyResponse.data?.eid)
        }
    }

    // 判断是否点赞
    private fun getIsLike(eid: Int) {
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .add("eid", eid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/isLike")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {
                    parseIsLikeJsonWithGson(responseData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseIsLikeJsonWithGson(jsonData: String) {
        val gson = Gson()
        val isLikeResponse = gson.fromJson(jsonData, IsLikeResponse::class.java)
        print(isLikeResponse)
        if (isLikeResponse.flag) {
            val likeAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_out)
            storyLikeImage.setImageResource(R.drawable.like_red)
            storyLikeImage.startAnimation(likeAnimation)
            liked = true
        } else {
            // 取消点赞
            storyLikeImage.setImageResource(R.drawable.like_black)
            // 更改点赞状态
            liked = false
        }
    }

    // 点赞
    private fun addLike(eid: Int) {
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .add("eid", eid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/addLike")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 取消点赞
    private fun delLike(eid: Int) {
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .add("eid", eid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/delLike")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 判断是否收藏
    private fun getIsCollect(eid: Int) {
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .add("eid", eid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/isCollect")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {
                    parseIsCollectJsonWithGson(responseData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseIsCollectJsonWithGson(jsonData: String) {
        val gson = Gson()
        val isCollectResponse = gson.fromJson(jsonData, IsLikeResponse::class.java)
        print(isCollectResponse)
        if (isCollectResponse.flag) {
            val likeAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_out)
            storyCollectImage.setImageResource(R.drawable.collect_red)
            storyCollectImage.startAnimation(likeAnimation)
            collected = true
        } else {
            // 取消收藏
            storyCollectImage.setImageResource(R.drawable.collect_black)
            // 更改收藏状态
            collected = false
        }
    }

    // 收藏
    private fun addCollect(eid: Int) {
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .add("eid", eid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/addCollect")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 取消收藏
    private fun delCollect(eid: Int) {
        thread {
            try {
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("uid", LiteMsgApplication.uid.toString())
                    .add("eid", eid.toString())
                    .build()
                val request = Request.Builder()
                    .url("http://101.37.32.91:8080/delCollect")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}