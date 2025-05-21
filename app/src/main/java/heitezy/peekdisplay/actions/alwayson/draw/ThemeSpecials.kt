package heitezy.peekdisplay.actions.alwayson.draw

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import heitezy.peekdisplay.R
import heitezy.peekdisplay.actions.alwayson.AlwaysOnCustomView
import heitezy.peekdisplay.helpers.P

object ThemeSpecials {
    internal fun drawDivider(
        canvas: Canvas,
        utils: Utils,
        flags: BooleanArray,
        tempHeight: Float,
    ) {
        if (flags[AlwaysOnCustomView.FLAG_SAMSUNG_3] && (
                utils.prefs.get(
                    P.SHOW_CLOCK,
                    P.SHOW_CLOCK_DEFAULT,
                ) || utils.prefs.get(P.SHOW_DATE, P.SHOW_DATE_DEFAULT)
            )
        ) {
            canvas.drawRect(
                utils.horizontalRelativePoint - utils.padding2 / 2,
                tempHeight + utils.padding16 * 2 + utils.topPadding,
                utils.horizontalRelativePoint + utils.padding2 / 2,
                utils.viewHeight - utils.padding16 + utils.topPadding,
                utils.getPaint(utils.bigTextSize, Color.WHITE),
            )
        }
    }

    internal fun drawBatteryCircle(
        canvas: Canvas,
        utils: Utils,
        flags: BooleanArray,
        width: Int,
        batteryPercent: Int,
    ) {
        if (flags[AlwaysOnCustomView.FLAG_MOTO] && (
                utils.prefs.get(
                    P.SHOW_BATTERY_ICON,
                    P.SHOW_BATTERY_ICON_DEFAULT
                ) || utils.prefs.get(P.SHOW_BATTERY_PERCENTAGE, P.SHOW_BATTERY_PERCENTAGE_DEFAULT)
            )
        ) {
            // M-Style: Draw circular progress, time, date, and battery percent in the center
            val centerX = width / 2f
            val radius = width * 0.3f
            val strokeWidth = utils.dpToPx(4f)
            val angle = 340f * batteryPercent / 100f

            // Draw background circle
            val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                color = 0x33FFFFFF
                this.strokeWidth = strokeWidth
            }
            val oval = android.graphics.RectF(centerX - radius, strokeWidth / 2 + utils.topPadding, centerX + radius, radius * 2 + strokeWidth / 2 + utils.topPadding)
            canvas.drawArc(oval, -260f, 80f, false, bgPaint)

            // Draw progress arc
            val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                color = 0xFF00E5FF.toInt()
                this.strokeWidth = strokeWidth
                strokeCap = Paint.Cap.ROUND
            }
            canvas.drawArc(oval, -260f, angle, false, arcPaint)

            // Draw lightning icon at the bottom (placeholder)
            utils.drawVector(canvas,
                R.drawable.ic_charging_white,
                (width / 2f).toInt(),
                (radius * 2).toInt() + utils.topPadding,
                0xFFFFFFFF.toInt(),
            )
        }
    }
}
