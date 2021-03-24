package com.example.eshc_scanner.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc_scanner.R
import com.example.eshc_scanner.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mToolbar: Toolbar

    private var _binding: FragmentHistoryBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        initialise()
    }

    private fun initialise() {
        mToolbar = mBinding.fragmentHistoryToolbar
        mRecyclerView = mBinding.rvFragmentHistory
        mToolbar.setupWithNavController(findNavController())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}