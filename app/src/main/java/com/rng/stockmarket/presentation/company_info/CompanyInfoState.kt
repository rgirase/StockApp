package com.rng.stockmarket.presentation.company_info

import com.rng.stockmarket.domain.model.CompanyInfo
import com.rng.stockmarket.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = ""
)
