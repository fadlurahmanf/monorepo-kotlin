package com.fadlurahmanf.core_logger.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.core_logger.R
import com.fadlurahmanf.core_logger.data.entity.LoggerEntity
import com.fadlurahmanf.core_logger.domain.datasources.LoggerLocalDatasource
import com.fadlurahmanf.core_logger.external.CustomState

private const val LOG_TYPE = "LOG_TYPE"

class LogHistoryFragment : Fragment() {
    private var logType: String? = null

    private lateinit var viewModel: LogViewModel
    private lateinit var adapter: LogHistoryAdapter
    private lateinit var btn: Button
    private var logs: ArrayList<LoggerEntity> = arrayListOf()

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            logType = it.getString(LOG_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv)

        val datasource = LoggerLocalDatasource(requireContext())
        viewModel = LogViewModel(datasource)

        adapter = LogHistoryAdapter()
        adapter.setLogs(logs)
        recyclerView.adapter = adapter

        viewModel.logs.observe(requireActivity()) {
            Log.d("MappLogger", "STATE: $it")
            when (it) {
                is CustomState.SUCCESS -> {
                    logs.clear()
                    logs.addAll(it.data)
                    adapter.notifyDataSetChanged()
                }

                else -> {}
            }
        }

        viewModel.getAllLogger()
    }

    companion object {
        @JvmStatic
        fun newInstance(logType: String) =
            LogHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(LOG_TYPE, logType)
                }
            }
    }
}