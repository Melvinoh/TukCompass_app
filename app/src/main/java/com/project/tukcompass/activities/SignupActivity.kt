package com.project.tukcompass.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.project.tukcompass.databinding.ActivitySignupBinding
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val fname = binding.etFirstName.text.toString().trim()
            val lname = binding.etLastName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val regNo = binding.etRegNumber.text.toString().trim()
            val role = binding.etRole.text.toString().trim()
            val courseName = binding.etCourse.text.toString().trim()
            val enrolmentYear = binding.etYear.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (!validateInput(fname, lname, email, regNo, role, courseName, password, confirmPassword)) return@setOnClickListener

            val signupRequest = SignupReqModel(
                fname, lname, email, regNo, role, courseName, "", enrolmentYear, password, confirmPassword
            )
            viewModel.signup(signupRequest)

            viewModel.signupResponse.observe(this) { response ->
               when(response){
                   is Resource.Success -> {
                       Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                       val user = response.data.new_user
                       Log.d("UserLog", "${user}")
                       startActivity(Intent(this, LoginActivity::class.java))
                   }
                   is Resource.Error -> {
                       Toast.makeText(this, "Signup failed: ${response.message}", Toast.LENGTH_SHORT).show()
                   }
                   is Resource.Loading -> {
                       Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
                   } else -> {}
               }
            }
        }
        binding.tvLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun validateInput(
        fname: String,
        lname: String,
        email: String,
        regNo: String,
        role: String,
        courseName: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.etEmail.error = "Invalid email"
                binding.etEmail.requestFocus()
                false
            }
            fname.isEmpty() -> {
                binding.etFirstName.error = "First name required"
                binding.etFirstName.requestFocus()
                false
            }
            lname.isEmpty() -> {
                binding.etLastName.error = "Last name required"
                binding.etLastName.requestFocus()
                false
            }
            regNo.isEmpty() -> {
                binding.etRegNumber.error = "Registration number required"
                binding.etRegNumber.requestFocus()
                false
            }
            role.isEmpty() -> {
                binding.etRole.error = "Role required"
                binding.etRole.requestFocus()
                false
            }
            courseName.isEmpty() -> {
                binding.etCourse.error = "Course required"
                binding.etCourse.requestFocus()
                false
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Password required"
                binding.etPassword.requestFocus()
                false
            }
            confirmPassword.isEmpty() -> {
                binding.etConfirmPassword.error = "Confirm password required"
                binding.etConfirmPassword.requestFocus()
                false
            }
            password != confirmPassword -> {
                binding.etConfirmPassword.error = "Passwords do not match"
                binding.etConfirmPassword.requestFocus()
                false
            }
            else -> true
        }
    }
}
