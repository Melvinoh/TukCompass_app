package com.project.tukcompass.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.project.tukcompass.databinding.ActivityLoginBinding
import com.project.tukcompass.models.LoginModels
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.LoginViewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val regNo = binding.etRegNo.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (!validate(regNo, password)) return@setOnClickListener

            val reqBody: LoginModels = LoginModels(regNo, password)

            viewModel.login(reqBody)
            viewModel.loginResponse.observe(this) { response ->
                Log.d("LoginResponse", response.toString())
                when (response) {
                    is Resource.success -> {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        val user: String = response.data.user.toString()
                        val token: String = response.data.token
                        Log.d("UserLog", user)
                        Log.d("TokenLog", token)
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                    is Resource.error -> {
                        Toast.makeText(this, "Login failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.loading -> {
                        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private  fun validate( regNo : String, password : String) : Boolean{

        if (regNo.isEmpty()){
            binding.etRegNo.error = "Registration number required"
            binding.etRegNo.requestFocus()
            return false
        }
        if (password.isEmpty()){
            binding.etPassword.error = "Password required"
            binding.etPassword.requestFocus()
            return false

        }
        return true
    }
}