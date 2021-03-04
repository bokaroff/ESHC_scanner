package com.example.eshc_scanner

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.eshc_scanner.databinding.ActivityMainBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.room.ItemRoomDatabase
import com.example.eshc_scanner.room.RoomRepository
import com.example.eshc_scanner.utilits.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var navigationItemSelectedListener:
            BottomNavigationView.OnNavigationItemSelectedListener
    private lateinit var destinationChangedListener:
            NavController.OnDestinationChangedListener

    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navController: NavController

    private var _binding: ActivityMainBinding? = null
    val mBinding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initialise()
        setUpNavController()
    }

    private fun initialise() {
        APP_ACTIVITY = this
        DB = FirebaseFirestore.getInstance()
        ITEM = Items()
        ITEM_ROOM_DATABASE = ItemRoomDatabase.getInstance(this)
        ITEM_ROOM_DAO = ITEM_ROOM_DATABASE.getItemRoomDao()
        REPOSITORY_ROOM = RoomRepository(ITEM_ROOM_DAO)
    }

    private fun setUpNavController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView = mBinding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.itemIconTintList = null

        setOnNavigationItemSelectedListener()
        setOnDestinationChangedListener()

    }

    private fun setOnNavigationItemSelectedListener() {
        navigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.fragmentHome -> {

                    }
                    R.id.fragmentView -> {

                    }
                    R.id.fragmentGuard -> {

                    }
                    R.id.fragmentGuardLate -> {

                    }
                    R.id.fragmentItemRoom -> {

                    }
                }
                true
            }
    }

    private fun setOnDestinationChangedListener() {
        destinationChangedListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.splashFragment -> {


                    }
                }
            }
    }



    override fun onResume() {
        super.onResume()
        bottomNavigationView
            .setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onPause() {
        super.onPause()
        bottomNavigationView
            .setOnNavigationItemSelectedListener(null)
        navController.removeOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
       Log.d(TAG, " $localClassName stop: ")
    }

}