package com.example.eshc_scanner.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.eshc_scanner.utilits.REPOSITORY_ROOM

class MainFragmentViewModel(application: Application): AndroidViewModel(application) {

        val selectedItem = REPOSITORY_ROOM.getSelectedItem
}