package com.fadlurahmanf.bebas_transaction.presentation.favorite

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.FavoriteContactModel
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
                    pinnedFavorites.forEach {
                        Log.d("BebasLogger", "TES PINNED FAVORITE: ${it}")
                    }
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
        if (isCurrentPinned) {

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
            }
        }
    }
}