package com.project.tukcompass.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.project.tukcompass.databinding.ActivityHomeBinding
import com.project.tukcompass.utills.EncryptedSharedPrefManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sharedPrefManager = EncryptedSharedPrefManager(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = sharedPrefManager.getUser()
        val token = sharedPrefManager.getToken()

    }
}