package com.example.eshc_scanner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc_scanner.R
import com.example.eshc_scanner.SplashFragment
import com.example.eshc_scanner.databinding.RvItemMiniBinding
import com.example.eshc_scanner.model.Items
import com.example.eshc_scanner.utilits.showToast
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.imageview.ShapeableImageView


class FireItemAdapter<T, U>(options: FirestoreRecyclerOptions<Items>) :
    FirestoreRecyclerAdapter<Items, FireItemAdapter.ItemViewHolder>(options) {

    private lateinit var mContext: Context

    override fun onViewAttachedToWindow(holder: ItemViewHolder) {

        holder.rvItemContainer.setOnClickListener {
            val item = getItem(holder.adapterPosition)
            SplashFragment.companionClick(item)
        }
    }

    override fun onViewDetachedFromWindow(holder: ItemViewHolder) {
        holder.rvItemContainer.setOnClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemViewHolder(
            RvItemMiniBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        mContext = parent.context
        return view
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: Items) {

        holder.rvItemContainer.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation)

        holder.objectName.text = model.objectName
        holder.kurator.text = model.kurator
        holder.objectPhone.text = model.objectPhone
        holder.mobilePhone.text = model.mobilePhone
    }

    class ItemViewHolder(binding: RvItemMiniBinding) : RecyclerView.ViewHolder(binding.root) {
        val objectName: TextView = binding.objectNameMini
        val kurator: TextView = binding.kuratorMini
        val objectPhone: TextView = binding.phoneMini
        val mobilePhone: TextView = binding.mobileMini
        val rvItemContainer: ConstraintLayout = binding.rvItemContainerMini
    }
}