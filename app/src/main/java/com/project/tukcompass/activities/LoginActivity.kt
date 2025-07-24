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
import com.project.tukcompass.models.UserModels
import com.project.tukcompass.utills.EncryptedSharedPrefManager
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        sharedPrefManager = EncryptedSharedPrefManager(this)

        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val regNo = binding.etRegNo.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (!validate(regNo, password)) return@setOnClickListener

            val reqBody: LoginModels = LoginModels(regNo, password)

            viewModel.login(reqBody)

            viewModel.loginResponse.observe(this) { response ->
                when (response) {
                    is Resource.Success -> {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        val user: UserModels = response.data.user
                        val token: String = response.data.token

                        sharedPrefManager.saveToken(token)
                        sharedPrefManager.saveUser(user)

                        Log.d("UserLog", "${user}")
                        Log.d("TokenLog", "${token}")

                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is Resource.Error -> {
                        Toast.makeText(this, "Login failed: ${response.message}", Toast.LENGTH_SHORT).show()
                        Log.d("error", "${response.message}")
                    }
                    is Resource.Loading -> {
                        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
        binding.txtSignUp.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
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