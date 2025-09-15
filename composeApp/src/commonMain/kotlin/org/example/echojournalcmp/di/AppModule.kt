package org.example.echojournalcmp.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
}