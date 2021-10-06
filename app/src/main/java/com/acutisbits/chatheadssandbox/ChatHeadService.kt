package com.acutisbits.chatheadssandbox

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.lifecycle.LifecycleService

class ChatHeadService : LifecycleService() {

    private var windowManager: WindowManager? = null
    private var chatHeadView: View? = null

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()

        chatHeadView = LayoutInflater.from(this).inflate(R.layout.chat_head_layout, null)

        val chatHeadParams = buildChatHeadWindowParams()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager?
        windowManager?.addView(chatHeadView, chatHeadParams)

        //Drag and move chat head using user's touch action.
        val chatHeadImage = chatHeadView?.findViewById(R.id.chat_head_profile_iv) as ImageView
        chatHeadImage.setOnTouchListener(
            ChatHeadTouchListener(
                chatHeadView!!,
                windowManager!!,
                chatHeadParams
            ) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                startActivity(intent)
                stopSelf()
            }
        )
    }

    private fun buildChatHeadWindowParams(): WindowManager.LayoutParams =
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 200
        }

    override fun onDestroy() {
        if (chatHeadView != null) windowManager?.removeView(chatHeadView)
        super.onDestroy()
    }
}
