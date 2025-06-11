

package com.duckduckgo.common.ui.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> ViewGroup.viewBinding() =
    ViewBindingDelegate(T::class.java, this)

class ViewBindingDelegate<T : ViewBinding>(
    bindingClass: Class<T>,
    view: ViewGroup,
) :
    ReadOnlyProperty<ViewGroup, T> {
    private val binding: T =
        try {
            val inflateMethod =
                bindingClass.getMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.javaPrimitiveType,
                )
            inflateMethod.invoke(null, LayoutInflater.from(view.context), view, true).cast()
        } catch (e: NoSuchMethodException) {
            val inflateMethod =
                bindingClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java)
            inflateMethod.invoke(null, LayoutInflater.from(view.context), view).cast()
        }

    override fun getValue(
        thisRef: ViewGroup,
        property: KProperty<*>,
    ): T {
        return binding
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> Any.cast(): T = this as T
