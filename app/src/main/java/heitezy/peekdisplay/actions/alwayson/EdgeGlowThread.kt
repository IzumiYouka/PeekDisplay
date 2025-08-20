package heitezy.peekdisplay.actions.alwayson

import android.app.Activity
import android.graphics.drawable.TransitionDrawable
import android.util.Log
import heitezy.peekdisplay.helpers.Global
import heitezy.peekdisplay.helpers.P
import heitezy.peekdisplay.services.NotificationService

class EdgeGlowThread(
    private val activity: Activity,
    private val background: TransitionDrawable?,
) : Thread() {
    @JvmField
    @Volatile
    internal var notificationAvailable: Boolean = false

    override fun run() {
        val transitionTime =
            P.getPreferences(activity).getInt(
                P.EDGE_GLOW_DURATION,
                P.EDGE_GLOW_DURATION_DEFAULT,
            )
        val transitionDelay =
            P.getPreferences(activity).getInt(
                P.EDGE_GLOW_DELAY,
                P.EDGE_GLOW_DELAY_DEFAULT,
            )
        try {
            while (!isInterrupted) {
                if (notificationAvailable) {
                    activity.runOnUiThread { background?.startTransition(transitionTime) }
                    sleep(transitionTime.toLong())
                    activity.runOnUiThread {
                        background?.reverseTransition(transitionTime)
                    }
                    sleep((transitionTime + transitionDelay).toLong())
                } else {
                    sleep(NotificationService.MINIMUM_UPDATE_DELAY)
                }
            }
        } catch (exception: InterruptedException) {
            Log.w(Global.LOG_TAG, exception.toString())
        }
    }
}
