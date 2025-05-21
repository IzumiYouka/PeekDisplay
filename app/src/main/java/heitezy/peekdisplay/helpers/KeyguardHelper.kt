package heitezy.peekdisplay.helpers

import android.app.Activity
import android.app.KeyguardManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.WindowManager

object KeyguardHelper {

    fun dismissKeyguard(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val km = activity.getSystemService(KeyguardManager::class.java)
            if (km.isKeyguardLocked) {
                km.requestDismissKeyguard(activity, object : KeyguardManager.KeyguardDismissCallback() {
                    override fun onDismissSucceeded() {
                        activity.finish()
                    }
                })
            } else {
                activity.finish()
            }
        } else {
            // It will not work for Android N secure keyguard (PIN/Pattern/etc.)
            activity.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
            Handler(Looper.getMainLooper()).postDelayed({
                activity.finish()
            }, 100)
        }
    }
} 