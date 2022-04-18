package com.rng.stockmarket.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.rng.stockmarket.data.remote.dto.IntradayInfoDto
import com.rng.stockmarket.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun IntradayInfoDto.toIntrdayInfo(): IntradayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val locaDateTime = LocalDateTime.parse(timeStamp, formatter)

    return IntradayInfo(locaDateTime, close)
}