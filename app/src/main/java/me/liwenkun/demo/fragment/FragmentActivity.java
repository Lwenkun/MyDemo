package me.liwenkun.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private TransactionOp currentSelectedOp;
    private final TransactionHelper transactionHelper
            = new TransactionHelper(getSupportFragmentManager());

    private final Adapter<RecyclerView.ViewHolder> optionsAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            AppCompatTextView textView = new AppCompatTextView(FragmentActivity.this);
            textView.setPadding(Utils.px(10), Utils.px(10), Utils.px(10), Utils.px(10));
            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(TransactionOp.values()[position].name);
            holder.itemView.setOnClickListener(v -> {
                currentSelectedOp = TransactionOp.values()[position];
                notifyDataSetChanged();
            });
            if (currentSelectedOp == TransactionOp.values()[position]) {
                holder.itemView.setBackgroundColor(Color.RED);
            } else {
                holder.itemView.setBackgroundColor(0xff999999);
            }
        }

        @Override
        public int getItemCount() {
            return TransactionOp.values().length;
        }
    };

    private final Adapter<RecyclerView.ViewHolder> tagsAdapter
            = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(TAGS[position]);
            holder.itemView.setOnClickListener(v -> {
                if (currentSelectedOp != null) {
                    transactionHelper.addTransactionOp(currentSelectedOp, TAGS[position]);
                    currentSelectedOp = null;
                    optionsAdapter.notifyDataSetChanged();
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

    private final Adapter<RecyclerView.ViewHolder> opsAdapter
            = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            AppCompatTextView textView = new AppCompatTextView(FragmentActivity.this);
            textView.setBackgroundColor(0xffffff00);
            MarginLayoutParams layoutParams
                    = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = Utils.px(5);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(Utils.px(10), Utils.px(10), Utils.px(10), Utils.px(10));
            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            List<TransactionOpAction> transactionOpActions = transactionHelper.getTransactionOp().getValue();
            if (transactionOpActions != null) {
                ((TextView) holder.itemView).setText(transactionOpActions.get(position).getActionDesc());
            }
        }

        @Override
        public int getItemCount() {
            List<TransactionOpAction> transactionOpActions = transactionHelper.getTransactionOp().getValue();
            return transactionOpActions == null ? 0 : transactionOpActions.size();
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        RecyclerView opOptions = findViewById(R.id.rv_options);
        opOptions.setAdapter(optionsAdapter);
        opOptions.setLayoutManager(new LinearLayoutManager(FragmentActivity.this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerView tags = findViewById(R.id.rv_tags);
        tags.setAdapter(tagsAdapter);
        tags.setLayoutManager(new LinearLayoutManager(FragmentActivity.this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerView ops = findViewById(R.id.rv_ops);
        ops.setAdapter(opsAdapter);
        ops.setLayoutManager(new LinearLayoutManager(FragmentActivity.this, LinearLayoutManager.HORIZONTAL, false));
        transactionHelper.getTransactionOp().observe(this, TransactionOpActions -> opsAdapter.notifyDataSetChanged());
        CheckBox cbAddToBackStack = findViewById(R.id.cb_add_to_backstack);
        findViewById(R.id.btn_commit).setOnClickListener(v -> {
            if (transactionHelper.commit(cbAddToBackStack.isChecked())) {
                v.post(this::logFragmentInfo);
            } else {
                Toast.makeText(FragmentActivity.this, "操作隊列為空", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_clear_op).setOnClickListener(v -> transactionHelper.cleanTransactionOp());
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            new Handler().post(this::logFragmentInfo);
        }
        super.onBackPressed();
    }

    private void logFragmentInfo() {
        List<Fragment> active = null;
        if (GET_ACTIVE_FRAGMENT != null) {
            try {
                // noinspection unchecked
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
