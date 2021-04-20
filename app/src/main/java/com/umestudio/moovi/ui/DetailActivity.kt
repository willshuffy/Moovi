package com.umestudio.moovi.ui

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.umestudio.moovi.R
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.DetailResponse
import com.umestudio.moovi.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private val TAG: String = "DetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onStart() {
        super.onStart()
        getMovieDetail()
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
        Log.d(TAG, "overviewResponse: ${detail.overview}")
    }
}
