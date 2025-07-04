package com.project.tukcompass.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.project.tukcompass.databinding.ActivitySignupBinding
import com.project.tukcompass.models.SignupModel
import com.project.tukcompass.viewModels.SignupViewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var  binding: ActivitySignupBinding
    private lateinit var fname: String
    private lateinit var lname: String
    private lateinit var email: String
    private lateinit var regNo: String
    private lateinit var role: String
    private lateinit var courseName: String
    private lateinit var enrolmentYear: String
    private lateinit var password: String
    private lateinit var confirmPassword: String

    private val viewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fname = binding.etFirstName.text.toString().trim()
        val lname = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val unitID = binding.etRegNumber.text.toString().trim()
        val role = binding.etRole.text.toString().trim()
        val courseName = binding.etCourse.text.toString().trim()
        val enrolmentYear = binding.etYear.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        binding.btnSignUp.setOnClickListener {
            validateInput()

            val signupRequest = SignupModel(fname, lname, email, unitID, role, courseName, "", enrolmentYear, password, confirmPassword)
            viewModel.signup(signupRequest)
            viewModel.signupResponse.observe(this) { response ->
                if (response != null && response.isSuccessful) {
                    Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
                }
            }
            startActivity(Intent(this, LoginActivity::class.java))

        }
    }
    private fun validateInput() {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email"
            binding.etEmail.requestFocus()
            return Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
        }else if(fname.isEmpty()) {
            binding.etFirstName.error = "First name required"
            binding.etFirstName.requestFocus()
            return Toast.makeText(this, "First name required", Toast.LENGTH_SHORT).show()
        }else if(lname.isEmpty()) {
            binding.etLastName.error = "Last name required"
            binding.etLastName.requestFocus()
            return
        }else if(regNo.isEmpty()) {
            binding.etRegNumber.error = "Registration number required"
            binding.etRegNumber.requestFocus()
            return Toast.makeText(this, "Registration number required", Toast.LENGTH_SHORT).show()
        }else if(role.isEmpty()) {
            binding.etRole.error = "Role required"
            binding.etRole.requestFocus()
            return
        }else if(courseName.isEmpty()) {
            binding.etCourse.error = "Course required"
            binding.etCourse.requestFocus()
            return Toast.makeText(this, "Course required", Toast.LENGTH_SHORT).show()
        }else if(password.isEmpty()) {
            binding.etPassword.error = "Password required"
            binding.etPassword.requestFocus()
            return Toast.makeText(this, "Password required", Toast.LENGTH_SHORT).show()
        }else if(confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error = "Confirm password required"
            binding.etConfirmPassword.requestFocus()
            return Toast.makeText(this, "Confirm password required", Toast.LENGTH_SHORT).show()
        }else if(password != confirmPassword) {
            binding.etConfirmPassword.error = "Passwords do not match"
            binding.etConfirmPassword.requestFocus()
            return Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUpUser() {

    }
}