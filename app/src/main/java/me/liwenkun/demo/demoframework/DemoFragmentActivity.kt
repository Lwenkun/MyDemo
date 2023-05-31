package me.liwenkun.demo.demoframework

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import me.liwenkun.demo.R
import me.liwenkun.demo.demoframework.DemoBook.DemoItem

class DemoFragmentActivity : DemoBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        val fragmentName = intent.getStringExtra(EXTRA_FRAGMENT_NAME)
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container, supportFragmentManager
                .fragmentFactory.instantiate(classLoader, fragmentName!!), "demo-fragment"
        )
            .commitNow()
    }

    companion object {
        const val EXTRA_FRAGMENT_NAME = "fragment_name"
        fun show(context: Activity, item: DemoItem) {
            Intent(context, DemoFragmentActivity::class.java).let {
                it.putExtra(EXTRA_FRAGMENT_NAME, item.demoPage!!.name)
                it.putExtras(getRequiredExtras(item.path))
                context.startActivity(it)
            }
        }
    }
}