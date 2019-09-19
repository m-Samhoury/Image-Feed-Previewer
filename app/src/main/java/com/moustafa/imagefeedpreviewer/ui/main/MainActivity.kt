package com.challenge.marleyspoon.features.recipeslist.main

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.moustafa.imagefeedpreviewer.R

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val appBarConfiguration = AppBarConfiguration(
        setOf(R.id.imageFeedsListFragment)
    )

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }
}