package com.core.sample

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.library.core.BaseActivity
import com.library.core.event.HideBottomNavEvent
import com.library.core.event.ShowBottomNavEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private const val BOTTOM_NAV_HIDE_SHOW_ANIMATION_DURATION = 150L

class MainActivity : BaseActivity(R.layout.activity_main) {

    private var bottomNavigationView : BottomNavigationView? = null

    override fun getNavRes(): Int {
        return R.id.main_nav_host
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.home_bottom_nav)
        bottomNavigationView?.setupWithNavController(navController)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onShowBottomNav(e: ShowBottomNavEvent) {
        if (bottomNavigationView!!.visibility == View.GONE) {
            bottomNavigationView!!.visibility = View.VISIBLE
            if (e.animate) {
                bottomNavigationView!!.clearAnimation()
                bottomNavigationView!!.animate()
                    .setListener(null)
                    .alpha(1f)
                    .translationY(0f)
                    .duration = BOTTOM_NAV_HIDE_SHOW_ANIMATION_DURATION
            } else {
                bottomNavigationView!!.translationY = 0f
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onHideBottomNav(e: HideBottomNavEvent) {
        if (bottomNavigationView!!.visibility == View.VISIBLE) {
            if (e.animate) {
                bottomNavigationView!!.clearAnimation()
                bottomNavigationView!!.animate()
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationEnd(animation: Animator?) {
                            bottomNavigationView!!.visibility = View.GONE
                        }

                        override fun onAnimationStart(animation: Animator?) {}
                        override fun onAnimationRepeat(animation: Animator?) {}
                        override fun onAnimationCancel(animation: Animator?) {}
                    })
                    .alpha(0f)
                    .translationY(bottomNavigationView!!.height.toFloat())
                    .duration = BOTTOM_NAV_HIDE_SHOW_ANIMATION_DURATION
            } else {
                bottomNavigationView!!.translationY = bottomNavigationView!!.height.toFloat()
                bottomNavigationView!!.visibility = View.GONE
            }
        }
    }
}
