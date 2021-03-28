package com.example.eshc_scanner.screens

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.eshc_scanner.utilits.REPOSITORY_ROOM
import com.example.eshc_scanner.utilits.TAG
import com.example.eshc_scanner.utilits.showToast

class MainFragmentViewModel(application: Application): AndroidViewModel(application) {

        val selectedItem = REPOSITORY_ROOM.getSelectedItem


}