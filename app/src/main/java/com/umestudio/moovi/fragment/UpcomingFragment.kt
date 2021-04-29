package com.umestudio.moovi.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.umestudio.moovi.R
import com.umestudio.moovi.adapter.MainAdapter
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.MovieModel
import com.umestudio.moovi.model.MovieResponse
import com.umestudio.moovi.retrofit.ApiService
import com.umestudio.moovi.ui.DetailActivity
import kotlinx.android.synthetic.main.fragment_upcoming.*
import kotlinx.android.synthetic.main.fragment_upcoming.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingFragment : Fragment() {

    lateinit var v: View
    lateinit var mainAdapter: MainAdapter
    private var isScrolling = false
    private var currentPage = 1
    private var totalPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_upcoming, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()

        getMovieUpcoming()
        showLoadingNextPage(false)
    }

    private fun setupRecyclerView() {

        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener{
            override fun onClick(clickmovies: MovieModel) {
                showMessage(clickmovies.title!!)
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }

        })

        v.rv_upcoming.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    private fun setupListener(){

        v.scroll_upcoming.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
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
                            getMovieUpcomingNextPage()
                        }
                    }
                }
            }

        })
    }

    fun showMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun getMovieUpcoming() {

        scroll_upcoming.scrollTo(0,0)
        currentPage = 1
        showLoading(true)
        ApiService().endpoint.getMovieUpcoming(Constant.API_KEY, 1)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                    showLoading(false)

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

    fun getMovieUpcomingNextPage() {

        currentPage += 1
        showLoadingNextPage(true)
        ApiService().endpoint.getMovieNowPlaying(Constant.API_KEY, currentPage)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                    showLoadingNextPage(false)

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
            true -> v.pb_upcoming.visibility = View.VISIBLE
            false -> v.pb_upcoming.visibility = View.GONE
        }
    }

    fun showLoadingNextPage(loading: Boolean) {
        when (loading) {
            true -> {
                isScrolling = true
                v.pb_upcoming_next_page.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                v.pb_upcoming_next_page.visibility = View.GONE
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

}