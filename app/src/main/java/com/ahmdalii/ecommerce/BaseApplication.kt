package com.ahmdalii.ecommerce

import android.app.Application
import com.ahmdalii.ecommerce.utils.Constants.BASE_URL
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        // Used to load the 'ecommerce' library on application startup.
        init {
            System.loadLibrary("ecommerce")
        }
    }

    override fun onCreate() {
        super.onCreate()

        BASE_URL = getBaseURL()
    }

    /**
     * A native method that is implemented by the 'ecommerce' native library,
     * which is packaged with this application.
     */
    private external fun getBaseURL(): String
}