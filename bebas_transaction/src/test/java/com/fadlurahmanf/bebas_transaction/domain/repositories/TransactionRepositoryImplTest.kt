package com.fadlurahmanf.bebas_transaction.domain.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fadlurahmanf.bebas_api.data.datasources.CmsRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.FulfillmentRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.IdentityRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.OrderRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.PaymentRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.TransactionRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.others.ItemBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryBankMasRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryBankResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.InquiryResultModel
import com.google.gson.Gson
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.rules.TestRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TransactionRepositoryImplTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var transactionRepositoryImpl: TransactionRepositoryImpl
    lateinit var cmsRemoteDatasource: CmsRemoteDatasource
    lateinit var cryptoTransactionRepositoryImpl: CryptoTransactionRepositoryImpl
    lateinit var fulfillmentRemoteDatasource: FulfillmentRemoteDatasource
    lateinit var identityRemoteDatasource: IdentityRemoteDatasource
    lateinit var orderRemoteDatasource: OrderRemoteDatasource
    lateinit var paymentRemoteDatasource: PaymentRemoteDatasource
    lateinit var transactionRemoteDatasource: TransactionRemoteDatasource

    @Before
    fun setupBefore() {
        cmsRemoteDatasource = mock<CmsRemoteDatasource>()
        cryptoTransactionRepositoryImpl = mock<CryptoTransactionRepositoryImpl>()
        fulfillmentRemoteDatasource = mock<FulfillmentRemoteDatasource>()
        identityRemoteDatasource = mock<IdentityRemoteDatasource>()
        orderRemoteDatasource = mock<OrderRemoteDatasource>()
        paymentRemoteDatasource = mock<PaymentRemoteDatasource>()
        transactionRemoteDatasource = mock<TransactionRemoteDatasource>()

        transactionRepositoryImpl = TransactionRepositoryImpl(
            cmsRemoteDatasource = cmsRemoteDatasource,
            cryptoTransactionRepositoryImpl = cryptoTransactionRepositoryImpl,
            fulfillmentRemoteDatasource = fulfillmentRemoteDatasource,
            identityRemoteDatasource = identityRemoteDatasource,
            orderRemoteDatasource = orderRemoteDatasource,
            paymentRemoteDatasource = paymentRemoteDatasource,
            transactionRemoteDatasource = transactionRemoteDatasource,
        )

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
    }

    @Test
    fun `success_get_sorted_bank_list`() {
        val bankListReturnAPI = listOf(
            ItemBankResponse(
                sknId = "FAKE_SKN_ID_2",
                rtgsId = "FAKE_RTGS_ID_2",
                name = "FAKE_NAME_2",
                nickName = "FAKE_NICKNAME_2",
                image = "FAKE_IMAGE_2",
                order = 2
            ),
            ItemBankResponse(
                sknId = "FAKE_SKN_ID_1",
                rtgsId = "FAKE_RTGS_ID_1",
                name = "FAKE_NAME_1",
                nickName = "FAKE_NICKNAME_1",
                image = "FAKE_IMAGE_1",
                order = 1
            )
        )
        val expectedBankList = listOf(
            ItemBankResponse(
                sknId = "FAKE_SKN_ID_1",
                rtgsId = "FAKE_RTGS_ID_1",
                name = "FAKE_NAME_1",
                nickName = "FAKE_NICKNAME_1",
                image = "FAKE_IMAGE_1",
                order = 1
            ),
            ItemBankResponse(
                sknId = "FAKE_SKN_ID_2",
                rtgsId = "FAKE_RTGS_ID_2",
                name = "FAKE_NAME_2",
                nickName = "FAKE_NICKNAME_2",
                image = "FAKE_IMAGE_2",
                order = 2
            )
        )
        val responseAPI = BaseResponse(
            code = "200",
            message = "SUCCESS",
            data = bankListReturnAPI
        )
        whenever(cmsRemoteDatasource.getBankList()).thenReturn(
            Observable.just(responseAPI)
        )

        val responseResult = transactionRepositoryImpl.getBankList()
        val bankListResult = responseResult.blockingSingle()

        verify(cmsRemoteDatasource, times(1)).getBankList()
        assertEquals(Gson().toJson(expectedBankList), Gson().toJson(bankListResult))
    }

    @Test
    fun `error_list_data_return_null`() {
        val responseAPI = BaseResponse<List<ItemBankResponse>>(
            code = "200",
            message = "SUCCESS",
            data = null
        )

        whenever(cmsRemoteDatasource.getBankList()).thenReturn(
            Observable.just(responseAPI)
        )

        val testObservable = TestObserver<List<ItemBankResponse>>()
        val observable = transactionRepositoryImpl.getBankList()
        verify(cmsRemoteDatasource, times(1)).getBankList()
        observable.subscribe(testObservable)
        testObservable.assertNotComplete()
        testObservable.assertFailure(BebasException::class.java)
        testObservable.assertError {
            assertEquals(
                BebasException.fromThrowable(it).toJson(),
                BebasException.generalRC("DATA_MISSING").toJson()
            )
            true
        }
    }

    @Test
    fun `get_bank_accounts_success`() {
        val bankAccountResponse = BankAccountResponse(
            accountName = "FAKE_ACCOUNT_NAME",
            accountNumber = "FAKE_ACCOUNT_NUMBER",
            productName = "FAKE_PRODUCT_NAME",
            workingBalance = 25000.0,
        )
        val expectedListPaymentSource = listOf<PaymentSourceModel>(
            PaymentSourceModel(
                accountName = "FAKE_ACCOUNT_NAME",
                accountNumber = "FAKE_ACCOUNT_NUMBER",
                balance = 25000.0,
                subLabel = "MAS Saving • FAKE_ACCOUNT_NUMBER",
                bankAccountResponse = bankAccountResponse
            )
        )
        val responseAPI = BaseResponse<List<BankAccountResponse>>(
            code = "200",
            message = "SUCCESS",
            data = listOf(
                bankAccountResponse
            )
        )
        whenever(transactionRemoteDatasource.getBankAccounts()).thenReturn(
            Observable.just(responseAPI)
        )

        val responseResult = transactionRepositoryImpl.getPaymentSourceModelFromGetBankAccounts()
        val bankAccountResult = responseResult.blockingSingle()

        verify(transactionRemoteDatasource, times(1)).getBankAccounts()
        assertEquals(Gson().toJson(bankAccountResult), Gson().toJson(expectedListPaymentSource))
    }

    @Test
    fun `get_bank_accounts_data_null`() {
        val responseAPI = BaseResponse<List<BankAccountResponse>>(
            code = "200",
            message = "SUCCESS",
            data = null
        )
        whenever(transactionRemoteDatasource.getBankAccounts()).thenReturn(
            Observable.just(responseAPI)
        )

        val observable = transactionRepositoryImpl.getMainBankAccount()
        val testObserver = TestObserver<BankAccountResponse>()
        observable.subscribe(testObserver)
        verify(transactionRemoteDatasource, times(1)).getBankAccounts()
        testObserver.assertNotComplete()
        testObserver.assertFailure(BebasException::class.java)
        testObserver.assertError {
            assertEquals(
                BebasException.generalRC("DATA_MISSING").toJson(),
                BebasException.fromThrowable(it).toJson()
            )
            true
        }
    }

    @Test
    fun `get_bank_accounts_data_empty`() {
        val responseAPI = BaseResponse<List<BankAccountResponse>>(
            code = "200",
            message = "SUCCESS",
            data = listOf()
        )
        whenever(transactionRemoteDatasource.getBankAccounts()).thenReturn(
            Observable.just(responseAPI)
        )

        val observable = transactionRepositoryImpl.getMainBankAccount()
        val testObserver = TestObserver<BankAccountResponse>()
        observable.subscribe(testObserver)

        verify(transactionRemoteDatasource, times(1)).getBankAccounts()

        testObserver.assertNotComplete()
        testObserver.assertFailure(BebasException::class.java)
        testObserver.assertError {
            assertEquals(
                BebasException.generalRC("MBA_00").toJson(),
                BebasException.fromThrowable(it).toJson()
            )
            true
        }
    }


    @Test
    fun `inquiry_between_bank_mas_success`() {
        val mainBankAccountResponse = BankAccountResponse(
            accountNumber = "FAKE_ACCOUNT_NUMBER",
            accountName = "FAKE_ACCOUNT_NAME",
            workingBalance = 25000.0
        )
        val baseResponseMainBankAccount = BaseResponse<List<BankAccountResponse>>(
            code = "200",
            message = "SUCCESS",
            data = listOf(mainBankAccountResponse)
        )
        val destinationAccountNumber = "FAKE_DESTINATION_ACCOUNT_NUMBER"
        val inquiryRequest = InquiryBankMasRequest(
            accountNumber = mainBankAccountResponse.accountNumber ?: "",
            destinationAccountNumber = destinationAccountNumber
        )

        val inquiryBankResult = InquiryBankResponse(
            destinationAccountName = "FAKE_DESTINATION_ACCOUNT_NAME"
        )
        val baseResponseInquiry = BaseResponse<InquiryBankResponse>(
            code = "200",
            message = "SUCCESS",
            data = inquiryBankResult
        )

        whenever(transactionRemoteDatasource.getBankAccounts()).thenReturn(
            Observable.just(baseResponseMainBankAccount)
        )
        whenever(transactionRemoteDatasource.inquiryBankMas(inquiryRequest)).thenReturn(
            Observable.just(baseResponseInquiry)
        )
        val inquiryObservable =
            transactionRepositoryImpl.inquiryBankMasReturnModel(destinationAccountNumber)
        val testInquiryObserver = TestObserver<InquiryResultModel>()
        inquiryObservable.subscribe(testInquiryObserver)

        verify(transactionRemoteDatasource, times(1)).getBankAccounts()
        verify(transactionRemoteDatasource, times(1)).inquiryBankMas(inquiryRequest)

        testInquiryObserver.assertComplete()
        testInquiryObserver.assertValue(
            InquiryResultModel(
                additionalTransfer = InquiryResultModel.InquiryTransferBank(
                    inquiryBank = inquiryBankResult,
                    isInquiryBankMas = true
                )
            )
        )
    }
}