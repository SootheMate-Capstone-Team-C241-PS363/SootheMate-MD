package com.dicoding.soothemate.ui.history

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
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
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.PredictViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter

    private var currentStress: Int = 0

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mainViewModel.getSession().observe(this@HistoryActivity) { user ->
            if (user != null){
                val token = user.token
                fetchHistoryWithToken(token)
            }
        }

        initSpinner()
        initRecyclerView()
    }

    private fun animateTextViewChange(targetProgress: Int) {
        val animator = ValueAnimator.ofInt(currentStress, targetProgress)
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            binding?.let { binding ->
                binding.tvPercentageStress.text = "$animatedValue%"
            }
        }
        animator.start()

        currentStress = targetProgress
    }

    private fun fetchHistoryWithToken(token: String) {
        binding.progressBar.visibility = View.VISIBLE
        val apiService = ApiConfig.getApiService(token)
        apiService.getHistory(null).enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful && response.body()?.data?.isNotEmpty() == true) {
                    binding.progressBar.visibility = View.GONE
                    response.body()?.data?.let { dataItems ->
                        adapter.submitList(dataItems)
                        binding.rvHistory.visibility = View.VISIBLE
                        binding.noHistory.visibility = View.GONE
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val sortedList = dataItems.sortedByDescending { dataItem ->
                            try {
                                dateFormat.parse(dataItem?.updateAt ?: "")
                            } catch (e: ParseException) {
                                null
                            }
                        }.filterNotNull()

                        val latestItem = sortedList.firstOrNull()
                        val stressLevel = latestItem?.stressLevel

                        if (stressLevel != null) {
                            animateTextViewChange(stressLevel)
                        }
                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.rvHistory.visibility = View.GONE
                    binding.noHistory.visibility = View.VISIBLE
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
            historyItem.id?.let { toDetail(it) }
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
        mainViewModel.getSession().observe(this@HistoryActivity) { user ->
            val token = user.token
            fetchHistoryWithFilter(filterQuery,token)
        }

    }

    private fun fetchHistoryWithFilter(filter: String, token: String) {
        lifecycleScope.launch {
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
