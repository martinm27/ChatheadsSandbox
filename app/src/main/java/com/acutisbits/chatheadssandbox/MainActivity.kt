package com.acutisbits.chatheadssandbox

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

const val ONE_SECOND_IN_MILLISECONDS = 1000L

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            when {
                ContextCompat.checkSelfPermission(this, Settings.ACTION_MANAGE_OVERLAY_PERMISSION) == PackageManager.PERMISSION_GRANTED -> {
                    initializeView()
                }
                ActivityCompat.shouldShowRequestPermissionRationale(this, Settings.ACTION_MANAGE_OVERLAY_PERMISSION) -> {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected. In this UI,
                    // include a "cancel" or "no thanks" button that allows the user to
                    // continue using your app without granting the permission.
                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }
        } else {
            initializeView()
        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            initializeView()
        }
    }

    /**
     * Set and initialize the view elements.
     */
    private fun initializeView() {
        val incomingCallTime = findViewById<TextView>(R.id.incoming_call_time)

        IncomingCallTimeUtility.incomingCallTime().observe(this, {
            incomingCallTime.text = it
        })

        findViewById<View>(R.id.notify_me).setOnClickListener {
            IncomingCallTimeUtility.startTimer()
        }

        findViewById<View>(R.id.end_call).setOnClickListener {
            stopService(Intent(this@MainActivity, ChatHeadService::class.java))
            IncomingCallTimeUtility.reset()
        }
    }

    override fun onStop() {
        if (IncomingCallTimeUtility.isCallActive()) {
            val intent = Intent(this@MainActivity, ChatHeadService::class.java)
            startService(intent)
            finish()
        }
        super.onStop()
    }
}
