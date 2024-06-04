package com.dicoding.soothemate.ui.home

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dicoding.soothemate.R
import com.dicoding.soothemate.customviews.CircularProgressView
import com.dicoding.soothemate.databinding.FragmentHomeBinding
import com.dicoding.soothemate.viewmodel.HomeViewModel

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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        circularProgressView = binding.circularProgressView

        // inisialisasi tracking dummy
        animateProgress(45f)
        animateTextViewChange(45)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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