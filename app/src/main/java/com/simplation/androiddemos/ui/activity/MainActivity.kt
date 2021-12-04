package com.simplation.androiddemos.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.simplation.androiddemos.ui.fragment.GeneralFragment
import com.simplation.androiddemos.R
import com.simplation.androiddemos.ui.fragment.ThirdFragment
import com.simplation.androiddemos.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.system.exitProcess

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/18 15:59
 * @描述: 主页面
 * @更新:
 */
class MainActivity : BaseActivity() {

    private var exitTime: Long = 0

    private val FRAGMENT_GENERAL = 0x01
    private val FRAGMENT_THIRD = 0x02

    private var mIndex = FRAGMENT_GENERAL

    private var generalFragment: GeneralFragment? = null
    private var thirdFragment: ThirdFragment? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }

        bottom_navigation.run {
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

        initDrawLayout()

        initNavView()

        showFragment(mIndex)

        floating_action_btn.setOnClickListener {
            Toast.makeText(this, "Floating Button Click...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initNavView() {
        /*nav_view.run {
            TODO()
            // setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
        }*/
    }

    private fun initDrawLayout() {
        drawer_layout.run {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                this,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    /**
     *
     */
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            return@OnNavigationItemSelectedListener when (item.itemId) {
                R.id.action_general -> {
                    showFragment(FRAGMENT_GENERAL)
                    true
                }

                R.id.action_third -> {
                    showFragment(FRAGMENT_THIRD)
                    true
                }

                else -> {
                    false
                }
            }
        }

    private fun showFragment(index: Int) {
        val translation = supportFragmentManager.beginTransaction()
        hideFragments(translation)

        mIndex = index
        when (index) {
            FRAGMENT_GENERAL -> {
                toolbar.title = getString(R.string.app_name)
                if (generalFragment == null) {
                    generalFragment =
                        GeneralFragment.getInstance()
                    translation.add(R.id.container, generalFragment!!, "general")
                } else {
                    translation.show(generalFragment!!)
                }
            }

            FRAGMENT_THIRD -> {
                toolbar.title = getString(R.string.third_sdk)
                if (thirdFragment == null) {
                    thirdFragment =
                        ThirdFragment.getInstance()
                    translation.add(R.id.container, thirdFragment!!, "third")
                } else {
                    translation.show(thirdFragment!!)
                }
            }
        }
        translation.commit()
    }

    private fun hideFragments(translation: FragmentTransaction) {
        generalFragment?.let {
            translation.hide(it)
        }
        thirdFragment?.let {
            translation.hide(it)
        }
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

    override fun onDestroy() {
        super.onDestroy()

        generalFragment = null
        thirdFragment = null
    }
}
