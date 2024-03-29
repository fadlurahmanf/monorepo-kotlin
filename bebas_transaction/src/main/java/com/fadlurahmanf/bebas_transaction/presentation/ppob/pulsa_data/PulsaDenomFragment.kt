package com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingPulsaPrePaidRequest
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PinVerificationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PulsaDataArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.SelectPaymentSourceArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransactionConfirmationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.ppob.PPOBDenomModel
import com.fadlurahmanf.bebas_transaction.data.dto.result.TransactionConfirmationResult
import com.fadlurahmanf.bebas_transaction.data.flow.PinVerificationFlow
import com.fadlurahmanf.bebas_transaction.data.flow.SelectPaymentSourceFlow
import com.fadlurahmanf.bebas_transaction.data.flow.TransactionConfirmationFlow
import com.fadlurahmanf.bebas_transaction.databinding.FragmentPulsaDenomBinding
import com.fadlurahmanf.bebas_transaction.external.TransactionConfirmationCallback
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionFragment
import com.fadlurahmanf.bebas_transaction.presentation.others.SelectPaymentSourceBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.pin.PinVerificationActivity
import com.fadlurahmanf.bebas_transaction.presentation.ppob.TransactionConfirmationBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.ppob.adapter.PPOBDenomAdapter
import javax.inject.Inject


private const val PULSA_DATA_ARGUMENT = "PULSA_DATA_ARGUMENT"

class PulsaDenomFragment :
    BaseTransactionFragment<FragmentPulsaDenomBinding>(FragmentPulsaDenomBinding::inflate),
    PPOBDenomAdapter.Callback, TransactionConfirmationCallback,
    SelectPaymentSourceBottomsheet.Callback {

    @Inject
    lateinit var viewModel: PulsaDataViewModel

    lateinit var argument: PulsaDataArgument
    private lateinit var adapter: PPOBDenomAdapter
    private val denoms: ArrayList<PPOBDenomModel> = arrayListOf()

    override fun injectFragment() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PULSA_DATA_ARGUMENT, PulsaDataArgument::class.java)
        } else {
            arguments?.getParcelable(PULSA_DATA_ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedBackBottomsheet(BebasException.generalRC("ARGUMENT_MISSING"))
            return
        }

        argument = p0Arg

    }

    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!::argument.isInitialized) {
            showForcedBackBottomsheet(BebasException.generalRC("ARGUMENT_MISSING"))
            return
        }

        val gm = GridLayoutManager(requireContext(), 2)
        adapter = PPOBDenomAdapter()
        adapter.setCallback(this)
        adapter.setList(denoms)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = gm

        viewModel.pulsaDenomState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    if (denoms.isNotEmpty()) {
                        binding.lLayoutPpobDenomShimmer.visibility = View.GONE
                        binding.rv.visibility = View.VISIBLE
                    } else {
                        binding.lLayoutPpobDenomShimmer.visibility = View.GONE
                        binding.rv.visibility = View.GONE
                    }
                }

                is NetworkState.LOADING -> {
                    if (denoms.isNotEmpty()) {
                        binding.lLayoutPpobDenomShimmer.visibility = View.GONE
                        binding.rv.visibility = View.VISIBLE
                    } else {
                        binding.lLayoutPpobDenomShimmer.visibility = View.VISIBLE
                        binding.rv.visibility = View.GONE
                    }
                }

                is NetworkState.SUCCESS -> {
                    denoms.clear()
                    denoms.addAll(it.data)
                    adapter.setList(denoms)

                    if (denoms.isNotEmpty()) {
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


        viewModel.getPulsaDenom(argument.providerName, argument.providerImage)
    }

    companion object {
        @JvmStatic
        fun newInstance(arg: PulsaDataArgument) =
            PulsaDenomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PULSA_DATA_ARGUMENT, arg)
                }
            }
    }

    private lateinit var denomClicked: PPOBDenomModel
    override fun onDenomClicked(model: PPOBDenomModel) {
        denomClicked = model
        showTransactionConfirmationBottomsheet()
    }

    private var transactionConfirmationBottomsheet: TransactionConfirmationBottomsheet? = null
    private fun showTransactionConfirmationBottomsheet(defaultPaymentSource: PaymentSourceModel? = null) {
        transactionConfirmationBottomsheet?.dismiss()
        transactionConfirmationBottomsheet = null
        transactionConfirmationBottomsheet = TransactionConfirmationBottomsheet()
        transactionConfirmationBottomsheet?.setCallback(this)
        transactionConfirmationBottomsheet?.arguments = Bundle().apply {
            putString(
                TransactionConfirmationBottomsheet.FLOW,
                TransactionConfirmationFlow.PULSA.name
            )
            putParcelable(
                TransactionConfirmationBottomsheet.ADDITIONAL_ARG, TransactionConfirmationArgument(
                    defaultPaymentSource = defaultPaymentSource,
                    destinationLabel = argument.providerName,
                    destinationSubLabel = argument.phoneNumber,
                    imageLogoUrl = argument.providerImage,
                    feeDetail = TransactionConfirmationArgument.FeeDetail(
                        total = denomClicked.totalBayar,
                        details = arrayListOf(
                            TransactionConfirmationArgument.FeeDetail.Detail(
                                label = "Harga",
                                value = denomClicked.pulsaDenomResponse?.value ?: -1.0
                            ),
                            TransactionConfirmationArgument.FeeDetail.Detail(
                                label = "Biaya Admin",
                                value = denomClicked.pulsaDenomResponse?.adminFee ?: -1.0
                            ),
                        )
                    ),
                    details = arrayListOf(
                        TransactionConfirmationArgument.Detail(
                            label = "Nomor Ponsel",
                            value = argument.phoneNumber
                        ),
                        TransactionConfirmationArgument.Detail(
                            label = "Provider",
                            value = argument.providerName
                        )
                    )
                )
            )
        }
        transactionConfirmationBottomsheet?.show(
            requireActivity().supportFragmentManager,
            TransactionConfirmationBottomsheet::class.java.simpleName
        )
    }

    private var selectPaymentSouceBottomsheet: SelectPaymentSourceBottomsheet? = null

    override fun onChangePaymentSource(selectedPaymentSource: PaymentSourceModel) {
        transactionConfirmationBottomsheet?.dismiss()
        transactionConfirmationBottomsheet = null

        selectPaymentSouceBottomsheet = SelectPaymentSourceBottomsheet()
        selectPaymentSouceBottomsheet?.setCallback(this)
        selectPaymentSouceBottomsheet?.arguments = Bundle().apply {
            putParcelable(
                SelectPaymentSourceBottomsheet.ARGUMENT, SelectPaymentSourceArgument(
                    flow = SelectPaymentSourceFlow.CIF_GET_BANK_ACCOUNTS
                )
            )
        }
        selectPaymentSouceBottomsheet?.show(
            requireActivity().supportFragmentManager,
            SelectPaymentSourceBottomsheet::class.java.simpleName
        )
    }

    override fun onButtonTransactionConfirmationClicked(result: TransactionConfirmationResult) {
        transactionConfirmationBottomsheet?.dismiss()

        val intent = Intent(requireContext(), PinVerificationActivity::class.java)
        intent.apply {
            putExtra(
                PinVerificationActivity.FLOW,
                PinVerificationFlow.POSTING_PULSA_PREPAID.name
            )
            putExtra(
                PinVerificationActivity.ARGUMENT, PinVerificationArgument(
                    additionalPulsaData = PinVerificationArgument.PulsaData(
                        postingRequest = PostingPulsaPrePaidRequest(
                            accountName = result.selectedAccountName,
                            accountNumber = result.selectedAccountNumber,
                            amount = denomClicked.pulsaDenomResponse?.value ?: -1.0,
                            billerCode = denomClicked.pulsaDenomResponse?.billerCode ?: "-",
                            ip = "0.0.0.0",
                            latitude = 0.0,
                            longitude = 0.0,
                            phoneNumber = argument.phoneNumber,
                            productCode = denomClicked.pulsaDenomResponse?.productCode ?: "-",
                            providerName = argument.providerName,
                            transactionFee = denomClicked.pulsaDenomResponse?.adminFee ?: -1.0
                        ),
                        inquiryResponse = argument.inquiry,
                        pulsaDenomClicked = denomClicked.pulsaDenomResponse!!
                    )
                )
            )
        }
        startActivity(intent)
    }

    override fun onSelectPaymentSource(paymentSource: PaymentSourceModel) {
        selectPaymentSouceBottomsheet?.dismiss()
        selectPaymentSouceBottomsheet = null

        showTransactionConfirmationBottomsheet(defaultPaymentSource = paymentSource)
    }
}