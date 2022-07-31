package com.kajtek.tarrrslator

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment


class SunsetFragment : Fragment() {

    private var mSceneView: View? = null
    private var mSunView: View? = null
    private var mSkyView: View? = null
    private var mBikeView: View? = null
    private var mBlueSkyColor = 0
    private var mSunsetSkyColor = 0
    private var mNightSkyColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sunset, container, false)

        mSceneView = view;
        mSunView = view?.findViewById(R.id.sun);
        mSkyView = view?.findViewById(R.id.sky);
        mBikeView = view?.findViewById(R.id.bike);

        mBlueSkyColor = resources.getColor(R.color.blue_sky)
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky)
        mNightSkyColor = resources.getColor(R.color.night_sky)

        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                startAnimation()
            }
        })

        return view
    }

    private fun startAnimation() {
        val sunYStart = mSunView!!.top.toFloat()
        val sunYEnd = mSkyView!!.height.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(mSunView!!, "y", sunYStart, sunYEnd)
            .setDuration(2000)
        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(mSkyView!!, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
            .setDuration(2000)

        val bikeYStart = mBikeView!!.left.toFloat()
        val bikeYEnd = mSkyView!!.width.toFloat()
        val bikeAnimator = ObjectAnimator
            .ofFloat(mBikeView!!, "x", bikeYStart, bikeYEnd)
            .setDuration(2500)

        heightAnimator.interpolator = AccelerateInterpolator()
        bikeAnimator.interpolator = AccelerateInterpolator()
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(mSkyView!!, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
            .setDuration(1000)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet
            .play(heightAnimator)
            .with(bikeAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)
        animatorSet.start()
    }
}