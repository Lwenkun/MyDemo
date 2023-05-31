package me.liwenkun.demo.animation

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import me.liwenkun.demo.R
import me.liwenkun.demo.demoframework.DemoBaseFragment
import me.liwenkun.demo.libannotation.Demo

@Demo(title = "属性动画")
class PropertyAnimationFragment : DemoBaseFragment() {
    private lateinit var animator: Animator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animator = AnimatorInflater.loadAnimator(context, R.animator.text_scale_and_alpha)
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                logInfo("animation start")
            }

            override fun onAnimationEnd(animation: Animator) {
                logInfo("animation end")
            }

            override fun onAnimationCancel(animation: Animator) {
                logInfo("animation cancel")
            }

            override fun onAnimationRepeat(animation: Animator) {
                logInfo("animation repeat")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_property_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<ImageView>(R.id.image)
        animator.setTarget(imageView)
    }

    override fun onStart() {
        super.onStart()
        if (!animator.isStarted) {
            animator.start()
        } else {
            animator.resume()
        }
    }

    override fun onStop() {
        super.onStop()
        animator.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        animator.cancel()
    }
}