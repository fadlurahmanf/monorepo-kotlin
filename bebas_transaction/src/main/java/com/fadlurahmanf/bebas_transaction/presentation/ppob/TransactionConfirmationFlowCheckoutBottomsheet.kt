package com.fadlurahmanf.bebas_transaction.presentation.ppob

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransactionConfirmationCheckoutArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.TransactionDetailModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transaction.OrderFeeDetailModel
import com.fadlurahmanf.bebas_transaction.data.dto.result.TransactionConfirmationResult
import com.fadlurahmanf.bebas_transaction.data.flow.TransactionConfirmationCheckoutFlow
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetTransactionConfirmationCheckoutBinding
import com.fadlurahmanf.bebas_transaction.external.TransactionConfirmationCallback
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.TransactionDetailAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class TransactionConfirmationFlowCheckoutBottomsheet :
    BaseTransactionBottomsheet<BottomsheetTransactionConfirmationCheckoutBinding>(
        BottomsheetTransactionConfirmationCheckoutBinding::inflate
    ) {

    companion object {
        const val ADDITIONAL_ARG = "ADDITIONAL_ARG"
        const val FLOW = "FLOW"
    }

    private var callback: TransactionConfirmationCallback? = null
    private val handler = Handler(Looper.getMainLooper())

    fun setCallback(callback: TransactionConfirmationCallback) {
        this.callback = callback
    }

    private lateinit var argument: TransactionConfirmationCheckoutArgument
    private lateinit var flow: TransactionConfirmationCheckoutFlow

    private lateinit var feeDetailAdapter: TransactionDetailAdapter
    private lateinit var detailAdapter: TransactionDetailAdapter
    private val feeDetails: ArrayList<OrderFeeDetailModel.Detail> =
        arrayListOf()
    private val details: ArrayList<TransactionConfirmationCheckoutArgument.Detail> = arrayListOf()

    @Inject
    lateinit var viewModel: TransactionConfirmationFlowCheckoutViewModel

    private lateinit var bottomsheetDialog: BottomSheetDialog

    override fun injectBottomsheet() {
        component.inject(this)
    }

    override fun setup() {
        binding.ivBack.setOnClickListener {
            dialog?.dismiss()
        }

        val stringFlow = arguments?.getString(FLOW) ?: return
        flow = enumValueOf<TransactionConfirmationCheckoutFlow>(stringFlow)

        val arg: TransactionConfirmationCheckoutArgument?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arg =
                arguments?.getParcelable(
                    ADDITIONAL_ARG,
                    TransactionConfirmationCheckoutArgument::class.java
                )
        } else {
            arg = arguments?.getParcelable<TransactionConfirmationCheckoutArgument>(ADDITIONAL_ARG)
        }

        if (arg == null) {
            return
        }

        bottomsheetDialog = (dialog as BottomSheetDialog)

        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

        argument = arg
        details.clear()
        details.addAll(argument.details)

        setupIdentityDestination()
        initFeeDetail()
        initDetail()
        initObserver()


        binding.btnNext.setOnClickListener {
            when(flow){
                TransactionConfirmationCheckoutFlow.PLN_PREPAID -> {
                    callback?.onButtonTransactionConfirmationClicked(TransactionConfirmationResult(
                        selectedAccountName = viewModel.selectedPaymentSource.value?.accountName ?: "-",
                        selectedAccountNumber = viewModel.selectedPaymentSource.value?.accountNumber ?: "-",
                        additionalPLNPrePaidCheckout = TransactionConfirmationResult.PLNPrePaidCheckout(
                            orderId = viewModel.orderDetail?.orderId ?: "-",
                            configGroupId = viewModel.orderDetail?.paymentConfigGroupId ?: "-",
                            paymentTypeCode = viewModel.orderDetail?.paymentTypeCode ?: "-",
                            paymentSourceSchema = viewModel.orderDetail?.schemas ?: listOf()
                        )
                    ))
                }
            }
        }

        binding.itemBebasPoinPaymentSource.switchUseBebaspoin.setOnCheckedChangeListener { _, isChecked ->
            when (flow) {
                TransactionConfirmationCheckoutFlow.PLN_PREPAID -> {
                    viewModel.orderPaymentSchema(
                        productCode = argument.additionalPLNPrePaid?.inquiryResponse?.productCode
                            ?: "-",
                        customerId = argument.additionalPLNPrePaid?.inquiryResponse?.clientNumber
                            ?: "-",
                        customerName = argument.additionalPLNPrePaid?.inquiryResponse?.clientName
                            ?: "-",
                        paymentTypeCode = argument.additionalPLNPrePaid?.paymentTypeCode ?: "-",
                        useLoyaltyBebasPoin = isChecked,
                        amount = argument.additionalPLNPrePaid?.inquiryResponse?.amount ?: -1.0,
                    )
                }
            }
        }

        binding.lItemPaymentSource.setOnClickListener {
            Log.d("BebasLogger", "ITEM PAYMENT SOURCE: ${viewModel.selectedPaymentSource.value}")
            if (viewModel.selectedPaymentSource.value != null) {
                callback?.onChangePaymentSource(viewModel.selectedPaymentSource.value!!)
            }
        }

        when (flow) {
            TransactionConfirmationCheckoutFlow.PLN_PREPAID -> {
                viewModel.getPaymentSourceConfig(
                    paymentTypeCode = argument.additionalPLNPrePaid?.paymentTypeCode
                        ?: "-",
                    productCode = argument.additionalPLNPrePaid?.inquiryResponse?.productCode
                        ?: "-",
                    customerId = argument.additionalPLNPrePaid?.inquiryResponse?.clientNumber
                        ?: "-",
                    customerName = argument.additionalPLNPrePaid?.inquiryResponse?.clientName
                        ?: "-",
                    amount = argument.additionalPLNPrePaid?.inquiryResponse?.amount ?: -1.0
                )
            }
        }
    }

    private fun initFeeDetail() {
        feeDetailAdapter = TransactionDetailAdapter()
        feeDetailAdapter.setList(feeDetails.map { detail ->
            TransactionDetailModel(
                label = detail.label,
                value = detail.value.toRupiahFormat(useSymbol = true, useDecimal = false)
            )
        })
        binding.rvFeeDetail.adapter = feeDetailAdapter
    }

    private fun initDetail() {
        detailAdapter = TransactionDetailAdapter()
        detailAdapter.setList(details.map { detail ->
            TransactionDetailModel(
                label = detail.label,
                value = detail.value
            )
        })
        binding.rvDetails.adapter = detailAdapter
    }

    private fun initObserver() {
        viewModel.paymentSourceState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    binding.lItemPaymentSourceShimmer.visibility = View.VISIBLE
                    binding.lItemPaymentSource.visibility = View.GONE
                }

                is NetworkState.LOADING -> {
                    binding.lItemPaymentSourceShimmer.visibility = View.VISIBLE
                    binding.lItemPaymentSource.visibility = View.GONE
                }

                is NetworkState.SUCCESS -> {
                    binding.lItemPaymentSourceShimmer.visibility = View.GONE
                    binding.lItemPaymentSource.visibility = View.VISIBLE

                    binding.itemBebasPoinPaymentSource.tvTotalBebasPoinUserHave.text =
                        "BebasPoin milikmu: ${
                            it.data.loyaltyPointPaymentSource?.balance?.toRupiahFormat(
                                useSymbol = false,
                                useDecimal = false
                            )
                        }"

                    binding.itemPaymentSource.tvAccountName.text =
                        it.data.mainPaymentSource.accountName
                    binding.itemPaymentSource.tvSavingTypeAndAccountNumber.text =
                        it.data.mainPaymentSource.subLabel
                    binding.itemPaymentSource.tvAccountBalance.text =
                        it.data.mainPaymentSource.balance.toRupiahFormat(
                            useSymbol = true,
                            useDecimal = false
                        )
                }

                else -> {

                }
            }
        }

        viewModel.oderFeeDetailState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    binding.llFeeDetail.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                }

                is NetworkState.IDLE -> {
                    binding.llFeeDetail.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                }

                NetworkState.LOADING -> {
                    binding.llFeeDetail.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                }

                is NetworkState.SUCCESS -> {
                    feeDetails.clear()
                    feeDetails.addAll(it.data.details)
                    feeDetailAdapter.resetList(feeDetails.map { detail ->
                        TransactionDetailModel(
                            label = detail.label,
                            value = detail.value.toRupiahFormat(
                                useSymbol = true,
                                useDecimal = false
                            )
                        )
                    })
                    binding.tvTotal.text =
                        it.data.total.toRupiahFormat(useSymbol = true, useDecimal = false)

                    binding.btnNext.visibility = View.VISIBLE
                    binding.llFeeDetail.visibility = View.VISIBLE

                    binding.nsv.postDelayed({
                                                bottomsheetDialog.behavior.state =
                                                    BottomSheetBehavior.STATE_EXPANDED
                                                binding.nsv.fullScroll(View.FOCUS_DOWN)
                                            }, 1000)
                }
            }
        }
    }

    private fun setupIdentityDestination() {
        when (flow) {
            TransactionConfirmationCheckoutFlow.PLN_PREPAID -> {
                binding.itemDestinationAccount.ivLogo.visibility = View.VISIBLE
                binding.itemDestinationAccount.initialAvatar.visibility = View.GONE
                Glide.with(binding.itemDestinationAccount.ivLogo).load(R.drawable.il_logo_pln)
                    .into(binding.itemDestinationAccount.ivLogo)

                binding.itemDestinationAccount.tvLabel.text = argument.destinationLabel
                binding.itemDestinationAccount.tvSubLabel.text = argument.destinationSubLabel
            }
        }
    }
}