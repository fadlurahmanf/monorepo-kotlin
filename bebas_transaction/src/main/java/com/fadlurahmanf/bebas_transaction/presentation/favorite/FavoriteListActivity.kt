package com.fadlurahmanf.bebas_transaction.presentation.favorite

import android.os.Bundle
import android.view.Menu
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.databinding.ActivityFavoriteListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import javax.inject.Inject

class FavoriteListActivity :
    BaseTransactionActivity<ActivityFavoriteListBinding>(ActivityFavoriteListBinding::inflate) {

    @Inject
    lateinit var viewModel: FavoriteViewModel
    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)

        viewModel.getTransferFavorite()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}