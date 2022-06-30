package com.example.litemsg.ui.read

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.litemsg.logic.Repository
import com.example.litemsg.logic.model.Story

class ReadViewModel : ViewModel() {

    private val getListLikeLiveData = MutableLiveData<Int>()

    val listLike = ArrayList<Story>()

    val listLikeLiveData = Transformations.switchMap(getListLikeLiveData) { uid ->
        Repository.getListLike(uid)
    }

    fun getListLike(uid: Int) {
        getListLikeLiveData.value = uid
    }

}