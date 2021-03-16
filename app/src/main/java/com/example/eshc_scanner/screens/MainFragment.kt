package com.example.eshc_scanner.screens

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eshc_scanner.R
import com.example.eshc_scanner.databinding.FragmentMainBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainFragment : androidx.fragment.app.Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: MainFragmentViewModel
    private lateinit var mObserveList: Observer<List<Items>>
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        initialise()
        getMainItem()
    }

    private fun initialise() {
        mToolbar = mBinding.mainFragmentToolbar
        APP_ACTIVITY.setSupportActionBar(mToolbar)
        bottomNavigationView.visibility = View.VISIBLE
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

                        Log.d(
                            TAG,
                            "MainFragment_updated: + entityID- ${ITEM.entity_id} +state- ${ITEM.state} + name-${ITEM.objectName} + " +
                                    "kurator-${ITEM.kurator} + phone-${ITEM.objectPhone}"
                        )


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
        }
        return super.onOptionsItemSelected(menu_item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mViewModel.selectedItem.removeObserver(mObserveList)
        mToolbar.title = ""
    }

}

