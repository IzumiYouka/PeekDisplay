package heitezy.peekdisplay.actions.alwayson

import android.app.Activity
import heitezy.peekdisplay.R
import heitezy.peekdisplay.custom.CustomFrameLayout
import heitezy.peekdisplay.custom.FingerprintView

class AlwaysOnViewHolder(activity: Activity) {
    @JvmField
    val frame: CustomFrameLayout = activity.findViewById(R.id.frame)

    @JvmField
    val customView: AlwaysOnCustomView = activity.findViewById(R.id.customView)

    @JvmField
    val fingerprintIcn: FingerprintView = activity.findViewById(R.id.fingerprintIcn)
}
