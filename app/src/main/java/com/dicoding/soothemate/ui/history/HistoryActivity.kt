package com.dicoding.soothemate.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.soothemate.R
import com.dicoding.soothemate.adapter.HistoryAdapter
import com.dicoding.soothemate.databinding.ActivityHistoryBinding
import com.dicoding.soothemate.data.api.ApiConfig
import com.dicoding.soothemate.data.api.HistoryResponse
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.flow.first
import androidx.lifecycle.lifecycleScope
import com.dicoding.soothemate.data.pref.UserPreference
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first



class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinner()
        initRecyclerView()
        fetchHistoryData()
    }

    private fun fetchHistoryData() {
        lifecycleScope.launch {
            val token = userPreference.getSession().first().token
            fetchHistoryWithToken(token)
        }
    }

    private fun fetchHistoryWithToken(token: String) {
        val apiService = ApiConfig.getApiService(token)
        apiService.getHistory(null).enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { dataItems ->
                        adapter.submitList(dataItems)
                    }
                } else {
                    Log.e("HistoryActivity", "Error fetching data: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.e("HistoryActivity", "Error in API call", t)
            }
        })
    }

    private fun initRecyclerView() {
        adapter = HistoryAdapter(emptyList()) { historyItem ->
            historyItem.id?.let { toDetail(it) }  // Pastikan bahwa historyItem memiliki field 'id'
        }
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter
    }

    private fun toDetail(historyId: String) {
        val intent = Intent(this, HistoryDetailActivity::class.java).apply {
            putExtra("HISTORY_ID", historyId)
        }
        startActivity(intent)
    }

    private fun initSpinner() {
        val spinner: Spinner = binding.sortBySpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_array,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedFilter = parent.getItemAtPosition(position).toString()
                applyFilter(selectedFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun applyFilter(selectedFilter: String) {
        val filterQuery = when (selectedFilter) {
            "Today" -> "today"
            "Yesterday" -> "yesterday"
            "Last 3 Days" -> "last_3_days"
            "Last 7 Days" -> "last_7_days"
            else -> ""
        }
        fetchHistoryWithFilter(filterQuery)
    }

    private fun fetchHistoryWithFilter(filter: String) {
        lifecycleScope.launch {
            val token = userPreference.getSession().first().token
            val apiService = ApiConfig.getApiService(token)
            apiService.getHistory(filter).enqueue(object : Callback<HistoryResponse> {
                override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { dataItems ->
                            adapter.submitList(dataItems)
                        }
                    } else {
                        Log.e("HistoryActivity", "Error fetching data: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                    Log.e("HistoryActivity", "Error in API call", t)
                }
            })
        }
    }
}
