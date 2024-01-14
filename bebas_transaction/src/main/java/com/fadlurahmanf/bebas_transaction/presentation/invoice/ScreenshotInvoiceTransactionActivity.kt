package com.fadlurahmanf.bebas_transaction.presentation.invoice

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.facebook.shimmer.BuildConfig
import com.fadlurahmanf.bebas_shared.data.argument.transaction.InvoiceTransactionArgumentConstant
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.flow.transaction.InvoiceTransactionFlow
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.InvoiceTransactionArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.TransactionDetailModel
import com.fadlurahmanf.bebas_transaction.databinding.ActivityInvoiceTransactionBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.TransactionDetailAdapter
import java.io.File
import java.io.FileOutputStream


class ScreenshotInvoiceTransactionActivity :
    BaseTransactionActivity<ActivityInvoiceTransactionBinding>(
        ActivityInvoiceTransactionBinding::inflate
    ) {

    private lateinit var argument: InvoiceTransactionArgument
    private lateinit var flow: InvoiceTransactionFlow

    private val details: ArrayList<TransactionDetailModel> = arrayListOf()
    lateinit var detailAdapter: TransactionDetailAdapter
    private val feeDetails: ArrayList<TransactionDetailModel> = arrayListOf()
    lateinit var feeDetailAdapter: TransactionDetailAdapter

    private lateinit var directoryDownloaded: File
    private lateinit var directoryDownloadedPath: String

    private val handler = Handler(Looper.getMainLooper())

    override fun injectActivity() {

    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        binding.llBottomLayout.visibility = View.GONE

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

        when (VERSION.SDK_INT) {
            in 21..28 -> {
                directoryDownloaded =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                directoryDownloadedPath = directoryDownloaded.absolutePath
            }

            else -> {
                directoryDownloaded =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                directoryDownloadedPath = directoryDownloaded.absolutePath
            }
        }

        argument = p0Arg

        setupTransactionStatus()
        setupTotalTransaction()
        setupDetailTransaction()

        handler.postDelayed({
                                takeScreenshot()
                            }, 1000)
    }

    private fun setupTransactionStatus() {
        binding.llStatus.visibility = View.GONE
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

    private fun takeScreenshot() {
        try {
            val v1 = window.decorView.rootView
            v1.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(v1.drawingCache)
            v1.isDrawingCacheEnabled = false

            val downloadedFile =
                File(directoryDownloadedPath + "/" + "BEBAS_INVOICE_${System.currentTimeMillis()}.png")

            val outputStream: FileOutputStream = FileOutputStream(downloadedFile)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

//            openScreenshot(downloadedFile)
            Log.d("BebasLogger", "SUCCESS SAVE SCREENSHOT")
            sharedFile(downloadedFile)
        } catch (e: Throwable) {
            showForcedBackBottomsheet(BebasException.generalRC("TS_00"))
        }
    }

    private fun openScreenshot(imageFile: File) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
        }
        val uri = FileProvider.getUriForFile(applicationContext, "$packageName.provider", imageFile)
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
        Log.d("BebasLogger", "SUCCESS OPEN SCREENSHOT")
    }

    private fun sharedFile(imageFile: File) {
        try {
            if (imageFile.exists()) {
                val uri = FileProvider.getUriForFile(applicationContext, "$packageName.provider", imageFile)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    setDataAndType(uri, "image/*")
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                Log.d("BebasLogger", "SUCCESS OPEN SHARED FILE")
                startActivity(intent)
            }
        } catch (e: Throwable) {
            showFailedBebasBottomsheet(BebasException.fromThrowable(e))
        }
    }
}