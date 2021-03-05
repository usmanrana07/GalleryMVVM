package com.usman.gallerydemo.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.usman.gallerydemo.R
import com.usman.gallerydemo.ui.dashboard.DashBoardActivity
import com.usman.gallerydemo.ui.gallery.GalleryActivity
import com.usman.gallerydemo.utils.launchActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        waitToGo()
    }

    private fun waitToGo() {
        lifecycleScope.launch {
            delay(1000)
            launchActivity(DashBoardActivity::class.java, shouldFinish = true)
        }
    }
}