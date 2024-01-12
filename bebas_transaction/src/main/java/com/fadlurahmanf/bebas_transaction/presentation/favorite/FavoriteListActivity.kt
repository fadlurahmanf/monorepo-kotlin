package com.fadlurahmanf.bebas_transaction.presentation.favorite

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.argument.transaction.FavoriteArgumentConstant
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.flow.transaction.FavoriteFlow
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PaymentDetailArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransferDetailArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.LatestTransactionModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.InquiryRequestModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.InquiryResultModel
import com.fadlurahmanf.bebas_transaction.data.flow.InputDestinationAccountFlow
import com.fadlurahmanf.bebas_transaction.data.flow.PaymentDetailFlow
import com.fadlurahmanf.bebas_transaction.data.flow.TransferDetailFlow
import com.fadlurahmanf.bebas_transaction.data.flow.favorite.LatestAdapterFlow
import com.fadlurahmanf.bebas_transaction.data.state.InquiryState
import com.fadlurahmanf.bebas_transaction.data.state.PinFavoriteState
import com.fadlurahmanf.bebas_transaction.databinding.ActivityFavoriteListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter.FavoriteAdapter
import com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter.LatestAdapter
import com.fadlurahmanf.bebas_transaction.presentation.others.BankListActivity
import com.fadlurahmanf.bebas_transaction.presentation.others.ContactListBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.InputDestinationAccountBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.SelectEWalletActivity
import com.fadlurahmanf.bebas_transaction.presentation.ppob.PaymentDetailActivity
import com.fadlurahmanf.bebas_transaction.presentation.transfer.TransferDetailActivity
import com.fadlurahmanf.bebas_ui.bottomsheet.FailedBottomsheet
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel
import javax.inject.Inject

class FavoriteListActivity :
    BaseTransactionActivity<ActivityFavoriteListBinding>(ActivityFavoriteListBinding::inflate),
    FavoriteAdapter.Callback, LatestAdapter.Callback, ContactListBottomsheet.Callback {

    @Inject
    lateinit var viewModel: FavoriteViewModel

    lateinit var favoriteFlow: FavoriteFlow
    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var latestAdapter: LatestAdapter
    private val latests: ArrayList<LatestTransactionModel> = arrayListOf()
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favorites: ArrayList<FavoriteContactModel> = arrayListOf()
    private lateinit var pinnedFavoriteAdapter: FavoriteAdapter
    private val pinnedFavorites: ArrayList<FavoriteContactModel> = arrayListOf()

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)

        val stringFavoriteFlow = intent.getStringExtra(FavoriteArgumentConstant.FAVORITE_FLOW)

        if (stringFavoriteFlow == null) {
            showForcedBackBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        favoriteFlow = enumValueOf<FavoriteFlow>(stringFavoriteFlow)

        binding.btnNewReceiver.setOnClickListener {
            onNewReceiverClick()
        }

        when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_TRANSFER -> {
                latestAdapter = LatestAdapter(LatestAdapterFlow.TRANSFER)
                binding.toolbar.title = "Transfer Favorit"
                binding.tvBtnNewReceiver.text = "Penerima Baru"
                binding.ivButtonNewReceiver.setImageResource(R.drawable.outline_person_add_alt_1_24)
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {
                latestAdapter = LatestAdapter(LatestAdapterFlow.PLN_PREPAID)
                binding.toolbar.title = "Nomor Pelanggan Favorit"
                binding.tvBtnNewReceiver.text = "Penerima Baru"
                binding.ivButtonNewReceiver.setImageResource(R.drawable.outline_person_add_alt_1_24)
            }

            FavoriteFlow.TRANSACTION_MENU_PULSA_DATA -> {
                latestAdapter = LatestAdapter(LatestAdapterFlow.PULSA_PREPAID)
                binding.toolbar.title = "Nomor Ponsel Favorit"
                binding.tvBtnNewReceiver.text = "Nomor Ponsel Baru"
                binding.ivButtonNewReceiver.setImageResource(R.drawable.round_phonelink_ring_24)
            }

            FavoriteFlow.TRANSACTION_MENU_TELKOM_INDIHOME -> {
                latestAdapter = LatestAdapter(LatestAdapterFlow.TELKOM_INDIHOME)
                binding.toolbar.title = "Nomor Pelanggan Favorit"
                binding.tvBtnNewReceiver.text = "Nomor Pelanggan Baru"
                binding.ivButtonNewReceiver.setImageResource(R.drawable.outline_person_add_alt_1_24)
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_POSTPAID_CHECKOUT -> {
                latestAdapter = LatestAdapter(LatestAdapterFlow.PLN_POSTPAID_CHECKOUT)
                binding.toolbar.title = "Nomor Pelanggan Favorit"
                binding.tvBtnNewReceiver.text = "Nomor Pelanggan Baru"
                binding.ivButtonNewReceiver.setImageResource(R.drawable.outline_person_add_alt_1_24)
            }

            FavoriteFlow.TRANSACTION_MENU_TOPUP_EWALLET -> {
                latestAdapter = LatestAdapter(LatestAdapterFlow.TOPUP_EWALLET)
                binding.toolbar.title = "Nomor Ponsel Favorit"
                binding.tvBtnNewReceiver.text = "Nomor Ponsel Baru"
                binding.ivButtonNewReceiver.setImageResource(R.drawable.round_phonelink_ring_24)
            }
        }

        latestAdapter.setCallback(this)
        favoriteAdapter = FavoriteAdapter()
        favoriteAdapter.setCallback(this)
        pinnedFavoriteAdapter = FavoriteAdapter()
        pinnedFavoriteAdapter.setCallback(this)
        binding.rvLatest.adapter = latestAdapter
        binding.rvFavorite.adapter = favoriteAdapter
        binding.rvPinnedFavorite.adapter = pinnedFavoriteAdapter

        viewModel.pinState.observe(this) {
            when (it) {
                is PinFavoriteState.FAILED -> {
                    var index = -1
                    for (element in 0 until favorites.size) {
                        if (favorites[element].id == it.favoriteId) {
                            index = element
                            break
                        }
                    }

                    if (index != -1) {
                        setPinnedData(it.previousStatePinned, index)
                    }

                    showSnackBarErrorLong(
                        binding.root,
                        message = "Failed to Pin Favorite!",
                        binding.llButtonBottom
                    )
                }

                is PinFavoriteState.SuccessPinned -> {
                    if (it.isPinned) {
                        showSnackBarShort(binding.root, "Successfully Pin Favorite!")
                    } else {
                        showSnackBarShort(binding.root, "Successfully Unpin Favorite!")
                    }
                }

                else -> {

                }
            }
        }

        viewModel.favoriteState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    val isPreviousFavoritesExist = favorites.isNotEmpty()
                    favorites.clear()
                    favorites.addAll(it.data)
                    if (isPreviousFavoritesExist) {
                        favoriteAdapter.resetList(favorites)
                    } else {
                        favoriteAdapter.setNewList(favorites)
                    }

                    if (favorites.isNotEmpty()) {
                        binding.llFavorites.visibility = View.VISIBLE
                        binding.llFavoritesShimmer.visibility = View.GONE
                        binding.rvFavorite.visibility = View.VISIBLE
                    } else {
                        binding.llFavorites.visibility = View.GONE
                        binding.llFavoritesShimmer.visibility = View.GONE
                        binding.rvFavorite.visibility = View.GONE
                    }

                    val isPreviousPinnedFavoritesExist = pinnedFavorites.isNotEmpty()
                    pinnedFavorites.clear()
                    pinnedFavorites.addAll(it.data.filter { model ->
                        model.isPinned
                    })
                    if (isPreviousPinnedFavoritesExist) {
                        pinnedFavoriteAdapter.resetList(pinnedFavorites)
                    } else {
                        pinnedFavoriteAdapter.setNewList(pinnedFavorites)
                    }

                    if (pinnedFavorites.isNotEmpty()) {
                        binding.llPinnedFavorites.visibility = View.VISIBLE
                        binding.llPinnedFavoritesShimmer.visibility = View.GONE
                        binding.rvPinnedFavorite.visibility = View.VISIBLE
                    } else {
                        binding.llPinnedFavorites.visibility = View.GONE
                        binding.llPinnedFavoritesShimmer.visibility = View.GONE
                        binding.rvPinnedFavorite.visibility = View.GONE
                    }
                }

                is NetworkState.FAILED -> {
                    binding.llFavorites.visibility = View.GONE
                    binding.llPinnedFavorites.visibility = View.GONE
                }

                is NetworkState.LOADING -> {
                    if (pinnedFavorites.isNotEmpty()) {
                        binding.llPinnedFavorites.visibility = View.VISIBLE
                        binding.llPinnedFavoritesShimmer.visibility = View.GONE
                        binding.rvPinnedFavorite.visibility = View.VISIBLE
                    } else {
                        binding.llPinnedFavorites.visibility = View.VISIBLE
                        binding.llPinnedFavoritesShimmer.visibility = View.VISIBLE
                        binding.rvPinnedFavorite.visibility = View.GONE
                    }

                    if (favorites.isNotEmpty()) {
                        binding.llFavorites.visibility = View.VISIBLE
                        binding.llFavoritesShimmer.visibility = View.GONE
                        binding.rvFavorite.visibility = View.VISIBLE
                    } else {
                        binding.llFavorites.visibility = View.VISIBLE
                        binding.llFavoritesShimmer.visibility = View.VISIBLE
                        binding.rvFavorite.visibility = View.GONE
                    }
                }

                else -> {

                }
            }
        }

        viewModel.inquiryState.observe(this) {
            when (it) {
                is InquiryState.FailedBebas -> {
                    dismissLoadingDialog()
                    showFailedBebasBottomsheet(it.exception)
                }

                is InquiryState.FailedFulfillment -> {
                    dismissLoadingDialog()
                    showFailedFulfillmentBottomsheet(it.exception)
                }

                InquiryState.LOADING -> {
                    showLoadingDialog()
                }

                is InquiryState.SuccessFromFavoriteActivity -> {
                    dismissLoadingDialog()
                    goToDetailAfterInquiry(
                        inquiryResult = it.result,
                        fromFavorite = it.isFromFavorite,
                        favoriteModel = it.favoriteModel,
                        fromLatest = it.isFromLatest,
                        latestModel = it.latestModel,
                    )
                }

                else -> {}
            }
        }

        viewModel.latestState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    binding.llLatest.visibility = View.GONE
                    binding.llLatestShimmer.visibility = View.GONE
                    binding.rvLatest.visibility = View.GONE
                }

                NetworkState.LOADING -> {
                    if (latests.isNotEmpty()) {
                        binding.llLatest.visibility = View.VISIBLE
                        binding.llLatestShimmer.visibility = View.GONE
                        binding.rvLatest.visibility = View.VISIBLE
                    } else {
                        binding.llLatest.visibility = View.VISIBLE
                        binding.llLatestShimmer.visibility = View.VISIBLE
                        binding.rvLatest.visibility = View.GONE
                    }
                }

                is NetworkState.SUCCESS -> {
                    val isPreviousLatestExist = latests.isNotEmpty()
                    latests.clear()
                    latests.addAll(it.data)
                    if (isPreviousLatestExist) {
                        latestAdapter.resetList(latests)
                    } else {
                        latestAdapter.setNewList(latests)
                    }

                    if (latests.isNotEmpty()) {
                        binding.llLatest.visibility = View.VISIBLE
                        binding.llLatestShimmer.visibility = View.GONE
                        binding.rvLatest.visibility = View.VISIBLE
                    } else {
                        binding.llLatest.visibility = View.GONE
                        binding.llLatestShimmer.visibility = View.GONE
                        binding.rvLatest.visibility = View.GONE
                    }
                }

                else -> {

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTransferFavorite(favoriteFlow)
        viewModel.getLatestTransaction(favoriteFlow)
    }

    private val contactPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                showContactListBottomsheet()
            }
        }

    private var contactListBottomsheet: ContactListBottomsheet? = null
    private fun showContactListBottomsheet() {
        contactListBottomsheet = ContactListBottomsheet()
        contactListBottomsheet?.setCallback(this)
        contactListBottomsheet?.show(
            supportFragmentManager,
            ContactListBottomsheet::class.java.simpleName
        )
    }

    private fun onNewReceiverClick() {
        when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_TRANSFER -> {
                val intent = Intent(this, BankListActivity::class.java)
                startActivity(intent)
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {
                showInputAccountBottomsheet()
            }

            FavoriteFlow.TRANSACTION_MENU_PULSA_DATA -> {
                when (ContextCompat.checkSelfPermission(
                    this.applicationContext,
                    Manifest.permission.READ_CONTACTS
                )) {
                    PackageManager.PERMISSION_GRANTED -> {
                        showContactListBottomsheet()
                    }

                    PackageManager.PERMISSION_DENIED -> {
                        showFailedBebasBottomsheet(
                            exception = BebasException(
                                title = "Information",
                                message = "Permission Contact",
                                buttonText = "Open Settings"
                            ),
                            callback = object : FailedBottomsheet.Callback {
                                override fun onButtonClicked() {
                                    dismissFailedBottomsheet()
                                    goToAppPermission()
                                }

                            }
                        )
                    }

                    else -> {
                        contactPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }

            FavoriteFlow.TRANSACTION_MENU_TELKOM_INDIHOME -> {

            }

            FavoriteFlow.TRANSACTION_MENU_PLN_POSTPAID_CHECKOUT -> {
                showInputAccountBottomsheet()
            }

            FavoriteFlow.TRANSACTION_MENU_TOPUP_EWALLET -> {
                val intent = Intent(this, SelectEWalletActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private var inputAccountBottomsheet: InputDestinationAccountBottomsheet? = null
    private fun showInputAccountBottomsheet() {
        inputAccountBottomsheet = InputDestinationAccountBottomsheet()
        inputAccountBottomsheet?.setCallback(object : InputDestinationAccountBottomsheet.Callback {
            override fun onNextClicked(dialog: Dialog?, destinationAccount: String) {
                super.onNextClicked(dialog, destinationAccount)
                dialog?.dismiss()
                when (favoriteFlow) {
                    FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {

                    }

                    FavoriteFlow.TRANSACTION_MENU_PLN_POSTPAID_CHECKOUT -> {
                        viewModel.inquiry(
                            InquiryRequestModel.InquiryPLNPostPaid(
                                customerId = destinationAccount,
                                billingCategory = "Listrik",
                                providerName = "PLN"
                            )
                        )
                    }

                    else -> {

                    }
                }
            }
        })

        when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {
                inputAccountBottomsheet?.arguments = Bundle().apply {
                    putString(
                        InputDestinationAccountBottomsheet.FLOW,
                        InputDestinationAccountFlow.PLN_PREPAID.name
                    )
                }
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_POSTPAID_CHECKOUT -> {
                inputAccountBottomsheet?.arguments = Bundle().apply {
                    putString(
                        InputDestinationAccountBottomsheet.FLOW,
                        InputDestinationAccountFlow.PLN_POSTPAID.name
                    )
                }
            }

            else -> {}
        }

        inputAccountBottomsheet?.show(
            supportFragmentManager,
            InputDestinationAccountBottomsheet::class.java.simpleName
        )
    }

    private fun goToDetailAfterInquiry(
        inquiryResult: InquiryResultModel,
        fromFavorite: Boolean = false,
        favoriteModel: FavoriteContactModel? = null,
        fromLatest: Boolean = false,
        latestModel: LatestTransactionModel? = null,
    ) {
        when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_TRANSFER -> {
                val isInquiryBankMas =
                    inquiryResult.additionalInquiryTransferBank?.isInquiryBankMas == true
                val intent = Intent(this, TransferDetailActivity::class.java)
                intent.apply {
                    putExtra(
                        TransferDetailActivity.FLOW,
                        if (isInquiryBankMas) TransferDetailFlow.TRANSFER_BETWEEN_BANK_MAS.name else TransferDetailFlow.TRANSFER_OTHER_BANK.name
                    )
                    putExtra(
                        TransferDetailActivity.ARGUMENT, TransferDetailArgument(
                            isFavorite = fromFavorite,
                            accountName = favoriteModel?.additionalTransferData?.aliasName
                                ?: latestModel?.additionalTransferData?.accountName ?: "-",
                            accountNumber = favoriteModel?.accountNumber
                                ?: latestModel?.accountNumber
                                ?: "-",
                            realAccountName = inquiryResult.additionalInquiryTransferBank?.inquiryBank?.destinationAccountName
                                ?: "-",
                            bankName = favoriteModel?.additionalTransferData?.bankName
                                ?: latestModel?.additionalTransferData?.bankName ?: "-",
                        )
                    )
                }
                startActivity(intent)
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {

            }

            FavoriteFlow.TRANSACTION_MENU_PULSA_DATA -> {
                val intent = Intent(this, PaymentDetailActivity::class.java)
                intent.apply {
                    putExtra(PaymentDetailActivity.FLOW, PaymentDetailFlow.PULSA_DATA.name)
                    putExtra(
                        PaymentDetailActivity.ARGUMENT, PaymentDetailArgument(
                            isFavorite = false,
                            isFavoriteEnabled = true,
                            labelIdentity = inquiryResult.inquiryPulsaData?.providerName ?: "-",
                            subLabelIdentity = inquiryResult.inquiryPulsaData?.phoneNumber ?: "-",
                            additionalPulsaData = PaymentDetailArgument.AdditionalPulsaDataArgument(
                                providerName = inquiryResult.inquiryPulsaData?.providerName ?: "",
                                providerImage = inquiryResult.inquiryPulsaData?.imageProvider,
                                phoneNumber = inquiryResult.inquiryPulsaData?.phoneNumber ?: "",
                                inquiry = inquiryResult.inquiryPulsaData!!
                            )
                        )
                    )
                }
                startActivity(intent)
            }

            FavoriteFlow.TRANSACTION_MENU_TELKOM_INDIHOME -> {
                val intent = Intent(this, PaymentDetailActivity::class.java)
                intent.apply {
                    putExtra(PaymentDetailActivity.FLOW, PaymentDetailFlow.TELKOM_INDIHOME.name)
                    putExtra(
                        PaymentDetailActivity.ARGUMENT, PaymentDetailArgument(
                            isFavorite = fromFavorite,
                            isFavoriteEnabled = true,
                            labelIdentity = inquiryResult.inquiryTelkomIndihome?.customerName
                                ?: "-",
                            subLabelIdentity = "Telkom • ${inquiryResult.inquiryTelkomIndihome?.customerNumber ?: "-"}",
                            additionalTelkomIndihome = PaymentDetailArgument.AdditionalTelkomIndihome(
                                providerImage = latestModel?.additionalTelkomIndihome?.providerLogo,
                                periode = inquiryResult.inquiryTelkomIndihome?.periode ?: "-",
                                tagihan = inquiryResult.inquiryTelkomIndihome?.amountTransaction
                                    ?: -1.0,
                                inquiry = inquiryResult.inquiryTelkomIndihome!!
                            )
                        )
                    )
                }
                startActivity(intent)
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_POSTPAID_CHECKOUT -> {
                val intent = Intent(this, PaymentDetailActivity::class.java)
                intent.apply {
                    putExtra(
                        PaymentDetailActivity.FLOW,
                        PaymentDetailFlow.PLN_POSTPAID_CHECKOUT.name
                    )
                    putExtra(
                        PaymentDetailActivity.ARGUMENT, PaymentDetailArgument(
                            isFavorite = fromFavorite,
                            isFavoriteEnabled = true,
                            labelIdentity = inquiryResult.inquiryPLNPostPaidCheckout?.clientName
                                ?: "-",
                            subLabelIdentity = "PLN • ${inquiryResult.inquiryPLNPostPaidCheckout?.clientNumber ?: "-"}",
                            additionalPLNPostPaidCheckout = PaymentDetailArgument.AdditionalPLNPostPaidCheckout(
                                clientName = inquiryResult.inquiryPLNPostPaidCheckout?.clientName
                                    ?: "-",
                                inquiry = inquiryResult.inquiryPLNPostPaidCheckout!!
                            )
                        )
                    )
                }
                startActivity(intent)
            }

            FavoriteFlow.TRANSACTION_MENU_TOPUP_EWALLET -> {

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPinClicked(isCurrentPinned: Boolean, favorite: FavoriteContactModel) {
        if (isCurrentPinned) {
            var indexFavorite: Int = -1
            for (element in 0 until favorites.size) {
                if (favorites[element].id == favorite.id) {
                    indexFavorite = element
                    break
                }
            }

            if (indexFavorite != -1) {
                setPinnedData(true, indexFavorite)
                viewModel.pinFavorite(favorites[indexFavorite].id, false)
            }
        } else {
            val newFavorite = favorite.copy(isPinned = true)
            pinnedFavorites.add(newFavorite)
            pinnedFavoriteAdapter.insertModel(newFavorite)

            var indexFavorite: Int = -1
            for (element in 0 until favorites.size) {
                if (favorites[element].id == favorite.id) {
                    indexFavorite = element
                    break
                }
            }

            if (indexFavorite != -1) {
                setPinnedData(false, indexFavorite)
                viewModel.pinFavorite(favorites[indexFavorite].id, true)
            }
        }


    }

    override fun onFavoriteItemClicked(favorite: FavoriteContactModel) {
        when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_TRANSFER -> {
                if (favorite.additionalTransferData?.sknId == "5480300" || favorite.additionalTransferData?.rtgsId == "BMSEIDJA") {
                    viewModel.inquiry(
                        InquiryRequestModel.InquiryBankMas(
                            destinationAccountNumber = favorite.accountNumber
                        ),
                        isFromFavorite = true,
                        favoriteModel = favorite,
                    )
                } else {

                }
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {
                val intent = Intent(this, PaymentDetailActivity::class.java)
                intent.apply {
                    putExtra(
                        PaymentDetailActivity.FLOW,
                        PaymentDetailFlow.PLN_PREPAID_CHECKOUT.name
                    )
                    putExtra(
                        PaymentDetailActivity.ARGUMENT, PaymentDetailArgument(
                            isFavorite = false,
                            isFavoriteEnabled = false,
                            labelIdentity = "PLN",
                            subLabelIdentity = favorite.accountNumber,
                            additionalPLNPrePaidCheckout = PaymentDetailArgument.AdditionalPLNPrePaidCheckout(
                                clientNumber = favorite.accountNumber
                            )
                        )
                    )
                }
                startActivity(intent)
            }

            FavoriteFlow.TRANSACTION_MENU_PULSA_DATA -> {
                viewModel.inquiry(
                    InquiryRequestModel.InquiryPulsaData(
                        phoneNumber = favorite.accountNumber
                    ),
                    isFromFavorite = true,
                    favoriteModel = favorite,
                )
            }

            FavoriteFlow.TRANSACTION_MENU_TELKOM_INDIHOME -> {
                viewModel.inquiry(
                    InquiryRequestModel.InquiryTelkomIndihome(
                        customerId = favorite.accountNumber
                    ),
                    isFromFavorite = true,
                    favoriteModel = favorite,
                )
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_POSTPAID_CHECKOUT -> {
                viewModel.inquiry(
                    InquiryRequestModel.InquiryPLNPostPaid(
                        customerId = favorite.accountNumber,
                        providerName = favorite.additionalPlnPostPaidData?.providerName ?: "-",
                        billingCategory = favorite.additionalPlnPostPaidData?.categoryName ?: "-"
                    ),
                    isFromFavorite = true,
                    favoriteModel = favorite,
                )
            }

            FavoriteFlow.TRANSACTION_MENU_TOPUP_EWALLET -> {

            }
        }
    }

    private fun setPinnedData(isCurrentPinned: Boolean, indexFavorite: Int) {
        if (isCurrentPinned) {
            pinnedFavorites.removeAt(indexFavorite)
            pinnedFavoriteAdapter.removeModel(indexFavorite)

            favorites[indexFavorite].isPinned = false
            favoriteAdapter.changeFavoriteModel(favorites, indexFavorite)
        } else {
            favorites[indexFavorite].isPinned = true
            favoriteAdapter.changeFavoriteModel(favorites, indexFavorite)
        }

        if (pinnedFavorites.isNotEmpty()) {
            binding.llPinnedFavorites.visibility = View.VISIBLE
            binding.llPinnedFavoritesShimmer.visibility = View.GONE
            binding.rvPinnedFavorite.visibility = View.VISIBLE
        } else {
            binding.llPinnedFavorites.visibility = View.GONE
            binding.llPinnedFavoritesShimmer.visibility = View.GONE
            binding.rvPinnedFavorite.visibility = View.GONE
        }
    }

    override fun onLatestTransactionItemClicked(latest: LatestTransactionModel) {
        when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_TRANSFER -> {
                viewModel.inquiry(
                    InquiryRequestModel.InquiryBankMas(
                        destinationAccountNumber = latest.accountNumber
                    ),
                    isFromLatest = true,
                    latestModel = latest
                )
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {

            }

            FavoriteFlow.TRANSACTION_MENU_PULSA_DATA -> {
                viewModel.inquiry(
                    InquiryRequestModel.InquiryPulsaData(
                        phoneNumber = latest.accountNumber
                    ),
                    isFromLatest = true,
                    latestModel = latest
                )
            }

            FavoriteFlow.TRANSACTION_MENU_TELKOM_INDIHOME -> {
                viewModel.inquiry(
                    InquiryRequestModel.InquiryTelkomIndihome(
                        customerId = latest.accountNumber
                    ),
                    isFromLatest = true,
                    latestModel = latest,
                )
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_POSTPAID_CHECKOUT -> {
                viewModel.inquiry(
                    InquiryRequestModel.InquiryPLNPostPaid(
                        customerId = latest.accountNumber,
                        providerName = latest.additionalPLNPostPaid?.providerName ?: "-",
                        billingCategory = latest.additionalPLNPostPaid?.billingCategory ?: "-"
                    ),
                    isFromFavorite = true,
                    latestModel = latest,
                )
            }

            FavoriteFlow.TRANSACTION_MENU_TOPUP_EWALLET -> {

            }
        }
    }

    override fun onContactClicked(contact: BebasContactModel) {
        viewModel.inquiry(InquiryRequestModel.InquiryPulsaData(phoneNumber = contact.phoneNumber))
    }
}