

package com.duckduckgo.common.ui.themepreview.ui.component.textinput

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duckduckgo.common.ui.view.text.TextInput.Action
import com.duckduckgo.mobile.android.R
import com.duckduckgo.mobile.android.databinding.ComponentTextInputViewBinding
import com.google.android.material.snackbar.Snackbar

@SuppressLint("NoFragment") // we don't use DI here
class ComponentTextInputFragment : Fragment() {

    private lateinit var binding: ComponentTextInputViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = ComponentTextInputViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.outlinedinputtext4.apply {
            setEndIcon(R.drawable.ic_copy_24)
            onAction { toastOnClick(it) }
        }
        binding.outlinedinputtext6.onAction { toastOnClick(it) }
        binding.outlinedinputtext8.onAction { toastOnClick(it) }
        binding.outlinedinputtext20.onAction { toastOnClick(it) }
        binding.outlinedinputtext30.onAction { toastOnClick(it) }
        binding.outlinedinputtext31.onAction { toastOnClick(it) }
        binding.outlinedinputtext32.onAction { toastOnClick(it) }
        binding.outlinedinputtext33.onAction { toastOnClick(it) }
        binding.outlinedinputtext21.error = "This is an error"
    }

    private fun toastOnClick(action: Action) = when (action) {
        is Action.PerformEndAction -> {
            Snackbar.make(binding.root, "Element clicked", Snackbar.LENGTH_SHORT).show()
        }
    }
}
