package com.example.eshc_scanner.utilits

import com.example.eshc_scanner.MainActivity
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.room.ItemRoomDao
import com.example.eshc_scanner.room.ItemRoomDatabase
import com.example.eshc_scanner.room.RoomRepository
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

const val TAG = "ktx"
const val CAMERA_REQUEST_CODE = 101
const val getAllMainItems = "SELECT * FROM items_table WHERE state = 'main'"
const val getAllChangedItems = "SELECT * FROM items_table WHERE state = 'changed'"

const val getSelectedItem =
    "SELECT * FROM items_table WHERE state = 'selected'"
const val deleteSelectedItem =
"DELETE FROM items_table WHERE state = 'selected'"

const val getAllChangedItemsWhereTimeBetween =
    "SELECT * FROM items_table WHERE state = 'changed'and itemLongTime BETWEEN :timeStart and :timeEnd"

const val getMainItemList00 =
    "SELECT * FROM items_table WHERE order00 = 'true' AND state = 'main' ORDER BY objectName Asc"
const val getMainItemList02 =
    "SELECT * FROM items_table WHERE order02 = 'true' AND state = 'main' ORDER BY objectName Asc"
const val getMainItemList04 =
    "SELECT * FROM items_table WHERE order04 = 'true' AND state = 'main' ORDER BY objectName Asc"
const val getMainItemList06 =
    "SELECT * FROM items_table WHERE order06 = 'true' AND state = 'main' ORDER BY objectName Asc"
const val getMainItemList08 =
    "SELECT * FROM items_table WHERE order08 = 'true' AND state = 'main' ORDER BY objectName Asc"
const val getMainItemList15 =
    "SELECT * FROM items_table WHERE order15 = 'true' AND state = 'main' ORDER BY objectName Asc"
const val getMainItemList21 =
    "SELECT * FROM items_table WHERE order21 = 'true' AND state = 'main' ORDER BY objectName Asc"
const val stateSelected = "selected"
const val stateMain = "main"
const val stateSent = "sent"
const val state = "state"

lateinit var APP_ACTIVITY: MainActivity
lateinit var ITEM: Items
lateinit var REPOSITORY_ROOM: RoomRepository
lateinit var ITEM_ROOM_DAO: ItemRoomDao
lateinit var ITEM_ROOM_DATABASE: ItemRoomDatabase
lateinit var optionsItems: FirestoreRecyclerOptions<Items>
lateinit var connectionLiveData: ConnectionLiveData


val collectionITEMS_REF = FirebaseFirestore.getInstance()
    .collection("Items")