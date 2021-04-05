package com.example.eshc_scanner

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc_scanner.adapters.AdapterItems
import com.example.eshc_scanner.databinding.FragmentSplashBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.*
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mAdapter: AdapterItems
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mToolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialise()
        getData()
        Log.d(TAG, "start: $javaClass")
    }

    private fun initialise() {
        mAdapter = AdapterItems()
        mRecyclerView = mBinding.rvFragmentView
        mToolbar = mBinding.fragmentViewToolbar
        mToolbar.setupWithNavController(findNavController())
        mRecyclerView.adapter = mAdapter
    }

    private fun getData() {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val selectedItemList = REPOSITORY_ROOM.getSelectedItem()
                Log.d(TAG, "if already item exist: + ${selectedItemList.size} ")

                for (i in selectedItemList){
                    Log.d(TAG, "${i.entity_id} + ${i.objectName}")
                }

                if (selectedItemList.isNotEmpty()) {

                    withContext(Dispatchers.Main) {
                        APP_ACTIVITY.navController.navigate(R.id.action_global_mainFragment)
                    }
                } else {
                    Log.d(TAG, " item doesnt exist: + ${selectedItemList.size} ")
                    val querySnapshot = collectionITEMS_REF
                        .orderBy("objectName", Query.Direction.ASCENDING)
                        .get().await()

                    val itemsList = querySnapshot.toObjects(Items::class.java)
                    withContext(Dispatchers.Main) {
                        mAdapter.setList(itemsList)
                    }
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
        mRecyclerView.adapter = null
    }

    companion object {
        fun companionClick(item: Items) {

            val builder = AlertDialog.Builder(APP_ACTIVITY)

            builder.apply {
                setMessage("Что ваш объект ${item.objectName} ")
                setTitle("Вы уверены?")
                setPositiveButton("Да") { _: DialogInterface, _: Int ->

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            Log.d(TAG, "Splash: + ${item.entity_id} + ${item.state}")
                            item.state = stateSelected

                            REPOSITORY_ROOM.insertItem(item)

                            Log.d(
                                TAG,
                                "first time select item: - ${item.entity_id} +state- ${item.state} + name-${item.objectName} + " +
                                        "kurator-${item.kurator} + phone-${item.objectPhone}"
                            )
                            withContext(Dispatchers.Main) {
                                APP_ACTIVITY.navController.navigate(
                                    R.id.action_splashFragment_to_mainFragment
                                )
                                showToast("Объект ${item.objectName} сохранен")
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                e.message?.let { showToast(it) }
                            }
                        }
                    }
                }
                setNegativeButton("Нет") { _, _ ->
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}