package com.umestudio.moovi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.umestudio.moovi.R
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.MovieResponse
import com.umestudio.moovi.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        getMovie()
    }

    fun getMovie(){

        ApiService().endpoint.getMovieNowPlaying(Constant.API_KEY, 1)
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    /*
                        cek error koneksi API dengan Log di bawah ini
                     */
                    Log.d(TAG, "errorMessage : $t")

                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful){
                        showMovie(response.body()!!)
                    }

                }

            })
    }

    fun showMovie(response: MovieResponse){
        Log.d(TAG, "responseMovie : $response")
        Log.d(TAG, "total_pages : ${response.total_pages}")

        for (movie in response.results){
            Log.d(TAG, "movie_title : ${movie.title}")
        }
    }

    fun  showMessage(msg: String){
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}
