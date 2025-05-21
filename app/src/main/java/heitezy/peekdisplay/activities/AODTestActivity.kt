package heitezy.peekdisplay.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import heitezy.peekdisplay.R
import heitezy.peekdisplay.actions.alwayson.AlwaysOnCustomView

@Suppress("MagicNumber")
class AODTestActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aod_test)

        Toast.makeText(this, "testing mode", Toast.LENGTH_SHORT).show()

        val view = findViewById<AlwaysOnCustomView>(R.id.view)
        view.setBatteryStatus(100, false)
        view.musicString = "Artist - Song"
        view.onSkipPreviousClicked = {
            Toast.makeText(this, "left", Toast.LENGTH_SHORT).show()
        }
        view.onSkipNextClicked = {
            Toast.makeText(this, "right", Toast.LENGTH_SHORT).show()
        }
        view.onTitleClicked = {
            Toast.makeText(this, "center", Toast.LENGTH_SHORT).show()
        }
    }
}
