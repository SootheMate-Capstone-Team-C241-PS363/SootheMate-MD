package com.dicoding.soothemate.ui.home

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.soothemate.customviews.CircularProgressView
import com.dicoding.soothemate.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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


        // inisialisasi tracking dummy
        animateProgress(45f)
        animateTextViewChange(45)
        realtimeClock()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun realtimeClock() {
        val textViewClock = binding.clockText

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentTime = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
                val timeString = dateFormat.format(currentTime)

                textViewClock.text = timeString

                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)
    }

    // animate tracking indicator
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

    // animate angka
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