package heitezy.peekdisplay.actions

import android.os.Bundle
import android.widget.ImageView
import heitezy.peekdisplay.R

class ChargingFlashActivity : OffActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charging_flash)

        turnOnScreen()
        fullscreen(findViewById(R.id.chargingLayout))

        object : Thread() {
            override fun run() {
                sleep(ANIMATION_DELAY)
                findViewById<ImageView>(R.id.chargingImage).animate().alpha(0f).duration = ANIMATION_DURATION
                sleep(ANIMATION_DURATION)
                runOnUiThread {
                    finishAndOff()
                }
            }
        }.start()
    }

    companion object {
        private const val ANIMATION_DELAY = 1500L
        private const val ANIMATION_DURATION = 1000L
    }
}
