package com.example.eshc_scanner.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.*


@Dao
interface ItemRoomDao {

 @Query(getAllMainItems)
 fun getAllItems(): LiveData<List<Items>>

 @Query(getAllChangedItems)
 fun getAllChangedItems(): LiveData<List<Items>>

 @Query(getSelectedItem)
 fun getSelectedItem(): LiveData<List<Items>>

 @Query(getSelectedItem)
 suspend fun getItem(): List<Items>


 @Query(getAllChangedItemsWhereTimeBetween)
 suspend fun getAllChangedItemsWhereTimeBetween(timeStart: Long, timeEnd: Long): List<Items>



// @Query(getMainItem)
// suspend fun getMainItemList(): Items

 @Query(getAllMainItems)
 suspend fun getMainItemList(): List<Items>

 @Query(getMainItemList00)
 suspend fun getMainItemList00(): List<Items>

 @Query(getMainItemList02)
 suspend fun getMainItemList02(): List<Items>

 @Query(getMainItemList04)
 suspend fun getMainItemList04(): List<Items>

 @Query(getMainItemList06)
 suspend fun getMainItemList06(): List<Items>

 @Query(getMainItemList08)
 suspend fun getMainItemList08(): List<Items>

 @Query(getMainItemList15)
 suspend fun getMainItemList15(): List<Items>

 @Query(getMainItemList21)
 suspend fun getMainItemList21(): List<Items>



 @Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend fun insertItem(item: Items)


 @Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend fun insertItemList(list: MutableList<Items>)



 @Query("DELETE FROM items_table WHERE item_id =:item_id and state = 'main' ")
 suspend fun deleteMainItem(item_id: String)

 @Query(deleteSelectedItem)
 suspend fun deleteSelectedItem()



 @Update
 suspend fun updateMainItem(item: Items)
}

