package me.liwenkun.demo.animation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import me.liwenkun.demo.R
import me.liwenkun.demo.demoframework.DemoBaseFragment
import me.liwenkun.demo.libannotation.Demo

@Demo(title = "帧动画")
class AnimationDrawableFragment : DemoBaseFragment() {
    private lateinit var frameAnimation: ImageView
    private lateinit var frameAnimation1: ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_frame_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        frameAnimation = view.findViewById(R.id.frame_animation)
        frameAnimation.setBackgroundResource(R.drawable.frame_animation)
        frameAnimation.setOnClickListener { v: View? ->
            if (frameAnimation.background is AnimationDrawable) {
                (frameAnimation.background as AnimationDrawable).start()
            }
        }
        if (frameAnimation.background is AnimationDrawable) {
            (frameAnimation.background as AnimationDrawable).stop()
        }
        frameAnimation1 = view.findViewById(R.id.frame_animation1)
        frameAnimation1.setBackgroundResource(R.drawable.frame_animation_png)
        frameAnimation1.setOnClickListener {
            if (frameAnimation1.background is AnimationDrawable) {
                (frameAnimation1.background as AnimationDrawable).start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (frameAnimation.background is AnimationDrawable) {
            (frameAnimation.background as AnimationDrawable).stop()
        }
        if (frameAnimation1.background is AnimationDrawable) {
            (frameAnimation1.background as AnimationDrawable).stop()
        }
    }
}