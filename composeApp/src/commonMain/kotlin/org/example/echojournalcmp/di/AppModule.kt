package org.example.echojournalcmp.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.example.echojournalcmp.core.database.EchoDatabase
import org.example.echojournalcmp.core.database.echo.EchoDao
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    // Makes it easier to use the dao, rather than keep referencing the like this i.e. database.dao
    single<EchoDao> {
        get<EchoDatabase>().echoDao
    }
}