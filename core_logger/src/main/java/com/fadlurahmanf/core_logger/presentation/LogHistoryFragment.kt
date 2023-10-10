package com.fadlurahmanf.core_logger.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fadlurahmanf.core_logger.R

private const val LOG_TYPE = "LOG_TYPE"

class LogHistoryFragment : Fragment() {
    private var logType: String? = null

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