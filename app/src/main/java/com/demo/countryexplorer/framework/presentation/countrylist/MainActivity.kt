package com.demo.countryexplorer.framework.presentation.countrylist

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.demo.countryexplorer.R
import com.demo.countryexplorer.databinding.ActivityMainBinding
import com.demo.countryexplorer.business.interactors.countrylist.CountryListingRepositoryImpl
import com.demo.countryexplorer.business.domain.state.MessageType
import com.demo.countryexplorer.business.domain.state.Response
import com.demo.countryexplorer.business.domain.state.StateMessageCallback
import com.demo.countryexplorer.business.domain.state.UIComponentType
import com.demo.countryexplorer.framework.presentation.UIController
import com.demo.countryexplorer.framework.presentation.countrylist.state.CountryStateEvent.FetchAllListsStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MainActivity : AppCompatActivity(), UIController {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerAdapter: CountryListAdapter
    var uiController: UIController = this
    private var dialogInView: MaterialDialog? = null

    private val viewModel by viewModels<CountryListingViewModel> {
        CountryListViewModelFactory(
            repository = CountryListingRepositoryImpl()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
        setupUI()
        initRecyclerView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(FetchAllListsStateEvent())
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

    @OptIn(FlowPreview::class)
    private fun subscribeObservers() {
        viewModel.viewState.observe(this) { viewState ->
            if (viewState != null) {
                viewState.countryLists.let { countries ->
                    recyclerAdapter.submitList(countries.countryLists)
                }

                if (viewModel.shouldRestoreScroll()) {
                    restoreScrollPosition()
                    viewModel.clearRotationFlag()
                }
            }
        }

        viewModel.shouldDisplayProgressBar.observe(this) {
            uiController.displayProgressBar(it)
        }

        viewModel.stateMessage.observe(this, Observer { stateMessage ->
            stateMessage?.let { message ->

                uiController.onResponseReceived(
                    response = message.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )

            }
        })
    }

    private fun restoreScrollPosition() {
        // Get the saved scroll position from the ViewModel
        val position: Int? = viewModel.getScrollPosition()
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

    override fun displayProgressBar(shouldDisplay: Boolean) {
        if (shouldDisplay) {
            binding.countryListingRecyclerview.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.countryListingRecyclerview.visibility = View.VISIBLE
        }
    }

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        when (response.uiComponentType) {
            is UIComponentType.Toast -> {
                response.message?.let {
                    displayToast(
                        message = it,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is UIComponentType.Dialog -> {
                displayDialog(
                    response = response,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is UIComponentType.None -> {
                // This would be a good place to send to your Error Reporting
                // software of choice (ex: Firebase crash reporting)
                stateMessageCallback.removeMessageFromStack()
            }

            else -> {

            }
        }
    }

    private fun Activity.displayToast(
        message: String,
        stateMessageCallback: StateMessageCallback
    ) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        stateMessageCallback.removeMessageFromStack()
    }

    private fun displayDialog(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        response.message?.let { message ->
            dialogInView = when (response.messageType) {

                is MessageType.Error -> {
                    MaterialDialog(this)
                        .show {
                            title(R.string.text_error)
                            message(text = message)
                            positiveButton(R.string.text_ok) {
                                stateMessageCallback.removeMessageFromStack()
                                dismiss()
                            }
                            onDismiss {
                                dialogInView = null
                            }
                            cancelable(false)
                        }
                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        } ?: stateMessageCallback.removeMessageFromStack()
    }
}