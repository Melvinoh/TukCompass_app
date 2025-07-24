package com.project.tukcompass.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.project.tukcompass.R
import com.project.tukcompass.databinding.ActivityHomeBinding
import com.project.tukcompass.fragments.HomeFragment
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


        if (savedInstanceState == null) {
            displayFragment(HomeFragment())
        }

    }
    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()

    }
}