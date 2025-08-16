package heitezy.peekdisplay.activities

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ScrollView
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import heitezy.peekdisplay.R
import heitezy.peekdisplay.receivers.AdminReceiver

class HelpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        // Handle window insets for the main container
        val rootView = findViewById<ScrollView>(R.id.help)
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

        findViewById<Button>(R.id.uninstall).setOnClickListener {
            (getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager)
                .removeActiveAdmin(ComponentName(this, AdminReceiver::class.java))
            startActivity(Intent(Intent.ACTION_DELETE).setData("package:$packageName".toUri()))
        }
        findViewById<Button>(R.id.batterySettings).setOnClickListener {
            startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
        }
        findViewById<Button>(R.id.manufacturer).setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW).setData(
                    "https://dontkillmyapp.com/".toUri(),
                ),
            )
        }
    }
}
