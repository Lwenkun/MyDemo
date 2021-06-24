package me.liwenkun.demo.animation;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.liwenkun.demo.demoframework.DemoBaseFragment;
import me.liwenkun.demo.R;
import me.liwenkun.demo.libannotation.Demo;

@Demo(category = "/安卓/动画/", title = "帧动画")
public class AnimationDrawableFragment extends DemoBaseFragment {
    private ImageView frameAnimation;
    private ImageView frameAnimation1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_frame_animation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameAnimation = view.findViewById(R.id.frame_animation);
        frameAnimation.setBackgroundResource(R.drawable.frame_animation);
        frameAnimation.setOnClickListener((v) -> {
            if (frameAnimation.getBackground() instanceof AnimationDrawable) {
                ((AnimationDrawable) frameAnimation.getBackground()).start();
            }
        });

        if (frameAnimation.getBackground() instanceof AnimationDrawable) {
            ((AnimationDrawable) frameAnimation.getBackground()).stop();
        }

        frameAnimation1 = view.findViewById(R.id.frame_animation1);
        frameAnimation1.setBackgroundResource(R.drawable.frame_animation_png);
        frameAnimation1.setOnClickListener((v) -> {
            if (frameAnimation1.getBackground() instanceof AnimationDrawable) {
                ((AnimationDrawable) frameAnimation1.getBackground()).start();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (frameAnimation.getBackground() instanceof AnimationDrawable) {
            ((AnimationDrawable) frameAnimation.getBackground()).stop();
        }
        if (frameAnimation1.getBackground() instanceof AnimationDrawable) {
            ((AnimationDrawable) frameAnimation1.getBackground()).stop();
        }
    }
}
