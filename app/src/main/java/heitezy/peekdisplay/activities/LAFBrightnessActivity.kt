package heitezy.peekdisplay.activities

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import heitezy.peekdisplay.R
import heitezy.peekdisplay.actions.alwayson.AlwaysOn
import heitezy.peekdisplay.helpers.P

class LAFBrightnessActivity : BaseActivity() {
    private lateinit var brightnessSwitch: SwitchCompat
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brightness)

        // Handle window insets for the main container
        val rootView = findViewById<RelativeLayout>(R.id.brightness)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )

            view.setPadding(
                insets.left,
                insets.top,
                insets.right,
                insets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        brightnessSwitch = findViewById(R.id.brightnessSwitch)
        seekBar = findViewById(R.id.seekBar)
        seekBar.progress = P.FORCE_BRIGHTNESS_VALUE_DEFAULT

        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    // Turning this into a single statement will not work!
                    val attributes = window.attributes
                    attributes.screenBrightness = (progress / FULL_BRIGHTNESS).toFloat()
                    window.attributes = attributes
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // Do nothing.
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // Do nothing.
                }
            },
        )
    }

    override fun onStart() {
        super.onStart()
        brightnessSwitch.isChecked = P.getPreferences(this).getBoolean(P.FORCE_BRIGHTNESS, P.FORCE_BRIGHTNESS_DEFAULT)
        seekBar.progress = P.getPreferences(this).getInt(P.FORCE_BRIGHTNESS_VALUE, P.FORCE_BRIGHTNESS_VALUE_DEFAULT)
    }

    override fun onStop() {
        super.onStop()
        P.getPreferences(this)
            .edit {
                putBoolean(P.FORCE_BRIGHTNESS, brightnessSwitch.isChecked)
                putInt(P.FORCE_BRIGHTNESS_VALUE, seekBar.progress)
            }
        AlwaysOn.finish()
    }

    companion object {
        private const val FULL_BRIGHTNESS = 100.0
    }
}
