package com.simplation.androiddemos.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.simplation.androiddemos.R
import kotlinx.android.synthetic.main.activity_main2.*
import kotlin.system.exitProcess

class Main2Activity : AppCompatActivity() {

    private var exitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_containerview) as NavHostFragment
        val navController = host.navController
        initBottomNavigationView(navigation_view, navController)
    }

    private fun initBottomNavigationView(
        bottomNavigationView: BottomNavigationView,
        navController: NavController,
    ) {
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                exitTime = System.currentTimeMillis()
                Toast.makeText(this,
                    R.string.exit_tip, Toast.LENGTH_SHORT).show()
            } else {
                finish()
                exitProcess(1)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}