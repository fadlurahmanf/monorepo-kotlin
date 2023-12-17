package com.fadlurahmanf.bebas_transaction.presentation.favorite

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.state.PinFavoriteState
import com.fadlurahmanf.bebas_transaction.databinding.ActivityFavoriteListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter.FavoriteAdapter
import javax.inject.Inject

class FavoriteListActivity :
    BaseTransactionActivity<ActivityFavoriteListBinding>(ActivityFavoriteListBinding::inflate),
    FavoriteAdapter.Callback {

    @Inject
    lateinit var viewModel: FavoriteViewModel
    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favorites: ArrayList<FavoriteContactModel> = arrayListOf()
    private lateinit var pinnedFavoriteAdapter: FavoriteAdapter
    private val pinnedFavorites: ArrayList<FavoriteContactModel> = arrayListOf()

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)

        favoriteAdapter = FavoriteAdapter()
        favoriteAdapter.setCallback(this)
        pinnedFavoriteAdapter = FavoriteAdapter()
        pinnedFavoriteAdapter.setCallback(this)
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

        viewModel.getTransferFavorite()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPinClicked(isCurrentPinned: Boolean, favorite: FavoriteContactModel) {
        pinOrUnpinFavorite(isCurrentPinned, favorite)
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
}