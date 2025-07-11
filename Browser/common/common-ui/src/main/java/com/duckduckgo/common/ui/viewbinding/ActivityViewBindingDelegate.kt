

package com.duckduckgo.common.ui.viewbinding

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> AppCompatActivity.viewBinding() =
    ActivityViewBindingDelegate(T::class.java, this)

class ActivityViewBindingDelegate<T : ViewBinding>(
    bindingClass: Class<T>,
    val activity: AppCompatActivity,
) : ReadOnlyProperty<AppCompatActivity, T> {

    private var binding: T? = null
    private val bindMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)

    override fun getValue(
        thisRef: AppCompatActivity,
        property: KProperty<*>,
    ): T {
        binding?.let {
            return it
        }

        val lifecycle = thisRef.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Cannot access viewBinding activity lifecycle is ${lifecycle.currentState}")
        }

        binding = bindMethod.invoke(null, thisRef.layoutInflater).cast<T>()

        return binding!!
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> Any.cast(): T = this as T
