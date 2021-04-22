package com.umestudio.moovi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.umestudio.moovi.R
import com.umestudio.moovi.adapter.TrailerAdapter
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.TrailerResponse
import com.umestudio.moovi.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_trailer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrailerActivity : AppCompatActivity() {

    private val TAG: String = "TrailerActivity"

    lateinit var trailerAdapter: TrailerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        setupView()
        setupListener()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        getMovieTrailer()
    }

    private fun setupView(){

    }

    private fun setupListener(){

    }

    private fun setupRecyclerView() {

        trailerAdapter = TrailerAdapter(arrayListOf(), object : TrailerAdapter.OnAdapterListener{
            override fun onLoad(key: String) {

            }

            override fun onPlay(key: String) {

            }


        })

        rv_trailer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trailerAdapter
        }
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
                progressbar_trailer.visibility = View.VISIBLE
            }
            false -> {
                progressbar_trailer.visibility = View.GONE
            }
        }
    }

    private fun showTrailer(trailer: TrailerResponse){
        trailerAdapter.setData(trailer.results)
    }
}
