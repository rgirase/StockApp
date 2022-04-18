package com.rng.stockmarket.data.csv

import android.os.Build
import androidx.annotation.RequiresApi
import com.opencsv.CSVReader
import com.rng.stockmarket.data.mapper.toIntrdayInfo
import com.rng.stockmarket.data.remote.dto.IntradayInfoDto
import com.rng.stockmarket.domain.model.IntradayInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntradayInfo> {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))

        return withContext(Dispatchers.IO) {
            csvReader.readAll().drop(1).mapNotNull { line ->
                val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                val close = line.getOrNull(4) ?: return@mapNotNull null

                val dto = IntradayInfoDto(timestamp, close.toDouble())
                dto.toIntrdayInfo()
            }.filter { it.timeStamp.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth }
                .sortedBy { it.timeStamp.hour }
                .also { csvReader.close() }
        }
    }
}