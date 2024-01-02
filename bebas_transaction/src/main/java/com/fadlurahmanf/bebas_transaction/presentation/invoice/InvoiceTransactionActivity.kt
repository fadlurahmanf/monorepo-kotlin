package com.fadlurahmanf.bebas_transaction.presentation.invoice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.extension.formatInvoiceTransaction
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_shared.extension.utcToLocal
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.InvoiceTransactionArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.TransactionDetailModel
import com.fadlurahmanf.bebas_transaction.data.flow.InvoiceTransactionFlow
import com.fadlurahmanf.bebas_transaction.databinding.ActivityInvoiceTransactionBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.TransactionDetailAdapter

class InvoiceTransactionActivity :
    BaseTransactionActivity<ActivityInvoiceTransactionBinding>(ActivityInvoiceTransactionBinding::inflate) {
    companion object {
        const val FLOW = "FLOW"
        const val ARGUMENT = "ARGUMENT"
    }

    lateinit var flow: InvoiceTransactionFlow
    lateinit var argument: InvoiceTransactionArgument

    private val details: ArrayList<TransactionDetailModel> = arrayListOf()
    lateinit var detailAdapter: TransactionDetailAdapter
    private val feeDetails: ArrayList<TransactionDetailModel> = arrayListOf()
    lateinit var feeDetailAdapter: TransactionDetailAdapter

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val p0Flow = intent.getStringExtra(FLOW)

        if (p0Flow == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        flow = enumValueOf(p0Flow)

        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARGUMENT, InvoiceTransactionArgument::class.java)
        } else {
            intent.getParcelableExtra(ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_ARG"))
            return
        }

        argument = p0Arg

        binding.tvTransactionId.text = "ID Transaksi: ${argument.transactionId}"
        binding.tvTransactionDate.text =
            argument.transactionDate.utcToLocal()?.formatInvoiceTransaction() ?: "-"

        setupTotalTransaction()
        setupDetailTransaction()

        when (argument.statusTransaction) {
            "FAILED" -> {
                binding.lottieStatus.setAnimation(R.raw.il_failed_transaction_invoice)
                binding.tvTransactionStatus.text = "Transaksi Gagal"
            }

            "SUCCESS" -> {
                binding.lottieStatus.setAnimation(R.raw.il_success_transaction_invoice)
                binding.tvTransactionStatus.text = "Transaksi Berhasil"
            }

            else -> {
                binding.lottieStatus.setAnimation(R.raw.il_pending_transaction_invoice)
                binding.tvTransactionStatus.text = "Transaksi Sedang Diproses"
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
                                                        binding.llStatus.animate()
                                                            .translationY(binding.llStatus.height.toFloat())
                                                    }, 3000)

        binding.layoutInvoiceDetail.llDetailShowCollapsedOrExpanded.setOnClickListener {
            showCollapsedOrExpanded()
        }

        binding.btnShared.setOnClickListener {

        }

        binding.btnFinished.setOnClickListener {
            val intent = Intent(
                this,
                Class.forName("com.fadlurahmanf.bebas_main.presentation.home.HomeActivity")
            )
            startActivity(intent)
        }
    }


    private fun setupTotalTransaction() {
        when (flow) {
            InvoiceTransactionFlow.FUND_TRANSFER_BANK_MAS -> {

            }

            InvoiceTransactionFlow.PULSA_PREPAID -> {

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
        }
    }

    private fun setupDetailTransaction() {
        details.clear()
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
                    value = argument.additionalTelkomIndihome?.inquiryResponse?.periode ?: "-"
                )
            )
        )
        detailAdapter = TransactionDetailAdapter()
        detailAdapter.setList(details)
        binding.layoutInvoiceDetail.rvDetail.adapter = detailAdapter

        feeDetails.clear()
        feeDetails.addAll(
            listOf(
                TransactionDetailModel(
                    label = "Tagihan",
                    value = argument.additionalTelkomIndihome?.inquiryResponse?.amountTransaction?.toRupiahFormat(
                        useSymbol = true,
                        useDecimal = true
                    ) ?: "-"
                ),
                TransactionDetailModel(
                    label = "Biaya Admin",
                    value = argument.additionalTelkomIndihome?.inquiryResponse?.transactionFee?.toRupiahFormat(
                        useSymbol = true,
                        useDecimal = true,
                        freeIfZero = true
                    ) ?: "-"
                ),
                TransactionDetailModel(
                    label = "Total",
                    value = ((argument.additionalTelkomIndihome?.inquiryResponse?.amountTransaction
                        ?: -1.0) + (argument.additionalTelkomIndihome?.inquiryResponse?.transactionFee
                        ?: -1.0)).toRupiahFormat(
                        useSymbol = true,
                        useDecimal = true
                    ),
                    valueStyle = R.style.Font_DetailValueBold
                )
            )
        )
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