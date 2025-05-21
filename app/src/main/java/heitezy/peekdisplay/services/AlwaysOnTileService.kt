package heitezy.peekdisplay.services

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import heitezy.peekdisplay.helpers.Global

class AlwaysOnTileService : TileService() {
    override fun onStartListening() {
        super.onStartListening()
        updateTile(Global.currentAlwaysOnState(this))
    }

    override fun onClick() {
        updateTile(Global.changeAlwaysOnState(this))
    }

    private fun updateTile(isActive: Boolean) {
        qsTile.state =
            if (isActive) {
                Tile.STATE_ACTIVE
            } else {
                Tile.STATE_INACTIVE
            }
        qsTile.updateTile()
    }
}
