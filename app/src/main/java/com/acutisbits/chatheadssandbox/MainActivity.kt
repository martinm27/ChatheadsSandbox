package com.acutisbits.chatheadssandbox

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            val activityResultRegistration = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                //Check if the permission is granted or not.
                if (it) {
                    initializeView()
                } else { //Permission is not available
                    Toast.makeText(
                        this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
            activityResultRegistration.launch(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        } else {
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
