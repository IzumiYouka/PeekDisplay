package heitezy.peekdisplay.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import heitezy.peekdisplay.R
import heitezy.peekdisplay.custom.BasePreferenceFragment
import heitezy.peekdisplay.helpers.P
import heitezy.peekdisplay.helpers.PreferenceScreenHelper

class LAFBackgroundActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
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
            addPreferencesFromResource(R.xml.pref_laf_background)
            checkPermissions()
            PreferenceScreenHelper.linkPreferenceToActivity(
                this,
                P.BACKGROUND_IMAGE,
                Intent(requireContext(), LAFBackgroundImageActivity::class.java),
            )
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                preferenceScreen.removePreference(
                    findPreference("hide_display_cutouts") ?: error("Invalid layout."),
                )
            }
        }
    }
}
