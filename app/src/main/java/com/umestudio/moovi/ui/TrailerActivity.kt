package com.umestudio.moovi.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
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
    lateinit var youTubePlayer: YouTubePlayer
    private var youtubeKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        setupView()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        getMovieTrailer()
    }

    private fun setupView(){

        val youTubePlayerView =
            findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                youtubeKey?.let {
                    youTubePlayer.cueVideo(it, 0f)
                }
            }
        })
    }

    private fun setupRecyclerView() {

        trailerAdapter = TrailerAdapter(arrayListOf(), object : TrailerAdapter.OnAdapterListener{
            override fun onLoad(key: String) {
                youtubeKey = key
            }

            override fun onPlay(key: String) {
                    youTubePlayer.loadVideo(key, 0f)
            }


        })

        rv_trailer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trailerAdapter
            setHasFixedSize(true)
        }
    }

    private fun getMovieTrailer(){
        showLoading(true)
        ApiService().endpoint.getMovieTrailer(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<TrailerResponse>{
                override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    showLoading(false)
                }

                override fun onResponse(
                    call: Call<TrailerResponse>,
                    response: Response<TrailerResponse>
                ) {

                    if (response.isSuccessful){
                        showLoading(false)
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
        trailer?.let {
            trailerAdapter.setData(trailer.results)
        }
    }
}
