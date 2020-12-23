package me.liwenkun.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoBook {

    public static void load() {
        try {
            DemoRegister.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Category ROOT_CATEGORY = new Category("Demo 手册");

    public static class Item implements Comparable<Item> {
        private Category parent;
        protected String name;

        @Override
        public int compareTo(Item o) {
            return name.compareTo(o.name);
        }

        public void setParent(Category parent) {
            this.parent = parent;
        }

        public Category getParent() {
            return parent;
        }
    }

    public static class Category extends Item {

        private final Map<String, DemoItem> demoItems = new HashMap<>();
        private final Map<String, Category> categoryItems = new HashMap<>();

        public Category(String name) {
            this.name = name;
        }

        public void addSubCategory(Category subItem) {
             categoryItems.put(subItem.name, subItem);
        }

        public void addDemoItem(DemoItem demoItem) {
            demoItems.put(demoItem.name, demoItem);
        }

        public List<DemoItem> getDemoItems() {
            List<DemoItem> sortedDemoItems = new ArrayList<>(demoItems.values());
            Collections.sort(sortedDemoItems);
            return sortedDemoItems;
        }

        public List<Category> getSubCategories() {
            List<Category> sortedSubCategories = new ArrayList<>(categoryItems.values());
            Collections.sort(sortedSubCategories);
            return sortedSubCategories;
        }
    }

    public static class DemoItem extends Item {

        Class<?> demoPage;

        public DemoItem(String name, Class<?> demoPage) {
            this.name = name;
            this.demoPage = demoPage;
        }
    }

    public static void add(String demoPath, String title, Class<?> demoActivityClass) {
        String[] pathFragments = demoPath.split("/");
        Category currCategory = ROOT_CATEGORY;
        for (String pathFragment : pathFragments) {
            if ("".equals(pathFragment)) {
                continue;
            }
            Category subCategory = currCategory.categoryItems.get(pathFragment);
            if (subCategory == null) {
                subCategory = new Category(pathFragment);
                subCategory.setParent(currCategory);
                currCategory.addSubCategory(subCategory);
            }
            currCategory = subCategory;
        }
        currCategory.addDemoItem(new DemoItem(title, demoActivityClass));
    }

    public static Category getRootCategory() {
        return ROOT_CATEGORY;
    }
}
