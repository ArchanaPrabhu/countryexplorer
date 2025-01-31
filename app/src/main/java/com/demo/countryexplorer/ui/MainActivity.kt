package com.demo.countryexplorer.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.countryexplorer.databinding.ActivityMainBinding
import com.demo.countryexplorer.repository.CountryListingRepositoryImpl
import com.demo.countryexplorer.util.UiState
import com.demo.countryexplorer.viewmodel.CountryListViewModelFactory
import com.demo.countryexplorer.viewmodel.CountryListingViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerAdapter: CountryListAdapter

    @OptIn(FlowPreview::class)
    private val viewModel by viewModels<CountryListingViewModel> {
        CountryListViewModelFactory(
            repository = CountryListingRepositoryImpl()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        initRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    @OptIn(FlowPreview::class)
    private fun initRecyclerView() {
        binding.countryListingRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator)
            addItemDecoration(topSpacingDecorator)
            recyclerAdapter = CountryListAdapter()
            adapter = recyclerAdapter

            // Listener to save the scroll position
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstVisibleItemPosition()
                    viewModel.saveScrollPosition(position)
                }
            })
        }
    }

    private fun showErrorState(errorMessage: String?) {
        Snackbar.make(binding.root, errorMessage ?: "Something went wrong", Snackbar.LENGTH_LONG)
            .show()
    }

    @OptIn(FlowPreview::class)
    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentCountryListContentState.collect {
                    when (val state = it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.countryListingRecyclerview.visibility = View.VISIBLE
                            recyclerAdapter.submitList(state.data)

                            // Restore scroll position only after rotation, not after every refresh
                            if (viewModel.shouldRestoreScroll()) {
                                restoreScrollPosition()
                                viewModel.clearRotationFlag()
                            }
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.countryListingRecyclerview.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.countryListingRecyclerview.visibility = View.GONE
                            showErrorState(state.exception.message)
                        }
                    }
                }
            }
        }
        viewModel.fetchCountryList()
    }

    private fun restoreScrollPosition() {
        // Get the saved scroll position from the ViewModel
        val position : Int? = viewModel.getScrollPosition()
        position?.let {
            binding.countryListingRecyclerview.post {
                binding.countryListingRecyclerview.scrollToPosition(it)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.setRotationFlag()
    }
}