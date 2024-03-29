package com.fadlurahmanf.bebas_transaction.presentation.invoice

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.argument.transaction.InvoiceTransactionArgumentConstant
import com.fadlurahmanf.bebas_shared.data.dto.NotificationRefreshPulsaTransactionModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.extension.formatInvoiceTransaction
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_shared.extension.utcToLocal
import com.fadlurahmanf.bebas_shared.receiver.FcmBroadcastReceiver
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.InvoiceTransactionArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.TransactionDetailModel
import com.fadlurahmanf.bebas_shared.data.flow.transaction.InvoiceTransactionFlow
import com.fadlurahmanf.bebas_transaction.databinding.ActivityInvoiceTransactionBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.TransactionDetailAdapter
import javax.inject.Inject

class InvoiceTransactionActivity :
    BaseTransactionActivity<ActivityInvoiceTransactionBinding>(ActivityInvoiceTransactionBinding::inflate) {

    private lateinit var refreshFlow: InvoiceTransactionFlow
    private var refreshPulsaArgument: NotificationRefreshPulsaTransactionModel? = null
    private val fcmListener = object : FcmBroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val stringFlow =
                intent?.getStringExtra(InvoiceTransactionArgumentConstant.REFRESH_FLOW) ?: return

            refreshFlow = enumValueOf(stringFlow)

            when (refreshFlow) {
                InvoiceTransactionFlow.FUND_TRANSFER_BANK_MAS -> {

                }

                InvoiceTransactionFlow.PULSA_PREPAID -> {
                    refreshPulsaArgument =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.extras?.getParcelable(
                                InvoiceTransactionArgumentConstant.REFRESH_ARGUMENT,
                                NotificationRefreshPulsaTransactionModel::class.java
                            )
                        } else {
                            intent.extras?.getParcelable(InvoiceTransactionArgumentConstant.REFRESH_ARGUMENT)
                        }

                    if (refreshPulsaArgument == null) {
                        return
                    }

                    viewModel.refreshStatusTransaction(argument.transactionId)
                }

                InvoiceTransactionFlow.TELKOM_INDIHOME -> {

                }

                InvoiceTransactionFlow.PLN_PREPAID_CHECKOUT -> {

                }
            }
        }
    }

    @Inject
    lateinit var viewModel: InvoiceTransactionViewModel

    lateinit var flow: InvoiceTransactionFlow
    lateinit var argument: InvoiceTransactionArgument

    private val details: ArrayList<TransactionDetailModel> = arrayListOf()
    lateinit var detailAdapter: TransactionDetailAdapter
    private val feeDetails: ArrayList<TransactionDetailModel> = arrayListOf()
    lateinit var feeDetailAdapter: TransactionDetailAdapter

    private val handler = Handler(Looper.getMainLooper())

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val p0Flow = intent.getStringExtra(InvoiceTransactionArgumentConstant.FLOW)

        if (p0Flow == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        flow = enumValueOf(p0Flow)

        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                InvoiceTransactionArgumentConstant.ARGUMENT,
                InvoiceTransactionArgument::class.java
            )
        } else {
            intent.getParcelableExtra(InvoiceTransactionArgumentConstant.ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_ARG"))
            return
        }

        argument = p0Arg

        initReceiver()

        binding.tvTransactionId.text = "ID Transaksi: ${argument.transactionId}"
        binding.tvTransactionDate.text =
            argument.transactionDate.utcToLocal()?.formatInvoiceTransaction() ?: "-"

        setupTransactionStatus()
        setupTotalTransaction()
        setupDetailTransaction()

        binding.layoutInvoiceDetail.llDetailShowCollapsedOrExpanded.setOnClickListener {
            showCollapsedOrExpanded()
        }

        binding.btnRefreshTransaction.setOnClickListener {
            viewModel.refreshStatusTransaction(argument.transactionId)
        }

        binding.btnShared.setOnClickListener {
            val intent = Intent(this, ScreenshotInvoiceTransactionActivity::class.java)
            intent.apply {
                putExtra(InvoiceTransactionArgumentConstant.FLOW, flow.name)
                putExtra(InvoiceTransactionArgumentConstant.ARGUMENT, argument)
            }
            startActivity(intent)
        }

        binding.btnFinished.setOnClickListener {
            val intent = Intent(
                this,
                Class.forName("com.fadlurahmanf.bebas_main.presentation.home.HomeActivity")
            )
            startActivity(intent)
        }

        viewModel.refreshState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                }

                is NetworkState.LOADING -> {
                    showLoadingDialog()
                }

                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    if (it.data.transactionStatus != "PENDING") {
                        argument.statusTransaction = it.data.transactionStatus ?: ""
                        setupTransactionStatus()
                    }
                }

                else -> {

                }
            }
        }
    }

    private fun initReceiver() {
        registerReceiver(
            fcmListener,
            IntentFilter("com.fadlurahmanf.bebas_fcm.ACTION_FCM_LISTENER")
        )
        Log.d("BebasLogger", "REGISTER RECEIVER IN ACTIVITY")
    }

    override fun onDestroy() {
        unregisterReceiver(fcmListener)
        super.onDestroy()
    }

    private fun setupTransactionStatus() {
        binding.llStatus.visibility = View.VISIBLE
        binding.llStatus.animate()
            .translationY(0f)
        binding.llBottomLayout.visibility = View.GONE
        binding.btnRefreshTransaction.visibility = View.GONE

        when (argument.statusTransaction) {
            "FAILED" -> {
                binding.btnShared.visibility = View.GONE
                binding.lottieStatus.setAnimation(R.raw.il_failed_transaction_invoice)
                binding.tvTransactionStatus.text = "Transaksi Gagal"
                Glide.with(binding.ivStatusTransaction)
                    .load(ContextCompat.getDrawable(this, R.drawable.il_transaction_status_failed))
                    .into(binding.ivStatusTransaction)
            }

            "SUCCESS" -> {
                binding.btnShared.visibility = View.VISIBLE
                binding.lottieStatus.setAnimation(R.raw.il_success_transaction_invoice)
                binding.tvTransactionStatus.text = "Transaksi Berhasil"
                Glide.with(binding.ivStatusTransaction)
                    .load(ContextCompat.getDrawable(this, R.drawable.il_transaction_status_success))
                    .into(binding.ivStatusTransaction)
            }

            else -> {
                binding.btnShared.visibility = View.GONE
                binding.lottieStatus.setAnimation(R.raw.il_pending_transaction_invoice)
                binding.tvTransactionStatus.text = "Transaksi Sedang Diproses"
                Glide.with(binding.ivStatusTransaction)
                    .load(ContextCompat.getDrawable(this, R.drawable.il_transaction_status_pendig))
                    .into(binding.ivStatusTransaction)
            }
        }

        binding.lottieStatus.playAnimation()

        handler.postDelayed({
                                binding.llStatus.animate()
                                    .translationY(binding.llStatus.height.toFloat())
                                binding.llStatus.visibility = View.GONE

                            }, 3000)

        handler.postDelayed({
                                binding.llBottomLayout.visibility =
                                    View.VISIBLE

                                if (argument.statusTransaction != "SUCCESS" && argument.statusTransaction != "FAILED") {
                                    binding.btnRefreshTransaction.visibility =
                                        View.VISIBLE
                                }
                            }, 4000)
    }


    private fun setupTotalTransaction() {
        when (flow) {
            InvoiceTransactionFlow.FUND_TRANSFER_BANK_MAS -> {
                binding.layoutInvoiceTotalTransaction.tvDestinationAccountName.text =
                    argument.additionalTransfer?.inquiryResponse?.destinationAccountName ?: "-"
                binding.layoutInvoiceTotalTransaction.tvSubLabel.text =
                    "${argument.additionalTransfer?.destinationBankNickName ?: "-"} • ${argument.additionalTransfer?.destinationAccountNumber ?: "-"}"
                binding.layoutInvoiceTotalTransaction.tvTotalPaymentValue.text =
                    argument.additionalTransfer?.nominal?.toDouble()?.toRupiahFormat(
                        useSymbol = true,
                        useDecimal = false
                    ) ?: "-"
            }

            InvoiceTransactionFlow.PULSA_PREPAID -> {
                Glide.with(binding.layoutInvoiceTotalTransaction.ivDestinationLogo)
                    .load(
                        Uri.parse(
                            argument.additionalPulsaData?.inquiryResponse?.imageProvider ?: ""
                        )
                    )
                    .into(binding.layoutInvoiceTotalTransaction.ivDestinationLogo)
                binding.layoutInvoiceTotalTransaction.tvDestinationAccountName.text =
                    argument.additionalPulsaData?.inquiryResponse?.providerName ?: "-"
                binding.layoutInvoiceTotalTransaction.tvSubLabel.text =
                    argument.additionalPulsaData?.inquiryResponse?.phoneNumber ?: "-"
                binding.layoutInvoiceTotalTransaction.tvTotalPaymentValue.text =
                    argument.additionalPulsaData?.totalTransaction?.toRupiahFormat(
                        useSymbol = true,
                        useDecimal = true
                    ) ?: "-"
            }

            InvoiceTransactionFlow.TELKOM_INDIHOME -> {
                Glide.with(binding.layoutInvoiceTotalTransaction.ivDestinationLogo)
                    .load(R.drawable.il_telkom_logo)
                    .into(binding.layoutInvoiceTotalTransaction.ivDestinationLogo)
                binding.layoutInvoiceTotalTransaction.tvDestinationAccountName.text =
                    argument.additionalTelkomIndihome?.destinationAccountName ?: "-"
                binding.layoutInvoiceTotalTransaction.tvSubLabel.text =
                    "Telkom • ${argument.additionalTelkomIndihome?.destinationAccountNumber ?: "-"}"
                binding.layoutInvoiceTotalTransaction.tvTotalPaymentValue.text =
                    argument.additionalTelkomIndihome?.totalTransaction?.toRupiahFormat(
                        useSymbol = true,
                        useDecimal = true
                    )
            }

            InvoiceTransactionFlow.PLN_PREPAID_CHECKOUT -> {

            }
        }
    }

    private fun setupDetailTransaction() {
        details.clear()
        feeDetails.clear()
        when (flow) {
            InvoiceTransactionFlow.FUND_TRANSFER_BANK_MAS -> {
                details.addAll(
                    listOf(
                        TransactionDetailModel(
                            label = "Jenis Transaksi",
                            value = "Transfer Antar Rekening"
                        ),
                        TransactionDetailModel(
                            label = "Rekening Sumber",
                            value = argument.additionalTransfer?.fromAccountNumber ?: "-"
                        ),
                    )
                )

                feeDetails.addAll(
                    listOf(
                        TransactionDetailModel(
                            label = "Total",
                            value = argument.additionalTransfer?.nominal?.toDouble()
                                ?.toRupiahFormat(
                                    useSymbol = true,
                                    useDecimal = false
                                ) ?: "-",
                            valueStyle = R.style.Font_DetailValueBold
                        )
                    )
                )
            }

            InvoiceTransactionFlow.PULSA_PREPAID -> {
                details.addAll(
                    listOf(
                        TransactionDetailModel(
                            label = "Jenis Transaksi",
                            value = "Pembelian Pulsa ${argument.additionalPulsaData?.inquiryResponse?.providerName ?: "-"}"
                        ),
                        TransactionDetailModel(
                            label = "Rekening Sumber",
                            value = argument.additionalPulsaData?.fromAccount ?: "-"
                        ),
                        TransactionDetailModel(
                            label = "Serial Number",
                            value = argument.additionalPulsaData?.postingResponse?.serialNumber
                                ?: "-"
                        ),
                    )
                )

                feeDetails.addAll(
                    listOf(
                        TransactionDetailModel(
                            label = "Tagihan",
                            value = argument.additionalPulsaData?.pulsaDenomClicked?.nominal?.toRupiahFormat(
                                useSymbol = true,
                                useDecimal = false
                            ) ?: "-"
                        ),
                        TransactionDetailModel(
                            label = "Biaya Admin",
                            value = argument.additionalPulsaData?.pulsaDenomClicked?.adminFee?.toRupiahFormat(
                                useSymbol = true,
                                useDecimal = false,
                                freeIfZero = true
                            ) ?: "-"
                        ),
                        TransactionDetailModel(
                            label = "Total",
                            value = argument.additionalPulsaData?.totalTransaction?.toRupiahFormat(
                                useSymbol = true,
                                useDecimal = true
                            ) ?: "-",
                            valueStyle = R.style.Font_DetailValueBold
                        )
                    )
                )
            }

            InvoiceTransactionFlow.TELKOM_INDIHOME -> {
                details.addAll(
                    listOf(
                        TransactionDetailModel(
                            label = "Jenis Transaksi",
                            value = "Pembayaran Telkom/IndiHome"
                        ),
                        TransactionDetailModel(
                            label = "Rekening Sumber",
                            value = argument.additionalTelkomIndihome?.fromAccount ?: "-"
                        ),
                        TransactionDetailModel(
                            label = "Periode",
                            value = argument.additionalTelkomIndihome?.inquiryResponse?.periode
                                ?: "-"
                        )
                    )
                )

                feeDetails.addAll(
                    listOf(
                        TransactionDetailModel(
                            label = "Tagihan",
                            value = argument.additionalTelkomIndihome?.inquiryResponse?.amountTransaction?.toRupiahFormat(
                                useSymbol = true,
                                useDecimal = false
                            ) ?: "-"
                        ),
                        TransactionDetailModel(
                            label = "Biaya Admin",
                            value = argument.additionalTelkomIndihome?.inquiryResponse?.transactionFee?.toRupiahFormat(
                                useSymbol = true,
                                useDecimal = false,
                                freeIfZero = true
                            ) ?: "-"
                        ),
                        TransactionDetailModel(
                            label = "Total",
                            value = argument.additionalTelkomIndihome?.totalTransaction?.toRupiahFormat(
                                useSymbol = true,
                                useDecimal = false
                            ) ?: "-",
                            valueStyle = R.style.Font_DetailValueBold
                        )
                    )
                )
            }

            InvoiceTransactionFlow.PLN_PREPAID_CHECKOUT -> {

            }
        }
        detailAdapter = TransactionDetailAdapter()
        detailAdapter.setList(details)
        binding.layoutInvoiceDetail.rvDetail.adapter = detailAdapter
        feeDetailAdapter = TransactionDetailAdapter()
        feeDetailAdapter.setList(feeDetails)

        binding.layoutInvoiceDetail.rvFeeDetail.adapter = feeDetailAdapter
    }

    private var isExpanded: Boolean = true
    private fun showCollapsedOrExpanded() {
        if (isExpanded) {
            binding.layoutInvoiceDetail.ivExpandedOrCollapsed.rotation = 180f
            binding.layoutInvoiceDetail.rvDetail.visibility = View.GONE
            binding.layoutInvoiceDetail.rvFeeDetail.visibility = View.GONE
            isExpanded = false
        } else {
            binding.layoutInvoiceDetail.ivExpandedOrCollapsed.rotation = 0f
            binding.layoutInvoiceDetail.rvDetail.visibility = View.VISIBLE
            binding.layoutInvoiceDetail.rvFeeDetail.visibility = View.VISIBLE
            isExpanded = true
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }


}