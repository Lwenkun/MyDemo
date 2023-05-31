package me.liwenkun.demo.resource.drawable;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import me.liwenkun.demo.demoframework.DemoBaseFragment;
import me.liwenkun.demo.R;
import me.liwenkun.demo.libannotation.Demo;

@Demo(title = "drawable")
public class DrawableResourceDemo extends DemoBaseFragment {

    private int gravityIndex;
    private int orientationIndex;
    private ClipDrawable clipDrawable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawable_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Drawable origin = ResourcesCompat.getDrawable(getResources(),
                R.drawable.android0, Objects.requireNonNull(getContext()).getTheme());

        List<Pair<String, Integer>> gravity
                = Arrays.asList(Pair.create("top", Gravity.TOP),
                Pair.create("bottom", Gravity.BOTTOM),
                Pair.create("start", Gravity.START),
                Pair.create("end", Gravity.END),
                Pair.create("center_vertical", Gravity.CENTER_VERTICAL),
                Pair.create("center_horizontal", Gravity.CENTER_HORIZONTAL),
                Pair.create("fill_vertical", Gravity.FILL_VERTICAL),
                Pair.create("fill_horizontal", Gravity.FILL_HORIZONTAL),
                Pair.create("center", Gravity.CENTER),
                Pair.create("fill", Gravity.FILL),
                Pair.create("clip_vertical", Gravity.CLIP_VERTICAL),
                Pair.create("clip_horizontal", Gravity.CLIP_HORIZONTAL));

        List<Pair<String, Integer>> orientations = Arrays.asList(Pair.create("vertical", ClipDrawable.VERTICAL),
                Pair.create("horizontal", ClipDrawable.HORIZONTAL));

        SeekBar levelController = view.findViewById(R.id.sb_level);

        clipDrawable = new ClipDrawable(origin, gravity.get(0).second, orientations.get(0).second);
        ImageView ivClip = view.findViewById(R.id.clip_drawable);
        ivClip.setImageDrawable(clipDrawable);
        clipDrawable.setLevel(levelController.getProgress());

        Button btnChangeClipGravity = view.findViewById(R.id.btn_change_clip_gravity);
        Button btnChangeClipOrientation = view.findViewById(R.id.btn_change_clip_orientation);
        btnChangeClipGravity.setText(gravity.get(0).first);
        btnChangeClipOrientation.setText(orientations.get(0).first);


        btnChangeClipGravity.setOnClickListener(v -> {
            Pair<String, Integer> nextGravity = gravity.get(++gravityIndex % gravity.size());
            Pair<String, Integer> currentOrientation = orientations.get(orientationIndex % orientations.size());
            clipDrawable = new ClipDrawable(origin, nextGravity.second, currentOrientation.second);
            ivClip.setImageDrawable(clipDrawable);
            ivClip.setImageLevel(levelController.getProgress());
            btnChangeClipGravity.setText(nextGravity.first);
        });

        btnChangeClipOrientation.setOnClickListener((v -> {
            Pair<String, Integer> currentGravity = gravity.get(gravityIndex % gravity.size());
            Pair<String, Integer> nextOrientation = orientations.get(++orientationIndex % orientations.size());
            clipDrawable = new ClipDrawable(origin, currentGravity.second, nextOrientation.second);
            ivClip.setImageDrawable(clipDrawable);
            ivClip.setImageLevel(levelController.getProgress());
            btnChangeClipOrientation.setText(nextOrientation.first);
        }));

        levelController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                clipDrawable.setLevel(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

}
