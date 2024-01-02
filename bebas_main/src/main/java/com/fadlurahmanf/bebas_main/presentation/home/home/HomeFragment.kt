package com.fadlurahmanf.bebas_main.presentation.home.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.fadlurahmanf.bebas_api.data.dto.promo.ItemPromoResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_main.data.dto.argument.ProductTransactionMenuArgument
import com.fadlurahmanf.bebas_main.data.dto.model.home.HomeBankAccountModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.databinding.FragmentHomeBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainFragment
import com.fadlurahmanf.bebas_main.presentation.home.adapter.BankAccountAdapter
import com.fadlurahmanf.bebas_main.presentation.home.adapter.ProductTransactionMenuAdapter
import com.fadlurahmanf.bebas_main.presentation.home.adapter.PromoAdapter
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationActivity
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : BaseMainFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    ProductTransactionMenuAdapter.Callback {
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModel: HomeFragmentViewModel

    lateinit var promoAdapter: PromoAdapter
    private var promos: ArrayList<ItemPromoResponse> = arrayListOf()

    lateinit var bankAccountAdapter: BankAccountAdapter
    private var bankAccounts: ArrayList<HomeBankAccountModel> = arrayListOf()

    lateinit var productTransactionMenuAdapter: ProductTransactionMenuAdapter
    private val menus: ArrayList<ProductTransactionMenuModel> = arrayListOf()

    override fun injectFragment() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModel.promosState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    promos.clear()
                    promos.addAll(it.data)
                    promoAdapter.setList(promos)

                    binding.llPromo.visibility = View.VISIBLE
                }

                is NetworkState.FAILED -> {
                    binding.llPromo.visibility = View.GONE
                }

                is NetworkState.LOADING -> {
                    binding.llPromo.visibility = View.GONE
                }

                else -> {

                }
            }
        }

        viewModel.bankAccounts.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    binding.layoutBankAccount.tvHelloUser.text =
                        "Halo ${it.data.firstOrNull()?.accountName ?: "-"}!"

                    bankAccounts.clear()
                    bankAccounts.addAll(it.data)
                    bankAccountAdapter.setList(bankAccounts)

                    binding.layoutBankAccountShimmer.llMain.visibility = View.GONE
                    binding.layoutBankAccount.llMain.visibility = View.VISIBLE
                }

                is NetworkState.FAILED -> {
                    binding.layoutBankAccountShimmer.llMain.visibility = View.VISIBLE
                    binding.layoutBankAccount.llMain.visibility = View.GONE
                }

                is NetworkState.LOADING -> {
                    binding.layoutBankAccountShimmer.llMain.visibility = View.VISIBLE
                    binding.layoutBankAccount.llMain.visibility = View.GONE
                }

                else -> {

                }
            }
        }

        viewModel.menuState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    menus.clear()
                    menus.addAll(it.data)
                    productTransactionMenuAdapter.setList(menus)

                    binding.rv.visibility = View.VISIBLE
                }

                is NetworkState.LOADING -> {
                    binding.rv.visibility = View.GONE
                }

                is NetworkState.FAILED -> {
                    binding.rv.visibility = View.GONE
                }

                else -> {}
            }
        }
    }

    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.itemNotificationBell.cNotificationBell.setOnClickListener {
            val intent = Intent(
                requireContext(),
                NotificationActivity::class.java
            )
            startActivity(intent)
        }

        bankAccountAdapter = BankAccountAdapter()
        bankAccountAdapter.setList(bankAccounts)
        binding.layoutBankAccount.vpBankAccount.adapter = bankAccountAdapter

        val layoutManager = GridLayoutManager(requireContext(), 4)
        productTransactionMenuAdapter = ProductTransactionMenuAdapter(
            type = ProductTransactionMenuAdapter.Type.HOME_PRODUCT_TRANSACTION_MENU
        )
        productTransactionMenuAdapter.setCallback(this)
        productTransactionMenuAdapter.setList(menus)
        binding.rv.layoutManager = layoutManager
        binding.rv.adapter = productTransactionMenuAdapter

        promoAdapter = PromoAdapter()
        promoAdapter.setList(promos)
        binding.vpPromo.adapter = promoAdapter
        binding.vpPromo.clipToPadding = false
        binding.vpPromo.clipChildren = false
        binding.vpPromo.offscreenPageLimit = 3

        viewModel.getMenus()
        viewModel.getBankAccounts()
        viewModel.getPromos()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var transactionMenuBottomsheet: ProductTransactionBottomsheet? = null
    override fun onTransactionMenuClicked(menuModel: ProductTransactionMenuModel) {
        transactionMenuBottomsheet?.dismiss()
        transactionMenuBottomsheet = null
        transactionMenuBottomsheet = ProductTransactionBottomsheet()
        transactionMenuBottomsheet?.arguments = Bundle().apply {
            putParcelable(
                ProductTransactionBottomsheet.ARGUMENT, ProductTransactionMenuArgument(
                    selectedProductMenuId = menuModel.productMenuId,
                    transactionMenus = viewModel.menus
                )
            )
        }
        transactionMenuBottomsheet?.show(
            requireActivity().supportFragmentManager,
            ProductTransactionBottomsheet::class.java.simpleName
        )
    }
}