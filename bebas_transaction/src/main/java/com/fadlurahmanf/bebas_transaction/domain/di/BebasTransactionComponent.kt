package com.fadlurahmanf.bebas_transaction.domain.di

import android.content.Context
import com.fadlurahmanf.bebas_storage.BebasStorageComponent
import com.fadlurahmanf.bebas_transaction.presentation.others.SelectPaymentSourceBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.BankListActivity
import com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity
import com.fadlurahmanf.bebas_transaction.presentation.invoice.InvoiceTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.others.ContactListBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.SelectEWalletActivity
import com.fadlurahmanf.bebas_transaction.presentation.pin.PinVerificationActivity
import com.fadlurahmanf.bebas_transaction.presentation.ppob.PaymentDetailActivity
import com.fadlurahmanf.bebas_transaction.presentation.ppob.TransactionConfirmationBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.ppob.TransactionConfirmationFlowCheckoutBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data.PaketDataDenomFragment
import com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data.PulsaDenomFragment
import com.fadlurahmanf.bebas_transaction.presentation.transfer.TransferConfirmationBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.transfer.TransferDetailActivity
import com.fadlurahmanf.core_crypto.CoreCryptoComponent
import com.fadlurahmanf.core_platform.CorePlatformComponent
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [
        CoreCryptoComponent::class,
        CorePlatformComponent::class,
        BebasStorageComponent::class
    ]
)
interface BebasTransactionComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            cryptoComponent: CoreCryptoComponent,
            platformComponent: CorePlatformComponent,
            bebasStorageComponent: BebasStorageComponent
        ): BebasTransactionComponent
    }

    fun inject(activity: FavoriteListActivity)
    fun inject(activity: BankListActivity)
    fun inject(activity: TransferDetailActivity)
    fun inject(activity: PinVerificationActivity)
    fun inject(activity: InvoiceTransactionActivity)
    fun inject(activity: PaymentDetailActivity)
    fun inject(activity: SelectEWalletActivity)

    // bottomsheet
    fun inject(botttomsheet: SelectPaymentSourceBottomsheet)
    fun inject(botttomsheet: TransferConfirmationBottomsheet)
    fun inject(botttomsheet: TransactionConfirmationBottomsheet)
    fun inject(botttomsheet: TransactionConfirmationFlowCheckoutBottomsheet)
    fun inject(botttomsheet: ContactListBottomsheet)
    fun inject(fragment: PulsaDenomFragment)
    fun inject(fragment: PaketDataDenomFragment)
}