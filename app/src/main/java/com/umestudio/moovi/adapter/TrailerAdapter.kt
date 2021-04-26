package com.umestudio.moovi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umestudio.moovi.R
import com.umestudio.moovi.model.TrailerModel
import kotlinx.android.synthetic.main.item_trailer.view.*

class TrailerAdapter(var videos: ArrayList<TrailerModel>, var listener: OnAdapterListener):
    RecyclerView.Adapter<TrailerAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= viewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_trailer, parent, false)
    )


    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(videos[position])

        holder.view.tv_trailer.setOnClickListener {
            listener.onPlay(videos[position].key!!)
        }
    }

 class viewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(trailer: TrailerModel){
            view.tv_trailer.text = trailer.name
        }

    }

    fun setData(loadVideos: List<TrailerModel>){
        videos.clear()
        videos.addAll(loadVideos)
        notifyDataSetChanged()
        listener.onLoad(loadVideos[0].key!!)
    }

    interface OnAdapterListener{
        fun onLoad(key: String)
        fun onPlay(key: String)
    }
}
