package com.example.trootechpractical.presentation.home

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.trootechpractical.R
import com.example.trootechpractical.databinding.ActivityHomeBinding
import com.example.trootechpractical.presentation.base.BaseActivity
import com.example.trootechpractical.presentation.home.ui.HomeDataViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeDataViewModel, ActivityHomeBinding>(ActivityHomeBinding::inflate) {


    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun initActivity() {
        setSupportActionBar(myBinding.appBarHome.toolbar)


        val drawerLayout: DrawerLayout = myBinding.drawerLayout
        val navView: NavigationView = myBinding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        myBinding.appBarHome.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_select_all -> {
                    val menu: Menu = myBinding.appBarHome.toolbar.getMenu()
                    val menuItem: MenuItem = menu.findItem(R.id.action_select_all)
                    if(menuItem.title?.equals("Select All") == true)
                    {
                        sendBroadcast(Intent("SELECT_ALL"))
                        menuItem.setTitle(getString(R.string.unselect_all))
                    }
                    else{
                        sendBroadcast(Intent("UNSELECT_ALL"))
                        menuItem.setTitle(getString(R.string.action_select_all))
                    }

                }
            }
            true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}