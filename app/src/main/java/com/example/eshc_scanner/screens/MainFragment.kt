package com.example.eshc_scanner.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eshc_scanner.R
import com.example.eshc_scanner.databinding.FragmentMainBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.SetOptions
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : androidx.fragment.app.Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: MainFragmentViewModel
    private lateinit var mObserveList: Observer<List<Items>>
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var tvName: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnSend: Button
    private lateinit var btnQR: ImageButton
    private lateinit var itemSaved: Items
    private lateinit var mSnack: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        Log.d(TAG, "onCreateView MainFragment")

        tvName = mBinding.mainFragmentTextViewName
        tvTime = mBinding.mainFragmentTextViewTime
        btnSend = mBinding.btnSend

        setHasOptionsMenu(true)

        tvName.text = arguments?.getString("data") ?: ""

        if (tvName.text.isNotEmpty()) {
            btnSend.isEnabled = true
            itemSaved = ITEM
            btnSend.setOnClickListener{sendData(itemSaved)}

        }
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
        getMainItem()
        btnQR.setOnClickListener { initScanner() }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume MainFragment")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop MainFragment")
    }

    private fun initialise() {
        mToolbar = mBinding.mainFragmentToolbar
        btnQR = mBinding.btnQR
        APP_ACTIVITY.setSupportActionBar(mToolbar)
    }

    private fun getMainItem() {
        mObserveList = Observer {
            for (i in it) {
                ITEM = i
                val name = i.objectName
                mToolbar.title = name
            }
        }

        mViewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        mViewModel.selectedItem.observe(this, mObserveList)
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
                ITEM.worker08 = result.contents
                val mScanResult = result.contents
                val bundle = Bundle()
                bundle.putString("data", mScanResult)

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

        item.serverTimeStamp = stringTime

        Log.d(
            TAG, "${item.serverTimeStamp} + ${item.worker08}  + ${item.objectName} " +
                    "+ ${item.state}"
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                collectionITEMS_REF.document(id)
                    .update(
                        "worker08", item.worker08,
                        "serverTimeStamp", stringTime
                    ).await()

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
        Log.d(TAG, "onDestroyView MainFragment")
        _binding = null
        mViewModel.selectedItem.removeObserver(mObserveList)
        mToolbar.title = ""
    }
}

