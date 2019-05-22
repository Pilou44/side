package com.wechantloup.side.modules.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wechantloup.side.R
import kotlinx.android.synthetic.main.item_toilets.view.*

class ListAdapter(private val mPresenter: ListContract.Presenter): RecyclerView.Adapter<ListAdapter.ToiletsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToiletsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_toilets, parent, false)
        return ToiletsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return when (mPresenter.getToiletsList()) {
            null -> 0
            else -> mPresenter.getToiletsList()!!.size
        }
    }

    override fun onBindViewHolder(holder: ToiletsViewHolder, position: Int) {
        val toilets =  mPresenter.getToiletsList()!![position]
        holder.itemView.address.text = toilets.getAddress()
        holder.itemView.administrator.text = toilets.getAdministrator()
        holder.itemView.opening_time.text = toilets.getOpening()
    }

    inner class ToiletsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}
