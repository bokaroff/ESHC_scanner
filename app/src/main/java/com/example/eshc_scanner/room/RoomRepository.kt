package com.example.eshc_scanner.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eshc_scanner.model.Items


class RoomRepository(private val itemRoomDao: ItemRoomDao) {

    val allChangedItems: LiveData<MutableList<Items>>
        get() = itemRoomDao.getAllChangedItems()

    suspend  fun getSelectedItem (): List<Items>{
        return itemRoomDao.getSelectedItem()
    }

    suspend fun insertItem(item: Items) {
        itemRoomDao.insertItem(item)
    }

    suspend fun updateMainItem(item: Items){
        itemRoomDao.updateMainItem(item)
    }
}

