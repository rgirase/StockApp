package com.rng.stockmarket.data.mapper

import com.rng.stockmarket.data.local.CompanyListingEntity
import com.rng.stockmarket.data.remote.dto.CompanyInfoDto
import com.rng.stockmarket.domain.model.CompanyInfo
import com.rng.stockmarket.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyList(): CompanyListing {
    return CompanyListing(name, symbol, exchange)
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(name, symbol, exchange)
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol, description = description,
        name = name, country = country, industry = industry
    )
}