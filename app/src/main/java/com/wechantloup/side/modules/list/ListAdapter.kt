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
        val toilet =  mPresenter.getToiletsList()!![position]
        holder.itemView.address.text = toilet.getAddress()
        holder.itemView.administrator.text = toilet.getAdministrator()
        holder.itemView.opening_time.text = toilet.getOpening()
        holder.itemView.favorite.isChecked = toilet.isFavorite
        holder.itemView.favorite.setOnClickListener { mPresenter.setFavorite(toilet)}

        if (toilet.distanceToMe >= 0) {
            holder.itemView.distance.visibility = View.VISIBLE
            val distance = holder.itemView.context.getString(R.string.distance, toilet.distanceToMe.toInt())
            holder.itemView.distance.text = distance
        } else {
            holder.itemView.distance.visibility = View.GONE
        }

        holder.itemView.setOnClickListener{ mPresenter.showDetails(toilet)}
    }

    inner class ToiletsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}
