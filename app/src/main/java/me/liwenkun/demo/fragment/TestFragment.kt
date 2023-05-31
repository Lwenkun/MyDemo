package me.liwenkun.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import me.liwenkun.demo.demoframework.Logger;

public class TestFragment extends Fragment {

    private String tag;
    private Logger logger;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        logger = ((Logger) context);
        tag = getTag();
        logLifecycle("onAttach");
    }

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logLifecycle("onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logLifecycle("onActivityCreated");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView showTag = new TextView(getContext());
        showTag.setBackgroundColor(0xffeeeeee);
        showTag.setText(tag);
        showTag.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        showTag.setGravity(Gravity.CENTER);
        logLifecycle("onCreateView");
        return showTag;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logLifecycle("onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        logLifecycle("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        logLifecycle("onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        logLifecycle("onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        logLifecycle("onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logLifecycle("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logLifecycle("onDestroy");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        logLifecycle("onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        logLifecycle("onViewStateRestored");
    }

    private void logLifecycle(String name) {
        logger.log("fragment " + tag + ' ' + name + " called", Logger.COLOR_INFO);
    }
}
