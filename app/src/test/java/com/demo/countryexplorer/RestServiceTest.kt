package com.demo.countryexplorer

import com.demo.countryexplorer.framework.datasource.network.api.countrylist.GitCountryListingAPI
import com.demo.countryexplorer.framework.datasource.network.abstraction.RestService
import com.demo.countryexplorer.framework.datasource.network.model.Country
import com.demo.countryexplorer.framework.datasource.network.model.Currency
import com.demo.countryexplorer.framework.datasource.network.model.Language
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RestServiceTest {

    private lateinit var countryListingService: CountryListingService

    @Mock
    private lateinit var errorHandlingCall: ErrorHandlingCall<List<Country>>

    @Mock
    private lateinit var apiInstanceTest: GitCountryListingAPI

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        countryListingService = CountryListingService(errorHandlingCall, apiInstanceTest)
        Mockito.`when`(apiInstanceTest.fetchCountryListings()).thenReturn(errorHandlingCall)
    }

    class CountryListingService(
        val errorHandlingCall: ErrorHandlingCall<List<Country>>,
        val apiInstanceTest: GitCountryListingAPI
    ) : RestService<GitCountryListingAPI>(
        api = GitCountryListingAPI::class.java
    ) {
        override fun getBaseUrl(): String {
            return BASE_URL
        }

        override suspend fun getCountryListing(): List<Country> {
            val response = apiInstanceTest.fetchCountryListings()
            return response.await().toList()
        }
    }

    @Test
    fun `test if countryListingService interacts with errorHandlingCall`() = runTest {
        Mockito.`when`(errorHandlingCall.await()).thenReturn(
            mutableListOf(
                Country(
                    "capital",
                    "test_code",
                    Currency("code", "name", "symbol"),
                    "flag",
                    Language("code", "language"),
                    "name",
                    "region"
                )
            )
        )
        val response = countryListingService.getCountryListing()
        assert(response.size == 1)
        assert(response[0].code == "test_code")
    }

    @Test
    fun `test if the base url is set`() = runTest {
        val response = countryListingService.getBaseUrl()
        assert(response == BASE_URL)
    }
    companion object {
        const val BASE_URL = "https://test_base_url"
    }
}