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
import com.example.eshc_scanner.adapters.FireItemAdapter
import com.example.eshc_scanner.databinding.FragmentSplashBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var adapterFireItem:  FireItemAdapter<Items, FireItemAdapter.ItemViewHolder>
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mToolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        bottomNavigationView.visibility = View.GONE
        return mBinding.root
    }

    private fun getData() {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val itemList = REPOSITORY_ROOM.getItem()
                Log.d(TAG, "itemList: + ${itemList.size} ")

                if (itemList.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        if (itemList.isNotEmpty()) {
                            APP_ACTIVITY.navController.navigate(R.id.action_splashFragment_to_mainFragment)
                        }
                    }
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    e.message?.let { showToast(it) }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initialise()
        getData()
        Log.d(TAG, "start: $javaClass")
    }

    private fun initialise() {
        mRecyclerView = mBinding.rvFragmentView
        mToolbar = mBinding.fragmentViewToolbar
        mToolbar.setupWithNavController(findNavController())

        val query = collectionITEMS_REF
            .orderBy("objectName", Query.Direction.ASCENDING)
        optionsItems = FirestoreRecyclerOptions.Builder<Items>()
            .setQuery(query, Items::class.java)
            .build()

        adapterFireItem = FireItemAdapter(optionsItems)
        mRecyclerView.adapter = adapterFireItem
        adapterFireItem.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapterFireItem.stopListening()
        Log.d(TAG, "stop: $javaClass")
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
                setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            Log.d(TAG, "Splash: + ${item.entity_id} + ${item.state}")
                            item.state = stateSelected

                            REPOSITORY_ROOM.insertItem(item)

                            val bundle = Bundle()
                            bundle.putSerializable("item", item)

                            Log.d(TAG, "Splash: + entityID- ${item.entity_id} +state- ${item.state} + name-${item.objectName} + " +
                                    "kurator-${item.kurator} + phone-${item.objectPhone}")
                            withContext(Dispatchers.Main) {
                                APP_ACTIVITY.navController.navigate(
                                    R.id.action_splashFragment_to_mainFragment,
                                    bundle
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
                setNegativeButton("Нет") {_, _ ->
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}