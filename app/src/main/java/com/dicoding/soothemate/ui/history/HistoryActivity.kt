package com.dicoding.soothemate.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.soothemate.R
import com.dicoding.soothemate.adapter.HistoryAdapter
import com.dicoding.soothemate.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinner()
        initRecyclerView()
        initView()
    }

    private fun initView() {
        binding.ivBgBanner.setOnClickListener {
            toDetail()
        }

    }

    private fun toDetail(){
        val intent = Intent(this@HistoryActivity, HistoryDetailActivity::class.java)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        val items = listOf("Today | %", "Today | %", "Today | %", "Today | %", "Today | %", "Today | %",
            "Today | %", "Today | %", "Today | %", "Today | %", "Today | %", "Today | %")

        val adapter = HistoryAdapter(items){_ ->
            toDetail()
        }
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter
    }

    private fun initSpinner() {
        val spinner: Spinner = binding.sortBySpinner

        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_array,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            spinner.adapter = adapter
        }

    }
}