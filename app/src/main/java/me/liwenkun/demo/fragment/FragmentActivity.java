package me.liwenkun.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.liwenkun.demo.R;
import me.liwenkun.demo.demoframework.DemoBaseActivity;
import me.liwenkun.demo.demoframework.Logger;
import me.liwenkun.demo.libannotation.Demo;
import me.liwenkun.demo.utils.Utils;

@Demo(category = "/安卓/fragment", title = "Fragment 事务和生命周期的关系")
public class FragmentActivity extends DemoBaseActivity {

    private static final Method GET_ACTIVE_FRAGMENT = getActiveFragmentMethod();
    private static final String[] TAGS = new String[]{"A", "B", "C", "D", "E", "F"};

    private Op currentSelected;
    private final List<Runnable> steps = new ArrayList<>();
    private FragmentTransaction currentFragmentTransaction;
    private int selectedOpPosition = -1;

    private enum  Op {
        ADD("Add") {
            @Override
            public Runnable addTransactionStep(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
                return () -> fragmentTransaction.add(R.id.fragment_container, TestFragment.newInstance(tag), tag);
            }
        },
        REMOVE("Remove") {
            @Override
            public Runnable addTransactionStep(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
                return () -> {
                    Fragment fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment != null) {
                        fragmentTransaction.remove(fragment);
                    }
                };
            }
        },
        ATTACH("Attach") {
            @Override
            public Runnable addTransactionStep(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
                return () -> {
                    Fragment fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment != null) {
                        fragmentTransaction.attach(fragment);
                    }
                };
            }
        },
        DETACH("Detach") {
            @Override
            public Runnable addTransactionStep(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
                return () -> {
                    Fragment fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment != null) {
                        fragmentTransaction.detach(fragment);
                    }
                };
            }
        },
        REPLACE("Replace") {
            public Runnable addTransactionStep(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
                return () -> {
                    fragmentTransaction.replace(R.id.fragment_container, TestFragment.newInstance(tag), tag);
                };
            }
        };

        String name;
        Op(String name) {
            this.name = name;
        }
        public abstract Runnable addTransactionStep(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag);
    }

    private Adapter optionsAdapter = new RecyclerView.Adapter() {

        @NonNull
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            AppCompatTextView textView = new AppCompatTextView(FragmentActivity.this);
            textView.setPadding(Utils.px(10), Utils.px(10), Utils.px(10), Utils.px(10));
            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(Op.values()[position].name);
            holder.itemView.setOnClickListener(v -> {
                int old = selectedOpPosition;
                selectedOpPosition = position;
                if (old == selectedOpPosition) {
                    selectedOpPosition = -1;
                    currentSelected = null;
                }
                notifyItemChanged(old);
                notifyItemChanged(position);
                currentSelected = Op.values()[position];
            });
            if (selectedOpPosition == position) {
                holder.itemView.setBackgroundColor(Color.RED);
            } else {
                holder.itemView.setBackgroundColor(0xff999999);
            }
        }

        @Override
        public int getItemCount() {
            return Op.values().length;
        }
    };

    private Adapter tagsAdapter = new RecyclerView.Adapter() {
        @NonNull
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            AppCompatTextView textView = new AppCompatTextView(FragmentActivity.this);
            textView.setBackgroundColor(0xffeeeeee);
            MarginLayoutParams layoutParams
                    = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = Utils.px(5);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(Utils.px(10), Utils.px(10), Utils.px(10), Utils.px(10));
            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(TAGS[position]);
            holder.itemView.setOnClickListener(v -> {
                if (currentFragmentTransaction == null) {
                    currentFragmentTransaction = getSupportFragmentManager().beginTransaction();
                }
                if (currentSelected != null) {
                    steps.add(currentSelected.addTransactionStep(getSupportFragmentManager(),
                            currentFragmentTransaction, TAGS[position]));
                    int old = selectedOpPosition;
                    selectedOpPosition = -1;
                    currentSelected = null;
                    if (old >= 0) {
                        optionsAdapter.notifyItemChanged(old);
                    }
                } else {
                    Toast.makeText(FragmentActivity.this, "請先選擇操作", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return TAGS.length;
        }
    };

    private static Method getActiveFragmentMethod() {
        try {
            Class<?> fragmentManagerImpl = Class.forName("androidx.fragment.app.FragmentManagerImpl");
            Method method = fragmentManagerImpl.getDeclaredMethod("getActiveFragments");
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        RecyclerView options = findViewById(R.id.options);
        options.setAdapter(optionsAdapter);
        options.setLayoutManager(new LinearLayoutManager(FragmentActivity.this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerView tags = findViewById(R.id.tags);
        tags.setAdapter(tagsAdapter);
        tags.setLayoutManager(new LinearLayoutManager(FragmentActivity.this, LinearLayoutManager.HORIZONTAL, false));
        findViewById(R.id.btn_commit).setOnClickListener(v -> {
            if (!steps.isEmpty()) {
                for (Runnable step : steps) {
                    step.run();
                }
                currentFragmentTransaction.commit();
                v.post(this::logFragmentInfo);
                currentFragmentTransaction = null;
            } else {
                Toast.makeText(FragmentActivity.this, "操作隊列為空", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logFragmentInfo() {
        List<Fragment> active = null;
        if (GET_ACTIVE_FRAGMENT != null) {
            try {
                active = (List<Fragment>) GET_ACTIVE_FRAGMENT.invoke(getSupportFragmentManager());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (active != null) {
            log("fragmentInfo->active: " + "{count: " + active.size()
                    + ", fragments: " + active.toString() + "}", Logger.COLOR_INFO);
        }
        List<Fragment> added = getSupportFragmentManager().getFragments();
        log("fragmentInfo->added: " + "{count: " + added.size()
                + ", fragments: " + added.toString() + "}", Logger.COLOR_INFO);
    }
}
