package org.example.echojournalcmp.di


import org.example.echojournalcmp.create_echo.CreateEchoViewModel
import org.example.echojournalcmp.echos.data.echo.RoomEchoDataSource
import org.example.echojournalcmp.echos.domain.echos.EchoDataSource
import org.example.echojournalcmp.echos.presentation.echos.EchosViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val echoJournalModule = module {
    viewModelOf(::EchosViewModel)
    viewModelOf(::CreateEchoViewModel)

   /* factory<EchoDataSource> {
        RoomEchoDataSource(get<EchoDao>())
    }*/
    factoryOf(::RoomEchoDataSource) bind EchoDataSource::class
}