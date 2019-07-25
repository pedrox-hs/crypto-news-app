package com.pedrenrique.cryptonews.features

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.ext.setTitle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val navHost by lazy {
        supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
    }

    private val navController: NavController by lazy {
        navHost.navController
    }

    val navFragmentManager: FragmentManager
        get() = navHost.childFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.setTitle(destination.label, null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
