package com.core.sample

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.core.sample.memory.UserInfoRepository
import com.core.sample.util.WaveInterpolator
import java.util.*


class FastActivity : Activity() {

    private var repository: UserInfoRepository? = null

    private fun show(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = (application as App).repository
        val languageNumber = repository!!.getLanguage()
        if (languageNumber.toInt() != -1) {
            var str = getLanguage(languageNumber.toInt())
            updateLanguage(str)
        }
        setContentView(R.layout.activity_fast)

        if (repository!!.isEmpty()) {
            findViewById<View>(R.id.root).setOnApplyWindowInsetsListener { v, insets ->
                repository!!.setStatusBarHeight(insets.systemWindowInsetTop);
                insets
            }
        }

        val rotate = AnimationUtils.loadAnimation(this, R.anim.splash_screen_rotate)
        rotate.interpolator = WaveInterpolator()

        val numberPicker = findViewById<NumberPicker>(R.id.number_picker)
        val go = findViewById<View>(R.id.go_button)

        val data = resources.getStringArray(R.array.languages)
        numberPicker.setMinValue(0)
        numberPicker.setMaxValue(data.size - 1)
        numberPicker.setDisplayedValues(data)
        setDividerColor(numberPicker, ContextCompat.getColor(this, R.color.blue))
        if (languageNumber.toInt() != -1) {
            numberPicker.value = languageNumber.toInt()
        }
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            var str = getLanguage(newVal)
            repository!!.setLanguage(newVal.toByte())
            updateLanguage(str)
//            setContentView(R.layout.activity_fast)
//            val text = "Changed from $oldVal to $newVal"
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }


        val image = findViewById<View>(R.id.image)

        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) { }

            override fun onAnimationEnd(animation: Animation?) {
                findViewById<View>(R.id.message).visibility = VISIBLE
                numberPicker.visibility = VISIBLE
                go.visibility = VISIBLE
//                val move = AnimationUtils.loadAnimation(this@FastActivity, R.anim.up_down)
//                image.startAnimation(move)
            }

            override fun onAnimationStart(animation: Animation?) { }

        })
        image.startAnimation(rotate)

        val root = findViewById<View>(R.id.root)
        val colorAnim = ObjectAnimator.ofInt(root, "backgroundColor",
            resources.getColor(R.color.gray1), resources.getColor(R.color.gray2))
        colorAnim.setDuration(3000)
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.start()
//        blinkAnimation(root)

//        val systemInfo = findViewById<TextView>(R.id.systemInfo)
//        systemInfo.text = "${launchTime} ms"
    }

    fun onGoClick(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun updateLanguage(language: String) {
        val res = baseContext.resources
        val dm = res.getDisplayMetrics()
        val conf = res.getConfiguration()

        if (conf.locale.language == language) {
            return //just skip
        }

        val locale = Locale(language, conf.locale.country)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(locale)
        } else {
            conf.locale = locale
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            applicationContext.createConfigurationContext(conf)
        }
        baseContext.resources.updateConfiguration(conf, dm)

        findViewById<TextView?>(R.id.message)?.setText(R.string.hello_words)
        findViewById<Button?>(R.id.go_button)?.setText(R.string.go)
//        setContentView(R.layout.activity_fast)
    }

    private fun getLanguage(number: Int): String {
        var str  = "en"
        if (number == 0) {
            str  = "en"
        } else if (number == 1) {
            str  = "ru"
        }
        return str
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

    private fun setDividerColor(picker: NumberPicker, color: Int) {

        val pickerFields = NumberPicker::class.java.declaredFields
        for (pf in pickerFields) {
            if (pf.name == "mSelectionDivider") {
                pf.isAccessible = true
                try {
                    val colorDrawable = ColorDrawable(color)
                    pf.set(picker, colorDrawable)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                break
            }
        }
    }
}