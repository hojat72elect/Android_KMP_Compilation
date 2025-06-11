

package experiments.trackersblocking

import android.content.Context
import android.util.AttributeSet
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.fire.FireActivity
import com.duckduckgo.common.ui.internal.databinding.ViewTrackersBlockingExperimentBinding
import com.duckduckgo.common.ui.view.dialog.TextAlertDialogBuilder
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.ViewViewModelFactory
import com.duckduckgo.di.scopes.ViewScope
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import experiments.trackersblocking.TrackersBlockingExperimentViewModel.ViewState
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InjectWith(ViewScope::class)
class TrackersBlockingExperimentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    @Inject
    lateinit var viewModelFactory: ViewViewModelFactory

    private val binding: ViewTrackersBlockingExperimentBinding by viewBinding()

    private val viewModel: TrackersBlockingExperimentViewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!, viewModelFactory)[TrackersBlockingExperimentViewModel::class.java]
    }

    private val trackersBlockingVariant1ToggleListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        viewModel.onTrackersBlockingVariant1ExperimentalUIModeChanged(isChecked)
        askForRestart()
    }

    private val trackersBlockingVariant2ToggleListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        viewModel.onTrackersBlockingVariant2ExperimentalUIModeChanged(isChecked)
        askForRestart()
    }

    private val trackersBlockingVariant3ToggleListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        viewModel.onTrackersBlockingVariant3ExperimentalUIModeChanged(isChecked)
        askForRestart()
    }

    override fun onAttachedToWindow() {
        AndroidSupportInjection.inject(this)
        super.onAttachedToWindow()

        val coroutineScope = findViewTreeLifecycleOwner()?.lifecycleScope

        viewModel.viewState()
            .onEach { render(it) }
            .launchIn(coroutineScope!!)
    }

    private fun render(viewState: ViewState) {
        binding.trackersBlockingVariant1ExperimentalUIMode.quietlySetIsChecked(viewState.modifiedControl, trackersBlockingVariant1ToggleListener)
        binding.trackersBlockingVariant2ExperimentalUIMode.quietlySetIsChecked(viewState.variant1, trackersBlockingVariant2ToggleListener)
        binding.trackersBlockingVariant3ExperimentalUIMode.quietlySetIsChecked(viewState.variant2, trackersBlockingVariant3ToggleListener)

        Snackbar.make(binding.root, "Updated", Snackbar.LENGTH_SHORT).show()
    }

    private fun askForRestart() {
        TextAlertDialogBuilder(context)
            .setTitle(R.string.appearanceNightModeDialogTitle)
            .setMessage(R.string.appearanceNightModeDialogMessage)
            .setPositiveButton(R.string.appearanceNightModeDialogPrimaryCTA)
            .setNegativeButton(R.string.appearanceNightModeDialogSecondaryCTA)
            .addEventListener(
                object : TextAlertDialogBuilder.EventListener() {
                    override fun onPositiveButtonClicked() {
                        FireActivity.triggerRestart(context, false)
                    }

                    override fun onNegativeButtonClicked() {
                        // no-op
                    }
                },
            )
            .show()
    }
}
