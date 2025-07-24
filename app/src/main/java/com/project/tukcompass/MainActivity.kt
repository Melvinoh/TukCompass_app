package com.project.tukcompass

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.project.tukcompass.activities.HomeActivity
import com.project.tukcompass.activities.LoginActivity
import com.project.tukcompass.activities.SignupActivity
import com.project.tukcompass.databinding.ActivityMainBinding
import com.project.tukcompass.utills.EncryptedSharedPrefManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPrefManager = EncryptedSharedPrefManager(this)
        val token = sharedPrefManager.getToken()
        if (token != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}