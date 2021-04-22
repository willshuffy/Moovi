package com.umestudio.moovi.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.umestudio.moovi.R
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.DetailResponse
import com.umestudio.moovi.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.item_movie.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private val TAG: String = "DetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getMovieDetail()
    }

    private fun setupView(){

        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupListener(){
        fab_trailer.setOnClickListener {
            startActivity(Intent(applicationContext, TrailerActivity::class.java))
        }
    }

    private fun getMovieDetail(){

        ApiService().endpoint.getMovieDetail(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<DetailResponse>{
                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                }

                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful){
                        showMovie(response.body()!!)
                    }
                }

            })
    }

    fun showMovie(detail: DetailResponse){
//        Log.d(TAG, "overviewResponse: ${detail.overview}")


        val backdropPath = Constant.BACKDROP_PATH + detail.backdrop_path
        Glide
            .with(this)
            .load(backdropPath)
            .centerCrop()
            .placeholder(R.drawable.placeholder_portrait)
            .into(iv_backdrop)

        tv_title.text = detail.title
        tv_vote.text = detail.vote_average.toString()
        tv_overview.text = detail.overview

        for (genre in detail.genres!!){
            tv_genre.text = "${genre.name}"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
