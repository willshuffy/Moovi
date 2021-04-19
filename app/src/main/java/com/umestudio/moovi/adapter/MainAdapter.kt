package com.umestudio.moovi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.umestudio.moovi.R
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.MovieModel
import kotlinx.android.synthetic.main.item_movie.view.*

/*
    STEP 1 : - buat properties pada constructor
             - alt+enter pada viewHolder
             - buat class viewHolder
 */
class MainAdapter(var movies: ArrayList<MovieModel>): RecyclerView.Adapter<MainAdapter.viewHolder>() {

    /*
        STEP 3 : - alt+enter pada class MainAdapter
                 - implement member onCreateViewHolder, getItemCount, onBindViewHolder
                 - lengkapi method override
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= viewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
    )


    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(movies[position])
    }

    /*
        STEP 2 : - lengkapi constructor pada class viewHolder
                 - tambahkan function
     */

    class viewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(movies: MovieModel){
            view.tv_title.text = movies.title
            val posterPath = Constant.POSTER_PATH + movies.poster_path

            //with picasso
//            Picasso.get()
//                .load(posterPath)
//                .placeholder(R.drawable.placeholder_portrait)
//                .error(R.drawable.placeholder_portrait)
//                .into(view.iv_poster)


            //with glide
            Glide
                .with(view)
                .load(posterPath)
                .centerCrop()
                .placeholder(R.drawable.placeholder_portrait)
                .into(view.iv_poster)
        }

    }

    /*
        STEP 4 : - tambahkan function setData (untuk mengset data API dan mereload data baru pada
                    recyclerview)
     */

    fun setData(loadMovies: List<MovieModel>){
        movies.clear()
        movies.addAll(loadMovies)
        notifyDataSetChanged()
    }
}
