package com.example.eshc_scanner.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.*


@Dao
interface ItemRoomDao {

 @Query(getAllChangedItems)
 fun getAllChangedItems(): LiveData<MutableList<Items>>

 @Query(getSelectedItem)
 suspend fun getSelectedItem(): List<Items>

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend fun insertItem(item: Items)

 @Update
 suspend fun updateMainItem(item: Items)
}

