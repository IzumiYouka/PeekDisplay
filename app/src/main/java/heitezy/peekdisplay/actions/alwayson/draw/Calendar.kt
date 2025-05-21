package heitezy.peekdisplay.actions.alwayson.draw

import android.graphics.Canvas
import heitezy.peekdisplay.helpers.P

object Calendar {
    internal fun draw(
        canvas: Canvas,
        utils: Utils,
        events: List<String>,
    ) {
        utils.viewHeight += utils.padding16
        for (it in events) {
            utils.drawRelativeText(
                canvas,
                it,
                utils.padding2,
                utils.padding2,
                utils.getPaint(
                    utils.smallTextSize,
                    utils.prefs.get(P.DISPLAY_COLOR_CALENDAR, P.DISPLAY_COLOR_CALENDAR_DEFAULT),
                ),
            )
        }
        utils.viewHeight += utils.padding16
    }
}
