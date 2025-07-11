

package com.duckduckgo.autofill.impl.ui.credential.management

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

fun FragmentManager.removeFragment(tag: String) {
    commit {
        findFragmentByTag(tag)?.let {
            this.remove(it)
        }
    }
}
