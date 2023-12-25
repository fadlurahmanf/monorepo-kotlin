package com.fadlurahmanf.bebas_transaction.presentation.favorite

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.argument.transaction.FavoriteArgumentConstant
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.flow.transaction.FavoriteFlow
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransferDetailArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.LatestTransactionModel
import com.fadlurahmanf.bebas_transaction.data.flow.InputDestinationAccountFlow
import com.fadlurahmanf.bebas_transaction.data.flow.TransferDetailFlow
import com.fadlurahmanf.bebas_transaction.data.state.InquiryBankState
import com.fadlurahmanf.bebas_transaction.data.state.PinFavoriteState
import com.fadlurahmanf.bebas_transaction.databinding.ActivityFavoriteListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter.FavoriteAdapter
import com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter.LatestAdapter
import com.fadlurahmanf.bebas_transaction.presentation.others.BankListActivity
import com.fadlurahmanf.bebas_transaction.presentation.others.InputDestinationAccountBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.transfer.TransferDetailActivity
import javax.inject.Inject

class FavoriteListActivity :
    BaseTransactionActivity<ActivityFavoriteListBinding>(ActivityFavoriteListBinding::inflate),
    FavoriteAdapter.Callback, LatestAdapter.Callback {

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

        binding.btnNewReceiver.setOnClickListener {
            onNewReceiverClick()
        }

        favoriteFlow = enumValueOf<FavoriteFlow>(stringFavoriteFlow)
        latestAdapter = LatestAdapter()
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
                    favorites.clear()
                    favorites.addAll(it.data)
                    favoriteAdapter.setList(favorites)

                    pinnedFavorites.clear()
                    pinnedFavorites.addAll(it.data.filter { model ->
                        model.isPinned
                    })
                    pinnedFavoriteAdapter.setList(pinnedFavorites)

                    if (pinnedFavorites.isNotEmpty()) {
                        binding.llPinnedFavorites.visibility = View.VISIBLE
                    }

                    if (favorites.isNotEmpty()) {
                        binding.llFavorites.visibility = View.VISIBLE
                    }
                }

                is NetworkState.FAILED -> {
                    binding.llFavorites.visibility = View.GONE
                    binding.llPinnedFavorites.visibility = View.GONE
                }

                is NetworkState.LOADING -> {
                    binding.llFavorites.visibility = View.GONE
                    binding.llPinnedFavorites.visibility = View.GONE
                }

                else -> {

                }
            }
        }

        viewModel.inquiryBankMasState.observe(this) {
            when (it) {
                is InquiryBankState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBottomsheet(it.exception)
                }

                InquiryBankState.LOADING -> {
                    showLoadingDialog()
                }

                is InquiryBankState.SuccessFromFavoriteActivity -> {
                    dismissLoadingDialog()
                    goToTransferDetailAfterInquiry(
                        inquiryResult = it.result,
                        fromFavorite = it.isFromFavorite,
                        favoriteModel = it.favoriteModel,
                        fromLatest = it.isFromLatest,
                        latestModel = it.latestModel,
                        isInquiryBankMas = it.isInquiryBankMas
                    )
                }

                else -> {}
            }
        }

        viewModel.latestState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    binding.llLatest.visibility = View.GONE
                }

                NetworkState.LOADING -> {
                    binding.llLatest.visibility = View.GONE
                }

                is NetworkState.SUCCESS -> {
                    latests.clear()
                    latests.addAll(it.data)
                    latestAdapter.setList(latests)

                    if (latests.isNotEmpty()) {
                        binding.llLatest.visibility = View.VISIBLE
                    }
                }

                else -> {

                }
            }
        }

        viewModel.getTransferFavorite(favoriteFlow)
        viewModel.getTransferLatest(favoriteFlow)
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

            }
        }
    }

    private var inputAccountBottomsheet: InputDestinationAccountBottomsheet? = null
    private fun showInputAccountBottomsheet() {
        inputAccountBottomsheet = InputDestinationAccountBottomsheet()
        inputAccountBottomsheet?.setCallback(object : InputDestinationAccountBottomsheet.Callback {
            override fun onNextClicked(dialog: Dialog?, destinationAccount: String) {
                super.onNextClicked(dialog, destinationAccount)

            }
        })
        inputAccountBottomsheet?.arguments = Bundle().apply {
            putString(
                InputDestinationAccountBottomsheet.FLOW,
                InputDestinationAccountFlow.PLN_PREPAID.name
            )
        }
        inputAccountBottomsheet?.show(
            supportFragmentManager,
            InputDestinationAccountBottomsheet::class.java.simpleName
        )
    }

    private fun goToTransferDetailAfterInquiry(
        inquiryResult: InquiryBankResponse,
        fromFavorite: Boolean = false,
        favoriteModel: FavoriteContactModel? = null,
        fromLatest: Boolean = false,
        latestModel: LatestTransactionModel? = null,
        isInquiryBankMas: Boolean = false
    ) {
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
                    accountNumber = favoriteModel?.accountNumber ?: latestModel?.accountNumber
                    ?: "-",
                    realAccountName = inquiryResult.destinationAccountName ?: "-",
                    bankName = favoriteModel?.additionalTransferData?.bankName
                        ?: latestModel?.additionalTransferData?.bankName ?: "-",
                )
            )
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPinClicked(isCurrentPinned: Boolean, favorite: FavoriteContactModel) {
        if (isCurrentPinned) {
            var indexFavorite: Int = -1
            for (element in 0 until favorites.size) {
                if (favorites[0].id == favorite.id) {
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
                if (favorites[0].id == favorite.id) {
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

    override fun onItemClicked(favorite: FavoriteContactModel) {
        when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_TRANSFER -> {
                if (favorite.additionalTransferData?.sknId == "5480300" || favorite.additionalTransferData?.rtgsId == "BMSEIDJA") {
                    viewModel.inquiryBankMas(
                        favorite.accountNumber,
                        isFromFavorite = true,
                        favoriteModel = favorite,
                    )
                } else {

                }
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {

            }

            FavoriteFlow.TRANSACTION_MENU_PULSA_DATA -> {

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
        } else {
            binding.llPinnedFavorites.visibility = View.GONE
        }
    }

    override fun onItemClicked(latest: LatestTransactionModel) {
        when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_TRANSFER -> {
                viewModel.inquiryBankMas(
                    latest.accountNumber,
                    isFromLatest = true,
                    latestModel = latest
                )
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {

            }

            FavoriteFlow.TRANSACTION_MENU_PULSA_DATA -> {

            }
        }
    }
}