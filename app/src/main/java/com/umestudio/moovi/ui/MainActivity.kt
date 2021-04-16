package com.umestudio.moovi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.umestudio.moovi.R
import com.umestudio.moovi.adapter.MainAdapter
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.MovieResponse
import com.umestudio.moovi.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()

        getMovie()
    }

    private fun setupRecyclerView(){

        mainAdapter = MainAdapter(arrayListOf())
        rv_main.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    fun getMovie(){

        showLoading(true)
        ApiService().endpoint.getMovieNowPlaying(Constant.API_KEY, 1)
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                    showLoading(false)
                    /*
                        cek error koneksi API dengan Log di bawah ini
                     */
                    Log.d(TAG, "errorMessage : $t")

                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful){
                        showMovie(response.body()!!)
                    }

                }

            })
    }

    fun showLoading(loading: Boolean){
        when(loading){
            true -> pb_main.visibility = View.VISIBLE
            false -> pb_main.visibility = View.GONE
        }
    }

    fun showMovie(response: MovieResponse){
//        Log.d(TAG, "responseMovie : $response")
//        Log.d(TAG, "total_pages : ${response.total_pages}")
//
//        for (movie in response.results){
//            Log.d(TAG, "movie_title : ${movie.title}")
//        }

        mainAdapter.setData(response.results)
    }

    fun  showMessage(msg: String){
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}
