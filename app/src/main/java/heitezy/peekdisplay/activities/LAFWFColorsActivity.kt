package heitezy.peekdisplay.activities

import android.os.Bundle
import heitezy.peekdisplay.R
import heitezy.peekdisplay.custom.BasePreferenceFragment

class LAFWFColorsActivity : BaseActivity() {
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
            addPreferencesFromResource(R.xml.pref_laf_wf_colors)
            checkPermissions()
        }
    }
}
