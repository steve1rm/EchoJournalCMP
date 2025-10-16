package org.example.echojournalcmp.widget

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import co.touchlab.kermit.Logger
import me.androidbox.echojournalcmp.R
import org.example.echojournalcmp.MainActivity
import org.example.echojournalcmp.navigation.ACTION_CREATE_ECHO

class RecordEchoWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = RecordEchoWidget()
}

class RecordEchoWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val contentDescription = context.getString(R.string.record_new_echo)

        provideContent {
            GlanceTheme {
                Column(
                    modifier = GlanceModifier
                        .clickable {
                            val intent = Intent(context, MainActivity::class.java).apply {
                                this.data = "https://echojournalcmp.com/echos/true".toUri()
                                this.action = ACTION_CREATE_ECHO
                            }
                            Logger.d {
                                "WIDGET intent ${intent.data}"
                            }
                            val pendingIntent = TaskStackBuilder
                                .create(context)
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

                            pendingIntent?.send()
                        }
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.widget),
                        contentDescription = contentDescription
                    )
                }
            }
        }
    }
}