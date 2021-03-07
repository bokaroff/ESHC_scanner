package com.example.eshc_scanner.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eshc_scanner.R
import com.example.eshc_scanner.databinding.FragmentObjectSetUpBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.APP_ACTIVITY
import com.example.eshc_scanner.utilits.ITEM
import com.example.eshc_scanner.utilits.REPOSITORY_ROOM
import com.example.eshc_scanner.utilits.showToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ObjectSetUpFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentObjectSetUpBinding? = null
    private val mBinding get() = _binding!!
    private var emptyCheckBox = false

    private lateinit var mToolbar: Toolbar
    private lateinit var btnSave: Button
    private lateinit var checkBox08: CheckBox
    private lateinit var checkBox15: CheckBox
    private lateinit var checkBox21: CheckBox
    private lateinit var checkBox00: CheckBox
    private lateinit var checkBox02: CheckBox
    private lateinit var checkBox04: CheckBox
    private lateinit var checkBox06: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentObjectSetUpBinding.inflate(layoutInflater, container, false)
        ITEM = arguments?.getSerializable("item") as Items
        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        initialise()
        saveItem()
    }

    private fun initialise() {
        mBinding.fragmentObjectSetUpTextView.text = ITEM.objectName
        btnSave = mBinding.fragmentObjectSetUpButton
        checkBox08 = mBinding.fragmentObjectSetUpField08
        checkBox15 = mBinding.fragmentObjectSetUpField15
        checkBox21 = mBinding.fragmentObjectSetUpField21
        checkBox00 = mBinding.fragmentObjectSetUpField00
        checkBox02 = mBinding.fragmentObjectSetUpField02
        checkBox04 = mBinding.fragmentObjectSetUpField04
        checkBox06 = mBinding.fragmentObjectSetUpField06
        mToolbar = mBinding.fragmentObjectSetUpToolbar
        mToolbar.setupWithNavController(findNavController())
    }

    private fun setCheckBox(): Items {

        when {
            checkBox08.isChecked -> ITEM.order08 = "true"
            !checkBox08.isChecked -> ITEM.order08 = "false"
        }
        when {
            checkBox15.isChecked -> ITEM.order15 = "true"
            !checkBox15.isChecked -> ITEM.order15 = "false"
        }
        when {
            checkBox21.isChecked -> ITEM.order21 = "true"
            !checkBox21.isChecked -> ITEM.order21 = "false"
        }
        when {
            checkBox00.isChecked -> ITEM.order00 = "true"
            !checkBox00.isChecked -> ITEM.order00 = "false"
        }
        when {
            checkBox02.isChecked -> ITEM.order02 = "true"
            !checkBox02.isChecked -> ITEM.order02 = "false"
        }
        when {
            checkBox04.isChecked -> ITEM.order04 = "true"
            !checkBox04.isChecked -> ITEM.order04 = "false"
        }
        when {
            checkBox06.isChecked -> ITEM.order06 = "true"
            !checkBox06.isChecked -> ITEM.order06 = "false"
        }
        emptyCheckBox = false
        return ITEM
    }

    private fun getCheckBoxState() {
        if (!checkBox08.isChecked && !checkBox15.isChecked && !checkBox21.isChecked
            && !checkBox00.isChecked && !checkBox02.isChecked && !checkBox04.isChecked && !checkBox06.isChecked
        ) {
            emptyCheckBox = true
        }
    }

    private fun saveItem() {
        btnSave.setOnClickListener {

            val item = setCheckBox()
            getCheckBoxState()

            if (emptyCheckBox) {
                showToast("Выберите 1 пункт доклада")
                return@setOnClickListener

            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        REPOSITORY_ROOM.insertItem(item)

                        val bundle = Bundle()
                        bundle.putSerializable("item", item)

                        withContext(Dispatchers.Main) {
                            APP_ACTIVITY.navController.navigate(R.id.action_objectSetUpFragment_to_mainFragment, bundle)
                            showToast("Объект ${item.objectName} сохранен")
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            e.message?.let { showToast(it) }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}