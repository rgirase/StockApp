package com.rng.stockmarket.data.remote


import com.rng.stockmarket.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockAPI {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListing(@Query("apikey") apiKey: String = API_KEY): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min")
    suspend fun getIntraDayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): CompanyInfoDto

    companion object {
        const val API_KEY = "LK4P30TCL451SMPJ"
        const val BASE_URL = "https://www.alphavantage.co"
    }
}