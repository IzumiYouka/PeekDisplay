package heitezy.peekdisplay.helpers

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.service.quicksettings.TileService
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import heitezy.peekdisplay.receivers.AlwaysOnAppWidgetProvider
import heitezy.peekdisplay.services.AlwaysOnTileService
import heitezy.peekdisplay.services.ForegroundService

internal object Global {
    const val LOG_TAG: String = "PeekDisplay"

    const val ALWAYS_ON_STATE_CHANGED: String =
        "heitezy.peekdisplay.ALWAYS_ON_STATE_CHANGED"

    fun currentAlwaysOnState(context: Context): Boolean =
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .getBoolean(P.ALWAYS_ON, P.ALWAYS_ON_DEFAULT)

    fun changeAlwaysOnState(context: Context): Boolean {
        val value =
            !PreferenceManager.getDefaultSharedPreferences(context).getBoolean(P.ALWAYS_ON, P.ALWAYS_ON_DEFAULT)
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putBoolean(P.ALWAYS_ON, value)
        }
        TileService.requestListeningState(
            context,
            ComponentName(context, AlwaysOnTileService::class.java),
        )
        context.sendBroadcast(
            Intent(context, AlwaysOnAppWidgetProvider::class.java)
                .setAction(ALWAYS_ON_STATE_CHANGED),
        )
        
        if (value) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, ForegroundService::class.java)
            )
        } else {
            context.stopService(Intent(context, ForegroundService::class.java))
        }
        
        return value
    }
}
