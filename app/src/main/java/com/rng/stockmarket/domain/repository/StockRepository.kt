package com.rng.stockmarket.domain.repository

import com.rng.stockmarket.domain.model.CompanyInfo
import com.rng.stockmarket.domain.model.CompanyListing
import com.rng.stockmarket.domain.model.IntradayInfo
import com.rng.stockmarket.utils.Resource
import kotlinx.coroutines.flow.Flow


interface StockRepository {
    suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo>
}