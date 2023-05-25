package me.liwenkun.demo.customview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.liwenkun.demo.R;
import me.liwenkun.demo.demoframework.DemoBaseFragment;
import me.liwenkun.demo.libannotation.Demo;

@Demo(title = "Canvas Playground")
public class CanvasPlaygroundFragment extends DemoBaseFragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getDemoFragmentActivity().setSourceCode(CanvasView.sourceCode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_canvas_playground, container, false);
    }
}