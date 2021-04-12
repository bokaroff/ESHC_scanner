package com.example.eshc_scanner.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc_scanner.adapters.AdapterItemsSimple
import com.example.eshc_scanner.databinding.FragmentHistoryBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.TAG

class HistoryFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mToolbar: Toolbar
    private lateinit var mAdapter: AdapterItemsSimple
    private lateinit var mViewModel: HistoryFragmentViewModel
    private lateinit var mObserveList: Observer<List<Items>>

    private var _binding: FragmentHistoryBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialise()
        getData()
    }

    private fun initialise() {
        mAdapter = AdapterItemsSimple()
        mToolbar = mBinding.fragmentHistoryToolbar
        mRecyclerView = mBinding.rvFragmentHistory
    }

    private fun getData() {

        mObserveList = Observer {
            val list = it.asReversed()
            val mutableList = list.toMutableList()

            mAdapter.setList(mutableList)
            mRecyclerView.adapter = mAdapter
        }
        mViewModel = ViewModelProvider(this)
            .get(HistoryFragmentViewModel::class.java)
        mViewModel.allChangedItems.observe(this, mObserveList)
        mToolbar.setupWithNavController(findNavController())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.allChangedItems.removeObserver(mObserveList)
        mRecyclerView.adapter = null
        _binding = null
    }
}