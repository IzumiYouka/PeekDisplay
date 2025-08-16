package heitezy.peekdisplay.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import heitezy.peekdisplay.R
import heitezy.peekdisplay.actions.alwayson.AlwaysOn
import heitezy.peekdisplay.adapters.LayoutListAdapter
import heitezy.peekdisplay.helpers.P

class LAFAlwaysOnLookActivity : BaseActivity(), LayoutListAdapter.OnItemClickListener {
    internal var value: String = P.USER_THEME_DEFAULT
    private lateinit var preview: ImageView
    private lateinit var layoutList: RecyclerView

    private val drawables =
        arrayOf(
            R.drawable.always_on_moto,
            R.drawable.always_on_google,
            R.drawable.always_on_oneplus,
            R.drawable.always_on_samsung,
            R.drawable.always_on_samsung2,
            R.drawable.always_on_samsung3,
            R.drawable.always_on_80s,
            R.drawable.always_on_fast,
            R.drawable.always_on_flower,
            R.drawable.always_on_game,
            R.drawable.always_on_handwritten,
            R.drawable.always_on_jungle,
            R.drawable.always_on_western,
            R.drawable.always_on_analog,
        )

    @Suppress("CyclomaticComplexMethod")
    private fun positionToString(position: Int): String =
        when (position) {
            ITEM_MOTO -> P.USER_THEME_MOTO
            ITEM_GOOGLE -> P.USER_THEME_GOOGLE
            ITEM_ONEPLUS -> P.USER_THEME_ONEPLUS
            ITEM_SAMSUNG -> P.USER_THEME_SAMSUNG
            ITEM_SAMSUNG2 -> P.USER_THEME_SAMSUNG2
            ITEM_SAMSUNG3 -> P.USER_THEME_SAMSUNG3
            ITEM_80S -> P.USER_THEME_80S
            ITEM_FAST -> P.USER_THEME_FAST
            ITEM_FLOWER -> P.USER_THEME_FLOWER
            ITEM_GAME -> P.USER_THEME_GAME
            ITEM_HANDWRITTEN -> P.USER_THEME_HANDWRITTEN
            ITEM_JUNGLE -> P.USER_THEME_JUNGLE
            ITEM_WESTERN -> P.USER_THEME_WESTERN
            ITEM_ANALOG -> P.USER_THEME_ANALOG
            else -> P.USER_THEME_DEFAULT
        }

    @Suppress("CyclomaticComplexMethod")
    private fun stringToPosition(string: String): Int =
        when (string) {
            P.USER_THEME_MOTO -> ITEM_MOTO
            P.USER_THEME_GOOGLE -> ITEM_GOOGLE
            P.USER_THEME_ONEPLUS -> ITEM_ONEPLUS
            P.USER_THEME_SAMSUNG -> ITEM_SAMSUNG
            P.USER_THEME_SAMSUNG2 -> ITEM_SAMSUNG2
            P.USER_THEME_SAMSUNG3 -> ITEM_SAMSUNG3
            P.USER_THEME_80S -> ITEM_80S
            P.USER_THEME_FAST -> ITEM_FAST
            P.USER_THEME_FLOWER -> ITEM_FLOWER
            P.USER_THEME_GAME -> ITEM_GAME
            P.USER_THEME_HANDWRITTEN -> ITEM_HANDWRITTEN
            P.USER_THEME_JUNGLE -> ITEM_JUNGLE
            P.USER_THEME_WESTERN -> ITEM_WESTERN
            P.USER_THEME_ANALOG -> ITEM_ANALOG
            else -> ITEM_MOTO
        }

    private fun setSelectedItem(
        adapter: LayoutListAdapter,
        position: Int,
    ) {
        preview.setImageResource(drawables[position])
        adapter.setSelectedItem(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_list)

        // Handle window insets for the main container
        val rootView = findViewById<RelativeLayout>(R.id.layout_list_activity)
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

        preview = findViewById(R.id.preview)
        layoutList = findViewById(R.id.layout_list)

        layoutList.layoutManager =
            LinearLayoutManager(this).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        layoutList.adapter =
            LayoutListAdapter(
                drawables,
                resources.getStringArray(R.array.pref_look_and_feel_ao_array_display),
                this,
            )
    }

    override fun onStart() {
        super.onStart()
        value = P.getPreferences(this).getString(P.USER_THEME, P.USER_THEME_DEFAULT) ?: P.USER_THEME_DEFAULT
        setSelectedItem(layoutList.adapter as LayoutListAdapter, stringToPosition(value))
    }

    override fun onStop() {
        super.onStop()
        P.getPreferences(this).edit { putString(P.USER_THEME, value) }
    }

    override fun onItemClick(position: Int) {
        preview.setImageResource(drawables[position])
        value = positionToString(position)
        AlwaysOn.finish()
    }

    companion object {
        private const val ITEM_MOTO = 0
        private const val ITEM_GOOGLE = 1
        private const val ITEM_ONEPLUS = 2
        private const val ITEM_SAMSUNG = 3
        private const val ITEM_SAMSUNG2 = 4
        private const val ITEM_SAMSUNG3 = 5
        private const val ITEM_80S = 6
        private const val ITEM_FAST = 7
        private const val ITEM_FLOWER = 8
        private const val ITEM_GAME = 9
        private const val ITEM_HANDWRITTEN = 10
        private const val ITEM_JUNGLE = 11
        private const val ITEM_WESTERN = 12
        private const val ITEM_ANALOG = 13
    }
}
