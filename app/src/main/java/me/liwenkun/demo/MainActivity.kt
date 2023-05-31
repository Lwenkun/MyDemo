package me.liwenkun.demo

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.liwenkun.demo.App.Companion.get
import me.liwenkun.demo.demoframework.DemoBaseActivity
import me.liwenkun.demo.demoframework.DemoBook
import me.liwenkun.demo.demoframework.DemoBook.DemoItem
import me.liwenkun.demo.demoframework.DemoFragmentActivity

class MainActivity : AppCompatActivity() {

    private lateinit var demoListAdapter: DemoListAdapter
    private lateinit var mainActivityModel: MainActivityModel
    private var showingBookmarks = false
    private var menuItemBookmark: MenuItem? = null
    private val demoItemDao = get().appDatabase.demoItemDao()
    private val demoItemsInBookmark: LiveData<List<DemoItem>> = demoItemDao.getStarred()

    private val onBackPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onCloseBookmarks()
            mainActivityModel.getCurrentCategory().value =
                mainActivityModel.getCurrentCategory().value!!.parent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainActivityModel::class.java]
        val rvDemoList = findViewById<RecyclerView>(R.id.demo_list)
        rvDemoList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        demoListAdapter = DemoListAdapter()
        demoListAdapter.setCallback(object : DemoListAdapter.Callback {
            override fun onCategoryClick(view: View, category: DemoBook.Category, position: Int) {
                mainActivityModel.setCurrentCategory(category)
            }

            override fun onDemoItemClick(view: View, demoItem: DemoItem, position: Int) {
                if (Activity::class.java.isAssignableFrom(demoItem.demoPage)) {
                    DemoBaseActivity.show(this@MainActivity, demoItem)
                } else if (Fragment::class.java.isAssignableFrom(demoItem.demoPage)) {
                    DemoFragmentActivity.show(this@MainActivity, demoItem)
                } else {
                    throw IllegalStateException("DemoItem#getDemoPage() can only be Activity or Fragment")
                }
            }
        })
        mainActivityModel.getCurrentCategory().observe(this) { category: DemoBook.Category ->
            demoListAdapter.update(category)
            title = category.name
            onBackPressedCallback.isEnabled = category.parent != null
        }
        rvDemoList.adapter = demoListAdapter

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        menuItemBookmark = menu.findItem(R.id.bookmark)
        return true
    }

    private val observer = Observer { demoItems: List<DemoItem> ->
        BOOK_MARKS.clear()
        demoItems.forEach {
            BOOK_MARKS.addDemoItem(it)
        }
        mainActivityModel.getCurrentCategory().setValue(BOOK_MARKS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.bookmark) {
            onOpenBookmarks()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onOpenBookmarks() {
        menuItemBookmark!!.isVisible = false
        BOOK_MARKS.parent = mainActivityModel.getCurrentCategory().value
        mainActivityModel.setCurrentCategory(BOOK_MARKS)
        demoItemsInBookmark.observe(this, observer)
        showingBookmarks = true
    }

    private fun onCloseBookmarks() {
        if (showingBookmarks) {
            menuItemBookmark!!.isVisible = true
            demoItemsInBookmark.removeObserver(observer)
            showingBookmarks = false
        }
    }

    companion object {
        private val BOOK_MARKS = DemoBook.Category("书签", "/bookmarks")
    }
}