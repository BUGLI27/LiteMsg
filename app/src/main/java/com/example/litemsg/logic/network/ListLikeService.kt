package com.example.litemsg.logic.network

import retrofit2.Call
import com.example.litemsg.logic.model.ListLikeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ListLikeService {

    @GET("listLike")
    fun getListLike(@Query("uid") uid: Int): Call<ListLikeResponse>

}