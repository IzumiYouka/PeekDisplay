package heitezy.peekdisplay.actions.alwayson.draw

import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Rect
import android.util.Log
import androidx.core.content.ContextCompat
import heitezy.peekdisplay.R
import heitezy.peekdisplay.helpers.Global
import heitezy.peekdisplay.helpers.P
import heitezy.peekdisplay.services.NotificationService
import kotlin.math.min

object NotificationIcons {
    private fun getNotificationRowLength(utils: Utils): Int {
        return when {
            utils.drawableSize == utils.dpToPx(Utils.DRAWABLE_SIZE_SMALL).toInt() -> 10
            utils.drawableSize == utils.dpToPx(Utils.DRAWABLE_SIZE).toInt() -> 7
            utils.drawableSize == utils.dpToPx(Utils.DRAWABLE_SIZE_ENLARGED).toInt() -> 6
            else -> 10 // Default fallback
        }
    }
    
    private fun getNotificationColumnLength(utils: Utils): Int {
        return when {
            utils.drawableSize == utils.dpToPx(Utils.DRAWABLE_SIZE_SMALL).toInt() -> 8
            utils.drawableSize == utils.dpToPx(Utils.DRAWABLE_SIZE).toInt() -> 6
            utils.drawableSize == utils.dpToPx(Utils.DRAWABLE_SIZE_ENLARGED).toInt() -> 5
            else -> 6 // Default fallback
        }
    }
    
    private const val NOTIFICATION_LIMIT: Int = 20
    private const val ICON_SPACING_INTERACTIVE: Float = 0.8f
    private const val ICON_SPACING_REGULAR: Float = 0.3f
    private const val RING_SPACING: Float = 0.7f
    var iconBounds = HashMap<Int, Rect>()
    private val highlightPaint = Paint().apply {
        style = Style.STROKE
        strokeWidth = 3f
        alpha = 0 // Make the highlightRect invisible but functional
    }

    // Get icon spacing based on interactive mode
    private fun getIconSpacing(utils: Utils): Float {
        return if (utils.prefs.get(P.INTERACTIVE_NOTIFICATION_ICONS, P.INTERACTIVE_NOTIFICATION_ICONS_DEFAULT) ||
            utils.prefs.get(P.SWIPE_NOTIFICATION_OPEN, P.SWIPE_NOTIFICATION_OPEN_DEFAULT)) {
            ICON_SPACING_INTERACTIVE
        } else if (!utils.prefs.get(P.INTERACTIVE_NOTIFICATION_ICONS, P.INTERACTIVE_NOTIFICATION_ICONS_DEFAULT) &&
            !utils.prefs.get(P.SWIPE_NOTIFICATION_OPEN, P.SWIPE_NOTIFICATION_OPEN_DEFAULT) &&
            utils.prefs.get(P.INVERT_INTERACTION_HIGHLIGHT, P.INVERT_INTERACTION_HIGHLIGHT_DEFAULT)) {
            ICON_SPACING_INTERACTIVE
        } else {
            ICON_SPACING_REGULAR
        }
    }

    private fun drawIcon(
        canvas: Canvas,
        utils: Utils,
        x: Int,
        index: Int,
        isFingerprintTouched: Boolean,
        touchedNotificationIndex: Int?,
        isLandscape: Boolean
    ) {
        val notificationEntry = NotificationService.notifications[index]

        @Suppress("TooGenericExceptionCaught")
        try {
            val padding = utils.padding2
            val drawable =
                if (index == NOTIFICATION_LIMIT - 1) {
                    ContextCompat.getDrawable(utils.context, R.drawable.ic_more)
                        ?: error("Invalid state.")
                } else {
                    notificationEntry.icon.loadDrawable(utils.context) ?: return
                }

            drawable.setTint(
                if (utils.prefs.get(P.TINT_NOTIFICATIONS, P.TINT_NOTIFICATIONS_DEFAULT)) {
                    notificationEntry.color
                } else {
                    utils.prefs.get(
                        P.DISPLAY_COLOR_NOTIFICATION,
                        P.DISPLAY_COLOR_NOTIFICATION_DEFAULT,
                    )
                }
            )

            drawable.alpha = 255

            val spacing = (utils.drawableSize * getIconSpacing(utils)).toInt()
            var left: Int
            var top: Int
            var right: Int
            var bottom: Int
            
            if (isLandscape) {
                // Landscape: columns on the right side
                val columnLength = getNotificationColumnLength(utils)
                val columnIndex = index / columnLength
                val rowIndex = index % columnLength
                val rightMargin = utils.prefs.get(P.NOTIFICATION_ICON_TOP_PADDING, P.NOTIFICATION_ICON_TOP_PADDING_DEFAULT)
                val startX = x - rightMargin
                
                left = startX - columnIndex * (utils.drawableSize + spacing)
                top = utils.drawableSize / 2 + rowIndex * (utils.drawableSize + spacing)
                right = left + utils.drawableSize
                bottom = top + utils.drawableSize
            } else {
                // Portrait: row-based layout
                if (utils.paint.textAlign == Paint.Align.LEFT) {
                    left = x + (utils.drawableSize + spacing) * (index % getNotificationRowLength(utils))
                    top = utils.viewHeight.toInt() - utils.drawableSize / 2 +
                            index / getNotificationRowLength(utils) * (utils.drawableSize + spacing)
                    right = left + utils.drawableSize
                    bottom = top + utils.drawableSize
                } else {
                    left = x - utils.drawableSize / 2 + (utils.drawableSize + spacing) * (index % getNotificationRowLength(utils))
                    top = utils.viewHeight.toInt() - utils.drawableSize / 2 +
                            index / getNotificationRowLength(utils) * (utils.drawableSize + spacing)
                    right = left + utils.drawableSize
                    bottom = top + utils.drawableSize
                }
            }

            val highlightRect = Rect(
                left - padding,
                top - padding,
                right + padding,
                bottom + padding
            )
            iconBounds[index] = highlightRect
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)

            val swipeNotificationOpenEnabled = utils.prefs.get(P.SWIPE_NOTIFICATION_OPEN, P.SWIPE_NOTIFICATION_OPEN_DEFAULT)
            if (!utils.prefs.get(P.INVERT_INTERACTION_HIGHLIGHT, P.INVERT_INTERACTION_HIGHLIGHT_DEFAULT)) {
                if ((isFingerprintTouched && swipeNotificationOpenEnabled) || touchedNotificationIndex == index) {
                    drawHighlight(
                        utils,
                        notificationEntry,
                        canvas,
                        highlightRect,
                        left,
                        right,
                        top,
                        bottom
                    )
                }
            } else {
                if ((isFingerprintTouched && swipeNotificationOpenEnabled) || touchedNotificationIndex != index) {
                    drawHighlight(
                        utils,
                        notificationEntry,
                        canvas,
                        highlightRect,
                        left,
                        right,
                        top,
                        bottom
                    )
                }
            }
        } catch (exception: Exception) {
            Log.e(Global.LOG_TAG, exception.toString())
        }
    }

    private fun drawHighlight(
        utils: Utils,
        notificationEntry: NotificationService.NotificationEntry,
        canvas: Canvas,
        highlightRect: Rect,
        left: Int,
        right: Int,
        top: Int,
        bottom: Int
    ) {
        val highlightColor: Int =
            if (utils.prefs.get(P.TINT_NOTIFICATIONS, P.TINT_NOTIFICATIONS_DEFAULT)) {
                notificationEntry.color
            } else {
                utils.prefs.get(
                    P.DISPLAY_COLOR_NOTIFICATION,
                    P.DISPLAY_COLOR_NOTIFICATION_DEFAULT,
                )
            }

        canvas.drawRect(highlightRect, highlightPaint)

        // Draw the outer ring background highlight
        val outerRingDrawable = ContextCompat.getDrawable(utils.context, R.drawable.ic_outerring)
            ?: return

        // Set the ring color to match the highlight color
        outerRingDrawable.setTint(highlightColor)

        // Calculate center position of the icon for the ring placement
        val centerX = (left + right) / 2
        val centerY = (top + bottom) / 2

        // Set bounds for the ring to be centered around the icon
        // The ring size should match the highlightRect dimensions
        val ringSize = right - left + (utils.drawableSize * RING_SPACING).toInt()
        outerRingDrawable.setBounds(
            centerX - ringSize / 2,
            centerY - ringSize / 2,
            centerX + ringSize / 2,
            centerY + ringSize / 2
        )

        outerRingDrawable.draw(canvas)
    }

    fun draw(
        canvas: Canvas,
        utils: Utils,
        width: Int,
        isFingerprintTouched: Boolean = false,
        touchedNotificationIndex: Int? = null
    ) {
        val notifications = NotificationService.notifications
        iconBounds.clear()

        val isLandscape = utils.context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val spacing = (utils.drawableSize * getIconSpacing(utils)).toInt()
        val x: Int = if (isLandscape) {
            width
        } else {
            if (utils.paint.textAlign == Paint.Align.LEFT) {
                utils.horizontalRelativePoint.toInt()
            } else {
                (
                    width - (
                        min(
                            notifications.size,
                            getNotificationRowLength(utils),
                        ) - 1
                    ) * (utils.drawableSize + spacing)
                ) / 2
            }
        }
        
        if (!isLandscape) {
            val topPadding = utils.prefs.get(P.NOTIFICATION_ICON_TOP_PADDING, P.NOTIFICATION_ICON_TOP_PADDING_DEFAULT)
            utils.viewHeight += utils.padding16 + utils.drawableSize / 2 + topPadding
        }

        for (index in 0 until min(notifications.size, NOTIFICATION_LIMIT)) {
            drawIcon(canvas, utils, x, index, isFingerprintTouched, touchedNotificationIndex, isLandscape)
        }
    }
}
