package com.rng.stockmarket.di

import com.rng.stockmarket.data.csv.CSVParser
import com.rng.stockmarket.data.csv.CompanyListParser
import com.rng.stockmarket.data.csv.IntradayInfoParser
import com.rng.stockmarket.data.repository.StockRepositoryImpl
import com.rng.stockmarket.domain.model.CompanyListing
import com.rng.stockmarket.domain.model.IntradayInfo
import com.rng.stockmarket.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListParser: CompanyListParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}