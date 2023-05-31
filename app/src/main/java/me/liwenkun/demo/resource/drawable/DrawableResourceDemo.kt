package me.liwenkun.demo.resource.drawable

import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.util.Pair
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.res.ResourcesCompat
import me.liwenkun.demo.R
import me.liwenkun.demo.demoframework.DemoBaseFragment
import me.liwenkun.demo.libannotation.Demo

@Demo(title = "drawable")
class DrawableResourceDemo : DemoBaseFragment() {
    private var gravityIndex = 0
    private var orientationIndex = 0
    private lateinit var clipDrawable: ClipDrawable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drawable_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val origin = ResourcesCompat.getDrawable(
            resources,
            R.drawable.android0, requireContext().theme
        )
        val gravity = listOf(
            Pair.create("top", Gravity.TOP),
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
            Pair.create("clip_horizontal", Gravity.CLIP_HORIZONTAL)
        )
        val orientations = listOf(
            Pair.create("vertical", ClipDrawable.VERTICAL),
            Pair.create("horizontal", ClipDrawable.HORIZONTAL)
        )

        val levelController = view.findViewById<SeekBar>(R.id.sb_level)

        clipDrawable = ClipDrawable(origin, gravity[0].second, orientations[0].second)
        clipDrawable.level = levelController.progress

        val ivClip = view.findViewById<ImageView>(R.id.clip_drawable)
        ivClip.setImageDrawable(clipDrawable)

        val btnChangeClipGravity = view.findViewById<Button>(R.id.btn_change_clip_gravity)
        val btnChangeClipOrientation = view.findViewById<Button>(R.id.btn_change_clip_orientation)

        btnChangeClipGravity.text = gravity[0].first
        btnChangeClipOrientation.text = orientations[0].first

        btnChangeClipGravity.setOnClickListener {
            val nextGravity = gravity[++gravityIndex % gravity.size]
            val currentOrientation = orientations[orientationIndex % orientations.size]
            clipDrawable = ClipDrawable(origin, nextGravity.second, currentOrientation.second)
            ivClip.setImageDrawable(clipDrawable)
            ivClip.setImageLevel(levelController.progress)
            btnChangeClipGravity.text = nextGravity.first
        }

        btnChangeClipOrientation.setOnClickListener {
            val currentGravity = gravity[gravityIndex % gravity.size]
            val nextOrientation = orientations[++orientationIndex % orientations.size]
            clipDrawable = ClipDrawable(origin, currentGravity.second, nextOrientation.second)
            ivClip.setImageDrawable(clipDrawable)
            ivClip.setImageLevel(levelController.progress)
            btnChangeClipOrientation.text = nextOrientation.first
        }

        levelController.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                clipDrawable.level = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}