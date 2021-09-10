package com.umestudio.moovi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.GridLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.umestudio.moovi.BuildConfig
import com.umestudio.moovi.R
import com.umestudio.moovi.adapter.MainAdapter
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.MovieModel
import com.umestudio.moovi.model.MovieResponse
import com.umestudio.moovi.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val movieNowPlaying = 0
const val movieUpcming = 1

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    lateinit var mainAdapter: MainAdapter
    private var movieCategory = 0
    private var api = ApiService().endpoint
    private var isScrolling = false
    private var currentPage = 1
    private var totalPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        setupListener()

    }

    override fun onStart() {
        super.onStart()

        getMovie()
        showLoadingNextPage(false)
    }

    private fun setupRecyclerView() {

        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener{
            override fun onClick(clickmovies: MovieModel) {
                showMessage(clickmovies.title!!)
                /*
                    cek listener : - run apps
                                   - click movies and show toast
                 */
                //==================================================================================

                Constant.MOVIE_ID = clickmovies.id!!
                Constant.MOVIE_TITLE = clickmovies.title!!
                startActivity(Intent(applicationContext, DetailActivity::class.java))
            }

        })

        rv_main.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    private fun setupListener(){
        iv_menus.setOnClickListener{
            showMenu(iv_menus)
        }

        scroll_main.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight){ //untuk memastikan scroll nya sudah mentok ke konten paling bawah
                    if (!isScrolling){
                        if (currentPage <= totalPage ){
                            getMovieNextPage()
                        }
                    }
                }
            }

        })
    }

    private fun getMovie() {

        scroll_main.scrollTo(0,0)
        currentPage = 1
        showLoading(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory){
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(BuildConfig.MOA_KEY, 1)
            }
            movieUpcming -> {
                apiCall = api.getMovieUpcoming(BuildConfig.MOA_KEY, 1)
            }
        }

        apiCall!!
            .enqueue(object : Callback<MovieResponse> {
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
                    if (response.isSuccessful) {
                        showMovie(response.body()!!)
                    }

                }

            })
    }

    private fun getMovieNextPage() {

        currentPage += 1
        showLoadingNextPage(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory){
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(BuildConfig.MOA_KEY, currentPage)
            }
            movieUpcming -> {
                apiCall = api.getMovieUpcoming(BuildConfig.MOA_KEY, currentPage)
            }
        }

        apiCall!!
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                    showLoadingNextPage(false)
                    /*
                        cek error koneksi API dengan Log di bawah ini
                     */
                    Log.d(TAG, "errorMessage : $t")

                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoadingNextPage(false)
                    if (response.isSuccessful) {
                        showMovieNextPage(response.body()!!)
                    }

                }

            })
    }

    fun showLoading(loading: Boolean) {
        when (loading) {
            true -> pb_main.visibility = View.VISIBLE
            false -> pb_main.visibility = View.GONE
        }
    }

    fun showLoadingNextPage(loading: Boolean) {
        when (loading) {
            true -> {
                isScrolling = true
                pb_main_next_page.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                pb_main_next_page.visibility = View.GONE
            }
        }
    }

    fun showMovie(response: MovieResponse) {
        totalPage = response.total_pages!!.toInt()
        mainAdapter.setData(response.results)
    }

    fun showMovieNextPage(response: MovieResponse) {
        totalPage = response.total_pages!!.toInt()
        mainAdapter.setDataNextPage(response.results)
        showMessage("Page $currentPage")
    }

    fun showMessage(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showMenu(view: View) {

        val menus = PopupMenu(this, view)
        menus.inflate(R.menu.main_menu)

        menus.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem ->

            when (item.itemId) {

                R.id.select_nowplaying -> {
                    showMessage("Movie Now Playing")
                    movieCategory = movieNowPlaying
                    getMovie()
                    true
                }
                R.id.select_upcoming -> {
                    showMessage("Movie Upcoming")
                    movieCategory = movieUpcming
                    getMovie()
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }

        })
        menus.show()
    }
}
