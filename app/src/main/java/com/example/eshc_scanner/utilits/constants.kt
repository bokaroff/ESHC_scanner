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
const val getAllChangedItems = "SELECT * FROM items_table WHERE state = 'changed'"

const val getSelectedItem =
    "SELECT * FROM items_table WHERE state = 'selected'"

const val stateSelected = "selected"
const val stateMain = "main"
const val stateChanged = "changed"

lateinit var APP_ACTIVITY: MainActivity
lateinit var ITEM: Items
lateinit var REPOSITORY_ROOM: RoomRepository
lateinit var ITEM_ROOM_DAO: ItemRoomDao
lateinit var ITEM_ROOM_DATABASE: ItemRoomDatabase
lateinit var connectionLiveData: ConnectionLiveData

val collectionITEMS_REF = FirebaseFirestore.getInstance()
    .collection("Items")