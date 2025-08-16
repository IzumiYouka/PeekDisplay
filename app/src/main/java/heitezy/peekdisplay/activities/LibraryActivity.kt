package heitezy.peekdisplay.activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import heitezy.peekdisplay.R

class LibraryActivity : BaseActivity() {
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
            .replace(R.id.settings, GeneralPreferenceFragment())
            .commit()
    }

    class GeneralPreferenceFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(
            savedInstanceState: Bundle?,
            rootKey: String?,
        ) {
            addPreferencesFromResource(R.xml.pref_about_list)
            preferenceScreen.removeAll()
            val libraries = resources.getStringArray(R.array.about_libraries)
            val licenses = resources.getStringArray(R.array.about_libraries_licenses)
            if (libraries.size != licenses.size) error("Library array size does not match license array size.")
            for (index in libraries.indices) {
                preferenceScreen.addPreference(
                    Preference(requireContext()).apply {
                        icon =
                            ResourcesCompat.getDrawable(
                                requireContext().resources,
                                R.drawable.ic_about_library,
                                requireContext().theme,
                            )
                        title = libraries[index]
                        summary = licenses[index]
                    },
                )
            }
        }
    }
}
