package com.fadlurahmanf.bebas_main.presentation.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import com.fadlurahmanf.bebas_main.data.dto.notification.NotificationModel
import com.fadlurahmanf.bebas_main.databinding.FragmentNotificationTransactionBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainFragment
import com.fadlurahmanf.bebas_main.presentation.notification.adapter.NotificationPagingAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@ExperimentalCoroutinesApi
class NotificationTransactionFragment : BaseMainFragment<FragmentNotificationTransactionBinding>(
    FragmentNotificationTransactionBinding::inflate
), NotificationPagingAdapter.CallBack {
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModel: NotificationViewModel

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var adapter: NotificationPagingAdapter

    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("BebasLogger", "TES_onBebasViewCreated")

        viewModel.notificationState.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter = NotificationPagingAdapter()
        adapter.setCallBack(this)
        binding.rv.adapter = adapter


        viewModel.getNotification(requireContext())
    }

    override fun injectFragment() {
        component.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationTransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClicked(notification: NotificationModel) {
        viewModel.getTransactionDetail(
            notification.additionalData?.transactionId ?: "-",
            notification.additionalData?.transactionType ?: "-"
        )
    }
}