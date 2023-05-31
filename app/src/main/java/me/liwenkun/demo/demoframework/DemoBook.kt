package me.liwenkun.demo.demoframework

import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.TypeConverters
import me.liwenkun.demo.App.Companion.get
import me.liwenkun.demo.DemoRegister
import me.liwenkun.demo.R

enum class DemoBook {
    INSTANCE;

    private fun load() {
        try {
            Thread {
                DemoRegister.init()
                rootCategory.postValue(ROOT_CATEGORY)
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val rootCategory = MutableLiveData<Category>()

    init {
        load()
    }

    open class Item : Comparable<Item> {
        @Ignore
        var parent: Category? = null

        @ColumnInfo(name = "name")
        lateinit var name: String

        @ColumnInfo(name = "path")
        lateinit var path: String

        constructor()

        constructor(name: String, path: String) {
            this.name = name
            this.path = path
        }

        override fun compareTo(other: Item): Int {
            return name.compareTo(other.name)
        }
    }

    class Category(name: String, path: String) : Item(name, path) {
        private val _demoItems: MutableMap<String, DemoItem> = HashMap()

        internal val _categoryItems: MutableMap<String, Category> = HashMap()

        fun addSubCategory(subItem: Category) {
            _categoryItems[subItem.name] = subItem
        }

        fun addDemoItem(demoItem: DemoItem) {
            _demoItems[demoItem.name] = demoItem
        }

        val demoItems: List<DemoItem>
            get() {
                return _demoItems.values.sorted()
            }

        val categories: List<Category>
            get() {
                return _categoryItems.values.sorted()
            }

        fun clear() {
            _demoItems.clear()
            _categoryItems.clear()
        }
    }

    @Entity(tableName = "demo_items", primaryKeys = ["path"])
    @TypeConverters(
        DemoItemConverters::class
    )
    class DemoItem : Item {
        @ColumnInfo(name = "is_starred")
        var isStarred = false

        @ColumnInfo(name = "demo_page_name")
        lateinit var demoPage: Class<*>

        constructor() : super()

        @Ignore
        constructor(name: String, path: String, demoPage: Class<*>) : super(name, path) {
            isStarred = false
            this.demoPage = demoPage
        }
    }

    companion object {
        private val ROOT_CATEGORY = Category(get().getString(R.string.demo_book), "/")
        @JvmStatic
        fun add(demoPath: String, title: String, demoActivityClass: Class<*>) {
            val pathFragments = demoPath.split('/')
            var currCategory = ROOT_CATEGORY
            val currentPath = StringBuilder()
            for (pathFragment in pathFragments) {
                if ("" == pathFragment) {
                    continue
                }
                currentPath.append('/')
                currentPath.append(pathFragment)
                var subCategory = currCategory._categoryItems[pathFragment]
                if (subCategory == null) {
                    subCategory = Category(pathFragment, currentPath.toString())
                    subCategory.parent = currCategory
                    currCategory.addSubCategory(subCategory)
                }
                currCategory = subCategory
            }
            currentPath.append('/')
            currentPath.append(title)
            val demoItem = DemoItem(title, currentPath.toString(), demoActivityClass)
            get().appDatabase.demoItemDao().insert(demoItem)
            currCategory.addDemoItem(demoItem)
        }
    }
}