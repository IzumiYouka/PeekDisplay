package heitezy.peekdisplay.actions.alwayson.draw

import android.graphics.Canvas
import heitezy.peekdisplay.helpers.P
import heitezy.peekdisplay.services.NotificationService

object NotificationCount {
    internal fun draw(
        canvas: Canvas,
        utils: Utils,
    ) {
        utils.drawRelativeText(
            canvas,
            if (NotificationService.count != 0) NotificationService.count.toString() else "",
            utils.padding16,
            utils.padding16,
            utils.getPaint(
                utils.mediumTextSize,
                utils.prefs.get(P.DISPLAY_COLOR_NOTIFICATION, P.DISPLAY_COLOR_NOTIFICATION_DEFAULT),
            ),
        )
    }
}
