package com.fadlurahmanf.bebas_transaction.presentation.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.argument.transaction.FavoriteArgument
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.flow.transaction.FavoriteFlow
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.dto.LatestTransactionModel
import com.fadlurahmanf.bebas_transaction.data.state.InquiryBankState
import com.fadlurahmanf.bebas_transaction.data.state.PinFavoriteState
import com.fadlurahmanf.bebas_transaction.databinding.ActivityFavoriteListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter.FavoriteAdapter
import com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter.LatestAdapter
import com.fadlurahmanf.bebas_transaction.presentation.others.BankListActivity
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

        val stringFavoriteFlow = intent.getStringExtra(FavoriteArgument.FAVORITE_FLOW)

        if (stringFavoriteFlow == null) {
            showForcedBackBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        binding.btnNewReceiver.setOnClickListener {
            val intent = Intent(this, BankListActivity::class.java)
            startActivity(intent)
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
                        showSnackBarShort(binding.root, "Failed to Pin Favorite!")
                        pinOrUnpinFavorite(it.previousStatePinned, favorites[index])
                    }
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

                    binding.llFavorites.visibility = View.VISIBLE
                    binding.llPinnedFavorites.visibility = View.VISIBLE
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

                is InquiryBankState.SUCCESS -> {
                    dismissLoadingDialog()
                    goToTransferDetailAfterInquiry(
                        it.result,
                        it.isFromFavorite,
                        it.favoriteModel,
                        it.isFromLatest,
                        it.latestModel
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

                    binding.llLatest.visibility = View.VISIBLE
                }

                else -> {

                }
            }
        }

        if (favoriteFlow == FavoriteFlow.TRANSACTION_MENU_TRANSFER) {
            viewModel.getTransferFavorite()
            viewModel.getTransferLatest()
        }
    }

    private fun goToTransferDetailAfterInquiry(
        inquiryResult: InquiryBankResponse,
        fromFavorite: Boolean = false,
        favoriteModel: FavoriteContactModel? = null,
        fromLatest: Boolean = false,
        latestModel: LatestTransactionModel? = null
    ) {
        val intent = Intent(this, TransferDetailActivity::class.java)
        intent.apply {
            putExtra(TransferDetailActivity.IS_FAVORITE, fromFavorite)
            putExtra(
                TransferDetailActivity.DESTINATION_ACCOUNT_NAME,
                favoriteModel?.nameInFavoriteContact ?: inquiryResult.destinationAccountName ?: "-"
            )
            putExtra(
                TransferDetailActivity.SUB_LABEL,
                "${favoriteModel?.labelTypeOfFavorite} • ${favoriteModel?.accountNumber}"
            )
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPinClicked(isCurrentPinned: Boolean, favorite: FavoriteContactModel) {
        pinOrUnpinFavorite(isCurrentPinned, favorite)
    }

    override fun onItemClicked(favorite: FavoriteContactModel) {
        viewModel.inquiryBankMas(
            favorite.accountNumber,
            isFromFavorite = true,
            favoriteModel = favorite
        )
    }

    fun pinOrUnpinFavorite(isCurrentPinned: Boolean, favorite: FavoriteContactModel) {
        if (isCurrentPinned) {
            var indexFavorite: Int = -1
            for (element in 0 until favorites.size) {
                if (favorites[0].id == favorite.id) {
                    indexFavorite = element
                    break
                }
            }

            if (indexFavorite != -1) {
                pinnedFavorites.removeAt(indexFavorite)
                pinnedFavoriteAdapter.removeModel(indexFavorite)

                favorites[indexFavorite].isPinned = false
                favoriteAdapter.changeFavoriteModel(favorites, indexFavorite)

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
                favorites[indexFavorite].isPinned = true
                favoriteAdapter.changeFavoriteModel(favorites, indexFavorite)

                viewModel.pinFavorite(favorites[indexFavorite].id, true)
            }
        }


        if (pinnedFavorites.isNotEmpty()) {
            binding.llPinnedFavorites.visibility = View.VISIBLE
        } else {
            binding.llPinnedFavorites.visibility = View.GONE
        }
    }

    override fun onItemClicked(latest: LatestTransactionModel) {
        viewModel.inquiryBankMas(latest.accountNumber, isFromLatest = true, latestModel = latest)
    }
}