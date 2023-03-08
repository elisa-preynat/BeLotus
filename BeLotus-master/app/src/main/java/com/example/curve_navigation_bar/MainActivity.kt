package com.example.curve_navigation_bar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.curve_navigation_bar.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navigationView = binding.bottomNavigation
        navigationView.setOnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> fragment = HomeFragment()
                R.id.nav_profile -> fragment = ProfileFragment()
                R.id.nav_bluetooth -> fragment = BluetoothFragment()
                R.id.nav_graph -> fragment = GraphFragment()
                R.id.nav_alert -> fragment = AlertFragment()
            }

            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.body_container, fragment)
                    .commit()
            }
            true
        }

        // Afficher le fragment initial
        supportFragmentManager.beginTransaction()
            .replace(R.id.body_container, HomeFragment())
            .commit()


    }
}

