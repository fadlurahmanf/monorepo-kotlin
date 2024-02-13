package com.fadlurahmanf.bebas_transaction.data.state

sealed class TransferDetailState {
    object IDLE : TransferDetailState()
    data class SUCCESS(
        val nominal: Long
    ) : TransferDetailState()

    data class MinimumNominalTransferFailed(
        val minimum: Long
    ) : TransferDetailState()
}
