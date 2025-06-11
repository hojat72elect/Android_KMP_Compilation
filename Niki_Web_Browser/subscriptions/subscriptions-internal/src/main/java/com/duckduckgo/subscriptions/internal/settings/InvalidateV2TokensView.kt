

package com.duckduckgo.subscriptions.internal.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.di.scopes.ViewScope
import com.duckduckgo.subscriptions.impl.auth2.AuthClient
import com.duckduckgo.subscriptions.impl.repository.AuthRepository
import com.duckduckgo.subscriptions.internal.SubsSettingPlugin
import com.duckduckgo.subscriptions.internal.databinding.SubsSimpleViewBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlinx.coroutines.launch

@InjectWith(ViewScope::class)
class InvalidateV2TokensView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var authClient: AuthClient

    private val binding: SubsSimpleViewBinding by viewBinding()

    override fun onAttachedToWindow() {
        AndroidSupportInjection.inject(this)
        super.onAttachedToWindow()

        binding.root.setPrimaryText("Invalidate V2 tokens")
        binding.root.setSecondaryText("Renders the existing V2 tokens invalid by calling /v2/logout endpoint")

        binding.root.setClickListener {
            findViewTreeLifecycleOwner()?.lifecycle?.coroutineScope?.launch {
                val accessToken = authRepository.getAccessTokenV2()
                val toastMessage = if (accessToken == null) {
                    "V2 access token not found"
                } else {
                    authClient.tryLogout(accessToken.jwt)
                    "V2 tokens invalidated"
                }
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@ContributesMultibinding(ActivityScope::class)
class InvalidateV2TokensViewPlugin @Inject constructor() : SubsSettingPlugin {
    override fun getView(context: Context): View {
        return InvalidateV2TokensView(context)
    }
}
