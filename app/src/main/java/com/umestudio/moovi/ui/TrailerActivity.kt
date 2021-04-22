package com.umestudio.moovi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.umestudio.moovi.R
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.TrailerResponse
import com.umestudio.moovi.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrailerActivity : AppCompatActivity() {

    private val TAG: String = "TrailerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        setupView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getMovieTrailer()
    }

    private fun setupView(){

    }

    private fun setupListener(){

    }

    private fun getMovieTrailer(){
        showLoading(true)
        ApiService().endpoint.getMovieTrailer(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<TrailerResponse>{
                override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                    showLoading(false)
                }

                override fun onResponse(
                    call: Call<TrailerResponse>,
                    response: Response<TrailerResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful){
                        showTrailer(response.body()!!)
                    }
                }

            })
    }

    private fun showLoading(loading: Boolean){
        when(loading){
            true -> {

            }
            false -> {

            }
        }
    }

    private fun showTrailer(trailer: TrailerResponse){
        for (res in trailer.results){
            Log.d(TAG, "nameVideo: ${res.name}")
        }
    }
}
