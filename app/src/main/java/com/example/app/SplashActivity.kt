package com.example.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen
import com.example.app.R
import kotlinx.coroutines.*

class SplashActivity : ComponentActivity() {
    private val splashScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashScope.launch {
            delay(800)
            val isLoggedIn = getSharedPreferences("bestmeal_prefs", MODE_PRIVATE)
                .getBoolean("logged_in", false)
            val next = if (isLoggedIn) MainActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this@SplashActivity, next))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        splashScope.cancel()
    }
}
