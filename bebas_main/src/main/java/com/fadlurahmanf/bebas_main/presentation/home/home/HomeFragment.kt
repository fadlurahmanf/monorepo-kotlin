package com.fadlurahmanf.bebas_main.presentation.home.home

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.fadlurahmanf.bebas_api.data.dto.promo.ItemPromoResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.argument.ProductTransactionMenuArgument
import com.fadlurahmanf.bebas_main.data.dto.model.home.HomeBankAccountModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.databinding.FragmentHomeBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainFragment
import com.fadlurahmanf.bebas_main.presentation.home.adapter.BankAccountAdapter
import com.fadlurahmanf.bebas_main.presentation.home.adapter.ProductTransactionMenuAdapter
import com.fadlurahmanf.bebas_main.presentation.home.adapter.PromoAdapter
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationActivity
import com.fadlurahmanf.bebas_shared.extension.toDp
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import javax.inject.Inject
import kotlin.math.roundToInt

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

    private var totalBankAccount = 1

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
                    totalBankAccount = bankAccounts.size

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

        viewModel.cifBebasPoinState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    if (it.data.point != null) {
                        binding.layoutBankAccount.tvTotalBebasPoin.text =
                            it.data.point!!.toDouble().toRupiahFormat(
                                useSymbol = false,
                                useDecimal = false
                            )
                    } else {
                        binding.layoutBankAccount.tvTotalBebasPoin.text =
                            getString(R.string.learn_now)
                    }
                }

                else -> {
                    binding.layoutBankAccount.tvTotalBebasPoin.text = getString(R.string.learn_now)
                }
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
        binding.layoutBankAccount.vpBankAccount.registerOnPageChangeCallback(object :
                                                                                 ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateLayoutParamScrollbar(position + 1)
            }
        })

        binding.layoutBankAccount.llBebasPoin.setOnClickListener {
            val intent = Intent(
                requireContext(),
                Class.forName("com.fadlurahmanf.bebas_loyalty.presentation.history.HistoryLoyaltyActivity")
            )
            startActivity(intent)
        }

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
        viewModel.getBebasPoin()
    }


    fun updateLayoutParamScrollbar(position: Int) {
        val scrollbarLp = LinearLayout.LayoutParams(
            3.toDp(),
            40.toDp()
        )
        scrollbarLp.setMargins(10.toDp(), 0, 8.toDp(), 0)
        binding.layoutBankAccount.clScrollbar.layoutParams = scrollbarLp


        val margin = (40 / totalBankAccount)
        val scrollbarActiveLp =
            ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        scrollbarActiveLp.setMargins(
            0,
            ((position - 1) * margin).toDp(),
            0,
            ((totalBankAccount - position) * margin).toDp()
        )
        binding.layoutBankAccount.vScrollbarActive.layoutParams = scrollbarActiveLp
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