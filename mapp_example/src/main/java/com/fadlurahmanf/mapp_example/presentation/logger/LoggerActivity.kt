package com.fadlurahmanf.mapp_example.presentation.logger

import android.content.Intent
import com.chuckerteam.chucker.api.Chucker
import com.fadlurahmanf.mapp_example.databinding.ActivityLoggerBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_shared.extension.formatDate5
import java.util.Calendar
import kotlin.random.Random

class LoggerActivity : BaseExampleActivity<ActivityLoggerBinding>(ActivityLoggerBinding::inflate) {

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        binding.btnChucker.onClicked {
            val intent = Chucker.getLaunchIntent(applicationContext)
            startActivity(intent)
        }

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

        binding.btnLogBetterstack.onClicked {
            logConsole().logRemotely("LoggerActivity LogBetterStack Log Remotely ${Calendar.getInstance().time.formatDate5()}")
        }

    }

}