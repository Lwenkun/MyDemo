package me.liwenkun.demo.fragment;

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

public class TestFragment extends Fragment {

    private static final String KEY_TAG = "key_tag";
    private String tag;

    public static TestFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString(KEY_TAG, tag);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments() == null ? "" : getArguments().getString(KEY_TAG);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        TextView showTag = new TextView(getContext());
        showTag.setBackgroundColor(0xffeeeeee);
        showTag.setText(tag);
        showTag.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        showTag.setGravity(Gravity.CENTER);
        return showTag;
    }
}
