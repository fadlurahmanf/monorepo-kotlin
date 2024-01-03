package com.fadlurahmanf.bebas_loyalty.presentation.history

import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_loyalty.databinding.FragmentHistoryLoyaltyBinding
import com.fadlurahmanf.bebas_loyalty.presentation.BaseLoyaltyFragment
import com.fadlurahmanf.bebas_loyalty.presentation.history.adapter.LoyaltyHistoryPagingAdapter
import javax.inject.Inject

private const val ARG_FRAGMENT_TYPE = "ARG_FRAGMENT_TYPE"

class HistoryLoyaltyFragment :
    BaseLoyaltyFragment<FragmentHistoryLoyaltyBinding>(FragmentHistoryLoyaltyBinding::inflate) {
    private var fragmentType: String? = null

    @Inject
    lateinit var viewModel: HistoryLoyaltyViewModel

    override fun injectFragment() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            fragmentType = it.getString(ARG_FRAGMENT_TYPE)
        }
    }

    private lateinit var adapter: LoyaltyHistoryPagingAdapter
    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.notificationState.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter = LoyaltyHistoryPagingAdapter()
        binding.rvHistory.adapter = adapter

        when (fragmentType) {
            "ALL" -> {
                viewModel.getNotification()
            }

            else -> {
                viewModel.getNotification(fragmentType?.lowercase() ?: "-")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(fragmentType: String) =
            HistoryLoyaltyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FRAGMENT_TYPE, fragmentType)
                }
            }
    }
}