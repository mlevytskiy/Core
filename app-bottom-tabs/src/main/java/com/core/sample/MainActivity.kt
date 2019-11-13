package com.core.sample

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.library.core.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

    override fun getNavRes(): Int {
        return R.id.main_nav_host
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.home_bottom_nav)
        bottomNavigationView.setupWithNavController(navController)
//        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
//            Toast.makeText(this, "click", Toast.LENGTH_LONG).show()
//            return@setOnNavigationItemSelectedListener true
//        }

    }
}
