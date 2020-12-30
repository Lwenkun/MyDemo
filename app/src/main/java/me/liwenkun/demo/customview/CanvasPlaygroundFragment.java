package me.liwenkun.demo.customview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.liwenkun.demo.R;
import me.liwenkun.demo.libannotation.Demo;

@Demo(category = "/安卓/自定义View/", title = "Canvas Playground")
public class CanvasPlaygroundFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_canvas_playground, container, false);
    }
}