package com.example.eshc_scanner.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
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

    private var timeStartBeforeMidnight: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeStartAfterMidnight: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEndBeforeMidnight: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEndAfterMidnight: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeRangeBeforeMidnight: Boolean = false
    private var timeRangeAfterMidnight: Boolean = false

    private var timeStart02: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEnd02: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeRange02: Boolean = false

    private var timeStart04: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEnd04: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeRange04: Boolean = false

    private var timeStart06: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEnd06: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeRange06: Boolean = false

    private var timeStart08: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEnd08: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeRange08: Boolean = false

    private var timeStart15: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEnd15: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeRange15: Boolean = false

    private var timeStart21: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEnd21: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeRange21: Boolean = false
    private var currentTime: Date = Date()

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!

    private var mSelectedItem = Items()
    private var mString = ""

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var tvName: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnSend: Button
    private lateinit var btnQR: ImageButton
    private lateinit var mSnack: Snackbar
    private lateinit var txt00: TextView
    private lateinit var txt02: TextView
    private lateinit var txt04: TextView
    private lateinit var txt06: TextView
    private lateinit var txt08: TextView
    private lateinit var txt15: TextView
    private lateinit var txt21: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        setHasOptionsMenu(true)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSnack = Snackbar
            .make(view, "Проверьте наличие интернета", Snackbar.LENGTH_INDEFINITE)

        val v: View = mSnack.view
        val txt = v.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        txt.textAlignment = View.TEXT_ALIGNMENT_CENTER

    }

    override fun onStart() {
        super.onStart()
        initialise()
        setCurrentTime()
        checkTimeRanges()
        getSelectedItem()
        btnQR.setOnClickListener { initScanner() }

        tvName.text = arguments?.getString("data") ?: ""

        if (tvName.text.isNotEmpty()) {

            val stringTime = SimpleDateFormat("HH:mm, dd MMM.yyyy", Locale.getDefault())
                .format(Date())

            tvTime.text = stringTime
            btnSend.isEnabled = true

            mSelectedItem.worker08 = tvName.text.toString()

            btnSend.setOnClickListener {
                if (timeRange08 || timeRange15 || timeRange21 || timeRangeBeforeMidnight || timeRangeAfterMidnight
                    || timeRange02 || timeRange04 || timeRange06)
                {
                    sendData(mSelectedItem)
                } else {
                    showToast("У вас другое время доклада")
                }
            }
        }
    }

    private fun initialise() {
        tvName = mBinding.mainFragmentTextViewName
        tvTime = mBinding.mainFragmentTextViewTime
        mToolbar = mBinding.mainFragmentToolbar
        btnSend = mBinding.btnSend
        btnQR = mBinding.btnQR
        txt00 = mBinding.txt00
        txt02 = mBinding.txt02
        txt04 = mBinding.txt04
        txt06 = mBinding.txt06
        txt08 = mBinding.txt08
        txt15 = mBinding.txt15
        txt21 = mBinding.txt21
        APP_ACTIVITY.setSupportActionBar(mToolbar)
    }

    private fun setCurrentTime() {
        timeStartBeforeMidnight.set(Calendar.HOUR_OF_DAY, 23)
        timeStartBeforeMidnight.set(Calendar.MINUTE, 40)
        timeStartBeforeMidnight.set(Calendar.SECOND, 0)
        timeEndBeforeMidnight.set(Calendar.HOUR_OF_DAY, 23)
        timeEndBeforeMidnight.set(Calendar.MINUTE, 59)
        timeEndBeforeMidnight.set(Calendar.SECOND, 59)

        timeStartAfterMidnight.set(Calendar.HOUR_OF_DAY, 0)
        timeStartAfterMidnight.set(Calendar.MINUTE, 0)
        timeStartAfterMidnight.set(Calendar.SECOND, 0)
        timeEndAfterMidnight.set(Calendar.HOUR_OF_DAY, 0)
        timeEndAfterMidnight.set(Calendar.MINUTE, 30)
        timeEndAfterMidnight.set(Calendar.SECOND, 0)

        timeStart02.set(Calendar.HOUR_OF_DAY, 2)
        timeStart02.set(Calendar.MINUTE, 40)
        timeStart02.set(Calendar.SECOND, 0)
        timeEnd02.set(Calendar.HOUR_OF_DAY, 3)
        timeEnd02.set(Calendar.MINUTE, 30)
        timeEnd02.set(Calendar.SECOND, 0)

        timeStart04.set(Calendar.HOUR_OF_DAY, 3)
        timeStart04.set(Calendar.MINUTE, 40)
        timeStart04.set(Calendar.SECOND, 0)
        timeEnd04.set(Calendar.HOUR_OF_DAY, 4)
        timeEnd04.set(Calendar.MINUTE, 30)
        timeEnd04.set(Calendar.SECOND, 0)

        timeStart06.set(Calendar.HOUR_OF_DAY, 5)
        timeStart06.set(Calendar.MINUTE, 40)
        timeStart06.set(Calendar.SECOND, 0)
        timeEnd06.set(Calendar.HOUR_OF_DAY, 6)
        timeEnd06.set(Calendar.MINUTE, 30)
        timeEnd06.set(Calendar.SECOND, 0)

        timeStart08.set(Calendar.HOUR_OF_DAY, 7)
        timeStart08.set(Calendar.MINUTE, 0)
        timeStart08.set(Calendar.SECOND, 0)
        timeEnd08.set(Calendar.HOUR_OF_DAY, 11)
        timeEnd08.set(Calendar.MINUTE, 30)
        timeEnd08.set(Calendar.SECOND, 0)

        timeStart15.set(Calendar.HOUR_OF_DAY, 14)
        timeStart15.set(Calendar.MINUTE, 40)
        timeStart15.set(Calendar.SECOND, 0)
        timeEnd15.set(Calendar.HOUR_OF_DAY, 15)
        timeEnd15.set(Calendar.MINUTE, 30)
        timeEnd15.set(Calendar.SECOND, 0)

        timeStart21.set(Calendar.HOUR_OF_DAY, 20)
        timeStart21.set(Calendar.MINUTE, 40)
        timeStart21.set(Calendar.SECOND, 0)
        timeEnd21.set(Calendar.HOUR_OF_DAY, 23)
        timeEnd21.set(Calendar.MINUTE, 0)
        timeEnd21.set(Calendar.SECOND, 0)
    }

    private fun checkTimeRanges() {
        currentTime = Calendar.getInstance(Locale.getDefault()).time

        if ((currentTime.after(timeStart08.time)) && (currentTime.before(timeEnd08.time))) {
            timeRange08 = true
        }

        if ((currentTime.after(timeStart15.time)) && (currentTime.before(timeEnd15.time))) {
            timeRange15 = true
        }

        if ((currentTime.after(timeStart21.time)) && (currentTime.before(timeEnd21.time))) {
            timeRange21 = true
        }

        if ((currentTime.after(timeStart02.time)) && (currentTime.before(timeEnd02.time))) {
            timeRange02 = true
        }

        if ((currentTime.after(timeStart04.time)) && (currentTime.before(timeEnd04.time))) {
            timeRange04 = true
        }

        if ((currentTime.after(timeStart06.time)) && (currentTime.before(timeEnd06.time))) {
            timeRange06 = true
        }

        if ((currentTime.after(timeStartBeforeMidnight.time))
            && (currentTime.before(timeEndBeforeMidnight.time))
        ) {
            timeRangeBeforeMidnight = true
        }

        if ((currentTime.after(timeStartAfterMidnight.time))
            && (currentTime.before(timeEndAfterMidnight.time))
        ) {
            timeRangeAfterMidnight = true
        }
    }


    private fun getSelectedItem() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mSelectedItem = REPOSITORY_ROOM.getSelectedItem()[0]

                withContext(Dispatchers.Main) {
                    mToolbar.title = mSelectedItem.objectName

                    if (mSelectedItem.order00 == "true"){
                        txt00.background = ResourcesCompat.getDrawable(resources, R.drawable.text_active, null)
                    }

                    if (mSelectedItem.order02 == "true"){
                        txt02.background = ResourcesCompat.getDrawable(resources, R.drawable.text_active, null)
                    }

                    if (mSelectedItem.order04 == "true"){
                        txt04.background = ResourcesCompat.getDrawable(resources, R.drawable.text_active, null)
                    }

                    if (mSelectedItem.order06 == "true"){
                        txt06.background = ResourcesCompat.getDrawable(resources, R.drawable.text_active, null)
                    }

                    if (mSelectedItem.order08 == "true"){
                        txt08.background = ResourcesCompat.getDrawable(resources, R.drawable.text_active, null)
                    }

                    if (mSelectedItem.order15 == "true"){
                        txt15.background = ResourcesCompat.getDrawable(resources, R.drawable.text_active, null)
                    }

                    if (mSelectedItem.order21 == "true"){
                        txt21 .background = ResourcesCompat.getDrawable(resources, R.drawable.text_active, null)
                    }
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
                        mSelectedItem.state = stateMain
                        REPOSITORY_ROOM.updateMainItem(mSelectedItem)

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
                arguments = null
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

                ITEM.objectName = item.objectName
                ITEM.kurator = item.kurator
                ITEM.worker08 = item.worker08
                ITEM.serverTimeStamp = item.serverTimeStamp
                ITEM.state = stateChanged

                REPOSITORY_ROOM.insertItem(ITEM)

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




