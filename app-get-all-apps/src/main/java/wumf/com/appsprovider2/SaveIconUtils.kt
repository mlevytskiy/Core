package wumf.com.appsprovider2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by max on 30.03.16.
 */
class SaveIconUtils(density: Int) {
    private val iconHeight: Int

    constructor(context: Context) : this(context.resources.displayMetrics.densityDpi) {}

    init {
        this.iconHeight = calculateIconHeight(density)
    }

    fun saveInFileInWithGoodQuality(file: File, drawable: Drawable) {
        val bitmap = drawableToBitmapWithGoodQuality(drawable)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            file.setReadable(false)
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 70, out)
            out.flush()
            file.setReadable(true)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    //do nothing
                }

            }
        }
    }

    private fun calculateIconHeight(density: Int): Int {
        return if (density == DisplayMetrics.DENSITY_LOW) {
            ICON_SIZE_LDPI
        } else if (density == DisplayMetrics.DENSITY_MEDIUM) {
            ICON_SIZE_MDPI
        } else if (density == DisplayMetrics.DENSITY_TV) {
            ICON_SIZE_TVDPI
        } else if (density == DisplayMetrics.DENSITY_HIGH) {
            ICON_SIZE_HDPI
        } else if (density == DisplayMetrics.DENSITY_XHIGH) {
            ICON_SIZE_XHDPI
        } else {
            ICON_SIZE_XXHDPI
        }
    }

    private fun drawableToBitmapWithGoodQuality(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null

        if (drawable is BitmapDrawable) {
            bitmap = drawable.bitmap
            if (bitmap != null) {
                if (drawable.intrinsicHeight > iconHeight || drawable.intrinsicWidth > iconHeight) {
                    bitmap =
                        Bitmap.createScaledBitmap(bitmap, iconHeight, iconHeight, false)
                } else {
                    //do nothing
                }
            }
        } else {
            if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                bitmap = Bitmap.createBitmap(
                    1,
                    1,
                    Bitmap.Config.ARGB_8888
                ) // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            }

            val canvas = Canvas(bitmap!!)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }

        return bitmap
    }

    companion object {

        private val ICON_SIZE_LDPI = 36
        private val ICON_SIZE_MDPI = 48
        private val ICON_SIZE_TVDPI = 64
        private val ICON_SIZE_HDPI = 72
        private val ICON_SIZE_XHDPI = 96
        private val ICON_SIZE_XXHDPI = 144
    }
}
