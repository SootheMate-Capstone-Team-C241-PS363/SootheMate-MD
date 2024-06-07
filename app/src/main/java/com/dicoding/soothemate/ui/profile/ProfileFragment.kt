package com.dicoding.soothemate.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.soothemate.databinding.FragmentProfileBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.ui.onboarding.OnboardingActivity
import com.dicoding.soothemate.ui.profile.changepass.ChangePassActivity
import com.dicoding.soothemate.ui.profile.editprofile.EditProfileActivity
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (savedInstanceState === null){
            mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
                var token = user.token
                profileViewModel.getDetailProfile(token)
            }
        }

        profileViewModel.detailProfile.observe(viewLifecycleOwner) { userDetail ->
            if (userDetail != null) {
                binding.username.text = userDetail.name
                binding.email.text = userDetail.email
                binding.birthDateValue.text = userDetail.birthDate
                binding.genderValue.text = userDetail.gender

                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val birthDate = dateFormatter.parse(userDetail.birthDate)
                val age = birthDate?.age ?: 0
                binding.ageValue.text = "$age years old"
            }
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        editProfile()
        changePass()
        logout()

        return root
    }

    val Date.age: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = Date(time - Date().time)
            return 1970 - (calendar.get(Calendar.YEAR) + 1)
        }

    private fun logout(){
        binding.logoutBtn.setOnClickListener {
            mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
                val token = user.token
                mainViewModel.logoutPost(token)
                mainViewModel.logoutSuccess.observe(viewLifecycleOwner) { success ->
                    if (success == true) {
                        startActivity(Intent(requireContext(), OnboardingActivity::class.java))
                    } else {
                        // handle error in here
                    }
                }
            }
        }
    }

    private fun changePass() {
        binding.resetPassword.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePassActivity::class.java))
        }
    }

    private fun editProfile() {
        binding.profilePicture.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}