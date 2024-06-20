package com.dicoding.soothemate.ui.profile.editprofile

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.soothemate.R
import com.dicoding.soothemate.customviews.CustomEditText
import com.dicoding.soothemate.customviews.CustomSelectOption
import com.dicoding.soothemate.databinding.ActivityEditProfileBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.utils.Utils
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.ProfileViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null
    private lateinit var utils: Utils

    private val storageDir: File by lazy {
        File(filesDir, "cropped_images").apply { mkdirs() }
    }

    val multiplePermissionId = 1
    private val listOfPremissionNeeded = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(
            android.Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        arrayListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState === null){
            mainViewModel.getSession().observe(this) { user ->
                var token = user.token
                profileViewModel.getDetailProfile(token)
            }

            profileViewModel.apiMessage.observe(this){
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        supportActionBar?.hide()

        utils = Utils()

        utils.setTransparentStatusBar(this)

        profileViewModel.detailProfile.observe(this@EditProfileActivity) { userDetail ->
            if (userDetail != null) {

                if (currentImageUri == null && userDetail.avatar != null) {
                    Glide.with(binding.root)
                        .load(userDetail.avatar)
                        .into(binding.profilePicture)
                }

                binding.usernameEdt.text = Editable.Factory.getInstance().newEditable(userDetail.name)
                binding.emailEdt.text = Editable.Factory.getInstance().newEditable(userDetail.email)
                binding.birthDate.text = Editable.Factory.getInstance().newEditable(userDetail.birthDate?:"")
                val genderArray = resources.getStringArray(R.array.dropdown_gender)
                val genderIndex = userDetail.gender?.let {
                    genderArray.indexOf(it)
                } ?: -1

                if (genderIndex != -1) {
                    binding.genderEdt.setSelection(genderIndex)
                } else {
                    binding.genderEdt.setSelection(0)
                }

            }
        }

        profileViewModel.isLoading.observe(this@EditProfileActivity) {
            showLoading(it)
        }


        changeEditTextBg(binding.editActivity, R.drawable.input_bg_white)
        changeSpinnerBg(binding.editActivity, R.drawable.input_bg_white)

        genderInit()
        showDatePickerDialog()
        updateUserInfo()
        exitPage()

        binding.profilePicture.setOnClickListener {
            if (checkMultiplePermission()){
                startGallery()
            }
        }
    }

    private fun checkMultiplePermission(): Boolean {
        val listOfPermission = arrayListOf<String>()
        for (permission in listOfPremissionNeeded) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listOfPermission.add(permission)
            }
        }
        if (listOfPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listOfPermission.toTypedArray(),
                multiplePermissionId
            )
            return false
        }
        return true
    }

    private fun startGallery() {
        getContent.launch("image/*")
    }

    private fun handleImageSelection(selectedImg: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))

        val options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(80)

        UCrop.of(selectedImg, destinationUri)
            .withOptions(options)
            .start(this, launcherIntentCrop)
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            handleImageSelection(it)
        }
    }

    private val launcherIntentCrop = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                val croppedUri: Uri? = UCrop.getOutput(data)
                croppedUri?.let {
                    currentImageUri = it
                    showImage()
                }
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val error = UCrop.getError(result.data!!)
            Log.e("UCrop Error", "Error while cropping: $error")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.profilePicture.setImageURI(it)
        }
    }

    private fun File.reduceFileImage(): File {
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = utils.uriToFile(uri, this@EditProfileActivity).reduceFileImage()

            lifecycleScope.launch {
                mainViewModel.getSession().observe(this@EditProfileActivity) { user ->
                    val token = user.token
                    profileViewModel.updateUserAvatar(imageFile, token)
                }
            }
        }
    }

    private fun updateUserInfo() {
        binding.saveBtn.setOnClickListener {
            val username = binding.usernameEdt.text.toString()
            val birthDate = binding.birthDate.text.toString()
            val selectedGender = binding.genderEdt.selectedItem.toString()

            if (validate()){
                lifecycleScope.launch {
                    mainViewModel.getSession().observe(this@EditProfileActivity) { user ->
                        val token = user.token
                        profileViewModel.updateUserInfo(username, selectedGender, birthDate, token)
                        if (currentImageUri != null) {
                            uploadImage()
                            currentImageUri = null
                        }
                    }
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${(selectedMonth + 1).toString().padStart(2, '0')}-${selectedDay.toString().padStart(2, '0')}"
            binding.birthDate.setText(selectedDate)
        }, year, month, day)

        binding.birthDate.setOnClickListener{
            datePickerDialog.show()
        }
    }

    private fun exitPage() {
        binding.backBtn.setOnClickListener{
            finish()
        }
    }

    private fun genderInit() {
        binding.apply {
            val gender: Spinner = genderEdt
            setSpinnerAdapter(gender, R.array.dropdown_gender)
            gender.prompt = getString(R.string.select_gender)
        }
    }

    private fun setSpinnerAdapter(spinner: Spinner, arrayResId: Int) {
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(arrayResId)
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
                return view
            }
        }
        spinner.adapter = adapter
    }


    private fun validate(): Boolean {
        val editTextValue = listOf(
            binding.usernameEdt,
            binding.birthDate
        )

        val dropdownValue = listOf(
            binding.genderEdt
        )


        var allValid = true


        for (editText in editTextValue) {
            if (!editText.isValidProfile()) {
                allValid = false
            }
        }

        for (dropdown in dropdownValue) {
            if (!dropdown.isValidProfile()) {
                allValid = false
            }
        }
        return allValid
    }

    private fun changeEditTextBg(viewGroup: ViewGroup, backgroundResId: Int) {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is CustomEditText) {
                view.setCustomBackground(backgroundResId)
            }
        }
    }

    private fun changeSpinnerBg(viewGroup: ViewGroup, backgroundResId: Int) {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is CustomSelectOption) {
                view.setCustomBackground(backgroundResId)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}