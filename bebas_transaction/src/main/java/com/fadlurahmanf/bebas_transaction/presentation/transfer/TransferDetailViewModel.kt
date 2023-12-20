package com.fadlurahmanf.bebas_transaction.presentation.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_transaction.data.state.TransferDetailState
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import javax.inject.Inject

class TransferDetailViewModel @Inject constructor(

) : BaseViewModel() {

    companion object {
        const val MINIMUM_TRANSFER = 10000L
    }

    private val _transferState = MutableLiveData<TransferDetailState>()
    val transferState: LiveData<TransferDetailState> = _transferState

    fun verify(nominal: Long) {
        _transferState.value = TransferDetailState.IDLE
        if (nominal < MINIMUM_TRANSFER) {
            _transferState.value =
                TransferDetailState.MinimumNominalTransferFailed(MINIMUM_TRANSFER)
        } else {
            _transferState.value = TransferDetailState.SUCCESS(nominal)
        }
    }
}
