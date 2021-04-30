package com.umestudio.moovi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umestudio.moovi.R
import com.umestudio.moovi.databinding.ItemMovieBinding
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.MovieModel

/*
    STEP 1 : - buat properties pada constructor
             - alt+enter pada viewHolder
             - buat class viewHolder
 */
class MainAdapter(var movies: ArrayList<MovieModel>,var listener: OnAdapterListener): RecyclerView.Adapter<MainAdapter.viewHolder>() {

    /*
        STEP 3 : - alt+enter pada class MainAdapter
                 - implement member onCreateViewHolder, getItemCount, onBindViewHolder
                 - lengkapi method override
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= viewHolder (
        ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(movies[position])

        holder.binding.ivPoster.setOnClickListener {
            Constant.MOVIE_ID = movies[position].id!!
            Constant.MOVIE_TITLE = movies[position].title!!
            listener.onClick(movies[position])
        }
    }

    /*
        STEP 2 : - lengkapi constructor pada class viewHolder
                 - tambahkan function
     */

    class viewHolder(val binding: ItemMovieBinding ): RecyclerView.ViewHolder(binding.root) {
        fun bind(movies: MovieModel){

            val posterPath = Constant.POSTER_PATH + movies.poster_path
            binding.tvTitle.text = movies.title

            //with picasso
//            Picasso.get()
//                .load(posterPath)
//                .placeholder(R.drawable.placeholder_portrait)
//                .error(R.drawable.placeholder_portrait)
//                .into(view.iv_poster)


            //with glide
            Glide
                .with(binding.root)
                .load(posterPath)
                .centerCrop()
                .placeholder(R.drawable.placeholder_portrait)
                .into(binding.ivPoster)
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

    fun setDataNextPage(loadMovies: List<MovieModel>){
        movies.addAll(loadMovies)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(clickmovies: MovieModel)
    }
}
