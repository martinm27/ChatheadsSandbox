package com.acutisbits.chatheadssandbox

import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

class ChatHeadTouchListener(
    private val chatHeadView: View,
    private val windowManager: WindowManager,
    private val params: WindowManager.LayoutParams,
    private val onEndInteractionAction: () -> Unit
    ) : View.OnTouchListener {

    private var lastAction = 0
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {

                //remember the initial position.
                initialX = params.x
                initialY = params.y

                //get the touch location
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                lastAction = event.action
                return true
            }
            MotionEvent.ACTION_UP -> {
                //As we implemented on touch listener with ACTION_MOVE,
                //we have to check if the previous action was ACTION_DOWN
                //to identify if the user clicked the view or not.
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    //close the service and remove the chat heads
                    onEndInteractionAction()
                }
                lastAction = event.action
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                //Calculate the X and Y coordinates of the view.
                params.x = initialX + (event.rawX - initialTouchX).toInt()
                params.y = initialY + (event.rawY - initialTouchY).toInt()

                //Update the layout with new X & Y coordinate
                windowManager.updateViewLayout(chatHeadView, params)
                lastAction = event.action
                return true
            }
        }
        return false
    }
}
