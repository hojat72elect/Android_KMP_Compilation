

package com.duckduckgo.autofill.impl.ui.credential.management.viewing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.autofill.impl.databinding.FragmentAutofillManagementDeviceUnsupportedBinding
import com.duckduckgo.common.ui.DuckDuckGoFragment
import com.duckduckgo.di.scopes.FragmentScope

@InjectWith(FragmentScope::class)
class AutofillManagementDeviceUnsupportedMode : DuckDuckGoFragment() {

    private lateinit var binding: FragmentAutofillManagementDeviceUnsupportedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAutofillManagementDeviceUnsupportedBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun instance() = AutofillManagementDeviceUnsupportedMode()
    }
}
