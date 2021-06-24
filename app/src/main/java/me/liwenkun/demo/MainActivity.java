package me.liwenkun.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.liwenkun.demo.demoframework.DemoBook;
import me.liwenkun.demo.demoframework.DemoFragmentActivity;

import static me.liwenkun.demo.demoframework.DemoBaseActivity.EXTRA_DEMO_TITLE;

public class MainActivity extends AppCompatActivity {

    static {
        DemoBook.load();
    }

    public static class MainActivityModel extends ViewModel {
        private MutableLiveData<DemoBook.Category> category;

        private MutableLiveData<DemoBook.Category> getCategory() {
            if (category == null) {
                category = new MutableLiveData<>();
                category.setValue(DemoBook.getRootCategory()); // 初始化數據
            }
            return category;
        }

        public void setCategory(DemoBook.Category category) {
            this.category.setValue(category);
        }
    }

    private DemoListAdapter demoListAdapter;

    private MainActivityModel mainActivityModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(MainActivityModel.class);
        RecyclerView rvDemoList = findViewById(R.id.demo_list);
        rvDemoList.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        demoListAdapter = new DemoListAdapter();
        demoListAdapter.setCallback(new DemoListAdapter.Callback() {
            @Override
            public void onCategoryClick(View view, DemoBook.Category category, int position) {
                mainActivityModel.setCategory(category);
            }

            @Override
            public void onDemoItemClick(View view, DemoBook.DemoItem demoItem, int position) {
                if (Activity.class.isAssignableFrom(demoItem.getDemoPage())) {
                    Intent showDemo = new Intent(MainActivity.this, demoItem.getDemoPage());
                    showDemo.putExtra(EXTRA_DEMO_TITLE, demoItem.getName());
                    startActivity(showDemo);
                } else if (Fragment.class.isAssignableFrom(demoItem.getDemoPage())) {
                    Intent showDemo = new Intent(MainActivity.this, DemoFragmentActivity.class);
                    showDemo.putExtra(DemoFragmentActivity.EXTRA_FRAGMENT_NAME, demoItem.getDemoPage().getName());
                    showDemo.putExtra(EXTRA_DEMO_TITLE, demoItem.getName());
                    startActivity(showDemo);
                }
            }
        });
        mainActivityModel.getCategory().observe(this, category -> {
            demoListAdapter.update(category);
            setTitle(category.getName());
        });
        rvDemoList.setAdapter(demoListAdapter);
    }

    @Override
    public void onBackPressed() {
        DemoBook.Category current = mainActivityModel.getCategory().getValue();
        if (current != null && current.getParent() != null) {
            mainActivityModel.getCategory().setValue(current.getParent());
        } else {
            super.onBackPressed();
        }
    }

    public static class DemoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public interface Callback extends DemoItemViewHolder.Callback, CategoryViewHolder.Callback {
        }

        private static final int TYPE_CATEGORY = 0;
        private static final int TYPE_DEMO_ITEM = 1;

        private Callback callback;
        private final List<DemoBook.Category> subCategories = new ArrayList<>();
        private final List<DemoBook.DemoItem> demoItems = new ArrayList<>();
        private RecyclerView host;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_CATEGORY) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.demo_category, parent, false);
                return new CategoryViewHolder(itemView);
            } else {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.demo_item, parent, false);
                return new DemoItemViewHolder(itemView);
            }
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            this.host = recyclerView;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CategoryViewHolder) {
                ((CategoryViewHolder) holder).bindData(((DemoBook.Category) getItem(position)));
                ((CategoryViewHolder) holder).setCallback(callback);
            } else if (holder instanceof DemoItemViewHolder) {
                ((DemoItemViewHolder) holder).bindData(((DemoBook.DemoItem) getItem(position)));
                ((DemoItemViewHolder) holder).setCallback(callback);
            }
        }

        public void update(DemoBook.Category currentCategory) {
            int oldCount = getItemCount();
            subCategories.clear();
            demoItems.clear();
            notifyItemRangeRemoved(0,  oldCount);
            if (host != null) {
                subCategories.addAll(currentCategory.getSubCategories());
                demoItems.addAll(currentCategory.getDemoItems());
                int newCount = getItemCount();
                notifyItemRangeInserted(0, newCount);
            }
        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            host = null;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position) instanceof DemoBook.Category
                    ? TYPE_CATEGORY : TYPE_DEMO_ITEM;
        }

        private DemoBook.Item getItem(int position) {
            if (position < subCategories.size()) {
                return subCategories.get(position);
            } else {
                return demoItems.get(position - subCategories.size());
            }
        }

        @Override
        public int getItemCount() {
            return subCategories.size() + demoItems.size();
        }

        public void setCallback(Callback callback) {
            this.callback = callback;
        }
    }

    private static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private interface Callback {
            void onCategoryClick(View view, DemoBook.Category category, int position);
        }

        private CategoryViewHolder.Callback callback;
        private DemoBook.Category category;
        private final TextView name;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onCategoryClick(itemView, category, getAdapterPosition());
                }
            });
        }

        private void bindData(DemoBook.Category category) {
            this.category = category;
            name.setText(category.getName());
        }

        private void setCallback(Callback callback) {
            this.callback = callback;
        }
    }

    private static class DemoItemViewHolder extends RecyclerView.ViewHolder {

        private interface Callback {
            void onDemoItemClick(View view, DemoBook.DemoItem demoItem, int position);
        }

        private Callback callback;
        private DemoBook.DemoItem demoItem;
        private final TextView name;

        public DemoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onDemoItemClick(itemView, demoItem, getAdapterPosition());
                }
            });
        }

        private void bindData(DemoBook.DemoItem demoItem) {
            this.demoItem = demoItem;
            name.setText(demoItem.getName());
        }

        private void setCallback(Callback callback) {
            this.callback = callback;
        }
    }
}