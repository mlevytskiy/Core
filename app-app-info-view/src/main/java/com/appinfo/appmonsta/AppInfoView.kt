package com.appinfo.appmonsta

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import wumf.com.appsprovider2.App
import wumf.com.appsprovider2.AppContainer
import wumf.com.appsprovider2.GooglePlayApp
import java.io.File

class AppInfoView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val textView: TextView
    private val imageView: ImageView

    init {
        orientation = VERTICAL
        gravity = CENTER
        setBackgroundResource(R.drawable.app_background)
        val view = View.inflate(getContext(), R.layout.view_app_info, this)
        textView = view.findViewById(R.id.text_view)
        imageView = view.findViewById(R.id.image_view)
    }

    fun setModel(model: AppInfoModelFromPhone) {
        textView.setText(model.appName)
        textView.setTextColor(Color.BLACK)
        imageView.setImageURI(Uri.fromFile(File(model.pathToIconFile)))
        imageView.setBackgroundColor(Color.TRANSPARENT)
    }

    fun setModel(model: AppContainer) {
        model.app?.let {
            setModel(it)
        } ?:run {
            model.gpApp?.let {
                setModel(it)
            } ?:kotlin.run {
                clearView()
            }
        }
    }

    fun clearView() {
        textView.setText("")
        imageView.setImageDrawable(null)
        imageView.setBackgroundColor(Color.LTGRAY)
    }

    fun setModel(model: App) {
        textView.setText(model.name)
        textView.setTextColor(Color.BLACK)
        model.icon?.let {
            imageView.setImageDrawable(model.icon)
            imageView.setBackgroundColor(Color.TRANSPARENT)
        } ?:run {
            imageView.setImageDrawable(null)
            imageView.setBackgroundColor(Color.LTGRAY)
        }
    }

    fun setModel(model: GooglePlayApp) {
        textView.setText(model.name)
        Glide.with(this).load(model.iconUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.setBackgroundColor(Color.TRANSPARENT)
                    textView.setTextColor(Color.BLACK)
                    return false
                }
            })
            .into(imageView)
    }
}