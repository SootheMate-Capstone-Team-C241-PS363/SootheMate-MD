package com.dicoding.soothemate.ui.home

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.dicoding.soothemate.R
import com.dicoding.soothemate.customviews.CircularProgressView
import com.dicoding.soothemate.databinding.FragmentHomeBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.ui.history.HistoryActivity
import com.dicoding.soothemate.ui.history.HistoryDetailActivity
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.PredictViewModel
import com.dicoding.soothemate.viewmodel.ProfileViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val predictViewModel: PredictViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var circularProgressView: CircularProgressView
    private var currentProgress: Float = 0f
    private var currentStress: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        circularProgressView = binding.circularProgressView

        mainViewModel.getSession().observe(viewLifecycleOwner){
            val token = it.token
            profileViewModel.getDetailProfile(token)
            predictViewModel.getHistory(null, token)
        }

        profileViewModel.detailProfile.observe(viewLifecycleOwner){
            if (it != null) {
                if (it.avatar != null){
                    Glide.with(binding.root)
                        .load(it.avatar)
                        .into(binding.profileBtn)
                }

                binding.currentUsername.text = it.name
            }
        }

        predictViewModel.stressHistoryValue.observe(viewLifecycleOwner) { dataList ->
            if (dataList != null && dataList.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                val sortedList = dataList.sortedByDescending { dataItem ->
                    try {
                        dateFormat.parse(dataItem?.updateAt)
                    } catch (e: ParseException) {
                        null
                    }
                }.filterNotNull()

                val latestItem = sortedList.firstOrNull()
                val stressLevel = latestItem?.stressLevel

                if (stressLevel != null) {
                    animateTextViewChange(stressLevel.toInt())
                    animateProgress(stressLevel.toFloat())
                }

            }
        }



        binding.calculateBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_home_to_navigation_add)
        }

        binding.profileBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_home_to_navigation_profile)
        }

        if (realtimeClock() in 24..11){
            binding.greetings.text = "Good Morning"
        } else  if (realtimeClock() in 12..14){
            binding.greetings.text = "Good Afternoon"
        } else  if (realtimeClock() in 15..18){
            binding.greetings.text = "Good Afternoon"
        } else  if (realtimeClock() in 19..23){
            binding.greetings.text = "Good Evening"
        }

        binding.historyBtn.setOnClickListener {
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }

        realtimeClock()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun realtimeClock(): Int {
        val textViewClock = binding.clockText

        val handler = Handler(Looper.getMainLooper())
        val hoursOnlyFormat = SimpleDateFormat("HH", Locale.getDefault())

        val runnable = object : Runnable {
            override fun run() {

                val dateFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
                val currentTime = Calendar.getInstance().time
                val timeString = dateFormat.format(currentTime)

                textViewClock.text = timeString

                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)

        return hoursOnlyFormat.format(Calendar.getInstance().time).toInt()
    }

    private fun animateProgress(targetProgress: Float) {
        val animator = ValueAnimator.ofFloat(currentProgress, targetProgress)
        animator.duration = 1000
        animator.addUpdateListener { valueAnimator ->
            val progress = valueAnimator.animatedValue as Float
            circularProgressView.setProgress(progress)
        }
        animator.start()

        currentProgress = targetProgress
    }

    private fun animateTextViewChange(targetProgress: Int) {
        val animator = ValueAnimator.ofInt(currentStress, targetProgress)
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            _binding?.let { binding ->
                binding.stressLevel.text = "$animatedValue%"
                binding.stressLevel2.text = "$animatedValue%"
            }
        }
        animator.start()

        currentStress = targetProgress
    }

}