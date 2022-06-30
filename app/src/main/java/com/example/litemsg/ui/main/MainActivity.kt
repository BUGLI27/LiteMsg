package com.example.litemsg.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.example.litemsg.R
import com.example.litemsg.ui.BaseActivity
import com.example.litemsg.ui.collect.CollectActivity
import com.example.litemsg.ui.comment.CommentActivity
import com.example.litemsg.ui.like.LikeActivity
import com.example.litemsg.ui.read.ReadActivity
import com.example.litemsg.ui.search.SearchActivity
import com.example.litemsg.ui.story.StoryActivity

class MainActivity : BaseActivity() {

    lateinit var indexBgImage: ImageView
    lateinit var indexButton: Button
    lateinit var readBtn: ImageButton
    lateinit var collectBtn: ImageButton
    lateinit var likeBtn: ImageButton
    lateinit var commentBtn: ImageButton
    lateinit var searchBtn: ImageButton
    lateinit var settingImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 加载动画  首页按钮
        indexButton = findViewById(R.id.indexButton)
        val loadAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale)
        indexButton.animation = loadAnimation

        // 设置背景图
        indexBgImage = findViewById(R.id.indexBgImage)
        val updateTime = System.currentTimeMillis().toString()
        Glide.with(this).load("https://bing.img.run/rand_uhd.php")
            .signature(ObjectKey(updateTime))
            .placeholder(indexBgImage.drawable)
            .into(indexBgImage)

        // 切换图片
        indexBgImage.setOnClickListener {
            val updateTime = System.currentTimeMillis().toString()
            Glide.with(this).load("https://bing.img.run/rand_uhd.php")
                .signature(ObjectKey(updateTime))
                .placeholder(indexBgImage.drawable)
                .into(indexBgImage)
        }

        // 点击主按钮
        indexButton.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
        }
        // 点击已读
        readBtn = findViewById(R.id.readBtn)
        readBtn.setOnClickListener {
            val intent = Intent(this, ReadActivity::class.java)
            startActivity(intent)
        }
        // 点击收藏
        collectBtn = findViewById(R.id.collectBtn)
        collectBtn.setOnClickListener {
            val intent = Intent(this, CollectActivity::class.java)
            startActivity(intent)
        }
        // 点击喜欢
        likeBtn = findViewById(R.id.likeBtn)
        likeBtn.setOnClickListener {
            val intent = Intent(this, LikeActivity::class.java)
            startActivity(intent)
        }
        // 点击评论
        commentBtn = findViewById(R.id.commentBtn)
        commentBtn.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            startActivity(intent)
        }
        // 点击溯查
        searchBtn = findViewById(R.id.searchBtn)
        searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        // 点击设置
        settingImageButton = findViewById(R.id.settingImageButton)
        settingImageButton.setOnClickListener {
            Toast.makeText(this, "设置页 开发中", Toast.LENGTH_SHORT).show()
        }

    }
}