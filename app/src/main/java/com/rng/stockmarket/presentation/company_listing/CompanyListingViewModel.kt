package com.rng.stockmarket.presentation.company_listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rng.stockmarket.domain.repository.StockRepository
import com.rng.stockmarket.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(private val repository: StockRepository) :
    ViewModel() {
    var state by mutableStateOf(CompanyListingState())
    private var searchJob: Job? = null

    init {
        getCompanyListing()
    }

    fun onEvent(event: CompanyListingEvents) {
        when (event) {
            is CompanyListingEvents.Refresh -> {
                getCompanyListing(fetchFromRemote = true)

            }
            is CompanyListingEvents.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListing()
                }
            }
        }
    }

    private fun getCompanyListing(
        query: String = state.searchQuery.lowercase(Locale.getDefault()),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanyListing(fetchFromRemote, query).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { list ->
                            state = state.copy(companies = list)
                        }
                    }

                    is Resource.Error -> Unit
                    is Resource.Loading -> {
                        state = state.copy(isLoading = result.isLoading)

                    }
                }
            }
        }

    }

}