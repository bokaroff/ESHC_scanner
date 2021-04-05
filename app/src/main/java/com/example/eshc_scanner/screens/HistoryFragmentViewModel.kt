package com.example.eshc_scanner.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.eshc_scanner.utilits.REPOSITORY_ROOM

class HistoryFragmentViewModel(application: Application): AndroidViewModel(application) {
   // val allChangedItems = REPOSITORY_ROOM.allChangedItems
    val allSavedToRoomItems = REPOSITORY_ROOM.savedToRoomItems
}