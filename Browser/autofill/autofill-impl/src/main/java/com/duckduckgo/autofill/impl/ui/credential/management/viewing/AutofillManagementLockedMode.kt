

package com.duckduckgo.autofill.impl.ui.credential.management.viewing

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duckduckgo.autofill.impl.databinding.FragmentAutofillManagementLockedBinding

@SuppressLint("NoFragment") // we don't use DI here
class AutofillManagementLockedMode : Fragment() {
    private lateinit var binding: FragmentAutofillManagementLockedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAutofillManagementLockedBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun instance() = AutofillManagementLockedMode()
    }
}
