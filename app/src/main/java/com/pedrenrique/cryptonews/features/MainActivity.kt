package com.pedrenrique.cryptonews.features

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.ext.setTitle
import com.pedrenrique.cryptonews.core.platform.LocaleManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val navHost by lazy {
        supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
    }

    private val navController: NavController by lazy {
        navHost.navController
    }

    val navFragmentManager: FragmentManager
        get() = navHost.childFragmentManager

    private val localeManager by inject<LocaleManager>()

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
        tvAttribution.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeManager.restoreLanguage())
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
