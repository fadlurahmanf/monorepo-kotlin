package com.fadlurahmanf.bebas_transaction.presentation.pin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.PostingPinVerificationRequestModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.PostingPinVerificationResultModel
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class PinVerificationViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: PinVerificationViewModel
    lateinit var transactionRepositoryImpl: TransactionRepositoryImpl

    @Before
    fun setupBefore() {
        transactionRepositoryImpl = mock<TransactionRepositoryImpl>()
        viewModel = PinVerificationViewModel(
            transactionRepositoryImpl = transactionRepositoryImpl
        )

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
    }

    @Test
    fun `verifyHitFundTransferBankMASRequest`() {
        val request = PostingPinVerificationRequestModel.FundTranfeerBankMas(
            fundTransferBankMASRequest = FundTransferBankMASRequest(
                accountNumber = "FAKE_ACCOUNT_NUMBER",
                amountTransaction = 12000,
                description = "FAKE_DESCRIPTION",
                destinationAccountName = "FAKE_DESTINATION_ACCOUNT_NAME",
                destinationAccountNumber = "FAKE_DESTINATION_ACCOUNT_NUMBER",
                ip = "FAKE_IP",
                latitude = 0.10101010,
                longitude = -1.01010101,
            )
        )

        val postingModelReturn = PostingPinVerificationResultModel(
            transactionStatus = "SUCCESS"
        )

        whenever(
            transactionRepositoryImpl.postingTransferBankMas(
                plainPin = "123456",
                fundTransferRequest = request.fundTransferBankMASRequest
            )
        ).thenReturn(
            Observable.just(postingModelReturn)
        )

        viewModel.postingPinVerification(
            plainPin = "123456",
            request = request
        )

        verify(transactionRepositoryImpl, times(1)).postingTransferBankMas(
            plainPin = "123456",
            fundTransferRequest = request.fundTransferBankMASRequest
        )
    }
}