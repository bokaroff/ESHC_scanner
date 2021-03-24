package com.example.eshc_scanner.screens

import android.content.Intent
import android.os.Bundle
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
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        tvName = mBinding.mainFragmentTextViewName
        tvTime = mBinding.mainFragmentTextViewTime
        btnSend = mBinding.btnSend

        setHasOptionsMenu(true)

        val list = arguments?.getStringArrayList("data")
        tvName.text = list?.get(0) ?: ""
        tvTime.text = list?.get(1) ?: ""

        if(tvName.text.isNotEmpty()){
            btnSend.isEnabled = true
        }

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialise()
        getMainItem()
        btnQR.setOnClickListener { initScanner() }
    }

    private fun initialise() {
        mToolbar = mBinding.mainFragmentToolbar
        btnQR = mBinding.btnQR
        APP_ACTIVITY.setSupportActionBar(mToolbar)
    }


    private fun getMainItem() {
        mObserveList = Observer {
            for (i in it){
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
                    APP_ACTIVITY.navController.navigate(R.id.action_mainFragment_to_historyFragment)
            }
        }

        return super.onOptionsItemSelected(menu_item)
    }

    private fun initScanner() {
        IntentIntegrator.forSupportFragment(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode,data )

        if (result !=null){
            if (result.contents == null){
                showToast("Отмена")
            }else {

                val stringTime = SimpleDateFormat("HH:mm, dd MMM.yyyy", Locale.getDefault())
                    .format(Date())

                val dataList = ArrayList<String>()
                dataList.add(result.contents)
                dataList.add(stringTime)

                val bundle = Bundle()
                    bundle.putStringArrayList("data", dataList)
                APP_ACTIVITY.navController.navigate(R.id.action_global_mainFragment, bundle)
               // showToast("результат ${result.contents}")
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mViewModel.selectedItem.removeObserver(mObserveList)
        mToolbar.title = ""
    }
}

