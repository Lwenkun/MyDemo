package me.liwenkun.demo.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
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

@Demo(title = "属性动画")
public class PropertyAnimationFragment extends DemoBaseFragment {

    Animator animator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animator = AnimatorInflater.loadAnimator(getContext(), R.animator.text_scale_and_alpha);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                logInfo("animation start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                logInfo("animation end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                logInfo("animation cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                logInfo("animation repeat");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_property_animation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.image);
        animator.setTarget(imageView);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!animator.isStarted()) {
            animator.start();
        } else {
            animator.resume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        animator.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        animator.cancel();
    }
}
