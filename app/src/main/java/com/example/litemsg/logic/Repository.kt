package com.example.litemsg.logic

import androidx.lifecycle.liveData
import com.example.litemsg.logic.model.ListLikeResponse
import com.example.litemsg.logic.model.Story
import com.example.litemsg.logic.network.LightNetwork
import kotlinx.coroutines.Dispatchers

object Repository {

    fun getListLike(uid: Int) = liveData(Dispatchers.IO) {
        val result = try {
            val listLikeResponse = LightNetwork.getListLike(uid)
            if (listLikeResponse.flag == true) {
                val data = listLikeResponse.data
                Result.success(data)
            } else {
                Result.failure(RuntimeException("response flag is ${listLikeResponse.flag}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Story>>(e)
        }
        emit(result)
    }

}