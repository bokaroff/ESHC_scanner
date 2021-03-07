package com.example.eshc_scanner

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var adapterItem: AdapterItems
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

    override fun onStart() {
        super.onStart()
        initialise()
        getMainItemsList()
        Log.d(TAG, "start: $javaClass")
    }

    private fun initialise() {
        mRecyclerView = mBinding.rvFragmentView
        mToolbar = mBinding.fragmentViewToolbar
        mToolbar.setupWithNavController(findNavController())
        adapterItem = AdapterItems()
        mRecyclerView.adapter = adapterItem
    }

    private fun getMainItemsList() {
        val itemsList = mutableListOf<Items>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val itemsData = REPOSITORY_ROOM.getMainItemList()

                if (itemsData.isEmpty()) {

                    val querySnapshot = collectionITEMS_REF
                        .orderBy("objectName", Query.Direction.ASCENDING)
                        .get().await()

                    for (documentSnapShot in querySnapshot) {
                        val item = documentSnapShot.toObject(Items::class.java)
                        item.item_id = documentSnapShot.id
                        itemsList.add(item)
                    }

                    REPOSITORY_ROOM.insertItemList(itemsList)

                    withContext(Dispatchers.Main){
                        adapterItem.setList(itemsList)
                    }

                    Log.d(TAG, "new loaded data: + ${itemsList.size} + ")
                } else {
                    withContext(Dispatchers.Main){
                        adapterItem.setList(itemsData)
                        Log.d(TAG, "from saved data: + ${itemsData.size}")
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
            val bundle = Bundle()
            bundle.putSerializable("item", item)

            APP_ACTIVITY.navController.navigate(R.id.action_splashFragment_to_objectSetUpFragment, bundle)



//        showToast(item.objectName)
        }
    }
}