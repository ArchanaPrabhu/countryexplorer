import androidx.lifecycle.SavedStateHandle
import com.demo.countryexplorer.framework.datasource.network.model.Country
import com.demo.countryexplorer.framework.datasource.network.model.Currency
import com.demo.countryexplorer.framework.datasource.network.model.Language
import com.demo.countryexplorer.business.interactors.countrylist.CountryListingRepository
import com.demo.countryexplorer.business.domain.state.UiState
import com.demo.countryexplorer.framework.presentation.countrylist.CountryListingViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
@FlowPreview
class CountryListingViewModelTest {

    // Set the main coroutine dispatcher for unit testing
    private val testDispatcher = UnconfinedTestDispatcher()


    // Mock dependencies
    @Mock
    private lateinit var repository: CountryListingRepository

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    // Subject under test
    private lateinit var viewModel: CountryListingViewModel

    @OptIn(FlowPreview::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain((testDispatcher)) // Set the main dispatcher to a test dispatcher
        viewModel = CountryListingViewModel(savedStateHandle, repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset dispatcher after test
    }

    @Test
    fun `test fetchCountryList success`() = runTest {
        // Test Setup
        val mockCountryList = listOf(
            Country(
                "capital",
                "code",
                Currency("code", "name", "symbol"),
                "flag",
                Language("code", "language"),
                "name",
                "region"
            ),
            Country(
                "capital",
                "code",
                Currency("code", "name", "symbol"),
                "flag",
                Language("code", "language"),
                "name",
                "region"
            )
        )
        val flowCountryList = flowOf(mockCountryList)

        Mockito.`when`(repository.getCountryListing()).thenReturn(flowCountryList)

        // Testing execution
        viewModel.fetchCountryList()

        // Wait for coroutines to complete
        advanceUntilIdle()

        // Validation
        val currentState = viewModel.currentCountryListContentState.value
        assertTrue(currentState is UiState.Success<*>)
        assertEquals(mockCountryList, (currentState as UiState.Success<*>).data)
    }

    @Test
    fun `test fetchCountryList error`() = runBlocking {
        // Test setup
        val errorMessage = "Error fetching countries"
        val exception = RuntimeException(errorMessage)
        val errorFlow by lazy { flow<Any> { throw exception } }

        Mockito.`when`(repository.getCountryListing()).thenAnswer { errorFlow }

        // Test execution
        viewModel.fetchCountryList()

        // Validation
        val currentState = viewModel.currentCountryListContentState.value
        assertTrue(currentState is UiState.Error)
        assertEquals(errorMessage, (currentState as UiState.Error).exception.message)
    }

}
