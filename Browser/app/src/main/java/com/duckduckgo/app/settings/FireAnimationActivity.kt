

package com.duckduckgo.app.settings

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.IntentCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.RenderMode
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.databinding.ActivityFireAnimationBinding
import com.duckduckgo.app.settings.clear.FireAnimation
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.view.setAndPropagateUpFitsSystemWindows
import com.duckduckgo.common.ui.view.show
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope

@InjectWith(ActivityScope::class)
class FireAnimationActivity : DuckDuckGoActivity() {

    private val binding: ActivityFireAnimationBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fireAnimationSerializable = IntentCompat.getSerializableExtra(intent, FIRE_ANIMATION_EXTRA, FireAnimation::class.java)

        if (fireAnimationSerializable == null) finish()

        configureFireAnimationView(fireAnimationSerializable as FireAnimation, binding.fireAnimationView)

        binding.fireAnimationView.show()
        binding.fireAnimationView.playAnimation()
    }

    private fun configureFireAnimationView(
        fireAnimation: FireAnimation,
        fireAnimationView: LottieAnimationView,
    ) {
        fireAnimationView.setAnimation(fireAnimation.resId)
        fireAnimationView.setRenderMode(RenderMode.SOFTWARE)
        fireAnimationView.enableMergePathsForKitKatAndAbove(true)
        fireAnimationView.setAndPropagateUpFitsSystemWindows(false)
        fireAnimationView.addAnimatorUpdateListener(accelerateAnimatorUpdateListener)
        fireAnimationView.addAnimatorListener(
            object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    finish()
                    overridePendingTransition(0, R.anim.tab_anim_fade_out)
                }
            },
        )
    }

    private val accelerateAnimatorUpdateListener = object : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            binding.fireAnimationView.speed += ANIMATION_SPEED_INCREMENT
            if (binding.fireAnimationView.speed > ANIMATION_MAX_SPEED) {
                binding.fireAnimationView.removeUpdateListener(this)
            }
        }
    }

    companion object {
        const val FIRE_ANIMATION_EXTRA = "FIRE_ANIMATION_EXTRA"

        private const val ANIMATION_MAX_SPEED = 1.4f
        private const val ANIMATION_SPEED_INCREMENT = 0.15f

        fun intent(
            context: Context,
            fireAnimation: FireAnimation,
        ): Intent {
            val intent = Intent(context, FireAnimationActivity::class.java)
            intent.putExtra(FIRE_ANIMATION_EXTRA, fireAnimation)
            return intent
        }
    }
}
