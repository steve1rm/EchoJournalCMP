package org.example.echojournalcmp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class EchoJournalCMPApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = {
                this.androidLogger(Level.DEBUG)
                this.androidContext(this@EchoJournalCMPApplication)
            }
        )
    }
}