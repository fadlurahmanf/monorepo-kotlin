package com.fadlurahmanf.bebas_transaction.presentation.ppob

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingTelkomIndihomeRequest
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PaymentDetailArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PinVerificationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PulsaDataArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransactionConfirmationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.ppob.PPOBDenomModel
import com.fadlurahmanf.bebas_transaction.data.dto.result.TransactionConfirmationResult
import com.fadlurahmanf.bebas_transaction.data.flow.PaymentDetailFlow
import com.fadlurahmanf.bebas_transaction.data.flow.PinVerificationFlow
import com.fadlurahmanf.bebas_transaction.data.flow.TransactionConfirmationFlow
import com.fadlurahmanf.bebas_transaction.databinding.ActivityPaymentDetailBinding
import com.fadlurahmanf.bebas_transaction.external.BebasTransactionHelper
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.pin.PinVerificationActivity
import com.fadlurahmanf.bebas_transaction.presentation.ppob.adapter.PPOBDenomAdapter
import com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data.PulsaDataTabAdapter
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class PaymentDetailActivity :
    BaseTransactionActivity<ActivityPaymentDetailBinding>(ActivityPaymentDetailBinding::inflate),
    TransactionConfirmationBottomsheet.Callback, PPOBDenomAdapter.Callback {

    companion object {
        const val ARGUMENT = "ARGUMENT"
        const val FLOW = "FLOW"
    }

    private lateinit var argument: PaymentDetailArgument
    private lateinit var flow: PaymentDetailFlow

    private lateinit var tabPulsaDataAdapter: PulsaDataTabAdapter
    private lateinit var ppobDenomAdapter: PPOBDenomAdapter
    private val plnDenoms: ArrayList<PPOBDenomModel> = arrayListOf()

    @Inject
    lateinit var viewModel: PaymentDetailViewModel


    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val stringFlow = intent.getStringExtra(FLOW)

        if (stringFlow == null) {
            showForcedBackBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        flow = enumValueOf(stringFlow)

        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARGUMENT, PaymentDetailArgument::class.java)
        } else {
            intent.getParcelableExtra(ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedBackBottomsheet(BebasException.generalRC("MISSING_ARGUMENT"))
            return
        }

        argument = p0Arg

        setupTitle()
        setupMainLayoutDetailPPOB()
        setupIdentityPPOB()
        initObserver()

        binding.btnNext.setOnClickListener {
            when (flow) {
                PaymentDetailFlow.TELKOM_INDIHOME -> {
                    showTransactionConfirmationBottomsheet()
                }

                else -> {

                }
            }
        }

        when (flow) {
            PaymentDetailFlow.PLN_PREPAID_CHECKOUT -> {
                ppobDenomAdapter = PPOBDenomAdapter()
                ppobDenomAdapter.setList(plnDenoms)
                ppobDenomAdapter.setCallback(this)
                val gridLayoutManager = GridLayoutManager(this, 2)
                binding.rv.layoutManager = gridLayoutManager
                binding.rv.adapter = ppobDenomAdapter
                viewModel.getPLNPrePaidDenom()
            }

            else -> {

            }
        }
    }

    private fun initObserver() {
        if (flow == PaymentDetailFlow.PLN_PREPAID_CHECKOUT) {
            viewModel.selectedDenomModel.observe(this) {
                if (it != null) {
                    binding.llBottomLayout.visibility = View.VISIBLE
                } else {
                    binding.llBottomLayout.visibility = View.GONE
                }
            }
        }

        viewModel.plnPrePaidDenomState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    if (plnDenoms.isNotEmpty()) {
                        binding.lLayoutPpobDenomShimmer.visibility = View.GONE
                        binding.rv.visibility = View.VISIBLE
                    } else {
                        binding.lLayoutPpobDenomShimmer.visibility = View.GONE
                        binding.rv.visibility = View.GONE
                    }
                }

                is NetworkState.LOADING -> {
                    if (plnDenoms.isNotEmpty()) {
                        binding.lLayoutPpobDenomShimmer.visibility = View.GONE
                        binding.rv.visibility = View.VISIBLE
                    } else {
                        binding.lLayoutPpobDenomShimmer.visibility = View.VISIBLE
                        binding.rv.visibility = View.VISIBLE
                    }
                }

                is NetworkState.SUCCESS -> {
                    plnDenoms.clear()
                    plnDenoms.addAll(it.data)
                    ppobDenomAdapter.setList(plnDenoms)

                    if (plnDenoms.isNotEmpty()) {
                        binding.lLayoutPpobDenomShimmer.visibility = View.GONE
                        binding.rv.visibility = View.VISIBLE
                    } else {
                        binding.lLayoutPpobDenomShimmer.visibility = View.GONE
                        binding.rv.visibility = View.GONE
                    }
                }

                else -> {

                }
            }
        }
    }

    private var transactionConfirmationBottomsheet: TransactionConfirmationBottomsheet? = null
    private fun showTransactionConfirmationBottomsheet() {
        transactionConfirmationBottomsheet?.dismiss()
        transactionConfirmationBottomsheet = null
        transactionConfirmationBottomsheet = TransactionConfirmationBottomsheet()
        val bundle = Bundle()
        transactionConfirmationBottomsheet?.setCallback(this)
        when (flow) {
            PaymentDetailFlow.PULSA_DATA -> {

            }

            PaymentDetailFlow.TELKOM_INDIHOME -> {
                val inquiry = argument.additionalTelkomIndihome?.inquiry
                bundle.apply {
                    putString(
                        TransactionConfirmationBottomsheet.FLOW,
                        TransactionConfirmationFlow.TELKOM_INDIHOME.name
                    )
                    putParcelable(
                        TransactionConfirmationBottomsheet.ADDITIONAL_ARG,
                        TransactionConfirmationArgument(
                            destinationLabel = inquiry?.customerName ?: "-",
                            destinationSubLabel = "Telkom • ${inquiry?.customerNumber ?: "-"}",
                            feeDetail = TransactionConfirmationArgument.FeeDetail(
                                total = (inquiry?.amountTransaction
                                    ?: -1.0) + (inquiry?.transactionFee ?: -1.0),
                                details = arrayListOf(
                                    TransactionConfirmationArgument.FeeDetail.Detail(
                                        label = "Tagihan",
                                        value = inquiry?.amountTransaction ?: -1.0,
                                    ),
                                    TransactionConfirmationArgument.FeeDetail.Detail(
                                        label = "Biaya Admin",
                                        value = inquiry?.transactionFee ?: -1.0,
                                    )
                                )
                            ),
                            details = arrayListOf<TransactionConfirmationArgument.Detail>(
                                TransactionConfirmationArgument.Detail(
                                    label = "Periode",
                                    value = inquiry?.periode ?: "-"
                                )
                            )
                        )
                    )
                }
            }

            PaymentDetailFlow.PLN_POSTPAID_CHECKOUT -> {

            }

            PaymentDetailFlow.PLN_PREPAID_CHECKOUT -> {

            }
        }

        transactionConfirmationBottomsheet?.arguments = bundle
        transactionConfirmationBottomsheet?.show(
            supportFragmentManager,
            TransactionConfirmationBottomsheet::class.java.simpleName
        )
    }

    private fun setupMainLayoutDetailPPOB() {
        when (flow) {
            PaymentDetailFlow.PULSA_DATA -> {
                binding.tabLayout.visibility = View.VISIBLE
                binding.vp.visibility = View.VISIBLE

                tabPulsaDataAdapter = PulsaDataTabAdapter(
                    applicationContext,
                    PulsaDataArgument(
                        providerImage = argument.additionalPulsaData?.providerImage,
                        providerName = argument.additionalPulsaData?.providerName ?: "-",
                        phoneNumber = argument.additionalPulsaData?.phoneNumber ?: "-",
                        inquiry = argument.additionalPulsaData?.inquiry!!
                    ), supportFragmentManager, lifecycle
                )

                binding.vp.adapter = tabPulsaDataAdapter
                supportActionBar?.elevation = 0f

                TabLayoutMediator(binding.tabLayout, binding.vp) { tab, position ->
                    tab.customView = tabPulsaDataAdapter.getTabView(position)
                }.attach()
            }

            PaymentDetailFlow.TELKOM_INDIHOME -> {
                binding.llDetailPpobTelkomIndihome.visibility = View.VISIBLE
                binding.llBottomLayout.visibility = View.VISIBLE
            }

            PaymentDetailFlow.PLN_POSTPAID_CHECKOUT -> {

            }

            PaymentDetailFlow.PLN_PREPAID_CHECKOUT -> {

            }
        }
    }

    private fun setupTitle() {
        when (flow) {
            PaymentDetailFlow.PULSA_DATA -> {
                binding.toolbar.title = "Pembelian Pulsa/Data"
            }

            PaymentDetailFlow.TELKOM_INDIHOME -> {
                binding.toolbar.title = "Pembayaran Telkom/IndiHome"
            }

            PaymentDetailFlow.PLN_POSTPAID_CHECKOUT -> {
                binding.toolbar.title = "Pembayaran Tagihan Listrik"
            }

            PaymentDetailFlow.PLN_PREPAID_CHECKOUT -> {
                binding.toolbar.title = "Pembelian Token Listrik"
            }
        }
    }

    private fun setupIdentityPPOB() {
        binding.layoutIdentityPpob.tvLabelIdentity.text = argument.labelIdentity
        binding.layoutIdentityPpob.tvIdentitySubLabel.text = argument.subLabelIdentity
        when (flow) {
            PaymentDetailFlow.PULSA_DATA -> {
                if (argument.additionalPulsaData?.providerImage != null) {
                    Glide.with(binding.layoutIdentityPpob.ivPpobLogo)
                        .load(Uri.parse(argument.additionalPulsaData?.providerImage))
                        .into(binding.layoutIdentityPpob.ivPpobLogo)
                }
            }

            PaymentDetailFlow.TELKOM_INDIHOME -> {
                if (argument.additionalTelkomIndihome?.providerImage != null) {
                    Glide.with(binding.layoutIdentityPpob.ivPpobLogo)
                        .load(Uri.parse(argument.additionalTelkomIndihome?.providerImage))
                        .into(binding.layoutIdentityPpob.ivPpobLogo)
                    binding.layoutIdentityPpob.tvInitialAvatar.visibility = View.GONE
                    binding.layoutIdentityPpob.ivPpobLogo.visibility = View.VISIBLE
                } else {
                    binding.layoutIdentityPpob.tvInitialAvatar.visibility = View.VISIBLE
                    binding.layoutIdentityPpob.ivPpobLogo.visibility = View.GONE

                    binding.layoutIdentityPpob.tvInitialAvatar.text =
                        BebasTransactionHelper.getInitial(argument.labelIdentity)
                }

                binding.layoutDetailPpobTelkomIndihome.tvPeriodeValue.text =
                    argument.additionalTelkomIndihome?.periode
                binding.layoutDetailPpobTelkomIndihome.tvTagihanValue.text =
                    argument.additionalTelkomIndihome?.tagihan?.toRupiahFormat(
                        useSymbol = true,
                        useDecimal = true
                    )
            }

            PaymentDetailFlow.PLN_POSTPAID_CHECKOUT -> {
                Glide.with(binding.layoutIdentityPpob.ivPpobLogo)
                    .load(R.drawable.il_logo_pln)
                    .into(binding.layoutIdentityPpob.ivPpobLogo)
            }

            PaymentDetailFlow.PLN_PREPAID_CHECKOUT -> {
                Glide.with(binding.layoutIdentityPpob.ivPpobLogo)
                    .load(R.drawable.il_logo_pln)
                    .into(binding.layoutIdentityPpob.ivPpobLogo)
            }
        }
    }

    override fun onButtonTransactionConfirmationClicked(result: TransactionConfirmationResult) {
        transactionConfirmationBottomsheet?.dismiss()

        val intent = Intent(this, PinVerificationActivity::class.java)
        when (flow) {
            PaymentDetailFlow.PULSA_DATA -> {

            }

            PaymentDetailFlow.TELKOM_INDIHOME -> {
                val inquiry = argument.additionalTelkomIndihome?.inquiry
                intent.apply {
                    putExtra(
                        PinVerificationActivity.FLOW,
                        PinVerificationFlow.POSTING_TELKOM_INDIHOME.name
                    )
                    putExtra(
                        PinVerificationActivity.ARGUMENT, PinVerificationArgument(
                            additionalTelkomIndihome = PinVerificationArgument.TelkomIndihome(
                                postingRequest = PostingTelkomIndihomeRequest(
                                    additionalDataPrivate = inquiry?.additionalDataPrivate ?: "-",
                                    amountTransaction = (inquiry?.amountTransaction
                                        ?: -1.0) + (inquiry?.transactionFee ?: -1.0),
                                    autodebitStatus = inquiry?.autoDebitStatus ?: false,
                                    customerNumber = inquiry?.customerNumber ?: "-",
                                    fromAccount = result.selectedAccountNumber,
                                    fromAccountName = result.selectedAccountName,
                                    transactionFee = inquiry?.transactionFee ?: -1.0
                                ),
                                inquiryResponse = inquiry!!
                            )
                        )
                    )
                }
            }

            PaymentDetailFlow.PLN_POSTPAID_CHECKOUT -> {

            }

            PaymentDetailFlow.PLN_PREPAID_CHECKOUT -> {

            }
        }
        startActivity(intent)
    }

    override fun onDenomClicked(model: PPOBDenomModel) {
        when (flow) {
            PaymentDetailFlow.PLN_PREPAID_CHECKOUT -> {
                var unSelectedIndex = -1
                var selectedIndex = -1
                val currentSelectedDenom = viewModel.selectedDenomModel.value
                if (currentSelectedDenom != null && !model.isSelected) {
                    for (index in 0 until plnDenoms.size) {
                        if (plnDenoms[index].id == currentSelectedDenom.id && plnDenoms[index].isSelected) {
                            unSelectedIndex = index
                        } else if (plnDenoms[index].id == model.id && !plnDenoms[index].isSelected) {
                            selectedIndex = index
                        }
                    }
                    if (unSelectedIndex != -1 && selectedIndex != -1) {
                        plnDenoms[unSelectedIndex].isSelected = false
                        plnDenoms[selectedIndex].isSelected = true
                        ppobDenomAdapter.selectAndUnselectDenom(
                            unSelectedIndex = unSelectedIndex,
                            selectedIndex = selectedIndex
                        )
                        viewModel.selectDenomModel(plnDenoms[selectedIndex])
                    }
                } else {
                    for (index in 0 until plnDenoms.size) {
                        if (plnDenoms[index].id == model.id) {
                            selectedIndex = index
                        }
                        if (selectedIndex != -1) {
                            plnDenoms[selectedIndex].isSelected = true
                            ppobDenomAdapter.selectDenom(selectedIndex = selectedIndex)
                            viewModel.selectDenomModel(plnDenoms[selectedIndex])
                        }
                    }
                }
            }

            else -> {

            }
        }
    }
}