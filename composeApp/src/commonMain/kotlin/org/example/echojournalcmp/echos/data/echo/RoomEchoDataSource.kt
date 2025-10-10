package org.example.echojournalcmp.echos.data.echo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.echojournalcmp.core.database.echo.EchoDao
import org.example.echojournalcmp.echos.domain.echos.Echo
import org.example.echojournalcmp.echos.domain.echos.EchoDataSource

class RoomEchoDataSource(
    private val echoDao: EchoDao
) : EchoDataSource {
    override fun observeEchos(): Flow<List<Echo>> {
        return echoDao.observeEchos()
            .map { echoWithTopics ->
                echoWithTopics.map { echoWithTopics ->
                    echoWithTopics.toEcho()
                }
            }
    }

    override fun observeTopics(): Flow<List<String>> {
        return echoDao.observeTopics()
            .map { topicEntities ->
                topicEntities.toTopics()
            }
    }

    override fun searchTopics(query: String): Flow<List<String>> {
        return echoDao.searchTopics(query)
            .map { topicEntities ->
                topicEntities.toTopics()
            }
    }

    override suspend fun insertEcho(echo: Echo) {
        echoDao.insertEchoWithTopics(echo.toEchoWithTopic())
    }
}