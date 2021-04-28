package com.umestudio.moovi.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.umestudio.moovi.R
import com.umestudio.moovi.adapter.MainAdapter
import com.umestudio.moovi.model.Constant
import com.umestudio.moovi.model.MovieModel
import com.umestudio.moovi.model.MovieResponse
import com.umestudio.moovi.retrofit.ApiService
import com.umestudio.moovi.ui.DetailActivity
import kotlinx.android.synthetic.main.fragment_upcoming.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingFragment : Fragment() {

    lateinit var v: View
    lateinit var mainAdapter: MainAdapter

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
    }

    override fun onStart() {
        super.onStart()

        getMovieUpcoming()
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

    fun showMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun getMovieUpcoming() {

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

    fun showLoading(loading: Boolean) {
        when (loading) {
            true -> v.pb_upcoming.visibility = View.VISIBLE
            false -> v.pb_upcoming.visibility = View.GONE
        }
    }

    fun showMovie(response: MovieResponse) {
        mainAdapter.setData(response.results)
    }

}