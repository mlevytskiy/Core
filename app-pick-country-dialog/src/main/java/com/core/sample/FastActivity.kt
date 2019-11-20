package com.core.sample

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fortest.something.feature.onboarding.showCountriesDialog
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.TextView
import android.widget.Toast
import java.lang.reflect.InvocationTargetException


class FastActivity : AppCompatActivity() {

    private var dialog: DialogInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fast)

        if (CountriesHolder.countries.isEmpty()) {
            CountriesHolder.syncLoad(assetManager = assets, resources = resources, packageName = packageName)
        }

        val textView = findViewById<TextView>(R.id.default_country)
        val deviceCountryCode = getDeviceCountryCode(this, CountriesHolder.countries)
        textView.setText(deviceCountryCode)


    }

    fun onClickShowDialog(view: View) {
        if (CountriesHolder.countries.isEmpty()) {
            CountriesHolder.syncLoad(assetManager = assets, resources = resources, packageName = packageName)
        }

        dialog = showCountriesDialog(this, CountriesHolder.countries, -1,
            { country->
                Toast.makeText(this, "Pick country ${country.name}", Toast.LENGTH_LONG).show()
                dialog?.dismiss()
            },

            { dialog-> dialog.dismiss() } )
    }

    private fun getDeviceCountryCode(context: Context, countries: ArrayList<Country>): String {
        var countryCode: String?

        // try to get country code from TelephonyManager service
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (tm != null) {
            // query first getSimCountryIso()
            countryCode = tm.simCountryIso
            if (countryCode != null && countryCode.length == 2)
                return countryCode.toLowerCase()

            if (tm.phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                // special case for CDMA Devices
                countryCode = getCDMACountryIso(countries)
            } else {
                // for 3G devices (with SIM) query getNetworkCountryIso()
                countryCode = tm.networkCountryIso
            }

            if (countryCode != null && countryCode.length == 2)
                return countryCode.toLowerCase()
        }

        // if network country not available (tablets maybe), get country code from Locale class
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryCode = context.resources.configuration.locales.get(0).country
        } else {
            countryCode = context.resources.configuration.locale.country
        }

        return if (countryCode != null && countryCode.length == 2) countryCode.toLowerCase() else "us"
    }

    @SuppressLint("PrivateApi")
    private fun getCDMACountryIso(countries: ArrayList<Country>): String? {
        try {

            val systemProperties = Class.forName("android.os.SystemProperties")
            val get = systemProperties.getMethod("get", String::class.java)

            val homeOperator = get.invoke(
                systemProperties,
                "ro.cdma.home.operator.numeric"
            ) as String

            val mcc = Integer.parseInt(homeOperator.substring(0, 3))
            countries.find { it.telCode == mcc }?.let {
                return it.code
            } ?:run {
                return null
            }
        } catch (ignored: ClassNotFoundException) {
        } catch (ignored: NoSuchMethodException) {
        } catch (ignored: IllegalAccessException) {
        } catch (ignored: InvocationTargetException) {
        } catch (ignored: NullPointerException) {
        }

        return null
    }

}