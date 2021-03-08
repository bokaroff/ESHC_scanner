package com.example.eshc_scanner.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.eshc_scanner.databinding.FragmentMainBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.ITEM
import com.example.eshc_scanner.utilits.REPOSITORY_ROOM
import com.example.eshc_scanner.utilits.bottomNavigationView
import com.example.eshc_scanner.utilits.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : androidx.fragment.app.Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    private var name: Items? = null


    private lateinit var textViewTitle: TextView
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
          val aaa = arguments?.
             getSerializable("item")?: ITEM

      //  ITEM = aaa as Items
        name = aaa as Items


      //  val args = arguments?.getSerializable("item")



        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        initialise()
        getMainItem()

    }

    private fun getMainItem() {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = REPOSITORY_ROOM.getSelectedItem()
                for (item in list) {
                    withContext(Dispatchers.Main) {
                        textViewTitle.text = item.objectName
                    }
                }

            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    e.message?.let { showToast(it) }
                }
            }
        }
    }

    private fun initialise() {
        textViewTitle = mBinding.mainFragmentToolbarTitle

        mToolbar = mBinding.mainFragmentToolbar
      //  mToolbar.setupWithNavController(findNavController())
        textViewTitle.text = name?.objectName


        bottomNavigationView.visibility = View.VISIBLE
    }

}

