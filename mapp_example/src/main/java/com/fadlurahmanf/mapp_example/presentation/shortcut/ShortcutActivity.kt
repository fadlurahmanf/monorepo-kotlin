package com.fadlurahmanf.mapp_example.presentation.shortcut

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_example.databinding.ActivityShortcutBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_example.presentation.notification.NotificationActivity

class ShortcutActivity :
    BaseExampleActivity<ActivityShortcutBinding>(ActivityShortcutBinding::inflate) {
    override fun injectActivity() {

    }

    override fun setup() {
        val shortcut = ShortcutInfoCompat.Builder(this, "notification")
            .setShortLabel("Notification")
            .setLongLabel("Open Notification")
            .setIcon(IconCompat.createWithResource(this, R.drawable.outline_featured_play_list_24))
            .setIntent(
                Intent(
                    this,
                    NotificationActivity::class.java
                ).apply {
                    action = Intent.ACTION_VIEW
                }
            )
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)
    }

}