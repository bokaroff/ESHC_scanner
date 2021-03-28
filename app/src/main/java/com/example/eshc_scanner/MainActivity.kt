package com.example.eshc_scanner

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.eshc_scanner.databinding.ActivityMainBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.room.ItemRoomDatabase
import com.example.eshc_scanner.room.RoomRepository
import com.example.eshc_scanner.utilits.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    lateinit var DB: FirebaseFirestore
    lateinit var navController: NavController
    private lateinit var mSnack: Snackbar

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

    // @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)

        Log.d(TAG, " Mainactivity OnCreate ")
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        connectionLiveData = ConnectionLiveData(this)

        initialise()
        setUpNavController()
        checkForPermissions(android.Manifest.permission.CAMERA, "камеру", CAMERA_REQUEST_CODE)
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, " Mainactivity onStart ")
        checkNetWorkConnection()
    }

    private fun initialise() {
        APP_ACTIVITY = this
        DB = FirebaseFirestore.getInstance()
        ITEM = Items()
        ITEM_ROOM_DATABASE = ItemRoomDatabase.getInstance(this)
        ITEM_ROOM_DAO = ITEM_ROOM_DATABASE.getItemRoomDao()
        REPOSITORY_ROOM = RoomRepository(ITEM_ROOM_DAO)

        mSnack = Snackbar
            .make(mBinding.root, "Проверьте наличие интернета", Snackbar.LENGTH_INDEFINITE)

        val view: View = mSnack.view
        val txt = view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        txt.textAlignment = TEXT_ALIGNMENT_CENTER
    }

    private fun setUpNavController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showToast("Отказано в разрешении на $name")
            } else {
                showToast("Разрешение на $name получено")
            }
        }

        when (requestCode) {
            CAMERA_REQUEST_CODE -> innerCheck("использование камеры")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Необходимо разрешение на $name для использования этого приложения")
            setTitle("Необходимо разрешение!")
            setPositiveButton("Да") { _: DialogInterface, _: Int ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun checkNetWorkConnection() {
        connectionLiveData.checkValidNetworks()
        connectionLiveData.observe(this, { isNetWorkAvailable ->
            when (isNetWorkAvailable) {
                false -> mSnack.show()
                true -> mSnack.dismiss()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d(TAG, " $localClassName stop: ")
    }
}