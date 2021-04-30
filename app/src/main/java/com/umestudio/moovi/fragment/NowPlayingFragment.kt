package com.umestudio.moovi.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.umestudio.moovi.adapter.MainAdapter
import com.umestudio.moovi.databinding.FragmentNowPlayingBinding
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.MovieModel
import com.umestudio.moovi.model.MovieResponse
import com.umestudio.moovi.retrofit.ApiService
import com.umestudio.moovi.ui.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NowPlayingFragment : Fragment() {

    lateinit var binding: FragmentNowPlayingBinding
    lateinit var mainAdapter: MainAdapter
    private var isScrolling = false
    private var currentPage = 1
    private var totalPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNowPlayingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()

        getMovieNowPlaying()
        showLoadingNextPage(false)
    }

    private fun setupRecyclerView() {

        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener{
            override fun onClick(clickmovies: MovieModel) {
                showMessage(clickmovies.title!!)
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }

        })

        binding.rvNowPlaying.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    private fun setupListener(){

        binding.scrollNowPlaying.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
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
                            getMovieNowPlayingNextPage()
                        }
                    }
                }
            }

        })
    }

    fun showMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun getMovieNowPlaying() {

        binding.scrollNowPlaying.scrollTo(0,0)
        currentPage = 1
        showLoading(true)
        ApiService().endpoint.getMovieNowPlaying(Constant.API_KEY, 1)
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

    fun getMovieNowPlayingNextPage() {

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
            true -> binding.pbNowPlaying.visibility = View.VISIBLE
            false -> binding.pbNowPlaying.visibility = View.GONE
        }
    }

    fun showLoadingNextPage(loading: Boolean) {
        when (loading) {
            true -> {
                isScrolling = true
                binding.pbNowPlayingNextPage.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                binding.pbNowPlayingNextPage.visibility = View.GONE
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