package heitezy.peekdisplay.actions.alwayson.draw

import android.graphics.Canvas
import heitezy.peekdisplay.actions.alwayson.AlwaysOnCustomView
import heitezy.peekdisplay.helpers.P
import java.text.SimpleDateFormat

object Date {
    internal fun draw(
        canvas: Canvas,
        utils: Utils,
        flags: BooleanArray,
        tempHeight: Float,
        dateFormat: SimpleDateFormat,
        width: Int,
    ) {
        if (flags[AlwaysOnCustomView.FLAG_SAMSUNG_3]) {
            utils.viewHeight =
                tempHeight + utils.getVerticalCenter(utils.getPaint(utils.bigTextSize)) + utils.topPadding
        }
        val date =
            dateFormat.format(System.currentTimeMillis()).run {
                if (flags[AlwaysOnCustomView.FLAG_CAPS_DATE]) {
                    this.uppercase()
                } else {
                    this
                }
            }
        utils.drawRelativeText(
            canvas,
            date,
            if (flags[AlwaysOnCustomView.FLAG_MOTO]) {
                (width * 0.1f).toInt() + utils.topPadding
            } else {
                utils.padding2
            },
            utils.padding2,
            utils.getPaint(
                if (flags[AlwaysOnCustomView.FLAG_BIG_DATE]) utils.bigTextSize else utils.mediumTextSize,
                utils.prefs.get(P.DISPLAY_COLOR_DATE, P.DISPLAY_COLOR_DATE_DEFAULT),
            ),
            if (flags[AlwaysOnCustomView.FLAG_SAMSUNG_3]) {
                utils.paint.measureText(date).toInt() / 2 + utils.padding16
            } else {
                0
            },
        )
        if (flags[AlwaysOnCustomView.FLAG_SAMSUNG_3]) {
            utils.viewHeight =
                tempHeight + utils.getTextHeight(utils.bigTextSize) + utils.padding16
        }
    }
}
