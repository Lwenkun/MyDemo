package me.liwenkun.demo.demoframework

import android.content.Context
import androidx.fragment.app.Fragment
import me.liwenkun.demo.demoframework.Logger.Companion.from

open class DemoBaseFragment : Fragment(), Logger {
    private lateinit var logger: Logger
    override fun onAttach(context: Context) {
        super.onAttach(context)
        logger = from(context)
    }

    override fun deleteAllLogs() {
        logger.deleteAllLogs()
    }

    val demoFragmentActivity: DemoFragmentActivity
        get() = requireActivity() as DemoFragmentActivity

    override fun log(tag: String?, message: String?, color: Int, promptChar: String?) {
        logger.log(tag, message, color, promptChar)
    }
}