package com.core.sample

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_fast.*


class FastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)


//        setContentView(R.layout.activity_main)
//
//        val rotate = AnimationUtils.loadAnimation(this, R.anim.splash_screen_rotate)
//        rotate.interpolator = WaveInterpolator()
//
//        rotate.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationRepeat(animation: Animation?) { }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                findViewById<View>(R.id.message).visibility = VISIBLE
////                val move = AnimationUtils.loadAnimation(this@FastActivity, R.anim.up_down)
////                image.startAnimation(move)
//            }
//
//            override fun onAnimationStart(animation: Animation?) { }
//
//        })
//        val image = findViewById<View>(R.id.image)
//        image.startAnimation(rotate)
//
//        val root = findViewById<View>(R.id.root)
//        val colorAnim = ObjectAnimator.ofInt(root, "backgroundColor",
//            resources.getColor(R.color.gray1), resources.getColor(R.color.gray2))
//        colorAnim.setDuration(2000)
//        colorAnim.setEvaluator(ArgbEvaluator())
//        colorAnim.start()
////        blinkAnimation(root)
//
////        val systemInfo = findViewById<TextView>(R.id.systemInfo)
////        systemInfo.text = "${launchTime} ms"
    }

    private fun blinkAnimation(view: View) {
        val colorAnim = ObjectAnimator.ofInt(view, "backgroundColor",
            resources.getColor(R.color.gray1), resources.getColor(R.color.gray2));
        colorAnim.setDuration(2000)
        colorAnim.setEvaluator(ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }
}