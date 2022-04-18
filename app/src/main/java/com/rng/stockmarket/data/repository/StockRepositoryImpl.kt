package com.rng.stockmarket.data.repository

import com.rng.stockmarket.data.csv.CSVParser
import com.rng.stockmarket.data.local.StockDatabase
import com.rng.stockmarket.data.mapper.toCompanyInfo
import com.rng.stockmarket.data.mapper.toCompanyList
import com.rng.stockmarket.data.mapper.toCompanyListingEntity
import com.rng.stockmarket.data.remote.StockAPI
import com.rng.stockmarket.domain.model.CompanyInfo
import com.rng.stockmarket.domain.model.CompanyListing
import com.rng.stockmarket.domain.model.IntradayInfo
import com.rng.stockmarket.domain.repository.StockRepository
import com.rng.stockmarket.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api: StockAPI,
    val db: StockDatabase,
    val companyListParser: CSVParser<CompanyListing>,
    val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository {
    private val dao = db.dao
    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListing = dao.searchCompanyListing(query)
            emit(Resource.Success(data = localListing.map { it.toCompanyList() }))
            val isDbEmpty = localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListing = try {
                val response = api.getListing()
                companyListParser.parse(response.byteStream())

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't Load Data"))
                null

            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("HTTP API Error"))
                null
            }

            remoteListing?.let { list ->
                dao.clearCompanyListings()
                dao.insertCompanyListing(list.map { it.toCompanyListingEntity() })
                emit(
                    Resource.Success(
                        data = dao.searchCompanyListing("").map { it.toCompanyList() })
                )
                emit(Resource.Loading(false))


            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntraDayInfo(symbol)
            val result = intradayInfoParser.parse(response.byteStream())
            Resource.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Couldn't Load Data")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("HTTP API Error")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val response = api.getCompanyInfo(symbol)
            Resource.Success(response.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Couldn't Load Data")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("HTTP API Error")
        }
    }

}