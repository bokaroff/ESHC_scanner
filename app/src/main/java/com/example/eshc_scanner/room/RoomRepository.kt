package com.example.eshc_scanner.room

import androidx.lifecycle.LiveData
import com.example.eshc_scanner.model.Items


class RoomRepository(private val itemRoomDao: ItemRoomDao) {

    val allChangedItems: LiveData<List<Items>>
        get() = itemRoomDao.getAllChangedItems()




  suspend  fun getSelectedItem (): List<Items>{
        return itemRoomDao.getSelectedItem()
    }

    suspend fun getMainItemList(): List<Items> {
        return itemRoomDao.getMainItemList()
    }

  //  suspend fun getMainItem(): Items {
    //    return itemRoomDao.getMainItemList()
  //  }

    suspend fun getMainItemList08(): List<Items> {
        return itemRoomDao.getMainItemList08()
    }

    suspend fun getMainItemList15(): List<Items> {
        return itemRoomDao.getMainItemList15()
    }

    suspend fun getMainItemList21(): List<Items> {
        return itemRoomDao.getMainItemList21()
    }

    suspend fun getMainItemList00(): List<Items> {
        return itemRoomDao.getMainItemList00()
    }

    suspend fun getMainItemList02(): List<Items> {
        return itemRoomDao.getMainItemList02()
    }

    suspend fun getMainItemList04(): List<Items> {
        return itemRoomDao.getMainItemList04()
    }

    suspend fun getMainItemList06(): List<Items> {
        return itemRoomDao.getMainItemList06()
    }

    suspend fun getAllChangedItemsWhereTimeBetween(timeStart: Long, timeEnd: Long): List<Items> {
        return itemRoomDao.getAllChangedItemsWhereTimeBetween(timeStart, timeEnd)
    }






    suspend fun insertItem(item: Items) {
        itemRoomDao.insertItem(item)
    }




    suspend fun insertItemList(list: MutableList<Items>) {
        itemRoomDao.insertItemList(list)
    }




    suspend fun deleteMainItem(item_id: String){
        itemRoomDao.deleteMainItem(item_id)
    }
}

