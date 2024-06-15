package com.dicoding.soothemate.ui.welcome

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.WindowCompat
import com.dicoding.soothemate.MainActivity
import com.dicoding.soothemate.databinding.ActivityWelcomeBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.ui.history.HistoryActivity
import com.dicoding.soothemate.ui.onboarding.OnboardingActivity
import com.dicoding.soothemate.viewmodel.MainViewModel

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        val paint = binding.tvSootheMate.paint
        val width = paint.measureText(binding.tvSootheMate.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, binding.tvSootheMate.textSize,
            intArrayOf(
                Color.parseColor("#800098DA"), Color.parseColor("#5DCCFC"), Color.parseColor("#0098DA")
            ), null, Shader.TileMode.REPEAT)

        binding.tvSootheMate.paint.setShader(textShader)
        Handler().postDelayed(Runnable {
            binding.bigLayout.transitionToEnd() }, 2000)

        binding.bigLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
            }

            override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                Handler().postDelayed({
                    viewModel.getSession().observe(this@WelcomeActivity) { user ->
                        if (!user.isLogin) {
                            startActivity(Intent(this@WelcomeActivity, OnboardingActivity::class.java))
                            finish()
                        } else {
                            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                            finish()
                        }
                }
            }, 2000)
        }

            override fun onTransitionTrigger(motionLayout: MotionLayout, triggerId: Int, positive: Boolean, progress: Float) {
            }
        })

    }
}