package heitezy.peekdisplay.activities

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.SwitchPreference
import heitezy.peekdisplay.R
import heitezy.peekdisplay.custom.BasePreferenceFragment
import heitezy.peekdisplay.helpers.P
import heitezy.peekdisplay.helpers.PreferenceScreenHelper

class LAFBehaviorActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Handle window insets for the main container
        val rootView = findViewById<FrameLayout>(R.id.settings)
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

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, PreferenceFragment())
            .commit()
    }

    class PreferenceFragment : BasePreferenceFragment() {
        override fun onCreatePreferences(
            savedInstanceState: Bundle?,
            rootKey: String?,
        ) {
            addPreferencesFromResource(R.xml.pref_laf_behavior)
            checkPermissions()
            PreferenceScreenHelper.linkPreferenceToActivity(
                this,
                P.FORCE_BRIGHTNESS,
                Intent(requireContext(), LAFBrightnessActivity::class.java),
            )
            if (preferenceManager.sharedPreferences?.getBoolean(P.ROOT_MODE, false) != true) {
                findPreference<SwitchPreference>(P.POWER_SAVING_MODE)?.isEnabled = false
                findPreference<SwitchPreference>(P.DISABLE_HEADS_UP_NOTIFICATIONS)
                    ?.isEnabled = false
            }
        }
    }
}
