package heitezy.peekdisplay.custom

import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import heitezy.peekdisplay.R
import heitezy.peekdisplay.actions.alwayson.AlwaysOn
import heitezy.peekdisplay.helpers.Permissions

abstract class BasePreferenceFragment :
    PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    
    companion object {
        private var previousAmbientModeState: Boolean = false
        private var previousPickupModeState: Boolean = false
    }
    
    private fun setSummary(
        key: String,
        resId: Int,
    ) {
        val pref: Preference? = findPreference(key)
        pref?.apply {
            isEnabled = false
            setSummary(resId)
        }
        (pref as? SwitchPreference)?.apply {
            setSummaryOff(resId)
            setSummaryOn(resId)
        }
    }

    protected fun checkPermissions() {
        if (!Permissions.isNotificationServiceEnabled(requireContext())) {
            Permissions.NOTIFICATION_PERMISSION_PREFS.forEach {
                setSummary(it, R.string.permissions_notification_access)
            }
        }
        if (!Permissions.isDeviceAdminOrRoot(requireContext())) {
            Permissions.DEVICE_ADMIN_OR_ROOT_PERMISSION_PREFS.forEach {
                setSummary(it, R.string.permissions_device_admin_or_root)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(
        preferences: SharedPreferences,
        key: String?,
    ) {
        if (key == heitezy.peekdisplay.helpers.P.ALWAYS_ON) {
            val alwaysOnEnabled = preferences.getBoolean(key, false)
            val context = requireContext()

            if (alwaysOnEnabled) {
                ContextCompat.startForegroundService(
                    context,
                    android.content.Intent(
                        context,
                        heitezy.peekdisplay.services.ForegroundService::class.java
                    )
                )

                preferences.edit().apply {
                    putBoolean("rules_ambient_mode", previousAmbientModeState)
                    putBoolean("rules_pickup_mode", previousPickupModeState)
                    apply()
                }

                if (previousAmbientModeState) {
                    onSharedPreferenceChanged(preferences, "rules_ambient_mode")
                }
                if (previousPickupModeState) {
                    onSharedPreferenceChanged(preferences, "rules_pickup_mode")
                }
            } else {
                context.stopService(
                    android.content.Intent(context, heitezy.peekdisplay.services.ForegroundService::class.java)
                )
                heitezy.peekdisplay.services.PickUpService.stopService(context)
                
                previousAmbientModeState = preferences.getBoolean("rules_ambient_mode", false)
                previousPickupModeState = preferences.getBoolean("rules_pickup_mode", false)
                
                if (previousAmbientModeState) {
                    onSharedPreferenceChanged(preferences, "rules_ambient_mode")
                }
                if (previousPickupModeState) {
                    onSharedPreferenceChanged(preferences, "rules_pickup_mode")
                }
            }
        }
        
        AlwaysOn.finish()
    }
}
