package com.fadlurahmanf.mapp_example.presentation.logger

import android.content.Intent
import com.fadlurahmanf.core_logger.presentation.LogConsole
import com.fadlurahmanf.mapp_config.presentation.MappApplication
import com.fadlurahmanf.mapp_example.databinding.ActivityLoggerBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import javax.inject.Inject
import kotlin.random.Random

class LoggerActivity : BaseExampleActivity<ActivityLoggerBinding>(ActivityLoggerBinding::inflate) {

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        binding.btnLogHistory.onClicked {
            val intent = Intent(
                this,
                Class.forName("com.fadlurahmanf.core_logger.presentation.LogHistoryActivity")
            )
            startActivity(intent)
        }

        binding.btnLogDebug.onClicked {
            logConsole().d("MappLogger", "RANDOM DEBUG (${Random.nextInt()})")
        }

        binding.btnLogError.onClicked {
            logConsole().e("MappLogger", "RANDOM ERROR (${Random.nextInt()})")
        }

        binding.btnLogInfo.onClicked {
            logConsole().i("MappLogger", "RANDOM INFO (${Random.nextInt()})")
        }

    }

}