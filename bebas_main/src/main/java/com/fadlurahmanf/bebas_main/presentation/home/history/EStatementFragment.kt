package com.fadlurahmanf.bebas_main.presentation.home.history

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.fadlurahmanf.bebas_api.data.dto.cif.EStatementResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_main.databinding.FragmentEStatementBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainFragment
import com.fadlurahmanf.bebas_main.presentation.home.history.adapter.EStatementAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EStatementFragment :
    BaseMainFragment<FragmentEStatementBinding>(FragmentEStatementBinding::inflate),
    EStatementAdapter.Callback {
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModel: EStatementViewModel

    private var accountNumber: String? = null

    override fun injectFragment() {
        component.inject(this)
    }

    private lateinit var adapter: EStatementAdapter
    private val estatements: ArrayList<EStatementResponse.Statement> = arrayListOf()
    override fun onBebasCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        adapter = EStatementAdapter()
        adapter.setCallback(this)
        viewModel.estatementState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {

                }

                is NetworkState.LOADING -> {

                }

                is NetworkState.SUCCESS -> {
                    accountNumber = it.data.account?.accountNumber
                    estatements.clear()
                    estatements.addAll(it.data.statements ?: arrayListOf())
                    adapter.setNewList(estatements)
                }

                else -> {

                }
            }
        }

        viewModel.downloadState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBottomsheet(it.exception)
                }

                is NetworkState.LOADING -> {
                    showLoadingDialog()
                }

                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    saveToFile(it.data)
                }

                else -> {

                }
            }
        }
    }

    private fun saveToFile(stream: InputStream) {
        val directoryDownloaded: File
        val directoryDownloadedPath: String
        when (Build.VERSION.SDK_INT) {
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

        val downloadedFile =
            File(directoryDownloadedPath + "/" + "BEBAS_ESTATEMENT_${System.currentTimeMillis()}.pdf")
        stream.use { inputStream ->
            downloadedFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        openPDF(downloadedFile)
    }

    private fun openPDF(pdfFile: File) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
        }
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            pdfFile
        )
        intent.setDataAndType(uri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
        Log.d("BebasLogger", "SUCCESS OPEN PDF")
    }


    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rv.adapter = adapter
        viewModel.getEStatements()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EStatementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDownloadEstatementClicked(estatement: EStatementResponse.Statement) {
        Thread {
            viewModel.downloadEStatement(
                context = requireContext(),
                accountNumber = accountNumber ?: "-",
                year = estatement.year ?: -1,
                month = estatement.month ?: -1,
            )
        }.start()
    }
}