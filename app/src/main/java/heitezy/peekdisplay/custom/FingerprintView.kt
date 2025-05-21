package heitezy.peekdisplay.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.preference.PreferenceManager
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import heitezy.peekdisplay.R
import heitezy.peekdisplay.helpers.P
import kotlin.math.sqrt

class FingerprintView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : View(context, attrs, defStyleAttr) {
        
        interface OnFingerprintTouchListener {
            fun onFingerprintTouchStateChanged(isTouched: Boolean, event: MotionEvent)
        }
        
        private var touchListener: OnFingerprintTouchListener? = null
        private var isTouched = false
        private var initialX = 0f
        private var initialY = 0f
        private var isLockOpen = false
        private var currentIconResource = R.drawable.ic_fingerprint_white
        
        fun setOnFingerprintTouchListener(listener: OnFingerprintTouchListener) {
            this.touchListener = listener
        }
        
        override fun onTouchEvent(event: MotionEvent): Boolean {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val useLockIcon = prefs.getBoolean(P.LOCK_ICON, P.LOCK_ICON_DEFAULT)
            
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isTouched = true
                    initialX = event.rawX
                    initialY = event.rawY
                    if (useLockIcon) {
                        isLockOpen = false
                        invalidate()
                    }
                    touchListener?.onFingerprintTouchStateChanged(true, event)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isTouched = false
                    touchListener?.onFingerprintTouchStateChanged(false, event)
                }
                MotionEvent.ACTION_MOVE -> {
                    if (useLockIcon) {
                        val dx = event.rawX - initialX
                        val dy = event.rawY - initialY
                        val distance = sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                        
                        // Update lock state based on distance threshold
                        val newLockState = distance > 300
                        if (newLockState != isLockOpen) {
                            isLockOpen = newLockState
                            invalidate()
                        }
                    }
                    touchListener?.onFingerprintTouchStateChanged(true, event)
                }
            }
            return true
        }
        
        override fun performClick(): Boolean {
            super.performClick()
            return true
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val useLockIcon = prefs.getBoolean(P.LOCK_ICON, P.LOCK_ICON_DEFAULT)
            
            // Determine which icon to use
            currentIconResource = when {
                useLockIcon && isLockOpen -> R.drawable.ic_lock_open
                useLockIcon -> R.drawable.ic_lock
                else -> R.drawable.ic_fingerprint_white
            }
            
            VectorDrawableCompat.create(resources, currentIconResource, null)?.run {
                setTint(
                    prefs.getInt(
                        P.DISPLAY_COLOR_FINGERPRINT,
                        P.DISPLAY_COLOR_FINGERPRINT_DEFAULT,
                    ),
                )
                setBounds(0, 0, width, height)
                draw(canvas)
            }
        }
    }
