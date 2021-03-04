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

class AdapterItems() : RecyclerView.Adapter<AdapterItems.SimpleViewHolder>() {
    private lateinit var mContext: Context
    private var mList = mutableListOf<Items>()


    override fun onViewAttachedToWindow(holder: SimpleViewHolder) {

        holder.rvItemContainer.setOnClickListener {
            val item = mList[holder.adapterPosition]
            SplashFragment.companionClick(item)
        }
    }

    override fun onViewDetachedFromWindow(holder: SimpleViewHolder) {
        holder.rvItemContainer.setOnClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = SimpleViewHolder(
            RvItemMiniBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        mContext = parent.context
        return view
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.rvItemContainer.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation)

        holder.objectName.text = mList[position].objectName
        holder.kurator.text = mList[position].kurator
        holder.objectPhone.text = mList[position].objectPhone
        holder.mobilePhone.text = mList[position].mobilePhone
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class SimpleViewHolder(binding: RvItemMiniBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val objectName: TextView = binding.objectNameMini
        val kurator: TextView = binding.kuratorMini
        val objectPhone: TextView = binding.phoneMini
        val mobilePhone: TextView = binding.mobileMini
        val rvItemContainer: ConstraintLayout = binding.rvItemContainerMini
    }

    fun setList(list: List<Items>) {
        mList = list.toMutableList()
        // Log.d(TAG, "AdaptermainItemList08:  ${mList.size}")
        notifyDataSetChanged()
    }

    fun removeItem(position: Int, item: Items, mutableList: MutableList<Items>) {
        mList.remove(item)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mutableList.size)
    }
}