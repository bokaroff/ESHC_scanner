package com.example.eshc_scanner.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eshc_scanner.R
import com.example.eshc_scanner.databinding.FragmentMainBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.*
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : androidx.fragment.app.Fragment() {

    private var timeStart02: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEnd02: Calendar = Calendar.getInstance(Locale.getDefault())
    private var currentTime: Date = Date()
    private var timeRange02: Boolean = false

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!

    // private var mSelectedItem = Items()
    private var mString = ""

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    lateinit var tvName: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnSend: Button
    private lateinit var btnQR: ImageButton
    private lateinit var mSnack: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate MainFragment")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        Log.d(TAG, "onCreateView MainFragment")

        setHasOptionsMenu(true)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated MainFragment")

        mSnack = Snackbar
            .make(view, "Проверьте наличие интернета", Snackbar.LENGTH_INDEFINITE)

        val v: View = mSnack.view
        val txt = v.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        txt.textAlignment = View.TEXT_ALIGNMENT_CENTER

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart MainFragment")
        initialise()
        setCurrentTime()
        getSelectedItem()
        btnQR.setOnClickListener { initScanner() }

        tvName.text = arguments?.getString("data") ?: ""

        if (tvName.text.isNotEmpty()) {

            btnSend.isEnabled = true

            val item = ITEM.copy()
            item.worker08 = tvName.text.toString()
          //  item.savedToRoom = "true"

            btnSend.setOnClickListener {
                currentTime = Calendar.getInstance(Locale.getDefault()).time
                if ((currentTime.after(timeStart02.time)) && (currentTime.before(timeEnd02.time))) {
                    sendData(item)
                } else {
                    showToast("У вас другое время доклада")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }

    private fun initialise() {
        tvName = mBinding.mainFragmentTextViewName
        tvTime = mBinding.mainFragmentTextViewTime
        mToolbar = mBinding.mainFragmentToolbar
        btnSend = mBinding.btnSend
        btnQR = mBinding.btnQR
        APP_ACTIVITY.setSupportActionBar(mToolbar)
    }

    private fun setCurrentTime() {
        timeStart02.set(Calendar.HOUR_OF_DAY, 2)
        timeStart02.set(Calendar.MINUTE, 20)
        timeStart02.set(Calendar.SECOND, 0)
        timeEnd02.set(Calendar.HOUR_OF_DAY, 3)
        timeEnd02.set(Calendar.MINUTE, 30)
        timeEnd02.set(Calendar.SECOND, 0)
    }

    private fun getSelectedItem() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ITEM = REPOSITORY_ROOM.getSelectedItem()[0]

                withContext(Dispatchers.Main) {
                    mToolbar.title = ITEM.objectName
                   // showToast("${ITEM.state} + ${ITEM.objectName}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.message?.let { showToast(it) }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(menu_item: MenuItem): Boolean {
        when (menu_item.itemId) {
            R.id.mainFragmentMenuItem -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        ITEM.state = stateMain
                        REPOSITORY_ROOM.updateMainItem(ITEM)

                        withContext(Dispatchers.Main) {
                            APP_ACTIVITY.navController.navigate(R.id.action_mainFragment_to_splashFragment)
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            e.message?.let { showToast(it) }
                        }
                    }
                }
            }
            R.id.mainFragmentMenuHistory -> {
                APP_ACTIVITY.navController.navigate(R.id.action_mainFragment_to_historyFragment)
            }
        }

        return super.onOptionsItemSelected(menu_item)
    }

    private fun initScanner() {
        IntentIntegrator.forSupportFragment(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                showToast("Отмена")
            } else {
                val string = result.contents
                val bundle = Bundle()
                bundle.putString("data", string)
                APP_ACTIVITY.navController.navigate(R.id.action_global_mainFragment, bundle)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun sendData(item: Items) {

        val stringTime = SimpleDateFormat("HH:mm, dd MMM.yyyy", Locale.getDefault())
            .format(Date())
        val id = item.item_id
        val currentTimeLongType = Calendar.getInstance(Locale.getDefault()).time.time

        item.serverTimeStamp = stringTime
        item.itemLongTime = currentTimeLongType

        CoroutineScope(Dispatchers.IO).launch {
            try {
                collectionITEMS_REF.document(id)
                    .update(
                        "worker08", item.worker08,
                        "serverTimeStamp", stringTime,
                        "itemLongTime", currentTimeLongType
                    ).await()
                item.savedToRoom = "true"
                REPOSITORY_ROOM.insertItem(item)

                withContext(Dispatchers.Main) {
                    tvName.text = ""
                    tvTime.text = ""
                    btnSend.isEnabled = false
                    showToast("Выполнено!")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.message?.let { showToast(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "onDestroyView MainFragment")
    }
}




