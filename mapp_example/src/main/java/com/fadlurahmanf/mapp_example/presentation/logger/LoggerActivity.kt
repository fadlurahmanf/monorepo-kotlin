package com.fadlurahmanf.mapp_example.presentation.logger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.core_logger.domain.repositories.LoggerRepositoryImpl
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_example.databinding.ActivityLoggerBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import javax.inject.Inject

class LoggerActivity : BaseExampleActivity<ActivityLoggerBinding>(ActivityLoggerBinding::inflate) {

    @Inject
    lateinit var logger: LoggerRepositoryImpl

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        binding.btnLogDebug.onClicked {
            logger.d("EXAMPLE LOG DEBUG")
        }
    }

}